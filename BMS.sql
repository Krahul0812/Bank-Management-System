DROP DATABASE IF EXISTS bankmanagementsystem;
CREATE DATABASE bankmanagementsystem;
USE bankmanagementsystem;

CREATE TABLE signup (
    formno VARCHAR(20),
    name VARCHAR(20),
    father_name VARCHAR(20),
    dob VARCHAR(20),
    gender VARCHAR(20),
    email VARCHAR(30),
    marital_status VARCHAR(20),
    address VARCHAR(50),
    city VARCHAR(20),
    pincode VARCHAR(20),
    state VARCHAR(20)
);

CREATE TABLE signuptwo (
    formno VARCHAR(20),
    religion VARCHAR(20),
    category VARCHAR(20),
    income VARCHAR(20),
    education VARCHAR(20),
    occupation VARCHAR(20),
    pan VARCHAR(20),
    aadhar VARCHAR(20),
    seniorcitizen VARCHAR(20),
    existingaccount VARCHAR(20)
);

CREATE TABLE signupthree (
    formno VARCHAR(20),
    accountType VARCHAR(40),
    cardNumber VARCHAR(25),
    pin VARCHAR(10),
    facility VARCHAR(100)
);

CREATE TABLE login (
    formno VARCHAR(20),
    cardNumber VARCHAR(25),
    pin VARCHAR(10)
);


SHOW TABLES;

CREATE TABLE bank (
    pin VARCHAR(10),
    date VARCHAR(50),
    type VARCHAR(20),
    amount VARCHAR(20)
);



USE bankmanagementsystem;
SELECT * FROM  login ;


