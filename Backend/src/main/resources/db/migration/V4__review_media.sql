CREATE TABLE IF NOT EXISTS review_media (
    review_id INT NOT NULL,
    photo BLOB,
    FOREIGN KEY (review_id) REFERENCES reviews (id)
);