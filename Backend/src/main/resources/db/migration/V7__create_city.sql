CREATE TABLE IF NOT EXISTS city (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    zip_code INT NOT NULL,
    address VARCHAR(255) NOT NULL
);