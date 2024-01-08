CREATE TABLE IF NOT EXISTS review (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    rating DOUBLE NOT NULL,
    date DATETIME NOT NULL,
    created_by INT,
    updated_by INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (customer_id) REFERENCES user (id)
);