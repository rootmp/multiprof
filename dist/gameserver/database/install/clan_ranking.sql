DROP TABLE IF EXISTS `clan_ranking`;
CREATE TABLE `clan_ranking` (
	`clan_id` INT NOT NULL DEFAULT '0',
	`clan_points` INT NOT NULL DEFAULT '0',
	PRIMARY KEY (`clan_id`)
) ENGINE=MyISAM;
