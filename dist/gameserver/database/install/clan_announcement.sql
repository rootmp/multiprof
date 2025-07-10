DROP TABLE IF EXISTS `clan_announcement`;
CREATE TABLE `clan_announcement` (
	`clan_id` INT NOT NULL DEFAULT '0',
	`body` text NOT NULL,
	`showOnEnter` BOOLEAN NOT NULL DEFAULT '0',
	PRIMARY KEY (`clan_id`)
) ENGINE=MyISAM;
