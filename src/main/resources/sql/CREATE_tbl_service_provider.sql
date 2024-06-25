CREATE TABLE tbl_service
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(150),
    service_category_id INT,
    FOREIGN KEY (service_category_id) REFERENCES tbl_service_category(id)
);

CREATE TABLE tbl_service_category
(
    id SERIAL PRIMARY KEY,
    name VARCHAR(150)
);

CREATE TABLE tbl_country
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);
CREATE TABLE tbl_state
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE tbl_state
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);
CREATE TABLE tbl_city
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE tbl_social_media
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE tbl_album
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(255),
    description TEXT
);

CREATE TABLE tbl_image
(
    id       SERIAL PRIMARY KEY,
    path     TEXT,
    category VARCHAR(255)
);

CREATE TABLE tbl_bank
(
    id   SERIAL PRIMARY KEY,
    name TEXT
);


CREATE TABLE tbl_banking_details
(
    full_name           VARCHAR(255),
    account_number      TEXT,
    service_provider_id INT,
    bank_id             INT,
    PRIMARY KEY (service_provider_id, bank_id),
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id),
    FOREIGN KEY (bank_id) REFERENCES tbl_bank (id)
);

CREATE TABLE tbl_banking_details
(
    id SERIAL PRIMARY KEY,
    full_name           VARCHAR(255),
    account_number      TEXT,
    service_provider_id INT,
    bank_id             INT,
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id),
    FOREIGN KEY (bank_id) REFERENCES tbl_bank (id)
);

CREATE TABLE tbl_coverage
(
    id                  SERIAL PRIMARY KEY,
    city_id             INT,
    country_id          INT,
    state_id            INT,
    service_provider_id INT,
    FOREIGN KEY (city_id) REFERENCES tbl_city (id),
    FOREIGN KEY (state_id) REFERENCES tbl_state (id),
    FOREIGN KEY (country_id) REFERENCES tbl_country (id),
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id)
);

CREATE TABLE tbl_coverage
(
    id                  SERIAL PRIMARY KEY,
    city_id             INT,
    country_id          INT,
    state_id            INT,
    service_provider_id INT,
    FOREIGN KEY (city_id) REFERENCES tbl_city (id),
    FOREIGN KEY (state_id) REFERENCES tbl_state (id),
    FOREIGN KEY (country_id) REFERENCES tbl_country (id),
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id)
);

CREATE TABLE tbl_service_provider
(
    id                     SERIAL PRIMARY KEY,
    business_name          VARCHAR(150),
    business_email_address VARCHAR(100),
    business_address       VARCHAR(255),
    business_description   TEXT,
    coverage_id            INT,
    facebook_link          TEXT,
    instagram_link         TEXT,
    tiktok_link            TEXT
);


CREATE TABLE tbl_service_details
(
    service_provider_id INT,
    service_id          INT,
    PRIMARY KEY (service_provider_id, service_id)
);

CREATE TABLE tbl_coverage
(
    id                  SERIAL PRIMARY KEY,
    city_id             INT,
    country_id          INT,
    state_id            INT,
    service_provider_id INT,
    FOREIGN KEY (city_id) REFERENCES tbl_city (id),
    FOREIGN KEY (state_id) REFERENCES tbl_state (id),
    FOREIGN KEY (country_id) REFERENCES tbl_country (id),
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id)
);

CREATE TABLE tbl_banking_details
(
    full_name           VARCHAR(255),
    account_number      TEXT,
    service_provider_id INT,
    bank_id             INT,
    PRIMARY KEY (service_provider_id, bank_id),
    FOREIGN KEY (service_provider_id) REFERENCES tbl_service_provider (id),
    FOREIGN KEY (bank_id) REFERENCES tbl_bank (id)
);




