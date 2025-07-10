package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExBalthusEvent implements IClientOutgoingPacket
{
	private int _round;
	private int _stage;
	private int _reward;
	private int _amount;
	private int _receivedAmount;
	private boolean _participate;
	private boolean _awarded;
	private int _time;

	public ExBalthusEvent(int round, int stage, int reward, int amount, int receivedAmount, boolean participate, boolean awarded, int time)
	{
		_round = round;
		_stage = stage;
		_reward = reward;
		_amount = amount;
		_receivedAmount = receivedAmount;
		_participate = participate;
		_awarded = awarded;
		_time = time;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_round); // round
		packetWriter.writeD(_stage); // progress % (draw row)
		packetWriter.writeD(_reward); // reward
		packetWriter.writeD(_receivedAmount); // here should change if player win coins reward (and should be = amount)
		packetWriter.writeD(_amount); // amount
		packetWriter.writeD(_participate ? 1 : 0); // 0: not participate in event, 1: participate
		packetWriter.writeC(_awarded ? 0 : 1); // pending award: 0- used, 1 - active now
		packetWriter.writeD(_time); // time passed
	}
}