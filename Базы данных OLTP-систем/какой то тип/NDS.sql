DECLARE
    dept     NUMBER;
    emp_row  EMPLOYEES%ROWTYPE;
    min_job  varchar(25);
    last_sal NUMBER;
    new_sal  NUMBER;
BEGIN
    EXECUTE IMMEDIATE 'create table C_EMP as select * from EMPLOYEES';
    dbms_output.put_line(rpad('NAME', 18) || '|' || rpad('JOB', 7) || '|' || rpad('NEW SAL', 7) || '|' ||
                         rpad('OLD SAL', 7) || '|' || rpad('DEPT', 4));
    --dbms_output.put_line(rpad('_', 47, '_'));
    FOR dept in (select DEPARTMENT_ID from (select DEPARTMENT_ID from EMPLOYEES order by SALARY desc fetch first 6 rows only) group by DEPARTMENT_ID) LOOP
        --данный вариант работает немного быстрее чем с distinct
        EXECUTE IMMEDIATE 'SELECT job_id FROM C_EMP WHERE department_id = ' || to_char(dept.DEPARTMENT_ID) || ' ORDER BY salary fetch first rows only' INTO min_job;
        for emp_row in (SELECT * FROM employees WHERE department_id = dept.DEPARTMENT_ID AND job_id = min_job) LOOP
            EXECUTE IMMEDIATE 'SELECT salary FROM c_emp WHERE employee_id = ' ||
                              to_char(emp_row.employee_id) INTO last_sal;
            EXECUTE IMMEDIATE 'UPDATE c_emp SET salary = salary * 1.15 WHERE employee_id = ' ||
                              to_char(emp_row.employee_id);
            EXECUTE IMMEDIATE 'SELECT salary FROM c_emp WHERE employee_id = ' ||
                              to_char(emp_row.employee_id) INTO new_sal;
            dbms_output.put_line(rpad(emp_row.FIRST_NAME || ' ' || emp_row.LAST_NAME, 18) || '|' ||
                                 rpad(emp_row.JOB_ID, 7) || '|' || rpad(to_char(new_sal), 7) || '|' ||
                                 rpad(to_char(last_sal), 7) || '|' || rpad(to_char(dept.DEPARTMENT_ID), 4));
        END LOOP;
    END LOOP;
    EXECUTE IMMEDIATE 'DROP TABLE c_emp';
END;

DROP TABLE c_emp;

select * from employees order by EMPLOYEE_ID desc fetch first rows only;
select * from (select * from EMPLOYEES order by EMPLOYEE_ID desc) where rownum = 1;