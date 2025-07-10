package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.GameTimeController;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.base.ClassId;

public class CharacterSelectedPacket implements IClientOutgoingPacket
{
	// SdSddddddddddffddddddddddddddddddddddddddddddddddddddddd d
	private int _sessionId, char_id, clan_id, sex, race, _base_class_id, class_id;
	private String _name, _title;
	private Location _loc;
	private double curHp, curMp;
	private int level, karma, _pk;
	private long _exp, _sp;

	public CharacterSelectedPacket(final Player cha, final int sessionId)
	{
		_sessionId = sessionId;

		_name = cha.getName();
		char_id = cha.getObjectId(); // FIXME 0x00030b7a ??
		_title = cha.getTitle();
		clan_id = cha.getClanId();
		sex = cha.getSex().ordinal();
		race = cha.getRace().ordinal();
		_base_class_id = ClassId.valueOf(cha.getBaseClassId()).getFirstParent(sex).getId();
		class_id = cha.getClassId().getId();
		_loc = cha.getLoc();
		curHp = cha.getCurrentHp();
		curMp = cha.getCurrentMp();
		_sp = cha.getSp();
		_exp = cha.getExp();
		level = cha.getLevel();
		karma = cha.getKarma();
		_pk = cha.getPkKills();
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeS(_name);
		packetWriter.writeD(char_id);
		packetWriter.writeS(_title);
		packetWriter.writeD(_sessionId);
		packetWriter.writeD(clan_id);
		packetWriter.writeD(0x00); // Builder Level
		packetWriter.writeD(sex);
		packetWriter.writeD(race);
		packetWriter.writeD(_base_class_id); // base class_id
		packetWriter.writeD(0x01); // active ??
		packetWriter.writeD(_loc.x);
		packetWriter.writeD(_loc.y);
		packetWriter.writeD(_loc.z);

		packetWriter.writeF(curHp);
		packetWriter.writeF(curMp);
		packetWriter.writeQ(_sp);
		packetWriter.writeQ(_exp);
		packetWriter.writeD(level);
		packetWriter.writeD(karma); // ?
		packetWriter.writeD(_pk);
		// extra info
		packetWriter.writeD(GameTimeController.getInstance().getGameTime()); // in-game time
		packetWriter.writeD(0x00); //
		packetWriter.writeD(class_id); // classId

		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);
		packetWriter.writeD(0);

		writeB(new byte[64]);
		packetWriter.writeD(0);
	}
}