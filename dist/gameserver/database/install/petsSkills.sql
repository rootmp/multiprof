DROP TABLE IF EXISTS `petsSkills`;
CREATE TABLE `petsSkills` (
	`hashId` bigint NOT NULL DEFAULT '0',
	`skillId` INT NOT NULL DEFAULT '0',
	`skillLevel` INT NOT NULL DEFAULT '0',
	`petId` INT NOT NULL DEFAULT '0',
	PRIMARY KEY  (`hashId`, `skillId`)
) ENGINE=MyISAM;
