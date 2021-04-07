DROP TABLE IF EXISTS driver_notes;
DROP TABLE IF EXISTS driver;
DROP TABLE IF EXISTS vehicle;
DROP TABLE IF EXISTS person;


CREATE TABLE IF NOT EXISTS vehicle (
    id         SERIAL PRIMARY KEY,
    type       TEXT NOT NULL,
    name       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS person (
    id          SERIAL PRIMARY KEY,
    name        TEXT NOT NULL,
    can_drive   BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS driver (
    id              SERIAL PRIMARY KEY,
    fk_person_id    INTEGER REFERENCES person (id) ON DELETE CASCADE,
    fk_vehicle_id   INTEGER REFERENCES vehicle (id) ON DELETE CASCADE,

    date_from       TIMESTAMP WITH TIME ZONE,
    date_to         TIMESTAMP WITH TIME ZONE
);

CREATE TABLE IF NOT EXISTS driver_notes (
    id                              SERIAL PRIMARY KEY,
    fk_person_to_vehicle_driver     INTEGER REFERENCES driver(id) ON DELETE CASCADE,
    note                            TEXT
);