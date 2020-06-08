CREATE DATABASE IF NOT EXISTS rettuce;
USE rettuce;

CREATE TABLE vegetables (
    id varchar(36) PRIMARY KEY,
    name varchar(40),
    price int NOT NULL,
    quantity_sold int NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT current_timestamp
);

CREATE TABLE sales (
    id varchar(36) PRIMARY KEY,
    amount int NOT NULL, -- yen
    created_at timestamp DEFAULT current_timestamp
);
