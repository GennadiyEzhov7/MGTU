
drop table office;
drop table persona;
drop table contingent;
commit;

select * from emp;
create table Office(
  deptno    number(2),
  dname     varchar2(28),
  loc       varchar2(26),
  constraint PK_DeptNo PRIMARY KEY(deptno) 
);

create table Persona(
  empno     number(4),
  ename     varchar2(20),
  job       varchar2(18),
  mgr       number(4),
  hiredate  date,
  sal       number(7,2),
  comm      number(7,2),
  deptno    number(2),
  constraint PK_EmpNo PRIMARY KEY(empno),
  constraint FK_DeptNo FOREIGN KEY(deptno) references Office(deptno)
);

create table Contingent(
  Id_Rec    number(5),
  deptno    number(2),
  Num_employees number(2),
  Add_employees number(2),
  Del_employees number(2),
  constraint PK_Id_Rec PRIMARY KEY(Id_Rec)
);


create sequence contingent_id_rec
start with 1
increment by 1
NOCACHE
NOCYCLE;
commit;
drop sequence contingent_id_rec;
SET SERVEROUTPUT ON;
    
create or replace trigger office_insert 
after insert on office
for each row
declare
  cWhere      varchar2(50) := 'office_insert';
  DeptNo      number;
begin
  dbms_output.Put_Line(cWhere || ' begin');
  DeptNo := :new.deptno;
  dbms_output.Put_Line(cWhere || ' DeptNo = ' || DeptNo);
  insert into contingent values(contingent_id_rec.nextval, DeptNo, 0, 0, 0);
  dbms_output.put_line(cWhere || ' end');
end;

insert into Office select * from dept;
rollback;
select * from Contingent;
commit;

create or replace trigger persona_edit
after insert or delete on persona
for each row
declare
  cWhere      varchar2(20) := 'office_insert';
begin
  DBMS_OUTPUT.put_line(cWhere || ' begin');
  if (inserting) then
    update contingent
    set num_employees = num_employees + 1,
    Add_employees = add_employees + 1
    where deptno = :new.deptno;
  elsif (deleting) then
    DBMS_OUTPUT.put_line(cWhere || ' :old.deptno: ' || :old.deptno);
    update contingent
    set num_employees = num_employees - 1,
    Del_employees = Del_employees + 1
    where deptno = :old.deptno;
  end if;
  DBMS_OUTPUT.put_line(cWhere || ' end');
end;

create or replace trigger persona_empno_break
before update on persona
for each row
declare
  cWhere varchar2(20) := 'persona_empno_break';
begin
  if (:old.empno != :new.empno) then
    raise_application_error(-20001,'Отвержение');
  end if;
end;

insert into persona select * from emp;
commit;

delete from office where deptno != 10;

update persona
    set sal = 5 where empno = 7839;

select * from persona;
insert into persona values(1111, 'PASHA', 'CLERK', 7698, SYSDATE, 20, NULL, 40);

select * from contingent;
select * from office;
select * from persona;