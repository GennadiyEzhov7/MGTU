CREATE TABLE new_jobs
AS (SELECT hr.jobs.job_id, hr.jobs.job_title,  hr.job_history.employee_id, hr.job_history.department_id, hr.job_history.start_date, hr.job_history.end_date, hr.jobs.min_salary, hr.jobs.max_salary
    FROM hr.jobs, hr.job_history);

select * from new_jobs;

drop table new_jobs;
