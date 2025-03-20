CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

DROP TABLE IF EXISTS reservation_rentals CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS camp_facilities CASCADE;
DROP TABLE IF EXISTS facilities CASCADE;
DROP TABLE IF EXISTS camp_rentals CASCADE;
DROP TABLE IF EXISTS rentals CASCADE;
DROP TABLE IF EXISTS camps CASCADE;
DROP TABLE IF EXISTS customers CASCADE;

CREATE TABLE IF NOT EXISTS customers (
    customer_id UUID DEFAULT uuid_generate_v4() NOT NULL,
    name TEXT NOT NULL,
    address TEXT,
    password TEXT,
    PRIMARY KEY (customer_id)
    );

CREATE TABLE IF NOT EXISTS camps (
    camp_id UUID DEFAULT uuid_generate_v4() NOT NULL,
    name TEXT NOT NULL,
    level INT,
    address TEXT,
    map_link TEXT,
    price INT,
    capacity INT,
    description TEXT,
    picture TEXT,
    PRIMARY KEY (camp_id)
    );

CREATE TABLE IF NOT EXISTS facilities (
    facility_id UUID DEFAULT uuid_generate_v4() NOT NULL,
    name TEXT NOT NULL,
    points INT NOT NULL,
    PRIMARY KEY (facility_id)
    );

CREATE TABLE IF NOT EXISTS camp_facilities (
    camp_id UUID NOT NULL,
    facility_id UUID NOT NULL,
    PRIMARY KEY (camp_id, facility_id),
    FOREIGN KEY (camp_id) REFERENCES camps (camp_id) ON DELETE CASCADE,
    FOREIGN KEY (facility_id) REFERENCES facilities (facility_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS rentals (
    rental_id UUID DEFAULT uuid_generate_v4() NOT NULL,
    name TEXT NOT NULL,
    price_per_day INT NOT NULL,
    PRIMARY KEY (rental_id)
    );

CREATE TABLE IF NOT EXISTS camp_rentals (
    camp_id UUID NOT NULL,
    rental_id UUID NOT NULL,
    PRIMARY KEY (camp_id, rental_id),
    FOREIGN KEY (camp_id) REFERENCES camps (camp_id) ON DELETE CASCADE,
    FOREIGN KEY (rental_id) REFERENCES rentals (rental_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS reservations (
    reservation_id UUID DEFAULT uuid_generate_v4() NOT NULL,
    camp_id UUID NOT NULL,
    customer_id UUID NOT NULL,
    check_in_date DATE,
    check_out_date DATE,
    status TEXT,
    number_of_tents INT,
    total_price INT NOT NULL,
    PRIMARY KEY (reservation_id),
    FOREIGN KEY (camp_id) REFERENCES camps (camp_id) ON DELETE CASCADE,
    FOREIGN KEY (customer_id) REFERENCES customers (customer_id) ON DELETE CASCADE
    );

CREATE TABLE IF NOT EXISTS reservation_rentals (
    reservation_id UUID NOT NULL,
    rental_id UUID NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (reservation_id, rental_id),
    FOREIGN KEY (reservation_id) REFERENCES reservations (reservation_id) ON DELETE CASCADE,
    FOREIGN KEY (rental_id) REFERENCES rentals (rental_id) ON DELETE CASCADE
    );

CREATE INDEX IF NOT EXISTS idx_reservations_camp_id ON reservations (camp_id);
CREATE INDEX IF NOT EXISTS idx_reservations_customer_id ON reservations (customer_id);
CREATE INDEX IF NOT EXISTS idx_reservation_rentals_rental_id ON reservation_rentals (rental_id);
CREATE INDEX IF NOT EXISTS idx_camp_facilities_facility_id ON camp_facilities (facility_id);
CREATE INDEX IF NOT EXISTS idx_camp_rentals_rental_id ON camp_rentals (rental_id);
CREATE INDEX IF NOT EXISTS idx_camp_rentals_camp_id ON camp_rentals (camp_id);
CREATE INDEX IF NOT EXISTS idx_camp_facilities_camp_id ON camp_facilities (camp_id);
