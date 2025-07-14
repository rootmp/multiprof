DROP TABLE IF EXISTS `account_variables`;
CREATE TABLE `account_variables` (
  `account_name` varchar(45) NOT NULL DEFAULT '',
  `var` varchar(50) NOT NULL DEFAULT '',
  `value` varchar(255) NOT NULL,
  `expire_time` bigint(20) NOT NULL DEFAULT '-1',
  PRIMARY KEY (`account_name`,`var`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;
