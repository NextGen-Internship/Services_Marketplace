CREATE TABLE IF NOT EXISTS user (
    id INT AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    phone_number INT UNIQUE NOT NULL,
    experience INT,
    rating DOUBLE,
    description VARCHAR(255),
    photo BLOB,
    PRIMARY KEY (id)
);