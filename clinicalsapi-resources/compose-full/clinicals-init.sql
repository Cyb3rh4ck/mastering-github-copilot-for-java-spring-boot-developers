CREATE DATABASE clinicals;
USE clinicals;

CREATE TABLE patient (
    id INT NOT NULL AUTO_INCREMENT,
    last_name VARCHAR(255) NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    age INT,
    PRIMARY KEY (id)
);

CREATE TABLE clinicaldata (
    id INT NOT NULL AUTO_INCREMENT,
    patient_id INT,
    component_name VARCHAR(255) NOT NULL,
    component_value VARCHAR(255) NOT NULL,
    measured_date_time TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_patient FOREIGN KEY (patient_id)
      REFERENCES patient(id)
);

-- Insert data into patient and clinicaldata
-- (Full INSERT statements from user input)
-- ... debido al tamaño, será incluido como está completo.

-- Agregar aquí los inserts del usuario


insert into patient values(1,'John','Mccain',52);
insert into patient values(2,'Siva','Shankar',32);
insert into patient values(3,'Anthony','Simon',22);
insert into patient values(4,'Bruce','Sanhurst',33);
insert into patient values(5,'Abhram','Mani',55);
insert into patient values(6,'Gandhi','Singh',12);
insert into patient values(7,'Antti','Krovinan',27);
insert into patient values(8,'Simba','White',24);
insert into patient values(9,'Rose','Tanic',29);
insert into patient values(10,'Rowling','Lte',49);



-- Aquí irán los inserts para clinicaldata



-- Eliminación al final (opcional)
-- drop table clinicaldata;
-- drop table patient;
-- drop database clinicals;