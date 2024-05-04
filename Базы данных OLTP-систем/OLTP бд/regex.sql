--1.1 
with t as
(select 'H18.DHSHJ7' as str from dual
 union all  select 'H18.AAA1A' from dual
 union all  select 'H18.AAA10' from dual 
 union all  select 'H18.AAAB4B' from dual  
)
select str, CASE WHEN (rownum = 2 OR rownum = 4) THEN REGEXP_REPLACE(str, '.$', '')ELSE str END result 
from t;


--1.2 
with t as
(select 'H18.DHSHJ7' as str from dual
 union all  select 'H18.AAA1A' from dual
 union all  select 'H18.AAA10' from dual 
 union all  select 'H18.AAAB4B' from dual  
)
select str, REGEXP_REPLACE(str, '\d$', '')
from t;

--Если же требуется заменить последнее число вне зависимости от его позиции, то заменить регулярное на  '\d[^[:digit:]]*$'

--2.1
Select REGEXP_REPLACE('208010100000', '.', 'X', 5, 1) from dual;

--2.2
Select 'aabs@sdfdasf,abs@dev, sd abs@fasdf' str, REGEXP_REPLACE('aabs@sdfdasf,abs@dev, sd abs@fasdf', '(,| )(abs@)', '\1abs@fn') val from dual;

--2.3
Select '«#2 apps(0_-ORACLE+*.ru_ )ПРИ13мер' Str, 
        REGEXP_REPLACE('«#2 apps(0_-ORACLE+*.ru_ )ПРИ13мер', '[^[:alpha:]]', '') ONLY_CHAR, 
        REGEXP_REPLACE('«#2 apps(0_-ORACLE+*.ru_ )ПРИ13мер', '[^[:digit:]]', '') ONLY_NUMBER 
        from dual;
        
--3.1
Select REGEXP_SUBSTR('5646.45, 45 sd eds,.sd 9 2 566.11 12.3', '[[:digit:]]+\.[[:digit:]]+', 1,level,NULL) Str
from dual
connect by level <= REGEXP_COUNT('5646.45, 45 sd eds,.sd 9 2 566.11 12.3', '[[:digit:]]+\.[[:digit:]]+');

--3.2
with t as (
select 'есть длинная строка - которую нужно разбить на строчки по сто символов.'
|| 'Но слова разбивать нельзя, переносить нужно по словам. Слова более 100 символов не разрывать. '
||'1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890 '
||rpad('qwerty',110,'o')||' абвгд' as s from dual)
select REGEXP_SUBSTR(s, '(.{1,100} )|(.{1,100}(.*?(\s|\z)))', 1,level,NULL) from t
CONNECT BY level <= REGEXP_COUNT(s, '(.{1,100} )|(.{1,100}(.*?(\s|\z)))');

--4
WITH t AS (SELECT   'asdf 5346743 gggg 91.2 98463 945426 243.6' a FROM DUAL)
SELECT SUM( TO_NUMBER( REGEXP_SUBSTR(a, '\d', 1,level,NULL) ) ) SS
FROM   t
CONNECT BY LEVEL <= REGEXP_COUNT(a, '\d');
