CREATE DATABASE IF NOT EXISTS rettuce;
USE rettuce;

CREATE TABLE vegetables (
    name varchar(40) PRIMARY KEY,
    price int NOT NULL,
    quantity_sold int NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT current_timestamp
);

INSERT INTO vegetables (name, price) VALUES
("rettuce", 300),
("apple", 300),
("pudding", 100);
