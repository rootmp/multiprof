DROP TABLE IF EXISTS `residence_functions`;
CREATE TABLE `residence_functions` (
	`residence_id` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`type` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`level` SMALLINT UNSIGNED NOT NULL DEFAULT '0',
	`end_time` INT NOT NULL DEFAULT '0',
	`in_debt` TINYINT NOT NULL DEFAULT '0',
	PRIMARY KEY  (`residence_id`,`type`)
) ENGINE=MyISAM;
