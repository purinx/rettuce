CREATE DATABASE IF NOT EXISTS rettuce;
USE rettuce;

CREATE TABLE sales (
    id int PRIMARY KEY,
    amount int NOT NULL, -- yen
    created_at timestamp DEFAULT current_timestamp
);
