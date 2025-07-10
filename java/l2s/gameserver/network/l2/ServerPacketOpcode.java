package l2s.gameserver.network.l2;

public final class ServerPacketOpcode
{
	private int _id;
	private int _exId;
	
	public ServerPacketOpcode(int id, int exId)
	{
		_id=id;
		_exId = exId;
	}

	public int getId()
	{
		return _id;
	}

	public int getExId()
	{
		return _exId;
	}
	
}