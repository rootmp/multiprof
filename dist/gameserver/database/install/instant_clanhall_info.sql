DROP TABLE IF EXISTS `instant_clanhall_info`;
CREATE TABLE `instant_clanhall_info` (
  `id` tinyint(3) unsigned NOT NULL DEFAULT '0',
  `siege_date` INT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`)
);
