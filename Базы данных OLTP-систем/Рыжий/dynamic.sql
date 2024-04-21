BEGIN
    EXECUTE IMMEDIATE 'CREATE TABLE DYNAMIC_EMP AS SELECT * FROM HR.employees';
END;

SELECT * FROM DYNAMIC_EMP;

--

BEGIN
    EXECUTE IMMEDIATE 'SELECT * 
                            FROM DYNAMIC_EMP 
                            WHERE ROWNUM <= 6 
                            ORDER BY SALARY DESC';
END:

SELECT * 
FROM DYNAMIC_EMP 
WHERE ROWNUM <= 6 
ORDER BY SALARY DESC;

--

DECLARE 
   TYPE dynamic_emp_type IS REF CURSOR;
   cur dynamic_emp_type;
   dynamic_emp_row DYNAMIC_EMP%ROWTYPE;
   query_ VARCHAR2(200) := 'SELECT * FROM DYNAMIC_EMP WHERE ROWNUM <= 6 ORDER BY SALARY DESC'; 
BEGIN
    OPEN cur FOR query_; 
    FOR i IN 0..6
    LOOP
        FETCH cur INTO dynamic_emp_row;
        EXECUTE IMMEDIATE 'UPDATE DYNAMIC_EMP SET salary =' ||  dynamic_emp_row.salary * 1.15 || 'WHERE employee_id = ' || dynamic_emp_row.employee_id;
    END LOOP;     
    CLOSE cur;
    
    OPEN cur FOR query_; 
    FOR i IN 0..6
    LOOP
        FETCH cur INTO dynamic_emp_row;
        DBMS_OUTPUT.PUT_LINE(RPAD(dynamic_emp_row.employee_id, 3, ' ') || ' | ' || RPAD(dynamic_emp_row.first_name,10 , ' ') || ' | ' || RPAD(dynamic_emp_row.last_name, 10, ' ') || ' | ' || RPAD(dynamic_emp_row.JOB_ID, 6 , ' ') || ' | ' || RPAD(dynamic_emp_row.salary, 6, ' '));
    END LOOP;     
    CLOSE cur;
END;