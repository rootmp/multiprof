DROP TABLE IF EXISTS `ranking_score`;
CREATE TABLE `ranking_score` (
  `nRank` int(11) NOT NULL,
  `nCharId` int(11) NOT NULL,
  `sUserName` varchar(255) DEFAULT NULL,
  `sPledgeName` varchar(255) DEFAULT NULL,
  `nLevel` int(11) DEFAULT NULL,
  `nClass` int(11) DEFAULT NULL,
  `nRace` int(11) DEFAULT NULL,
  `nRaceRank` int(11) DEFAULT '0',
  `nClassRank` int(11) DEFAULT '0',
  `nScore` int(11) DEFAULT NULL,
  PRIMARY KEY (`nRank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

