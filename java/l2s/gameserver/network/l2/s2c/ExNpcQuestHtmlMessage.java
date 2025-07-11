package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * @author VISTALL
 * @date 16:25/24.04.2011
 */
public class ExNpcQuestHtmlMessage implements IClientOutgoingPacket
{
	private int _npcObjId;
	private CharSequence _html;
	private int _questId;

	public ExNpcQuestHtmlMessage(int npcObjId, CharSequence html, int questId)
	{
		_npcObjId = npcObjId;
		_html = html;
		_questId = questId;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_npcObjId);
		packetWriter.writeS(_html);
		packetWriter.writeD(_questId);
		return true;
	}
}
