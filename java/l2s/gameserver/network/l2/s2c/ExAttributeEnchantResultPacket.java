package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import l2s.gameserver.model.base.Element;

/**
 * @author Bonux
 **/
public class ExAttributeEnchantResultPacket implements IClientOutgoingPacket
{
	private final boolean _isWeapon;
	private final Element _element;
	private final int _oldValue;
	private final int _newValue;
	private final int _usedStones;
	private final int _failedStones;

	public ExAttributeEnchantResultPacket(boolean isWeapon, Element element, int oldValue, int newValue, int usedStones, int failedStones)
	{
		_isWeapon = isWeapon;
		_element = element;
		_oldValue = oldValue;
		_newValue = newValue;
		_usedStones = usedStones;
		_failedStones = failedStones;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(0x00); // TODO
		packetWriter.writeH(0x00); // TODO
		packetWriter.writeC(_isWeapon ? 0x01 : 0x00); // Armor - 0x00 / Weapon - 0x01
		packetWriter.writeH(_element.getId()); // Element
		packetWriter.writeH(_oldValue);
		packetWriter.writeH(_newValue);
		packetWriter.writeH(_usedStones);
		packetWriter.writeH(_failedStones);
	}
}