CREATE TABLE IF NOT EXISTS offers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    request_id INT NOT NULL,
    FOREIGN KEY (request_id) REFERENCES requests (id),
    description VARCHAR(255) NOT NULL,
    offer_price DOUBLE NOT NULL,
    date DATETIME NOT NULL,
    offer_status ENUM('Active', 'Inactive', 'Pending') NOT NULL,
    created_by INT,
    updated_by INT,
    created_at DATETIME,
    updated_at DATETIME
);