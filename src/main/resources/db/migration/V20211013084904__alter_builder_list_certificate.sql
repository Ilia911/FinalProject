ALTER TABLE `builder_list_certificate`
ADD CONSTRAINT `certificate_id`
  FOREIGN KEY (`certificate_id`)
  REFERENCES `builder_certificate` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `builder_list_certificate`
  ADD CONSTRAINT `builder_id`
    FOREIGN KEY (`builder_id`)
    REFERENCES `builder_user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
