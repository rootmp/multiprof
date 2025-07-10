DROP TABLE IF EXISTS `character_collection_favorites`;
CREATE TABLE `character_collection_favorites` (
	`char_id` INT NOT NULL DEFAULT '0',
	`collection_id` SMALLINT UNSIGNED NOT NULL DEFAULT '0',
	PRIMARY KEY  (`char_id`,`collection_id`)
) ENGINE=MyISAM;