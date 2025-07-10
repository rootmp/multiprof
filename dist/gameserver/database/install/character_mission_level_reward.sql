DROP TABLE IF EXISTS `character_mission_level_reward`;
CREATE TABLE `character_mission_level_reward` (
	`char_id` INT(11) NOT NULL,
	`level` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`points` INT NOT NULL DEFAULT '0',
	`last_taken_basic` INT NOT NULL DEFAULT '0',
	`last_taken_additional` INT NOT NULL DEFAULT '0',
	`last_taken_bonus` INT NOT NULL DEFAULT '0',
	`taken_final` TINYINT(1) NOT NULL DEFAULT '0',
	PRIMARY KEY  (`char_id`)
);