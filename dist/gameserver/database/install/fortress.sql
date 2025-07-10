DROP TABLE IF EXISTS `fortress`;
CREATE TABLE `fortress` (
  `id` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `name` varchar(45) NOT NULL,
  `last_siege_date` bigint(20) NOT NULL,
  `owner_id` INT NOT NULL DEFAULT '0',
  `own_date` bigint(20) NOT NULL,
  `siege_date` bigint(20) NOT NULL,
  `cycle` int(11) NOT NULL,
  PRIMARY KEY (`id`)
);

