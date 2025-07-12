package l2s.gameserver.network.l2.c2s;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.commons.network.IIncomingPacket;
import l2s.commons.network.IIncomingPackets;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;
import l2s.gameserver.network.l2.IncomingExPackets507;


public class ExPacket implements IClientIncomingPacket
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ExPacket.class);
	private IIncomingPackets<GameClient> _exIncomingPacket;
	private IIncomingPacket<GameClient> _exPacket;
	
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		int exPacketId = packet.readH() & 0xFFFF;
		
		int rev = client.getRevision();
		if (exPacketId < 0)
			return false;
		
		if(rev == 507)
		{
			if (exPacketId >= IncomingExPackets507.PACKET_ARRAY.length)
				return false;

	    if (exPacketId == 401 && packet.readD() == -1) //custom packer ExLetterCollectorTakeReward
	    {
	    	int type = packet.readD();
	    	_exIncomingPacket = IncomingExPackets507.PACKET_EX_ARRAY[type];
	    }
	    else if(exPacketId == 78)//BOOK_MARK
	    {
	    	int type = packet.readD();
	    	_exIncomingPacket = IncomingExPackets507.PACKET_BOOK_MARK_ARRAY[type];
	    }
	    else
	    	_exIncomingPacket = IncomingExPackets507.PACKET_ARRAY[exPacketId];
	    
			if (_exIncomingPacket == null)
			{
				LOGGER.info("{}: Unknown packet: {}", getClass().getSimpleName(), Integer.toHexString(exPacketId));
				return false;
			}
		}
		_exPacket = _exIncomingPacket.newIncomingPacket();
		
		if(client.isLogCPackets())
			LOGGER.info("Received packet {}", _exPacket.getClass().getSimpleName());
		
		return (_exPacket != null) && _exPacket.read(client, packet);
	}
	
	@Override
	public void run(GameClient client) throws Exception
	{
		if (!client.getIgnoreInvalidConnectionState() && !_exIncomingPacket.getConnectionStates().contains(client.getConnectionState()))
		{
			LOGGER.info("{}: Connection at invalid state: {} Required State: {}", _exIncomingPacket, client.getConnectionState(), _exIncomingPacket.getConnectionStates());
			return;
		}
		_exPacket.run(client);
	}
}
