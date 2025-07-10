DROP TABLE IF EXISTS `castle`;
CREATE TABLE `castle` (
  `id` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `name` varchar(25) NOT NULL,
  `tax` INT UNSIGNED NOT NULL DEFAULT '5',
  `treasury` bigint(20) unsigned NOT NULL DEFAULT '0',
  `last_siege_date` INT UNSIGNED NOT NULL,
  `owner_id` INT NOT NULL DEFAULT '0',
  `own_date` INT UNSIGNED NOT NULL,
  `siege_date` INT UNSIGNED NOT NULL,
  `side` tinyint(1) unsigned NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
);