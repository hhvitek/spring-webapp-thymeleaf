INSERT INTO vehicle (id, type, name) VALUES (1, 'Car', 'Škoda 100 blue');
INSERT INTO vehicle (id, type, name) VALUES (2, 'Car', 'Škoda 100 green');
INSERT INTO vehicle (id, type, name) VALUES (3, 'Bus', 'Škoda 706 red');
INSERT INTO vehicle (id, type, name) VALUES (4, 'Car', 'Lada VAZ 2101');

INSERT INTO person (id, name, can_drive) VALUES (1, 'Petr', true);
INSERT INTO person (id, name, can_drive) VALUES (2, 'Michal', true);
INSERT INTO person (id, name, can_drive) VALUES (3, 'Eliška', false);

INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 1, '2021-01-01 01:01:01.000', '2021-01-02 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 2, '2021-01-05 01:01:01.000', '2021-01-06 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (3, 3, '2021-01-02 01:01:01.000', '2021-01-04 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 1, '2021-01-01 01:01:01.000', '2021-01-02 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 2, '2021-01-05 01:01:01.000', '2021-01-06 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (3, 3, '2021-01-02 01:01:01.000', '2021-01-04 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 1, '2021-01-01 01:01:01.000', '2021-01-02 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (1, 2, '2021-01-05 01:01:01.000', '2021-01-06 01:01:01.000');
INSERT INTO driver (fk_person_id, fk_vehicle_id, date_from, date_to) VALUES (3, 3, '2021-01-02 01:01:01.000', '2021-01-04 01:01:01.000');