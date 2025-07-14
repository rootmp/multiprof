DROP TABLE IF EXISTS `account_collection_favorites`;
CREATE TABLE `account_collection_favorites` (
  `account_name` varchar(30) NOT NULL DEFAULT '0',
  `collection_id` smallint(5) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`account_name`,`collection_id`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
