INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('A-1', 50, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('A-2', 25, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('A-3', 25, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('B-1', 50, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('B-2', 30, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('C-1', 50, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('C-2', 30, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('D-1', 50, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('D-2', 25, 10, 10, false,0);
INSERT INTO AREA (AISLE, LENGTH, WIDTH, HEIGHT, FREEZE, VIEWED) VALUES ('D-3', 25, 10, 10, false,0);
INSERT INTO PRODUCT (AISLE, PRODUCT_ID, NAME,LENGTH, WIDTH, HEIGHT, FREEZE, COUNT, EXPIRY,VIEWED) VALUES ('C-1', 48, 'Tide Pods',1, 1, 1, false, 4,PARSEDATETIME(CURRENT_DATE()+20,'yyyy-MM-dd'),0);
INSERT INTO PRODUCT (AISLE, PRODUCT_ID, NAME,LENGTH, WIDTH, HEIGHT, FREEZE, COUNT, EXPIRY,VIEWED) VALUES ('D-3', 101, 'Magna-Tiles Forest Animals',2, 1, 1, false, 20,PARSEDATETIME(CURRENT_DATE()+30,'yyyy-MM-dd'),0);
INSERT INTO PRODUCT (AISLE, PRODUCT_ID, NAME,LENGTH, WIDTH, HEIGHT, FREEZE, COUNT, EXPIRY,VIEWED) VALUES ('A-1', 63, 'Raw Manuka Honey',1, 1, 2, false, 6,PARSEDATETIME(CURRENT_DATE()+10,'yyyy-MM-dd'),0);
INSERT INTO PRODUCT (AISLE, PRODUCT_ID, NAME,LENGTH, WIDTH, HEIGHT, FREEZE, COUNT, EXPIRY,VIEWED) VALUES ('B-1', 29, 'Salmon fillets',1, 1, 1, false, 10,PARSEDATETIME(CURRENT_DATE()+3,'yyyy-MM-dd'),0);
INSERT INTO PRODUCT (AISLE, PRODUCT_ID, NAME,LENGTH, WIDTH, HEIGHT, FREEZE, COUNT, EXPIRY,VIEWED) VALUES ('A-2', 14, 'Kirkland Signature All Chocolate',1, 1, 2, false, 5,PARSEDATETIME(CURRENT_DATE()+5,'yyyy-MM-dd'),0);

