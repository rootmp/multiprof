package l2s.gameserver.network.l2.c2s.ability;

import l2s.commons.network.PacketReader;
import l2s.gameserver.enums.AbilitiesScheme;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;

public class RequestExChangeAbilityPreset implements IClientIncomingPacket
{
	private AbilitiesScheme cPreset; 

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		cPreset = AbilitiesScheme.valueOf(packet.readC()); 
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		//activeChar.getAbilitiesManager().changeActiveScheme(cPreset);
		
	}
}
