--1
select ename, sal, round(avg(sal)over(),3) from emp; -- Время исполнения 0.083 сек. и 7 логических операций ввода-вывода
select ename, sal, (select round(sum(sal)/COUNT(sal),3) from emp) avg_sal from emp;-- Время исполнения 0.116 сек. и 14 логических операций ввода-вывода

--2
select ename, round(sum(sal)/COUNT(sal),3) from emp group by ename order by ename;-- Время исполнения 0.101 сек. и 9 логических операций ввода-вывода
select ename, sal, (select round(sum(e2.sal)/COUNT(e2.sal),3) from emp e2 where e2.ename <= emp.ename) avg_sal from emp order by ename;-- Время исполнения 0.111 сек. и 105 логических операций ввода-вывода

--3
select ename, deptno, sum (sal) over(order by ename, deptno) sum_ename_deptno,
sum(sal) over (order by deptno, ename) sum_deptno_ename
from emp order by ename,deptno;-- Время исполнения 0.102 сек. и 7 логических операций ввода-вывода

select emp.ename, emp.deptno, (select sum(e2.sal) from emp e2 where e2.ename <= emp.ename) sum_ename_deptno, 
sub.sum_deptno_ename 
from emp emp 
join (select ename, (select sum(e2.sal) from emp e2 where e2.deptno < emp.deptno or(e2.deptno = emp.deptno and e2.ename <= emp.ename)) sum_deptno_ename from emp order by deptno, ename) sub 
on emp.ename = sub.ename order by ename;-- Время исполнения 0.118 сек. и 211 логических операций ввода-вывода


--4
select deptno, ename, sal,
sum(sal) over (partition by deptno order by ename rows 2 preceding) sliding_total from emp order by deptno, ename;-- Время исполнения 0.101 сек. и 7 логических операций ввода-вывода

select deptno, ename, sal, 
(select sum(s.sal) from (select e2.sal from emp e2 where e2.deptno = emp.deptno and e2.ename <= emp.ename order by e2.deptno desc, e2.ename desc fetch first 3 rows only) s) sliding_total
from emp order by deptno, ename;-- Время исполнения 0.113 сек. и 111 логических операций ввода-вывода

--5
select ename, sal, hiredate, hiredate-100 windows_top,
first_value(ename) over (order by hiredate asc range 100 preceding) ename_perc,
first_value (hiredate) over(order by hiredate asc range 100 preceding) hiredate_prec
from emp order by hiredate asc;-- Время исполнения 0.114 сек. и 13 логических операций ввода-вывода

select ename, sal, hiredate, hiredate-100 windows_top,
(select ename from (select e2.ename from emp e2 where e2.hiredate > emp.hiredate - 100 order by e2.hiredate asc fetch first row only)) ename_perc,
(select hiredate from (select e2.hiredate from emp e2 where e2.hiredate > emp.hiredate - 100 order by e2.hiredate asc fetch first row only)) hiredate_prec from emp 
order by hiredate;-- Время исполнения 0.126 сек. и 189 логических операций ввода-вывода


--6
select ename, hiredate, sal, round (avg(sal) over (order by hiredate asc range 100 preceding),2) avg_sal_before_100, round( avg(sal) over (order by hiredate desc range 100 preceding),2) avg_sal_after_100
from emp order by hiredate;-- Время исполнения 0.1 сек. и 7 логических операций ввода-вывода

select ename, hiredate, sal, 
(select round(sum(sal)/count(sal), 2) from emp e2 where e2.hiredate >= emp.hiredate-100 and e2.hiredate <= emp.hiredate) avg_sal_before_100,
(select round(sum(sal)/count(sal), 2) from emp e2 where e2.hiredate >= emp.hiredate and e2.hiredate <= emp.hiredate + 100) avg_sal_after_100
from emp order by hiredate;-- Время исполнения 0.103 сек. и 195 логических операций ввода-вывода


--7
select ename, hiredate, sal ,first_value(ename) over (order by hiredate asc rows 5 preceding) ename_prec, first_value (hiredate) over (order by hiredate asc rows 5  preceding) hiredate_prec
from emp order by hiredate asc;-- Время исполнения 0.108 сек. и 7 логических операций ввода-вывода

WITH numbers as (select ROWNUM rn, a.* from(select ename, hiredate from emp order by hiredate) a)
select ename, hiredate, sal, (Select ename from numbers where( 
case when (Select rn from numbers where ename = emp.ename) - 5 > 0 AND
            rn = ((Select rn from numbers where ename = emp.ename) - 5)
        then 1
     when (Select rn from numbers where ename = emp.ename) - 5 <= 0 AND rn = 1
        then 1
    else 0
    end)=1) ename_prec, (Select hiredate from numbers where( 
case when (Select rn from numbers where ename = emp.ename) - 5 > 0 AND
            rn = ((Select rn from numbers where ename = emp.ename) - 5)
        then 1
     when (Select rn from numbers where ename = emp.ename) - 5 <= 0 AND rn = 1
        then 1
    else 0
    end)=1) hiredate_prec from emp order by hiredate asc;-- Время исполнения 0.114 сек. и 14 логических операций ввода-вывода





--8
select ename, hiredate, first_value (ename) over (order by hiredate asc range between 100 preceding and 100 following) as a1,  
last_value (ename) over (order by hiredate asc range between 100 preceding and 100 following)as a2
from emp order by hiredate;-- Время исполнения 0.101 сек. и 9 логических операций ввода-вывода

select ename, hiredate, 
(select ename from (select e2.ename from emp e2 where e2.hiredate > emp.hiredate - 100 order by e2.hiredate asc fetch first row only)) A1,
(select ename from (select e2.ename from emp e2 where e2.hiredate <= emp.hiredate + 100 order by e2.hiredate desc fetch first row only)) A2 from emp 
order by hiredate;-- Время исполнения 0.092 сек. и 201 логических операций ввода-вывода

