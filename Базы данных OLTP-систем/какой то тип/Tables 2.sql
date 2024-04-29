--1
create cluster jobs_cluster_512 (job_id varchar2(10)) size 512;
create cluster jobs_cluster_1024 (job_id varchar2(10)) size 1024;
create cluster jobs_cluster_2048 (job_id varchar2(10)) size 2048;
create cluster jobs_cluster_4096 (job_id varchar2(10)) size 4096;

create index jobs_index_512 on cluster jobs_cluster_512;
create index jobs_index_1024 on cluster jobs_cluster_1024;
create index jobs_index_2048 on cluster jobs_cluster_2048;
create index jobs_index_4096 on cluster jobs_cluster_4096;

create table cluster_512
    cluster jobs_cluster_512 (job_id) as
        select * from job_info where 1=0;
create table cluster_1024
    cluster jobs_cluster_1024 (job_id) as
        select * from job_info where 1=0;
create table cluster_2048
    cluster jobs_cluster_2048 (job_id) as
        select * from job_info where 1=0;
create table cluster_4096
    cluster jobs_cluster_4096 (job_id) as
        select * from job_info where 1=0;

begin
    dbms_stats.gather_table_stats('SYSTEM', 'cluster_512');
    dbms_stats.gather_table_stats('SYSTEM', 'cluster_1024');
    dbms_stats.gather_table_stats('SYSTEM', 'cluster_2048');
    dbms_stats.gather_table_stats('SYSTEM', 'cluster_4096');
end;

select
    table_name,
    blocks
from
    user_tables
where
    table_name like 'CLUSTER_%';

/*
 Так как записи для всех таблиц одинаковы,
 размер выделенной памяти меняется в зависимости от значения SIZE
 */

select
    index_name,
    CLUSTERING_FACTOR
from
    user_indexes
where
    INDEX_NAME like 'INDEX_%';

/*
  Значение CLUSTERING_FACTOR - количество исопльзуемых блоков. Чем их больше, тем хуже работа индекса,
  т.к. данные разбросаны по этим самым блокам. Самое эффективный размер в нашем случае - 1024.
 */

--2
create cluster hash_cluster (job_id varchar(10)) size 512 hashkeys 10 hash is mod(job_id, 10);
create table hash_table cluster hash_cluster (job_id) as
    select *
    from jobs_history
    where 1=0;

select *
from user_clusters
where cluster_name = 'HASH_CLUSTER';
/*
 по сравнению с кластерами из предыдущего задания, отличие есть только в key_size.
 */

 --3
savepoint Employees_Canon;
create type Col_Type as object (id_col integer);
create type Table_Type is Table of Col_Type;

----
alter table EMPLOYEES add (Table_Col Table_Type) nested table Table_Col store as nested_table;
update EMPLOYEES set Table_Col = coalesce(Table_Col, Table_Type()) multiset union all Table_Type(Col_Type(1));
update EMPLOYEES set Table_Col = coalesce(Table_Col, Table_Type()) multiset union all Table_Type(Col_Type(2));
update EMPLOYEES set Table_Col = coalesce(Table_Col, Table_Type()) multiset union all Table_Type(Col_Type(3));

select * from EMPLOYEES;
/*
 В селекторе добавляется колонка, которая содержит подколонки.
 */
----возвращается копия
alter table EMPLOYEES add (Table_Col Table_Type) nested table Table_Col store as nested_table return as value;
----возвращается указатель
alter table EMPLOYEES add (Table_Col Table_Type) nested table Table_Col store as nested_table return as locator ;

ROLLBACK TO SAVEPOINT Employees_Canon;


--4
create type Prev_Job as object (jobId integer, startDate Date, endDate Date);
create type Prev_Jobs is Table of Prev_Job;
alter table EMPLOYEES add (JOBS Prev_Jobs) nested table JOBS store as NESTED_JOBS;