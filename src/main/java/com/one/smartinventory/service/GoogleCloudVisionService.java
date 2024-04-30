package com.one.smartinventory.service;

import com.google.cloud.vertexai.VertexAI;
import com.google.cloud.vertexai.api.Content;
import com.google.cloud.vertexai.api.GenerateContentResponse;
import com.google.cloud.vertexai.generativeai.ContentMaker;
import com.google.cloud.vertexai.generativeai.GenerativeModel;
import com.google.cloud.vertexai.generativeai.PartMaker;
import com.google.cloud.vertexai.generativeai.ResponseHandler;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class GoogleCloudVisionService {

    private static final String DESCRIPTION = "description";

    private POSTaggerME posTagger;


    @PostConstruct
    public void init() {
        try {
            InputStream modelIn = GoogleCloudVisionService.class.getResourceAsStream("/en-pos-maxent.bin");
            if (modelIn != null) {
                POSModel posModel = new POSModel(modelIn);
                this.posTagger = new POSTaggerME(posModel);
            }
        } catch (IOException e) {
            log.error("Could not load NLP model", e);
        }

    }

    public List<String> processImage(byte[] image) {
        List<String> descriptions = new ArrayList<>();
        ByteString imgBytes = ByteString.copyFrom(image);
        List<AnnotateImageRequest> requests = new ArrayList<>();
        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature label = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder().addFeatures(label).setImage(img).build();
        requests.add(request);
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();
            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    log.error("Error {}", res.getError().getMessage());
                    return descriptions;
                }
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    annotation.getAllFields().forEach((k, v) -> {
                        if (k.getName().contains(DESCRIPTION)) {
                            descriptions.add(v.toString());
                        }
                    });
                }
            }
        } catch (IOException e) {
            log.error("Couldn't process image", e);
        }
        log.info("image descriptions {}", descriptions);
        return descriptions;
    }

    @SneakyThrows
    public Set<String> process(String projectId, String location, String modelName, byte[] imageBytes) {
        Set<String> texts = new HashSet<>();
        String description;
        try (VertexAI vertexAi = new VertexAI(projectId, location)) {
            GenerativeModel model = new GenerativeModel(modelName, vertexAi);
            List<Content> contents = new ArrayList<>();
            contents.add(ContentMaker
                    .fromMultiModalData(
                            "What is the text in the image?", "what is in the image?",
                            PartMaker.fromMimeTypeAndData("image/jpeg", imageBytes)));
            GenerateContentResponse response = model.generateContent(contents);
            description = ResponseHandler.getText(response);
            log.info(description);
        }
        extractText(description, texts);
        log.info("searchable texts in the image {}", texts);
        return texts;
    }


    private void extractText(String content, Set<String> texts) {
        SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
        String[] tokens = tokenizer.tokenize(content.replaceAll("text|image",""));
        String[] tags = posTagger.tag(tokens);
        for (int i = 0; i < tags.length; i++) {
            if (tokens[i].length() > 2) {
                if (tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("NN") || tags[i].equals("NNS")) {
                    texts.add(tokens[i].toLowerCase());
                }
            }
        }
    }

}
