package l2s.commons.network;

import java.nio.charset.StandardCharsets;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;

/**
 * @author Nos
 * @author Java-man
 */
public final class PacketWriter
{
	private final ByteBuf _buf;

	public PacketWriter(ByteBuf buf)
	{
		_buf = buf;
	}

	/**
	 * Gets the writable bytes.
	 * @return the writable bytes
	 */
	public int getWritableBytes()
	{
		return _buf.writableBytes();
	}

	/**
	 * Writes a byte.
	 * @param value the byte (The 24 high-order bits are ignored)
	 */
	public void writeC(int value)
	{
		_buf.writeByte(value);
	}

	/**
	 * Writes a byte.
	 * @param value the byte (The 24 high-order bits are ignored)
	 */
	public void writeC(boolean value)
	{
		_buf.writeByte(value ? 0x01 : 0x00);
	}

	/**
	 * Writes a short.
	 * @param value the short (The 16 high-order bits are ignored)
	 */
	public void writeH(int value)
	{
		_buf.writeShortLE(value);
	}

	/**
	 * Writes a short.
	 * @param value the short (The 16 high-order bits are ignored)
	 */
	public void writeH(boolean value)
	{
		_buf.writeShortLE(value ? 0x01 : 0x00);
	}

	/**
	 * Writes an integer.
	 * @param value the integer
	 */
	public void writeD(int value)
	{
		_buf.writeIntLE(value);
	}

	/**
	 * Writes an integer.
	 * @param value the integer
	 */
	public void writeD(boolean value)
	{
		_buf.writeIntLE(value ? 0x01 : 0x00);
	}

	/**
	 * Writes a long.
	 * @param value the long
	 */
	public void writeQ(long value)
	{
		_buf.writeLongLE(value);
	}

	/**
	 * Writes a float.
	 * @param value the float
	 */
	public void writeE(float value)
	{
		_buf.writeIntLE(Float.floatToIntBits(value));
	}

	/**
	 * Writes a double.
	 * @param value the double
	 */
	public void writeF(double value)
	{
		_buf.writeLongLE(Double.doubleToLongBits(value));
	}

	/**
	 * Writes a string.
	 * @param value the string
	 */
	public void writeS(CharSequence value)
	{
		if(value != null)
		{
			_buf.writeCharSequence(value, StandardCharsets.UTF_16LE);
		}

		_buf.writeChar(0);
	}

	/**
	 * Writes a string with fixed length specified as [short length, char[length] data].
	 * @param value the string
	 */
	public void writeSizedString(CharSequence value)
	{
		if(value != null)
		{
			_buf.writeShortLE(value.length());
			_buf.writeCharSequence(value, StandardCharsets.UTF_16LE);
		}
		else
		{
			_buf.writeShort(0);
		}
	}

	public void writeSizedString2(CharSequence value, int length)
	{
		if(value != null)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(value).setLength(length - 1);
			writeS(sb);
		}
		else
		{
			_buf.writeShort(0);
		}
	}

	/**
	 * Writes a byte array.
	 * @param bytes the byte array
	 */
	public void writeB(byte[] bytes)
	{
		_buf.writeBytes(bytes);
	}

	/**
	 * Отсылает число позиций + массив
	 */
	public void writeDD(int[] values, boolean sendCount)
	{
		if(sendCount)
			_buf.writeIntLE(values.length);
		for(int value : values)
			_buf.writeIntLE(value);
	}

	@Override
	public String toString()
	{
		return "PacketWriter{" + "_buf=" + ByteBufUtil.prettyHexDump(_buf) + '}';
	}
}
