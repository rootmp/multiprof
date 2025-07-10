DROP TABLE IF EXISTS `clan_data`;
CREATE TABLE `clan_data` (
	`clan_id` INT NOT NULL DEFAULT '0',
	`clan_level` TINYINT UNSIGNED NOT NULL DEFAULT '0',
	`ally_id` INT NOT NULL DEFAULT '0',
	`crest` VARBINARY(256) NULL DEFAULT NULL,
	`reputation_score` INT NOT NULL DEFAULT '0',
	`warehouse` INT NOT NULL DEFAULT '0',
	`expelled_member` INT UNSIGNED NOT NULL DEFAULT '0',
	`leaved_ally` INT UNSIGNED NOT NULL DEFAULT '0',
	`dissolved_ally` INT UNSIGNED NOT NULL DEFAULT '0',
	`auction_bid_at` INT NOT NULL DEFAULT '0',
	`academy_graduates` INT NOT NULL DEFAULT '0',
	`castle_defend_count` INT NOT NULL DEFAULT '0',
	`disband_end` INT NOT NULL DEFAULT '0',
	`disband_penalty` INT NOT NULL DEFAULT '0',
	`hunting_progress` INT NOT NULL DEFAULT '0',
	`yesterday_hunting_reward` INT NOT NULL DEFAULT '0',
	`yesterday_attendance_reward` INT NOT NULL DEFAULT '0',
	`arena_stage` TINYINT NOT NULL DEFAULT '0',
	`clan_points` INT NOT NULL DEFAULT '0',
	PRIMARY KEY (`clan_id`),
	KEY `ally_id` (`ally_id`)
) ENGINE=MyISAM;
