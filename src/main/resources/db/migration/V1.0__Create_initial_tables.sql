CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE address (
    id BIGSERIAL PRIMARY KEY,
    street VARCHAR(255) NOT NULL,
    number VARCHAR(50),
    complement VARCHAR(100),
    neighborhood VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(2) NOT NULL,
    zip_code VARCHAR(9) NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    user_id BIGINT,
    CONSTRAINT fk_address_user FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE category (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE restaurant (
    id BIGSERIAL PRIMARY KEY,
    restaurant_name VARCHAR(255) NOT NULL,
    cnpj VARCHAR(18) NOT NULL UNIQUE,
    owner_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    CONSTRAINT fk_restaurant_owner FOREIGN KEY (owner_id) REFERENCES users(id),
    CONSTRAINT fk_restaurant_address FOREIGN KEY (address_id) REFERENCES address(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurant_category (
    restaurant_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL,
    PRIMARY KEY (restaurant_id, category_id),
    CONSTRAINT fk_rc_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    CONSTRAINT fk_rc_category FOREIGN KEY (category_id) REFERENCES category(id)
);

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    image_url VARCHAR(255),
    restaurant_id BIGINT NOT NULL,
    CONSTRAINT fk_product_restaurant FOREIGN KEY (restaurant_id) REFERENCES restaurant(id),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
