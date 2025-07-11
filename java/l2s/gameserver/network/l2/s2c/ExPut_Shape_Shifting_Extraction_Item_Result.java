package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author Bonux
 **/
public class ExPut_Shape_Shifting_Extraction_Item_Result implements IClientOutgoingPacket
{
	public static L2GameServerPacket FAIL = new ExPut_Shape_Shifting_Extraction_Item_Result(0x00);
	public static L2GameServerPacket SUCCESS = new ExPut_Shape_Shifting_Extraction_Item_Result(0x01);

	private final int _result;

	public ExPut_Shape_Shifting_Extraction_Item_Result(int result)
	{
		_result = result;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_result); // Result
		return true;
	}
}