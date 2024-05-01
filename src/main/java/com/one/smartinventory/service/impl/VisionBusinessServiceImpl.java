package com.one.smartinventory.service.impl;

import com.google.common.collect.Sets;
import com.one.smartinventory.model.Product;
import com.one.smartinventory.service.*;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.SimpleTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class VisionBusinessServiceImpl implements VisionBusinessService {

    private final ProductBusinessService productBusinessService;

    private final TasksBusinessService tasksBusinessService;

    private final VisitorBusinessService visitorBusinessService;

    private final GoogleCloudVisionService cloudVisionService;

    private final Set<String> people = Set.of("customers", "customer", "people", "man", "men", "woman", "women");

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


    @Autowired
    public VisionBusinessServiceImpl(ProductBusinessService productBusinessService,
                                     TasksBusinessService tasksBusinessService,
                                     VisitorBusinessService visitorBusinessService,
                                     GoogleCloudVisionService cloudVisionService) {
        this.productBusinessService = productBusinessService;
        this.tasksBusinessService = tasksBusinessService;
        this.visitorBusinessService = visitorBusinessService;
        this.cloudVisionService = cloudVisionService;

    }

    @Override
    public void scanImage(String locator, long productId, boolean customer) {
        if (customer) {
            visitorBusinessService.updateVisit(locator, LocalDate.now());
        }
        Product product = productBusinessService.get(productId);
        updateProduct(product, locator);
    }

    @Override
    public void scanImage(String projectId, String location, String modelName, String locator, byte[] bytes) {
        Product bestMatch = null;
        int length = Integer.MIN_VALUE;
        if (bytes.length > 0) {
            String description = cloudVisionService.process(projectId, location, modelName, bytes);
            Set<String> texts = extractText(description);
            log.info("searchable texts in the image {}", texts);
            for (String name : texts) {
                Product product = findBestMatchProduct(name);
                if (product != null) {
                    if (name.length() > length) {
                        bestMatch = product;
                        length = name.length();
                    }
                }
            }
            if (bestMatch != null) {
                log.info("Best matched product is {}", bestMatch.getName());
            }
            if (!Sets.intersection(texts, people).isEmpty()) {
                visitorBusinessService.updateVisit(locator, LocalDate.now());
            }
        }
        updateProduct(bestMatch, locator);
    }

    private void updateProduct(Product product, String locator) {
        if (product != null && !product.getAisle().equals(locator)) {
            product.setCount(product.getCount() - 1);
            productBusinessService.create(product);
            tasksBusinessService.createGenericTask(product.getProductId(), locator, product.getAisle(), 1);
        }
    }

    private Product findBestMatchProduct(String name) {
        List<Product> products = productBusinessService.findMatch(name);
        if (products.isEmpty()) {
            return null;
        }
        int index = Integer.MAX_VALUE;
        Product bestMatch = null;
        for (Product product : products) {
            int match = product.getName().toLowerCase().indexOf(name);
            if (match < index) {
                index = match;
                bestMatch = product;
            }
        }
        return bestMatch;
    }

    private Set<String> extractText(String content) {
        Set<String> texts = new HashSet<>();
        if (content != null) {
            SimpleTokenizer tokenizer = SimpleTokenizer.INSTANCE;
            String[] tokens = tokenizer.tokenize(content.replaceAll("text|image", ""));
            String[] tags = posTagger.tag(tokens);
            for (int i = 0; i < tags.length; i++) {
                if (tokens[i].length() > 2) {
                    if (tags[i].equals("NNP") || tags[i].equals("NNPS") || tags[i].equals("NN") ||
                            tags[i].equals("NNS")) {
                        texts.add(tokens[i].toLowerCase());
                    }
                }
            }
        }
        return texts;
    }
}
