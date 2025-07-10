-- ----------------------------
-- Table structure for enchant_broken_items
-- ----------------------------
DROP TABLE IF EXISTS `enchant_broken_items`;
CREATE TABLE `enchant_broken_items` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`char_id` INT NOT NULL,
	`item_id` INT NOT NULL,
	`enchant` TINYINT NOT NULL DEFAULT '0',
	`time` INT NOT NULL,
	PRIMARY KEY  (`id`,`char_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;