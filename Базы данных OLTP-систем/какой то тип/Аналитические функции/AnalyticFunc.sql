--1
select ename,
       deptno,
       sal,
       sum(sal) over (order by deptno, ename)                 running_total,
       sum(sal) over (partition by deptno order by ename)     departmint_total,
       row_number() over (partition by deptno order by ename) seq
from emp
order by deptno, ename;
--minus
select ename,
       deptno,
       sal,
       (select sum(sal)
        from emp e2
        where e2.deptno < emp.deptno
           or (e2.deptno = emp.deptno and e2.ename <= emp.ename)) running_total,
       (select sum(sal)
        from emp e3
        where e3.deptno = emp.deptno
          and e3.ename <= emp.ename)                              departament_total,
       (select count(ename)
        from emp e4
        where e4.deptno = emp.deptno
          and e4.ename <= emp.ename)                              seq
from emp
order by deptno, ename;

--2
select ename,
       sal,
       round(avg(sal) over (order by ename), 3) avg
from emp;
--minus
select ename,
       sal,
       round((select avg(sal) from emp e2 where e2.ename <= emp.ename), 3) avg
from emp
order by ename;


--3
select ename,
       deptno,
       sum(sal) over (order by ename, deptno) sum_ename_deptno,
       sum(sal) over (order by deptno, ename) sum_deptno_ename
from emp
order by ename, deptno;
--minus
select ename,
       deptno,
       (select sum(sal)
        from emp e2
        where e2.ename < emp.ename
           or (e2.ename = emp.ename and e2.deptno <= emp.deptno)) sum_ename_deptno,
       (select sum(sal)
        from emp e2
        where e2.deptno < emp.deptno
           or (e2.deptno = emp.deptno and e2.ename <= emp.ename)) sum_deptno_ename
from emp
order by ename, deptno;


--4
select deptno,
       ename,
       sal,
       sum(sal) over (partition by deptno order by ename rows 2 preceding) sliding_total
from emp
order by deptno, ename;
--minus
select deptno,
       ename,
       sal,
       (select sum(e2.sal)
        from (select * from emp order by ename desc) e2
        where e2.deptno = emp.deptno
          and e2.ename <= emp.ename
          and rownum <= 3) sliding_total
from emp
order by deptno, ename;

--5
select ename,
       sal,
       hiredate,
       hiredate - 100                                                         windows_top,
       first_value(ename) over (order by hiredate asc range 100 preceding)    ename_perc,
       first_value(hiredate) over (order by hiredate asc range 100 preceding) hiredate_prec
from emp
order by hiredate asc;

select ename,
       sal,
       hiredate,
       hiredate - 100                            windows_top,
       (select e2.ename
        from emp e2
        where e2.hiredate between emp.HIREDATE - 100 and emp.HIREDATE
        order by hiredate fetch first rows only) ename_perc,
       (select e2.hiredate
        from emp e2
        where e2.hiredate between emp.HIREDATE - 100 and emp.HIREDATE
        order by hiredate fetch first rows only) hiredate_perc
from emp
order by hiredate;

--6
select ename,
       hiredate,
       sal,
       round(avg(sal) over (order by hiredate asc range 100 preceding), 2)  avg_sal_before_100,
       round(avg(sal) over (order by hiredate desc range 100 preceding), 2) avg_sal_after_100
from emp
order by hiredate;

select ename,
       hiredate,
       sal,
       round((select avg(e2.sal)
              from (select * from emp order by hiredate) e2
              where e2.hiredate between emp.HIREDATE - 100 and emp.HIREDATE), 2) avg_sal_before_100,
       round((select avg(e2.sal)
              from (select * from emp order by hiredate desc) e2
              where e2.hiredate between emp.HIREDATE and emp.HIREDATE + 100), 2) avg_sal_after_100
from emp
order by hiredate;


--7
select ename,
       hiredate,
       sal,
       first_value(ename) over (order by hiredate asc rows 5 preceding)    ename_prec,
       first_value(hiredate) over (order by hiredate asc rows 5 preceding) hiredate_prec
from emp
order by hiredate asc;

select ename,
       hiredate,
       sal,
       (select ename
        from (select e2.ename, e2.hiredate
              from emp e2
              where e2.hiredate <= emp.hiredate
              order by e2.hiredate desc
                  fetch first 6 rows only) e3
        order by hiredate
            fetch first rows only) ename_prec,
       (select hiredate
        from (select e2.hiredate
              from emp e2
              where e2.hiredate <= emp.hiredate
              order by e2.hiredate desc
                  fetch first 6 rows only) e3
        order by hiredate
            fetch first rows only) hiredate_prec
from emp
order by hiredate;

--8
select ename,
       hiredate,
       first_value(ename) over (order by hiredate asc range between 100 preceding and 100 following) as a1,
       last_value(ename) over (order by hiredate asc range between 100 preceding and 100 following)  as a2
from emp
order by hiredate;

select ename,
       hiredate,
       (select e2.ename
        from emp e2
        where e2.hiredate between emp.HIREDATE - 100 and emp.HIREDATE + 100
        order by hiredate
            fetch first rows only) a1,
       (select e2.ename
        from emp e2
        where e2.hiredate between emp.HIREDATE - 100 and emp.HIREDATE + 100
        order by hiredate desc
            fetch first rows only) a2
from emp
order by hiredate;

