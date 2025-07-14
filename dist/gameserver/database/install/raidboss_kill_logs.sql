DROP TABLE IF EXISTS `raidboss_kill_logs`;
CREATE TABLE `raidboss_kill_logs` (
  `rb_id` int(11) NOT NULL,
  `clan_id` int(11) NOT NULL,
  `killer_id` int(11) NOT NULL,
  `time` bigint(13) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;