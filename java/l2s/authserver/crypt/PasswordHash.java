package l2s.authserver.crypt;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class PasswordHash
{
	private final static Logger _log = LoggerFactory.getLogger(PasswordHash.class);

	private final String _name;

	public PasswordHash(String name)
	{
		_name = name;
	}

	public boolean compare(String password, String expected)
	{
		try
		{
			return encrypt(password).equals(expected);
		}
		catch (Exception e)
		{
			_log.error(_name + ": encryption error!", e);
			return false;
		}
	}

	public String encrypt(String password) throws Exception
	{
		AbstractChecksum checksum = JacksumAPI.getChecksumInstance(_name);
		checksum.setEncoding("BASE64");
		checksum.update(password.getBytes());
		return checksum.format("#CHECKSUM");
	}
}