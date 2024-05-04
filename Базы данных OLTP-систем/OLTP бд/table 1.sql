/*
Create table longStrings
( recNumber number NOT NULL, 
longLongString LONG NOT NULL
);

BEGIN  
FOR loop_counter IN 1..100 LOOP 
INSERT INTO longStrings (recNumber, longLongString) 
VALUES (loop_counter, dbms_random.string('L', 8000)); 
END LOOP;
COMMIT; 
END;

Select * from longStrings;

insert into longStrings (recNumber, longLongString) values (101, dbms_random.string('L', 8000));
insert into longStrings (recNumber, longLongString) values (102, dbms_random.string('L', 8000));
delete from longStrings where recNumber = 5;
delete from longStrings where recNumber = 13;
delete from longStrings where recNumber = 10;
insert into longStrings (recNumber, longLongString) values (103, dbms_random.string('L', 8000));
insert into longStrings (recNumber, longLongString) values (104, dbms_random.string('L', 8000));
delete from longStrings where recNumber = 99;
insert into longStrings (recNumber, longLongString) values (105, dbms_random.string('L', 8000));

Select * from longStrings;

insert into longStrings (recNumber, longLongString) values (101, dbms_random.string('L', 8000));
insert into longStrings (recNumber, longLongString) values (102, dbms_random.string('L', 8));
delete from longStrings where recNumber = 5;
delete from longStrings where recNumber = 13;
delete from longStrings where recNumber = 10;
insert into longStrings (recNumber, longLongString) values (103, dbms_random.string('L', 80));
insert into longStrings (recNumber, longLongString) values (104, dbms_random.string('L', 8000));
delete from longStrings where recNumber = 99;
insert into longStrings (recNumber, longLongString) values (105, dbms_random.string('L', 8));

Select * from longStrings;*/

Create table iot (owner, object_type, object_name,  constraint iot_pk primary key(owner,object_type,object_name)) organization index NOCOMPRESS as select  distinct owner, object_type, object_name from all_objects;
select * from iot;
analyze index iot_pk validate structure;
select lf_blks, br_blks, used_space,
       opt_cmpr_count, opt_cmpr_pctsave
  from index_stats;

Create table iot1 (owner, object_type, object_name,  constraint iot_pk1 primary key(owner,object_type,object_name)) organization index COMPRESS 1 as select distinct owner, object_type, object_name from all_objects;
analyze index iot_pk1 validate structure;
select lf_blks, br_blks, used_space,
       opt_cmpr_count, opt_cmpr_pctsave
  from index_stats;
  
Create table iot2 (owner, object_type, object_name,  constraint iot_pk2 primary key(owner,object_type,object_name)) organization index COMPRESS 2 as select distinct owner, object_type, object_name from all_objects;
analyze index iot_pk2 validate structure;
select lf_blks, br_blks, used_space,
       opt_cmpr_count, opt_cmpr_pctsave
  from index_stats;

