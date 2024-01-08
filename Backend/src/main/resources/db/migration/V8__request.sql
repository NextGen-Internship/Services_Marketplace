CREATE TABLE IF NOT EXISTS request (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    service_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,
    created_by INT,
    updated_by INT,
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (customer_id) REFERENCES user (id),
    FOREIGN KEY (service_id) REFERENCES service (id)
);