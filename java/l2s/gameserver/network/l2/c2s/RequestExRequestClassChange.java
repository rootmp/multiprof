package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;
import l2s.gameserver.model.base.ClassLevel;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.ExClaschangeSetAlarm;
import l2s.gameserver.network.l2.s2c.ExElementalSpiritInfo;
import l2s.gameserver.network.l2.s2c.MagicSkillUse;

public class RequestExRequestClassChange implements IClientIncomingPacket
{
	private int _classId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_classId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		ClassId classId = ClassId.valueOf(_classId);
		if (classId == null)
		{
			player.sendActionFailed();
			return;
		}

		if (!player.canClassChange())
		{
			player.sendActionFailed();
			return;
		}

		if (!player.isInPeaceZone() || player.isInCombat())
		{
			player.sendActionFailed();
			player.sendPacket(SystemMsg.YOU_CANNOT_TRANSFER_YOUR_CLASS_IN_A_NON_PEACE_ZONE_LOCATION);
			player.sendPacket(ExClaschangeSetAlarm.STATIC);
			return;
		}

		if (!player.setClassId(classId.getId(), false))
		{
			player.sendActionFailed();
			return;
		}

		if (player.getClassId().isOfLevel(ClassLevel.FIRST))
		{
			giveRewards(player, 1);
		}
		else if (player.getClassId().isOfLevel(ClassLevel.SECOND))
		{
			giveRewards(player, 2);
		}

		player.sendPacket(new MagicSkillUse(player, 5103, 1, 1000, 0));
		player.sendPacket(SystemMsg.CONGRATULATIONS__YOUVE_COMPLETED_A_CLASS_TRANSFER);
		player.sendPacket(new ExElementalSpiritInfo(player, 0));
		player.broadcastUserInfo(true);
	}

	private void giveRewards(Player player, int classLevel)
	{
		if (classLevel == 2)
		{
			player.getInventory().addItem(91665, 10); // Scroll: 100,000 SP (Sealed)
			player.getInventory().addItem(95489, 1); // 2nd Class Change Gift Box
		}
		else if (classLevel == 1)
		{
			int FIRST_CLASS_CHANGE_GIFT_BOX = 93496;
			int MOON_ARMOR_SET = 93493;
			int MOON_SHELL_SET = 93494;
			int MOON_CAPE_SET = 93495;
			int ADEN_SWORD = 93028;
			int ADEN_DAGGER = 93029;
			int ADEN_BOW = 93030;
			int ADEN_CLUB = 93031;
			int ADEN_TWO_HANDED_SWORD = 93032;
			int ADEN_TWO_HANDED_BLUNT = 93033;
			int ADEN_SPEAR = 93034;
			int ADEN_FISTS = 93035;
			int ADEN_RAPIER = 93036;
			int ADEN_ANCIENT_SWORD = 93037;
			int ADEN_PISTOLS = 94897;
			int ADEN_DUAL_SWORDS = 95691;
			int BONE_QUIVER = 32250;
			int ELEMENTAL_ORB = 94892;
			int SHIELD = 95689;

			int weaponId1 = 0;
			int weaponId2 = 0;
			int armorId = 0;
			int additionalId = 0;

			switch (player.getClassId())
			{
				case WARRIOR:
				{
					weaponId1 = ADEN_SPEAR;
					weaponId2 = ADEN_DUAL_SWORDS;
					armorId = MOON_ARMOR_SET;
					break;
				}
				case KNIGHT:
				case ELVEN_KNIGHT:
				case PALUS_KNIGHT:
				{
					weaponId1 = ADEN_SWORD;
					armorId = MOON_ARMOR_SET;
					additionalId = SHIELD;
					break;
				}
				case ROGUE:
				case ELVEN_SCOUT:
				case ASSASIN:
				{
					weaponId1 = ADEN_DAGGER;
					weaponId2 = ADEN_BOW;
					armorId = MOON_SHELL_SET;
					additionalId = BONE_QUIVER;
					break;
				}
				case WIZARD:
				case CLERIC:
				case ELVEN_WIZARD:
				case ORACLE:
				case DARK_WIZARD:
				case SHILLEN_ORACLE:
				case ORC_SHAMAN:
				{
					weaponId1 = ADEN_TWO_HANDED_BLUNT;
					armorId = MOON_CAPE_SET;
					break;
				}
				case ORC_RAIDER:
				{
					weaponId1 = ADEN_TWO_HANDED_SWORD;
					armorId = MOON_ARMOR_SET;
					break;
				}
				case ORC_MONK:
				{
					weaponId1 = ADEN_FISTS;
					armorId = MOON_SHELL_SET;
					break;
				}
				case SCAVENGER:
				case ARTISAN:
				{
					weaponId1 = ADEN_SPEAR;
					weaponId2 = ADEN_CLUB;
					armorId = MOON_ARMOR_SET;
					additionalId = SHIELD;
					break;
				}
				case TROOPER:
				{
					weaponId1 = ADEN_ANCIENT_SWORD;
					armorId = MOON_SHELL_SET;
					break;
				}
				case SOUL_FINDER:
				{
					weaponId1 = ADEN_RAPIER;
					armorId = MOON_SHELL_SET;
					break;
				}
				case WARDER:
				{
					weaponId1 = ADEN_BOW;
					armorId = MOON_SHELL_SET;
					additionalId = BONE_QUIVER;
					break;
				}
				case H_DEATH_BLADE:
				case E_DEATH_BLADE:
				case DE_DEATH_BLADE:
				{
					weaponId1 = ADEN_SWORD;
					armorId = MOON_ARMOR_SET;
					break;
				}
				case SHARPSHOOTER:
				{
					weaponId1 = ADEN_PISTOLS;
					armorId = MOON_SHELL_SET;
					additionalId = ELEMENTAL_ORB;
					break;
				}
				case WOLF_MASTER:
				{
					weaponId1 = ADEN_SPEAR;
					armorId = MOON_ARMOR_SET;
					break;
				}
			}

			player.getInventory().addItem(weaponId1, 1);
			if (weaponId2 != 0)
			{
				player.getInventory().addItem(weaponId2, 1);
			}
			player.getInventory().addItem(armorId, 1);
			if (additionalId != 0)
			{
				player.getInventory().addItem(additionalId, 1);
			}
			player.getInventory().addItem(FIRST_CLASS_CHANGE_GIFT_BOX, 1);
		}
	}
}
