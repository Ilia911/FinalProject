
INSERT INTO builder.certificate (ID, NAME)
VALUES ('1', 'Filling window and door openings');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('2', 'Installation of external networks and structures');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('3', 'Execution of works on the device of anti-corrosion ' ||
             'coatings of building structures of buildings and structures');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('4', 'Execution of works on the arrangement of road surfaces ' ||
             'of pedestrian zones from sidewalk slabs');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('5', 'Execution of works on the construction of insulating coatings');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('6', 'Execution of work on the arrangement of foundations, ' ||
             'foundations of buildings and structures');
INSERT INTO builder.certificate (ID, NAME)
VALUES ('7', 'Performing work on the installation of thermal insulation ' ||
             'of the enclosing structures of buildings and structures');

INSERT INTO builder.user (NAME, PASSWORD, ROLE, EMAIL)
VALUES ('Customer', '$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC', 'CUSTOMER', 'castomer@gmail.com');
INSERT INTO builder.user (NAME, PASSWORD, ROLE, EMAIL)
VALUES ('SecondCustomer', 'password', 'CUSTOMER', 'secondCastomer@gmail.com');
INSERT INTO builder.user (NAME, PASSWORD, ROLE, EMAIL)
VALUES ('Contractor', '$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC', 'CONTRACTOR',
        'contractor@gmail.com');
INSERT INTO builder.user (NAME, PASSWORD, ROLE, EMAIL)
VALUES ('Owner', '$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC', 'OWNER', 'owner@gmail.com');
INSERT INTO builder.user (NAME, PASSWORD, ROLE, EMAIL)
VALUES ('Admin', '$2a$12$ma0C6gCWmZL/d3FSt78mF.FCcLdfBqc4XXyRrDBv54EbWEPcj3OIC', 'ADMIN', 'admin@gmail.com');

INSERT INTO builder.contract (OWNER_ID, DESCRIPTION, START_DATE, END_DATE, START_PRICE)
VALUES ('1', 'first contract', '2022-01-01', '2022-12-31', '28000');
INSERT INTO builder.contract (OWNER_ID, DESCRIPTION, START_DATE, END_DATE, START_PRICE)
VALUES ('2', 'second contract', '2022-03-01', '2022-09-30', '30000');

INSERT INTO builder.offer (OFFER_OWNER_ID, CONTRACT_ID, PRICE)
VALUES ('3', '1', '27500');
INSERT INTO builder.offer (OFFER_OWNER_ID, CONTRACT_ID, PRICE)
VALUES ('4', '1', '26000');

INSERT INTO builder.user_certificate (user_id, certificate_id)
VALUES ('3', '1');
INSERT INTO builder.user_certificate (user_id, certificate_id) VALUES ('3', '6');
INSERT INTO builder.user_certificate (user_id, certificate_id) VALUES ('3', '7');
INSERT INTO builder.user_certificate (user_id, certificate_id) VALUES ('4', '4');
INSERT INTO builder.user_certificate (user_id, certificate_id) VALUES ('4', '5');



COMMIT;