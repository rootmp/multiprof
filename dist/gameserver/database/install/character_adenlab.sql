DROP TABLE IF EXISTS `character_adenlab`;
CREATE TABLE `character_adenlab` (
  `char_Id` int(11) NOT NULL,
  `bossID` int(11) NOT NULL,
  `currentSlot` int(3) DEFAULT NULL,
  `openCards` int(3) DEFAULT NULL,
  `transcendEnchant` int(3) DEFAULT NULL,
  `normalGameSaleDailyCount` int(3) DEFAULT NULL,
  `normalGameDailyCount` int(3) DEFAULT NULL,
  PRIMARY KEY (`char_Id`,`bossID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
