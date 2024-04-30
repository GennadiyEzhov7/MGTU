--Creating tables
create table EMP_COPY as select * from emp where 1=0;
insert into EMP_COPY select * from emp;
select * from EMP_COPY;

create table DEPT_COPY as select * from DEPT where 1=0;
insert into DEPT_COPY select * from DEPT;
select * from DEPT_COPY;

--Clear cache
begin
  dbms_stats.delete_table_stats('SCOTT','DDEPT');
  dbms_stats.delete_table_stats('SCOTT','EEMP');
end;

--Create indices
create index DEPTNO_IDX on emp(deptno);
drop index JOB_IDX;
drop index DEPTNO_IDX;
drop index EMP_DEPT_IDX;

--Get plan
SELECT * FROM TABLE(dbms_xplan.display(null,null,'basic'));

--1
Select * from emp_copy where job like 'MANA%';
create index JOB_IDX on emp_copy(job);

--2
Select * from emp_copy where upper(job) like upper('Mana%');
Create index I_JOB_UPPER on emp_copy (upper(job));
drop index I_JOB_UPPER;

--3
select
    ename,
    job,
    e.deptno,
    dname,
    loc
from
    emp_copy e,
    dept_copy d
where
    e.deptno=d.deptno and dname = 'SALES' and upper(job) like upper('Mana%');

select
    /*+ RULE */ ename,
    job,
    e.deptno,
    dname,
    loc
from
    emp_copy e,
    dept_copy d
where
    e.deptno=d.deptno and dname = 'SALES' and upper(job) like upper('Mana%');

create index emp_deptno_fk on emp_copy (deptno);