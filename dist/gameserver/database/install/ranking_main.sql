DROP TABLE IF EXISTS `ranking_main`;
CREATE TABLE `ranking_main` (
  `rank` int(11) NOT NULL,
  `charId` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `classId` int(11) DEFAULT NULL,
  `race` int(11) DEFAULT NULL,
  `clanName` varchar(255) DEFAULT NULL,
  `raceRank` int(11) DEFAULT '0',
  `classRank` int(11) DEFAULT '0',
  PRIMARY KEY (`rank`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
