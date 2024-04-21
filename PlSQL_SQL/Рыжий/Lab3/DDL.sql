CREATE TABLE "Order" (
    id            INTEGER NOT NULL,
    product_code  INTEGER NOT NULL,
    count         INTEGER,
    cost          INTEGER
)
LOGGING;

CREATE UNIQUE INDEX order__idxv1 ON
    "Order" (
        product_code
    ASC )
        LOGGING;

ALTER TABLE "Order" ADD CONSTRAINT order_pk PRIMARY KEY ( id );

CREATE TABLE product (
    code          INTEGER NOT NULL,
    name          VARCHAR2(4000),
    vendor_id     INTEGER NOT NULL,
    warehouse_id  INTEGER
)
LOGGING;

ALTER TABLE product ADD CONSTRAINT product_pk PRIMARY KEY ( code );

CREATE TABLE sale (
    id            INTEGER NOT NULL,
    count         INTEGER,
    cost          INTEGER,
    product_code  INTEGER NOT NULL
)
LOGGING;

CREATE UNIQUE INDEX sale__idxv1 ON
    sale (
        product_code
    ASC )
        LOGGING;

ALTER TABLE sale ADD CONSTRAINT sale_pk PRIMARY KEY ( id );

CREATE TABLE t_journal (
    warehouse_id  INTEGER NOT NULL,
    name          VARCHAR2(4000),
    change_count  INTEGER,
    time          TIMESTAMP,
    trantime      TIMESTAMP NOT NULL
)
LOGGING;

ALTER TABLE t_journal ADD CONSTRAINT t_journal_pk PRIMARY KEY ( trantime,
                                                                warehouse_id );

CREATE TABLE t_order (
    "start"   TIMESTAMP,
    end       TIMESTAMP,
    order_id  INTEGER NOT NULL,
    trantime  TIMESTAMP NOT NULL
)
LOGGING;

ALTER TABLE t_order ADD CONSTRAINT t_order_pk PRIMARY KEY ( trantime,
                                                            order_id );

CREATE TABLE t_sale (
    time      TIMESTAMP,
    sale_id   INTEGER NOT NULL,
    trantime  TIMESTAMP NOT NULL
)
LOGGING;

ALTER TABLE t_sale ADD CONSTRAINT t_sale_pk PRIMARY KEY ( trantime,
                                                          sale_id );

CREATE TABLE vendor (
    id       INTEGER NOT NULL,
    name     VARCHAR2(4000),
    address  VARCHAR2(4000),
    phone    VARCHAR2(4000)
)
LOGGING;

ALTER TABLE vendor ADD CONSTRAINT vendor_pk PRIMARY KEY ( id );

CREATE TABLE warehouse (
    id            INTEGER NOT NULL,
    count         INTEGER,
    product_code  INTEGER NOT NULL,
    order_id      INTEGER
)
LOGGING;

CREATE UNIQUE INDEX warehouse__idx ON
    warehouse (
        order_id
    ASC )
        LOGGING;

ALTER TABLE warehouse ADD CONSTRAINT warehouse_pk PRIMARY KEY ( id );

ALTER TABLE "Order"
    ADD CONSTRAINT order_product_fk FOREIGN KEY ( product_code )
        REFERENCES product ( code )
    NOT DEFERRABLE;

ALTER TABLE product
    ADD CONSTRAINT product_vendor_fk FOREIGN KEY ( vendor_id )
        REFERENCES vendor ( id )
    NOT DEFERRABLE;

ALTER TABLE product
    ADD CONSTRAINT product_warehouse_fk FOREIGN KEY ( warehouse_id )
        REFERENCES warehouse ( id )
    NOT DEFERRABLE;

ALTER TABLE sale
    ADD CONSTRAINT sale_product_fk FOREIGN KEY ( product_code )
        REFERENCES product ( code )
    NOT DEFERRABLE;

ALTER TABLE t_journal
    ADD CONSTRAINT t_journal_warehouse_fk FOREIGN KEY ( warehouse_id )
        REFERENCES warehouse ( id )
    NOT DEFERRABLE;

ALTER TABLE t_order
    ADD CONSTRAINT t_order_order_fk FOREIGN KEY ( order_id )
        REFERENCES "Order" ( id )
    NOT DEFERRABLE;

ALTER TABLE t_sale
    ADD CONSTRAINT t_sale_sale_fk FOREIGN KEY ( sale_id )
        REFERENCES sale ( id )
    NOT DEFERRABLE;

ALTER TABLE warehouse
    ADD CONSTRAINT warehouse_order_fk FOREIGN KEY ( order_id )
        REFERENCES "Order" ( id )
    NOT DEFERRABLE;

/
create or replace TRIGGER Trg4 
    BEFORE INSERT ON "Order" 
    FOR EACH ROW 
    BEGIN
        INSERT INTO t_Order (Order_id, tranzTime) VALUES (:new.id, CURRENT_TIMESTAMP) ;
   END; 

/
create or replace TRIGGER Trg2 
    BEFORE INSERT ON "SALE"
    FOR EACH ROW
    DECLARE
    cnt INT;
    BEGIN
        SELECT sale.count AS cnt
        FROM Warehouse 
        WHERE Product_code = :new.Product_code;
        
        IF (:new.count > 0) AND (:new.count <= cnt) THEN
            INSERT INTO t_Sale (Order_id, tranzTime) VALUES (:new.id, CURRENT_TIMESTAMP);
        ELSE
            raise_application(-20001, 'Несуществующее кол-во продукта') ;
        END IF;
    END;

/
create or replace TRIGGER Trg5 
    BEFORE INSERT OR UPDATE ON Warehouse 
    FOR EACH ROW 
    BEGIN
        INSERT INTO t_journal (Warehouse_id, name, change_count, tranzTime) 
            VALUES (:new.id, USER, :new.count - nvl(:old.count, 0), CURRENT_DATE) ;
    END;
