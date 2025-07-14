DROP TABLE IF EXISTS `account_relics_collections`;
CREATE TABLE `account_relics_collections` (
  `account_name` varchar(50) NOT NULL,
  `collection_id` int(11) NOT NULL,
  `relic_id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  PRIMARY KEY (`account_name`,`collection_id`,`relic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
