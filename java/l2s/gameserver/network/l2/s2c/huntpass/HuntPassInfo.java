package l2s.gameserver.network.l2.s2c.huntpass;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.HuntPass;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class HuntPassInfo implements IClientOutgoingPacket
{
	private final int _interfaceType;
	private final HuntPass _huntPass;
	private final int _timeEnd;
	private final int _isPremium;
	private final int _points;
	private final int _step;
	private final int _rewardStep;
	private final int _premiumRewardStep;

	public HuntPassInfo(Player player, int interfaceType)
	{

		_interfaceType = interfaceType;
		_huntPass = player.getHuntPass();
		_timeEnd = _huntPass.getHuntPassDayEnd();
		_isPremium = _huntPass.isPremium() ? 1 : 0;
		_points = _huntPass.getPoints();
		_step = _huntPass.getCurrentStep();
		_rewardStep = _huntPass.getRewardStep();
		_premiumRewardStep = _huntPass.getPremiumRewardStep();

	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_interfaceType);//cPassType
		packetWriter.writeD(_timeEnd); // nLeftTime
		packetWriter.writeC(_isPremium); // bIsPremium
		packetWriter.writeD(_points); // nCurCount
		packetWriter.writeD(_step); // nCurStep
		packetWriter.writeD(_rewardStep); // nRewardStep
		packetWriter.writeD(_premiumRewardStep); // PnPremiumRewardStep
		return true;
	}
}
