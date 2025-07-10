package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class AdminForgePacket implements IClientOutgoingPacket
{
	private List<Part> _parts = new ArrayList<Part>();

	private static class Part
	{
		public byte b;
		public String str;

		public Part(byte bb, String string)
		{
			b = bb;
			str = string;
		}
	}

	public AdminForgePacket()
	{
		//
	}

	@Override
	protected boolean writeOpcodes()
	{
		return true;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		for (Part p : _parts)
		{
			generate(p.b, p.str);
		}

	}

	public boolean generate(byte b, String string)
	{
		if ((b == 'C') || (b == 'c'))
		{
			packetWriter.writeC(Integer.decode(string));
			return true;
		}
		else if ((b == 'D') || (b == 'd'))
		{
			packetWriter.writeD(Integer.decode(string));
			return true;
		}
		else if ((b == 'H') || (b == 'h'))
		{
			packetWriter.writeH(Integer.decode(string));
			return true;
		}
		else if ((b == 'F') || (b == 'f'))
		{
			packetWriter.writeF(Double.parseDouble(string));
			return true;
		}
		else if ((b == 'S') || (b == 's'))
		{
			packetWriter.writeS(string);
			return true;
		}
		else if ((b == 'T') || (b == 't'))
		{
			writeString(string);
			return true;
		}
		else if ((b == 'B') || (b == 'b') || (b == 'X') || (b == 'x'))
		{
			writeB(new BigInteger(string).toByteArray());
			return true;
		}
		else if ((b == 'Q') || (b == 'q'))
		{
			packetWriter.writeQ(Long.decode(string));
			return true;
		}
		return false;
	}

	public void addPart(byte b, String string)
	{
		_parts.add(new Part(b, string));
	}

}