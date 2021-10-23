drop schema if exists builder cascade;
create schema IF NOT EXISTS `builder`;

CREATE TABLE IF NOT EXISTS `builder`.`role` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE IF NOT EXISTS `builder`.`certificate` (
  `id` INT NOT NULL,
  `name` VARCHAR(200) NULL,
  PRIMARY KEY (`id`)
  );

CREATE TABLE IF NOT EXISTS `builder`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` VARCHAR(100) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `role_id` INT NOT NULL ,
  `email` VARCHAR(100) NOT NULL,
  CONSTRAINT `unique_user_email` UNIQUE (`email`),
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `builder`.`role` (`id`)
  );

CREATE TABLE IF NOT EXISTS `builder`.`contract` (
  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `owner_id` INT NOT NULL,
  `description` VARCHAR(500) NOT NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NOT NULL,
  `start_price` INT NOT NULL,
  CONSTRAINT `fk_owner_id` FOREIGN KEY (`owner_id`) REFERENCES `builder`.`user` (`id`));

CREATE TABLE IF NOT EXISTS `builder`.`offer` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `offer_owner_id` INT NOT NULL,
    `contract_id` INT NOT NULL,
    `price` INT NOT NULL,
    PRIMARY KEY (`id`),
    CONSTRAINT `fk_offer_owner_id` FOREIGN KEY (`offer_owner_id`) REFERENCES `builder`.`user` (`id`),
    CONSTRAINT `fk_contract_id` FOREIGN KEY (`contract_id`) REFERENCES `builder`.`contract` (`id`)
);

CREATE TABLE IF NOT EXISTS `builder`.`user_certificate` (
  `user_id` INT NOT NULL,
  `certificate_id` INT NOT NULL,
  PRIMARY KEY (`user_id`, `certificate_id`),
  CONSTRAINT `fk_certificate_id` FOREIGN KEY (`certificate_id`) REFERENCES `builder`.`certificate` (`id`),
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `builder`.`user` (`id`));

CREATE INDEX IF NOT EXISTS fk_user_id ON builder.user_certificate(user_id);
CREATE INDEX IF NOT EXISTS fk_certificate_id ON builder.user_certificate(certificate_id);

COMMIT;