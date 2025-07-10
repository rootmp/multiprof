DROP TABLE IF EXISTS `character_teleports`;
CREATE TABLE `character_teleports` (
	`char_id` INT NOT NULL DEFAULT '0',
	`teleport_id` SMALLINT UNSIGNED NOT NULL DEFAULT '0',
	PRIMARY KEY  (`char_id`,`teleport_id`)
) ENGINE=MyISAM;