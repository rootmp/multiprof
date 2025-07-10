DROP TABLE IF EXISTS `character_spectatinglist`;
CREATE TABLE `character_spectatinglist` (
	`obj_Id` INT NOT NULL DEFAULT '0',
	`target_Id` INT NOT NULL DEFAULT '0',
	`char_name` VARCHAR(50) CHARACTER SET UTF8 NOT NULL DEFAULT '',
	PRIMARY KEY  (`obj_Id`,`target_Id`)
) ENGINE=MyISAM;