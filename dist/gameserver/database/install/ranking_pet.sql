DROP TABLE IF EXISTS `ranking_pet`;
CREATE TABLE `ranking_pet` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `objId` int(11) NOT NULL,
  `player_obj_Id` int(11) NOT NULL,
  `item_obj_id` int(11) NOT NULL,
  `player_name` varchar(255) NOT NULL,
  `clan_name` varchar(255) DEFAULT NULL,
  `user_race` int(11) DEFAULT NULL,
  `user_level` int(11) DEFAULT NULL,
  `pet_index` int(11) NOT NULL,
  `nick_name` varchar(255) NOT NULL,
  `exp` bigint(20) DEFAULT NULL,
  `sp` int(11) DEFAULT NULL,
  `fed` int(11) DEFAULT NULL,
  `pet_level` int(11) DEFAULT NULL,
  `max_fed` int(11) DEFAULT NULL,
  `npc_class_id` int(11) DEFAULT NULL,
  `evolve_level` int(11) DEFAULT NULL,
  `random_names` int(11) DEFAULT NULL,
  `passive_skill` int(11) DEFAULT NULL,
  `passive_skill_level` int(11) DEFAULT NULL,
  `pet_index_rank` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
