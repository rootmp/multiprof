package l2s.gameserver.network.l2.s2c;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.ServerPacketOpcodes;
import l2s.gameserver.network.l2.ServerPacketOpcodes507;

/**
 * Format: (chd) ddd[dS]d[dS]
 * d: unknown
 * d: always -1
 * d: blue players number
 * [
 * 		d: player object id
 * 		S: player name
 * ]
 * d: blue players number
 * [
 * 		d: player object id
 * 		S: player name
 * ]
 */
public abstract class ExBlockUpSetList implements IClientOutgoingPacket
{
	@Override
	public ByteBuf getOpcodes()
	{
		try
		{
			ServerPacketOpcodes spo = ServerPacketOpcodes507.ExBlockUpSetList;
			ByteBuf opcodes = Unpooled.buffer();
			opcodes.writeByte(spo.getId());
			int exOpcode = spo.getExId();
			if(exOpcode >= 0)
				opcodes.writeShortLE(exOpcode);
			return opcodes.retain();
		}
		catch(IllegalArgumentException e)
		{}
		catch(Exception e)
		{
			LOGGER.error("Cannot find serverpacket opcode: " + getClass().getSimpleName() + "!");
		}
		return Unpooled.EMPTY_BUFFER;
	}

	public static class TeamList extends ExBlockUpSetList
	{
		private final List<Player> _bluePlayers;
		private final List<Player> _redPlayers;
		private final int _roomNumber;

		public TeamList(List<Player> redPlayers, List<Player> bluePlayers, int roomNumber)
		{
			_redPlayers = redPlayers;
			_bluePlayers = bluePlayers;
			_roomNumber = roomNumber - 1;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x00); // type

			packetWriter.writeD(_roomNumber);
			packetWriter.writeD(0xffffffff);

			packetWriter.writeD(_bluePlayers.size());
			for(Player player : _bluePlayers)
			{
				packetWriter.writeD(player.getObjectId());
				packetWriter.writeS(player.getName());
			}
			packetWriter.writeD(_redPlayers.size());
			for(Player player : _redPlayers)
			{
				packetWriter.writeD(player.getObjectId());
				packetWriter.writeS(player.getName());
			}
			return true;
		}
	}

	public static class AddPlayer extends ExBlockUpSetList
	{
		private final int _objectId;
		private final String _name;
		private final boolean _isRedTeam;

		public AddPlayer(Player player, boolean isRedTeam)
		{
			_objectId = player.getObjectId();
			_name = player.getName();
			_isRedTeam = isRedTeam;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x01); // type

			packetWriter.writeD(0xffffffff);

			packetWriter.writeD(_isRedTeam ? 0x01 : 0x00);
			packetWriter.writeD(_objectId);
			packetWriter.writeS(_name);
			return true;
		}
	}

	public static class RemovePlayer extends ExBlockUpSetList
	{
		private final int _objectId;
		private final boolean _isRedTeam;

		public RemovePlayer(Player player, boolean isRedTeam)
		{
			_objectId = player.getObjectId();
			_isRedTeam = isRedTeam;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x02); // type

			packetWriter.writeD(0xffffffff);

			packetWriter.writeD(_isRedTeam ? 0x01 : 0x00);
			packetWriter.writeD(_objectId);
			return true;
		}
	}

	public static class ChangeTimeToStart extends ExBlockUpSetList
	{
		private final int _seconds;

		public ChangeTimeToStart(int seconds)
		{
			_seconds = seconds;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x03); // type
			packetWriter.writeD(_seconds);
			return true;
		}
	}

	public static class RequestReady extends ExBlockUpSetList
	{
		public static final RequestReady STATIC = new RequestReady();

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x04); // type
			return true;
		}
	}

	public static class ChangeTeam extends ExBlockUpSetList
	{
		private int _objectId;
		private boolean _fromRedTeam;

		public ChangeTeam(Player player, boolean fromRedTeam)
		{
			_objectId = player.getObjectId();
			_fromRedTeam = fromRedTeam;
		}

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0x05); // type

			packetWriter.writeD(_objectId);
			packetWriter.writeD(_fromRedTeam ? 0x01 : 0x00);
			packetWriter.writeD(_fromRedTeam ? 0x00 : 0x01);
			return true;
		}
	}

	public static class CloseUI extends ExBlockUpSetList
	{
		public static final CloseUI STATIC = new CloseUI();

		@Override
		public boolean write(PacketWriter packetWriter)
		{
			packetWriter.writeD(0xffffffff); // type
			return true;
		}
	}
}