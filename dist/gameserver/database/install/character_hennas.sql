DROP TABLE IF EXISTS `character_hennas`;
CREATE TABLE `character_hennas` (
	`char_obj_id` INT NOT NULL,
	`class_index` TINYINT UNSIGNED NOT NULL,
	`slot` INT NOT NULL,
	`symbol_id` SMALLINT UNSIGNED NOT NULL DEFAULT '0',
	`enchant_exp` INT UNSIGNED NOT NULL DEFAULT '0',
	`potential_id` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	PRIMARY KEY  (`char_obj_id`,`class_index`,`slot`)
) ENGINE=MyISAM;