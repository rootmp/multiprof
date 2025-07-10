DROP TABLE IF EXISTS `character_random_craft_info`;
CREATE TABLE `character_random_craft_info` (
	`char_id` INT NOT NULL,
	`slot_id` INT NOT NULL,
	`item_id` INT NOT NULL,
	`result_id` INT NOT NULL DEFAULT '0',
	`count` BIGINT(20) NOT NULL,
	`enchantLevel` INT NOT NULL,
	`locked` BOOLEAN NOT NULL DEFAULT '0',
	`refreshToUnlock` INT NOT NULL DEFAULT '20',
	PRIMARY KEY  (`char_id`,`slot_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;