CREATE TABLE `builder_user` (
  `id` INT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `builder_certificate` (
  `id` INT NOT NULL,
  `name` VARCHAR(200) NULL,
  PRIMARY KEY (`id`));


CREATE TABLE `user_role` (
  `id` INT NOT NULL,
  `name` VARCHAR(45) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `builder_list_certificate` (
  `builder_id` INT NOT NULL,
  `certificate_id` INT NOT NULL);