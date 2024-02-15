ALTER TABLE files
ADD COLUMN service_id INT,
ADD CONSTRAINT fk_service_id
    FOREIGN KEY (service_id)
    REFERENCES service(id);
