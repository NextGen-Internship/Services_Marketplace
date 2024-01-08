CREATE TABLE IF NOT EXISTS review_media (
    review_id INT NOT NULL,
    FOREIGN KEY (review_id) REFERENCES reviews (id),
    photo BLOB
);