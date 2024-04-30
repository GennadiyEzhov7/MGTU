--1
select manager_id,
       job_id,
       sum(salary)
from EMPLOYEES
group by rollup (manager_id, job_id);

--2
select manager_id,
       job_id,
       sum(salary)
from employees
group by cube (manager_id, job_id);

--3
select department_id,
       manager_id,
       job_id,
       sum(salary)
from employees
group by grouping sets (
    (department_id, manager_id, job_id),
    ( manager_id, job_id),
    ( department_id, manager_id)
);

select /*TestTest*/ * from v$SQL where sql_text like '%/*TestTest*/%';
select /*TestTest*/ * from v$SQL where SORTS = :s;