DROP TABLE IF EXISTS `character_training_camp`;
CREATE TABLE `character_training_camp` (
	`account_name` VARCHAR(45) NOT NULL,
	`char_id` INT NOT NULL,
	`class_index` INT NOT NULL,
	`level` INT NOT NULL,
	`start_time` INT NOT NULL,
	`end_time` INT NOT NULL,
	PRIMARY KEY  (`account_name`)
) ENGINE=MyISAM;
