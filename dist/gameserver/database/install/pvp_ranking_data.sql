DROP TABLE IF EXISTS `pvp_ranking_data`;
CREATE TABLE `pvp_ranking_data` (
	`obj_Id` INT NOT NULL,
	`kills` INT NOT NULL,
	`deaths` INT NOT NULL,
	`points` INT NOT NULL,
	`week` INT NOT NULL,
	PRIMARY KEY  (`obj_Id`,`week`)
) ENGINE=MyISAM;