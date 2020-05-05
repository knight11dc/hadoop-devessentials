create database POC;
use POC;
create table Race( id char(1), value char(6));
insert into Race values ('W','White'),('L','Latino'),('O',"Other");
create table Region( id char(2), value char(12));
insert into Region values ('W','West'),('NC','NorthCentral'),('O',"Other"),('S','South');
