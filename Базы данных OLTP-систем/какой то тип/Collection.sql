--1
CREATE TYPE typ_item AS OBJECT
(
    prodid NUMBER(5),
    price  NUMBER(7, 2)
)
/

CREATE TYPE typ_item_nst
AS TABLE OF typ_item
/

CREATE TABLE pOrder
(
    ordid     NUMBER(5),
    supplier  NUMBER(5),
    requester NUMBER(4),
    ordered   DATE,
    items     typ_item_nst
) NESTED TABLE items STORE AS item_stor_tab
/

--2, 3
INSERT INTO pOrder (ordid, supplier, requester, ordered, items) VALUES (1003, 12345, 9876, SYSDATE, NULL);

INSERT INTO TABLE (SELECT items FROM pOrder WHERE ordid = 1000) VALUES(typ_item(99, 129.00));--сработает, т.к. items не null
INSERT INTO TABLE (SELECT items FROM pOrder WHERE ordid = 1003) VALUES(typ_item(99, 129.00));--не сработает, т.к. items null
update pOrder set items = typ_item_nst() multiset union all typ_item_nst(typ_item(999, 1299.00)) where ordid = 1000;--без разницы null или нет

select * from pOrder;

--4
CREATE TYPE typ_cr_card AS OBJECT
(
    card_type VARCHAR2 (25),
    card_num NUMBER
)
/

CREATE TYPE typ_cr_card_nst
AS TABLE OF typ_cr_card
/

ALTER TABLE SHCUSTOMERS ADD (credit_cards typ_cr_card_nst) NESTED TABLE credit_cards STORE AS c_c_store_tab;

--5
create or replace package credit_card_pkg is
    procedure display_card_info(c_id in integer);
    procedure insert_card_info(p_cust_id in integer, arg_name in varchar2, arg_number in integer);
end credit_card_pkg;
/
create or replace PACKAGE BODY credit_card_pkg IS
    procedure display_card_info (c_id integer) is
        v_card_info typ_cr_card_nst;
    begin
        SELECT
            credit_cards INTO v_card_info
        FROM
            SHCUSTOMERS
        WHERE
            cust_id = c_id;

        DBMS_OUTPUT.PUT_LINE('Customer id: ' || to_char(c_id));
        IF v_card_info.EXISTS(1) THEN
            for item in (select card_type, card_num from table(v_card_info)) loop
                DBMS_OUTPUT.PUT_LINE('Card: ' || item.card_type || ' Number: ' || to_char(item.card_num));
            end loop;
        ELSE
            DBMS_OUTPUT.PUT_LINE('No card');
        END IF;
    end display_card_info;

    procedure insert_card_info (p_cust_id integer, arg_name varchar2, arg_number integer) is
        v_card_info typ_cr_card_nst;
        is_ex integer;
    begin
        select
            CREDIT_CARDS into v_card_info
        from
            SHCUSTOMERS
        where
            cust_id = p_cust_id;

        if v_card_info.EXISTS(1) then
            select
                count(*) into is_ex
            from
                table(v_card_info)
            where
                card_num = arg_number;

            if is_ex = 0 then
                update SHCUSTOMERS set credit_cards = coalesce(credit_cards, typ_cr_card_nst()) multiset union all typ_cr_card_nst(typ_cr_card(arg_name, arg_number)) where cust_id = p_cust_id;
            else
                update table(select CREDIT_CARDS from SHCUSTOMERS where cust_id = p_cust_id) set card_type = arg_name where card_num = arg_name;
            end if;
        else
            update SHCUSTOMERS set credit_cards = coalesce(credit_cards, typ_cr_card_nst()) multiset union all typ_cr_card_nst(typ_cr_card(arg_name, arg_number)) where cust_id = p_cust_id;
        end if;
    end insert_card_info;
end credit_card_pkg;

select * from SHCUSTOMERS where credit_cards is not null;

begin
    CREDIT_CARD_PKG.display_card_info(100);
end;

begin
    CREDIT_CARD_PKG.insert_card_info(100, 'Mastercard', 111111111);
end;

--7
select
    Cust.cust_id,
    Cust.CUST_LAST_NAME,
    Cards.*
from
    SHCUSTOMERS Cust,
    table(select CREDIT_CARDS from SHCUSTOMERS where cust_id = Cust.cust_id) Cards;
--where Cust.cust_id = 100
