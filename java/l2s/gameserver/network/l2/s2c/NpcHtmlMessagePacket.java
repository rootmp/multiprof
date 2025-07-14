package l2s.gameserver.network.l2.s2c;

import l2s.commons.network.PacketWriter;

/**
 * the HTML parser in the client knowns these standard and non-standard tags and
 * attributes VOLUMN UNKNOWN UL U TT TR TITLE TEXTCODE TEXTAREA TD TABLE SUP SUB
 * STRIKE SPIN SELECT RIGHT PRE P OPTION OL MULTIEDIT LI LEFT INPUT IMG I HTML
 * H7 H6 H5 H4 H3 H2 H1 FONT EXTEND EDIT COMMENT COMBOBOX CENTER BUTTON BR BODY
 * BAR ADDRESS A SEL LIST VAR FORE READONL ROWS VALIGN FIXWIDTH BORDERCOLORLI
 * BORDERCOLORDA BORDERCOLOR BORDER BGCOLOR BACKGROUND ALIGN VALU READONLY
 * MULTIPLE SELECTED TYP TYPE MAXLENGTH CHECKED SRC Y X QUERYDELAY NOSCROLLBAR
 * IMGSRC B FG SIZE FACE COLOR DEFFON DEFFIXEDFONT WIDTH VALUE TOOLTIP NAME MIN
 * MAX HEIGHT DISABLED ALIGN MSG LINK HREF ACTION ClassId fstring
 */
public class NpcHtmlMessagePacket implements IClientOutgoingPacket
{
	private final int _npcObjId;
	private final int _itemId;
	private final CharSequence _html;
	private final boolean _playVoice;

	public NpcHtmlMessagePacket(int npcObjId, int itemId, boolean playVoice, CharSequence html)
	{
		_npcObjId = npcObjId;
		_itemId = itemId;
		_playVoice = playVoice;
		_html = html;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeD(_npcObjId);
		packetWriter.writeS(_html);
		packetWriter.writeD(_itemId);
		packetWriter.writeC(0x00); // new 245
		packetWriter.writeD(!_playVoice);
		return true;
	}
}