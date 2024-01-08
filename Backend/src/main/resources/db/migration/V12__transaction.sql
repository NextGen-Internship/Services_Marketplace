CREATE TABLE IF NOT EXISTS transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id),
    offer_id INT NOT NULL,
    FOREIGN KEY (offer_id) REFERENCES offers (id),
    amount DOUBLE NOT NULL,
    timestamp DATETIME NOT NULL,
    transaction_status ENUM('Active', 'Inactive', 'Pending') NOT NULL
);