DROP TABLE IF EXISTS `character_potens`;
CREATE TABLE `character_potens` (
  `charId` int(10) unsigned NOT NULL DEFAULT '0',
  `enchant_level` int(11) DEFAULT NULL,
  `enchant_exp` int(11) DEFAULT NULL,
  `poten_id` int(11) NOT NULL DEFAULT '0',
  `slot_id` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`charId`,`slot_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1 COLLATE=latin1_general_ci;
