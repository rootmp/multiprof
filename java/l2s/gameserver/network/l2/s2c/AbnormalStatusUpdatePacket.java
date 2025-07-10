package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.ArrayList;
import java.util.List;

/**
 * sample 0000: 85 02 00 10 04 00 00 01 00 4b 02 00 00 2c 04 00 .........K...,..
 * 0010: 00 01 00 58 02 00 00 ...X... format h (dhd)
 *
 * @version $Revision: 1.3.2.1.2.6 $ $Date: 2005/04/05 19:41:08 $
 */
public class AbnormalStatusUpdatePacket implements IClientOutgoingPacket
{
	public static final int INFINITIVE_EFFECT = -1;
	private List<Abnormal> _effects = new ArrayList<>();

	class Abnormal
	{
		int _skillId;
		int _dat;
		int _abnormalType;
		int _duration;

		public Abnormal(int skillId, int dat, int abnormalType, int duration)
		{
			_skillId = skillId;
			_dat = dat;
			_abnormalType = abnormalType;
			_duration = duration;
		}
	}

	public AbnormalStatusUpdatePacket()
	{
		_effects = new ArrayList<Abnormal>();
	}

	public void addEffect(int skillId, int dat, int abnormalType, int duration)
	{
		_effects.add(new Abnormal(skillId, dat, abnormalType, duration));
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_effects.size());

		for (Abnormal temp : _effects)
		{
			packetWriter.writeD(temp._skillId);
			packetWriter.writeH(temp._dat);
			packetWriter.writeD(temp._abnormalType);
			writeOptionalD(temp._duration);
		}
	}
}