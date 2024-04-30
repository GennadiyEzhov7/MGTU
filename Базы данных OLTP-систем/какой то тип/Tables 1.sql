create table LongTable(
    id integer primary key,
    bigText clob
);

--1
declare
    text clob := 'aaaaaa';
    longText clob := 'aaaaaa';
    time number;
begin
    for i in 1..1000 loop
        longText := longText || text;
    end loop;

    time := DBMS_UTILITY.GET_TIME();
    for i in 1..1000 loop
        insert into LongTable values (i, text);
    end loop;
    DBMS_OUTPUT.PUT_LINE('Short insert time: ' || (DBMS_UTILITY.GET_TIME() - time) / 100); --около 0.1 секунды
    delete from LongTable where 1=1;

    time := DBMS_UTILITY.GET_TIME();
    for i in 1..1000 loop
        insert into LongTable values (i, longText);
    end loop;
    DBMS_OUTPUT.PUT_LINE('Long insert time: ' || (DBMS_UTILITY.GET_TIME() - time) / 100); --примерно 1.5 - 2 секунды

    time := DBMS_UTILITY.GET_TIME();
    for i in 1..1000 loop
        delete from LongTable where id=i;
    end loop;
    DBMS_OUTPUT.PUT_LINE('Delete time: ' || (DBMS_UTILITY.GET_TIME() - time) / 100); --около 0.1 секунды

    time := DBMS_UTILITY.GET_TIME();
    for i in 1..500 loop
        insert into LongTable values (i, longText);
        insert into LongTable values (i + 500, text);
    end loop;
    DBMS_OUTPUT.PUT_LINE('Long-short insert time: ' || (DBMS_UTILITY.GET_TIME() - time) / 100); --примерно 0.5 - 1.5 секунды

    time := DBMS_UTILITY.GET_TIME();
    for i in 1..500 loop
        delete from LongTable where id=i;
        delete from LongTable where id=i+500;
    end loop;
    DBMS_OUTPUT.PUT_LINE('Long-short delete time: ' || (DBMS_UTILITY.GET_TIME() - time) / 100); --примерно 0.1 - 0.3 секунды
end;

--2
create table ALL_OBJECTS_NO as
    select owner, object_name, object_type
    from ALL_OBJECTS;
create index NO_COMPRESS_IDX on ALL_OBJECTS_NO (owner, object_name, object_type);

create table ALL_OBJECTS_LOW as
    select /*+ COMPRESS */ owner, object_name, object_type
    from ALL_OBJECTS;
create index LOW_COMPRESS_IDX on ALL_OBJECTS_LOW (owner, object_name, object_type);

create table ALL_OBJECTS_MEDIUM as
    select /*+ COMPRESS (MEDIUM) */ owner, object_name, object_type
    from ALL_OBJECTS;
create index MEDIUM_COMPRESS_IDX on ALL_OBJECTS_MEDIUM (owner, object_name, object_type);

create table ALL_OBJECTS_HIGH as
    select /*+ COMPRESS (HIGH) */ owner, object_name, object_type
    from ALL_OBJECTS;
create index HIGH_COMPRESS_IDX on ALL_OBJECTS_HIGH (owner, object_name, object_type);

begin
    dbms_stats.gather_index_stats('UNIVERSITY', 'NO_COMPRESS_IDX');
    dbms_stats.gather_index_stats('UNIVERSITY', 'LOW_COMPRESS_IDX');
    dbms_stats.gather_index_stats('UNIVERSITY', 'MEDIUM_COMPRESS_IDX');
    dbms_stats.gather_index_stats('UNIVERSITY', 'HIGH_COMPRESS_IDX');
end;

begin
    dbms_stats.DELETE_INDEX_STATS('UNIVERSITY', 'NO_COMPRESS_IDX');
    dbms_stats.DELETE_INDEX_STATS('UNIVERSITY', 'LOW_COMPRESS_IDX');
    dbms_stats.DELETE_INDEX_STATS('UNIVERSITY', 'MEDIUM_COMPRESS_IDX');
    dbms_stats.DELETE_INDEX_STATS('UNIVERSITY', 'HIGH_COMPRESS_IDX');
end;
--или
analyze index NO_COMPRESS_IDX compute statistics;
analyze index LOW_COMPRESS_IDX compute statistics;
analyze index MEDIUM_COMPRESS_IDX compute statistics;
analyze index HIGH_COMPRESS_IDX compute statistics;

select * from user_indexes where index_name like '%_COMPRESS_IDX';