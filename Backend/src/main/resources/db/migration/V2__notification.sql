CREATE TABLE IF NOT EXISTS notifications (
    id INT AUTO_INCREMENT,
    user_id INT NOT NULL,
    type ENUM('Request', 'Offer', 'News') NOT NULL,
    content VARCHAR(255),
    status ENUM('Read', 'Unread'),
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);