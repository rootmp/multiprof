DROP TABLE IF EXISTS `account_vip_attendance`;
CREATE TABLE `account_vip_attendance` (
  `account_name` varchar(30) NOT NULL,
  `cAttendanceDay` int(11) DEFAULT NULL,
  `cRewardDay` int(11) DEFAULT NULL,
  `cFollowBaseDay` int(11) DEFAULT NULL,
  `dateLastReward` int(11) DEFAULT NULL,
  `expire_time` bigint(20) NOT NULL,
  PRIMARY KEY (`account_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
