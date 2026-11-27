
USE shelterclinic_schema;

CREATE TABLE user(
    user_id INT AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,

    PRIMARY KEY(user_id)
);

CREATE TABLE shelter(
    shelter_id INT NOT NULL,
    singleton TINYINT GENERATED ALWAYS AS (1) STORED,
    name VARCHAR(50) NOT NULL,
    capacity INT NOT NULL,
    password VARCHAR(255) NOT NULL,

    PRIMARY KEY(shelter_id),
    CONSTRAINT uq_shelter_singleton UNIQUE (singleton),
    CONSTRAINT chk_shelter_capacity CHECK (capacity BETWEEN 1 AND 150)
);

CREATE TABLE animal(
    animal_id INT AUTO_INCREMENT,
    name VARCHAR(50),
    description VARCHAR(500) NOT NULL,
    age INT,
    species ENUM('CAT', 'DOG') NOT NULL,

    PRIMARY KEY(animal_id),
    CONSTRAINT chk_descr_length CHECK (CHAR_LENGTH(description) > 20)
);

CREATE TABLE medical_record (
    animal_id INT NOT NULL,
    is_fixed ENUM ('SPAYED', 'NEUTERED', 'NONE', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',
    is_vaccinated ENUM ('YES', 'NO', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN',

    PRIMARY KEY (animal_id),
    FOREIGN KEY(animal_id) REFERENCES animal(animal_id) ON DELETE CASCADE
);

CREATE TABLE medical_conditions (
    animal_id INT NOT NULL,
    description VARCHAR(255) NOT NULL,

    PRIMARY KEY (animal_id, description),
    FOREIGN KEY(animal_id) REFERENCES animal(animal_id) ON DELETE CASCADE

);

CREATE TABLE Tag(
    tag_name Varchar(50) NOT NULL,
    PRIMARY KEY(tag_name)
);

CREATE TABLE animal_tag(
    animal_id INT NOT NULL,
    tag_name Varchar(50),

    PRIMARY KEY(animal_id, tag_name),
    FOREIGN KEY(animal_id) REFERENCES animal(animal_id) ON DELETE CASCADE,
    FOREIGN KEY(tag_name) REFERENCES Tag(tag_name) ON DELETE CASCADE
);
