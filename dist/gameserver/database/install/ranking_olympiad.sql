DROP TABLE IF EXISTS `ranking_olympiad`;
CREATE TABLE `ranking_olympiad` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `charId` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `clanName` varchar(255) DEFAULT NULL,
  `level` int(11) DEFAULT NULL,
  `classId` int(11) DEFAULT NULL,
  `clanLevel` int(11) DEFAULT NULL,
  `competitions_win` int(11) DEFAULT NULL,
  `competitions_loose` int(11) DEFAULT NULL,
  `olympiad_points` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
