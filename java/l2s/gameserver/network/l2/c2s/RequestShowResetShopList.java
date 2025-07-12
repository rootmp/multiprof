package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.data.xml.holder.BeautyShopHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.s2c.ExResponseBeautyRegistResetPacket;
import l2s.gameserver.templates.beatyshop.BeautySetTemplate;

public class RequestShowResetShopList implements IClientIncomingPacket
{
	private int _hairStyle;
	private int _face;
	private int _hairColor;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_hairStyle = packet.readD();
		_face = packet.readD();
		_hairColor = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		final Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		final BeautySetTemplate set = BeautyShopHolder.getInstance().getTemplate(activeChar);
		if(set == null)
		{
			activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
			return;
		}
		long reqAdena = 0L;
		boolean reset = false;
		if(_hairStyle > 0 && _hairColor > 0)
		{
			if(set.getHair(_hairStyle) == null)
			{
				activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
				return;
			}
			reqAdena += set.getHair(_hairStyle).getResetPrice();
			reset = true;
		}

		if(_face > 0)
		{
			if(set.getFace(_face) == null)
			{
				activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
				return;
			}
			reqAdena += set.getFace(_face).getResetPrice();
			reset = true;
		}

		if(!reset || activeChar.getAdena() < reqAdena)
		{
			activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 0));
			return;
		}

		activeChar.getInventory().destroyItemByItemId(57, reqAdena);
		if(_hairStyle > 0)
		{
			activeChar.setBeautyHairStyle(0);
			activeChar.setBeautyHairColor(0);
		}
		if(_face > 0)
		{
			activeChar.setBeautyFace(0);
		}
		activeChar.sendPacket(new ExResponseBeautyRegistResetPacket(activeChar, 1, 1));
	}
}
