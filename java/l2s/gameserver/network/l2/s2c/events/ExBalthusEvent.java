package l2s.gameserver.network.l2.s2c.events;

import l2s.gameserver.network.l2.s2c.L2GameServerPacket;

/**
 * @author nexvill
 */
public class ExBalthusEvent extends L2GameServerPacket
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
	protected final void writeImpl()
	{
		writeD(_round); // round
		writeD(_stage); // progress % (draw row)
		writeD(_reward); // reward
		writeD(_receivedAmount); // here should change if player win coins reward (and should be = amount)
		writeD(_amount); // amount
		writeD(_participate ? 1 : 0); // 0: not participate in event, 1: participate
		writeC(_awarded ? 0 : 1); // pending award: 0- used, 1 - active now
		writeD(_time); // time passed
	}
}