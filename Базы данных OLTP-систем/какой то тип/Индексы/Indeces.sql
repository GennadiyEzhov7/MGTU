create table TEST_IDX as
select owner,
       object_Name,
       object_id,
       object_type
from all_objects;

begin
    for i in 1..6
        loop
            insert into TEST_IDX
            select owner,
                   object_Name,
                   object_id + i * 10000,
                   object_type
            from all_objects;
        end loop;
end;

--A
select *
from TEST_IDX
where object_id = 0;
Create index test_idx_objId on test_idx (object_id) nologging compute statistics;

--B
SELECT /*+ FULL(SHcustomers) */ *
FROM SHcustomers
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804
  AND cust_credit_limit = 10000;

SELECT c.*
FROM SHcustomers c
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804
  AND cust_credit_limit = 10000;

--C
CREATE INDEX cust_cust_gender_idx ON SHcustomers (cust_gender) NOLOGGING COMPUTE STATISTICS;
CREATE INDEX cust_cust_postal_code_idx ON SHcustomers (cust_postal_code) NOLOGGING COMPUTE STATISTICS;
CREATE INDEX cust_cust_credit_limit_idx ON SHcustomers (cust_credit_limit) NOLOGGING COMPUTE STATISTICS;

begin
    dbms_stats.GATHER_INDEX_STATS('UNIVERSITY', 'cust_cust_gender_idx');
    dbms_stats.GATHER_INDEX_STATS('UNIVERSITY', 'cust_cust_postal_code_idx');
    dbms_stats.GATHER_INDEX_STATS('UNIVERSITY', 'cust_cust_credit_limit_idx');
end;

select *
from user_indexes
where index_name like 'CUST%';

ALTER INDEX CUSTOMERS_PK MONITORING USAGE;
ALTER INDEX CUST_CUST_POSTAL_CODE_IDX MONITORING USAGE;
ALTER INDEX CUST_CUST_GENDER_IDX MONITORING USAGE;
ALTER INDEX CUST_CUST_CREDIT_LIMIT_IDX MONITORING USAGE;

select *
from v$object_usage;

SELECT /*+ INDEX(c) */ c.*
FROM SHcustomers c
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804
  AND cust_credit_limit = 10000;

SELECT c.*
FROM SHcustomers c
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804
  AND cust_credit_limit = 10000

--D
drop index CUST_CUST_GENDER_IDX;
drop index CUST_CUST_POSTAL_CODE_IDX;
drop index CUST_CUST_CREDIT_LIMIT_IDX;

CREATE INDEX cust_gender_limit_code_idx ON SHcustomers (cust_gender, cust_credit_limit, cust_postal_code) NOLOGGING COMPUTE STATISTICS;

SELECT /*+ INDEX(c) */ c.*
FROM SHcustomers c
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804
  AND cust_credit_limit = 10000;

SELECT /*+ INDEX(c cust_gender_limit_code_idx) */ c.*
FROM Shcustomers c
WHERE cust_gender = 'M'
  AND cust_credit_limit = 10000;

--E
SELECT /*+ INDEX(c cust_gender_limit_code_idx) */ c.*
FROM SHcustomers c
WHERE cust_gender = 'M'
  AND cust_postal_code = 40804;

--F
SELECT /*+ INDEX(c) */ c.*
FROM SHcustomers c
WHERE cust_postal_code = 40804
  AND cust_credit_limit = 10000;

--G
CREATE INDEX cust_last_first_name_idx ON SHcustomers (cust_last_name, cust_first_name) NOLOGGING COMPUTE STATISTICS;

SELECT c.cust_last_name, c.cust_first_name
FROM SHcustomers c;

SELECT c.cust_first_name, c.cust_last_name
FROM SHcustomers c;

SELECT c.cust_first_name, c.cust_last_name, c.CUST_GENDER
FROM SHcustomers c;

--H
CREATE INDEX cust_last_name_idx ON SHcustomers (cust_last_name) NOLOGGING COMPUTE STATISTICS;
CREATE INDEX cust_first_name_idx ON SHcustomers (cust_first_name) NOLOGGING COMPUTE STATISTICS;

SELECT /*+ INDEX_JOIN(c cust_first_name_idx cust_last_name_idx) */ c.cust_last_name, c.cust_first_name
FROM SHcustomers c;

--I
SELECT cust_id, country_id FROM SHcustomers WHERE LOWER( cust_last_name) LIKE 'gentle';

--J
CREATE INDEX lower_cust_last_name_idx ON SHcustomers(LOWER(cust_last_name));

--K
CREATE table promotions_iot
(
    promo_id          number primary key,
    promo_name        VARCHAR2(40),
    promo_subcategory VARCHAR2(30),
    promo_category    VARCHAR2(30),
    promo_cost        NUMBER,
    promo_begin_date  DATE,
    promo_end_date    DATE
) ORGANIZATION INDEX;

INSERT INTO promotions_iot
SELECT promo_id, promo_name, promo_subcategory, promo_category, promo_cost, promo_begin_date, promo_end_date
FROM Shpromotions;

SELECT /*+ NO_INDEX(s) */ * FROM SHpromotions s WHERE promo_id > 300;

SELECT /*+ INDEX(promotions) */ * FROM SHpromotions WHERE promo_id > 300;

--L
SELECT * FROM promotions_iot WHERE promo_id > 300;

--M
create table t(c number, d number);

begin
 for i in 1..10000 loop
   insert into t values(1,i);
 end loop;
end;

create index it on t(c,d);

select count(*) from t where d=1;

--N
begin
    dbms_stats.gather_table_stats('UNIVERSITY','T',cascade=>TRUE);
end;

begin
    dbms_stats.DELETE_TABLE_STATS('UNIVERSITY','T');
end;

--O
select /*+ INDEX_FFS(t it) */ count(*) from t where d=1;
