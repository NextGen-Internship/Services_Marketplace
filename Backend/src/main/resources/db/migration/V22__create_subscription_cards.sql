CREATE TABLE IF NOT EXISTS subscription (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    start_date DATETIME,
    end_date DATETIME,
    isActive BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE IF NOT EXISTS cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    card_token VARCHAR(255) NOT NULL,
    isDeleted BOOLEAN,
    FOREIGN KEY (user_id) REFERENCES user (id)
);