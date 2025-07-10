-- ----------------------------
-- Table structure for private_store_history
-- ----------------------------
DROP TABLE IF EXISTS `private_store_history`;
CREATE TABLE `private_store_history` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`store_type` TINYINT NOT NULL,
	`time` INT NOT NULL,
	`item_id` INT NOT NULL,
	`count` BIGINT NOT NULL DEFAULT '1',
	`enchant` TINYINT NOT NULL DEFAULT '0',
	`price` BIGINT NOT NULL DEFAULT '0',
	PRIMARY KEY  (`id`,`store_type`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;