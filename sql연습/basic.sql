select version();

create table pet(
	name varchar(100),
    owner varchar(50),
	species varchar(20),
    gender char(1),
    birth date,
    death date
);

-- schema 확인
describe pet;
desc pet;

-- table 삭제
drop table pet;
show tables;

-- insert : DML(C)
insert into pet values('몽쉘', '신예진', 'dog', 'f','2022-10-18', NULL);

-- select : DML(R)
select * from pet;

-- update: DML(U)
update pet set name='몽쉘이' where name='몽쉘';

-- load data: mysql(CLI) 전용
load data local infile '/root/pet.txt' into table pet;

-- select 연습
select name, species
from pet
where name='bowser';



select name, species, birth
from pet
where birth >= '1998-01-10';

select name, species, gender
from pet
where species = 'dog'
	and gender = 'f';
    
select name, species
from pet
where species = 'bird'
	and gender = 'snake';
