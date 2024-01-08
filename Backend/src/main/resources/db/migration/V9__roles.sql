CREATE TABLE IF NOT EXISTS roles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role ENUM('Admin', 'User', 'Provider') NOT NULL
);