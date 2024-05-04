--1
Select manager_id, job_id, sum(SALARY) from employees where manager_id is not null group by ROLLUP(manager_id, job_id);

--2
Select manager_id, job_id, sum(SALARY) from employees where manager_id is not null group by CUBE(manager_id, job_id);

--3
Select Department_id, manager_id, job_id, sum(SALARY) from employees GROUP BY GROUPING SETS((Department_id,  manager_id, job_id), (Manager_id, job_id), (Department_id,  manager_id));