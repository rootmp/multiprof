DROP TABLE IF EXISTS `account_relics`;
CREATE TABLE `account_relics` (
  `account_name` varchar(255) NOT NULL,
  `relic_id` int(11) NOT NULL,
  `level` int(11) NOT NULL,
  `count` int(11) NOT NULL,
  PRIMARY KEY (`account_name`,`relic_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
