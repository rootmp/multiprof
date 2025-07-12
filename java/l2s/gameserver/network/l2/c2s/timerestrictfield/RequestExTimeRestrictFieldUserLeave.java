package l2s.gameserver.network.l2.c2s.timerestrictfield;


import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.TimeRestrictFieldHolder;
import l2s.gameserver.geometry.Location;
import l2s.gameserver.instancemanager.ReflectionManager;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.entity.Reflection;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.c2s.IClientIncomingPacket;
import l2s.gameserver.network.l2.components.SystemMsg;
import l2s.gameserver.network.l2.s2c.timerestrictfield.ExTimeRestrictFieldUserExit;

public class RequestExTimeRestrictFieldUserLeave implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player activeChar = client.getActiveChar();
		if(activeChar == null)
			return;
		Reflection _ref = activeChar.getReflection();
		if(_ref == ReflectionManager.MAIN)
			return;
	/*	
		if(activeChar.isInCombat() || activeChar.isCastingNow())
		{
			activeChar.sendPacket(SystemMsg.S_13721);
			return;
		}
		activeChar.sendPacket(new ExTimeRestrictFieldUserExit(TimeRestrictFieldHolder.getInstance().getFieldId(activeChar.getReflection().getInstancedZoneId())));
		Location return_loc = Rnd.get(_ref.getInstancedZone().getReturnCoords());
		
		if(return_loc!=null)
			activeChar.teleToLocation(return_loc, ReflectionManager.MAIN);
		else
			activeChar.teleToClosestTown();
		activeChar.unsetVar("backCoords");*/
	}
}