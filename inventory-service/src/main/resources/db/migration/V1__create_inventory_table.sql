CREATE TABLE inventory.stock (
    id UUID PRIMARY KEY,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL
);