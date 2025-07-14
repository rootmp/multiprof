DROP TABLE IF EXISTS `character_adenlab_special`;
CREATE TABLE `character_adenlab_special` (
  `char_id` int(11) NOT NULL DEFAULT '0',
  `boss_id` int(11) NOT NULL DEFAULT '0',
  `slot` int(3) NOT NULL DEFAULT '0',
  `option1_level` int(3) DEFAULT NULL,
  `option2_level` int(3) DEFAULT NULL,
  PRIMARY KEY (`char_id`,`boss_id`,`slot`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;