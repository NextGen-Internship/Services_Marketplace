ALTER TABLE files
DROP FOREIGN KEY files_ibfk_1,
ADD COLUMN source_id INT,
ADD COLUMN source_type VARCHAR(255),
DROP COLUMN review_id;