
ALTER TABLE request
DROP COLUMN is_active;

ALTER TABLE request
ADD COLUMN request_status VARCHAR(50) ;
