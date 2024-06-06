-- Generated by Oracle SQL Developer Data Modeler 20.2.0.167.1538
--   at:        2024-06-06 18:22:36 YEKT
--   site:      Oracle Database 12c
--   type:      Oracle Database 12c



-- predefined type, no DDL - MDSYS.SDO_GEOMETRY

-- predefined type, no DDL - XMLTYPE

CREATE TABLE "Order" (
    id            INTEGER NOT NULL,
    product_code  INTEGER NOT NULL,
    warehouse_id  INTEGER NOT NULL,
    count         INTEGER,
    cost          INTEGER
)
LOGGING;

CREATE UNIQUE INDEX "order" ON
    "Order" (
        id
    ASC );

ALTER TABLE "Order" ADD CONSTRAINT order_pk PRIMARY KEY ( id );

CREATE TABLE product (
    code  INTEGER NOT NULL,
    name  VARCHAR2(200)
)
LOGGING;

ALTER TABLE product ADD CONSTRAINT product_pk PRIMARY KEY ( code );

CREATE TABLE sale (
    id            INTEGER NOT NULL,
    count         INTEGER,
    "date"        DATE,
    cost          INTEGER,
    product_code  INTEGER NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX sale ON
    sale (
        product_code
    ASC );

ALTER TABLE sale ADD CONSTRAINT sale_pk PRIMARY KEY ( id );

CREATE TABLE warehouse (
    id            INTEGER NOT NULL,
    count         INTEGER,
    order_id      INTEGER NOT NULL,
    product_code  INTEGER NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX warehouse ON
    warehouse (
        order_id
    ASC,
        product_code
    ASC );

ALTER TABLE warehouse ADD CONSTRAINT warehouse_pk PRIMARY KEY ( id );

ALTER TABLE "Order"
    ADD CONSTRAINT order_product_fk FOREIGN KEY ( product_code )
        REFERENCES product ( code )
    NOT DEFERRABLE;

ALTER TABLE "Order"
    ADD CONSTRAINT order_warehouse_fk FOREIGN KEY ( warehouse_id )
        REFERENCES warehouse ( id )
    NOT DEFERRABLE;

ALTER TABLE sale
    ADD CONSTRAINT sale_product_fk FOREIGN KEY ( product_code )
        REFERENCES product ( code )
    NOT DEFERRABLE;

ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_product_fk FOREIGN KEY ( product_code )
        REFERENCES product ( code )
    NOT DEFERRABLE;



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                             4
-- CREATE INDEX                             3
-- ALTER TABLE                              8
-- CREATE VIEW                              0
-- ALTER VIEW                               0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                           0
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          0
-- CREATE MATERIALIZED VIEW                 0
-- CREATE MATERIALIZED VIEW LOG             0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- TSDP POLICY                              0
-- 
-- ORDS DROP SCHEMA                         0
-- ORDS ENABLE SCHEMA                       0
-- ORDS ENABLE OBJECT                       0
-- 
-- ERRORS                                   0
-- WARNINGS                                 0
