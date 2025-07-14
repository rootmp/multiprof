DROP TABLE IF EXISTS `account_collections`;
CREATE TABLE `account_collections` (
  `account_name` varchar(30) NOT NULL,
  `tab_id` smallint(5) unsigned NOT NULL,
  `collection_id` int(10) unsigned NOT NULL,
  `item_id` int(10) unsigned NOT NULL,
  `item_count` int(10) unsigned NOT NULL,
  `enchant` smallint(5) unsigned NOT NULL,
  `bless` smallint(5) unsigned NOT NULL,
  `blessCondition` smallint(5) unsigned NOT NULL,
  `slot_id` smallint(5) unsigned NOT NULL
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
