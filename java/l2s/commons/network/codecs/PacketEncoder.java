package l2s.commons.network.codecs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import l2s.commons.network.IOutgoingPacket;
import l2s.commons.network.PacketWriter;

/**
 * @author Nos
 */
@Sharable
public class PacketEncoder extends MessageToByteEncoder<IOutgoingPacket>
{
  private static final Logger logger = LoggerFactory.getLogger(PacketEncoder.class);
	private final int _maxPacketSize;

	public PacketEncoder(int maxPacketSize)
	{
		super();
		_maxPacketSize = maxPacketSize;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, IOutgoingPacket packet, ByteBuf out)
	{
		try
		{
			if(packet.canBeWritten())
			{
				PacketWriter packetWriter = new PacketWriter(out);
				if(!packet.getClass().getSimpleName().equals("MTLPacket")&& !packet.getClass().getSimpleName().equals("SocialActionPacket")
						&& !packet.getClass().getSimpleName().equals("DeleteObjectPacket")
						&& !packet.getClass().getSimpleName().equals("MagicSkillLaunchedPacket")
						&& !packet.getClass().getSimpleName().equals("MagicSkillUse")
						&& !packet.getClass().getSimpleName().equals("StatusUpdatePacket")
						&& !packet.getClass().getSimpleName().equals("NpcInfoPacket"))
				{
					ByteBuf opcodes = packet.getOpcodes();
					StringBuilder opcodeStr = new StringBuilder();
					if (opcodes != null && opcodes.isReadable())
					{
						int readable = opcodes.readableBytes();
						for (int i = 0; i < readable; i++)
							opcodeStr.append(String.format("%02X ", opcodes.getUnsignedByte(opcodes.readerIndex() + i)));
					}
					System.out.println("PacketEncoder: " + packet.getClass().getSimpleName() + " [Opcode: " + opcodeStr.toString().trim() + "]");

				}
				
				writeOpcode(packet, out);
				if(packet.write(packetWriter))
				{
					if(out.writerIndex() > _maxPacketSize)
						throw new IllegalStateException("Packet (" + packet.getType() + "/" + packet + ") size (" + out.writerIndex() + ") is bigger than the limit (" + _maxPacketSize + ")");
				}
				else
					out.clear();
			}
			else
			{
				// Avoid sending the packet
				out.clear();
			}
		}
		catch(Throwable e)
		{
			Channel channel = ctx.channel();
			if(channel != null)
			{
				channel.close();
			}

			logger.error("Failed sending Packet(type: {}, content: {})", packet.getType(), e);

			// Avoid sending the packet if some exception happened
			out.clear();
		}
	}

	private void writeOpcode(IOutgoingPacket packet, ByteBuf out)
	{
			ByteBuf opcodes = packet.getOpcodes();
			if(opcodes != null && opcodes.isReadable())
			{
				ByteBuf originalData = out.copy();
				out.clear();
				out.writeBytes(opcodes);
				out.writeBytes(originalData);
				originalData.release();
				opcodes.release();
			}
	}


}