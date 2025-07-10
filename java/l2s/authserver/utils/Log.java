/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.authserver.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2s.authserver.Config;
import l2s.authserver.accounts.Account;
import l2s.authserver.database.DatabaseFactory;

public class Log
{
	private final static Logger _log = LoggerFactory.getLogger(Log.class);
	private static final Logger _logAuth = LoggerFactory.getLogger("auth");
	
	private static final SimpleDateFormat SIMPLE_FORMAT = new SimpleDateFormat("HH:mm dd.MM.yyyy");
	
	private static final String INSERTINFO = "INSERT INTO account_log (time, login, ip) VALUES(?,?,?)";

	public static void LogAccount(Account account)
	{
		if (!Config.LOGIN_LOG)
		{
			return;
		}

		StringBuilder output = new StringBuilder();
		output.append("ACCOUNT[");
		output.append(account.getLogin());
		output.append("] IP[");
		output.append(account.getLastIP());
		output.append("] LAST_ACCESS_TIME[");
		output.append(SIMPLE_FORMAT.format(account.getLastAccess() * 1000L));
		output.append("]");
		_logAuth.info(output.toString());

		try (Connection con = DatabaseFactory.getInstance().getConnection(); 
				PreparedStatement statement = con.prepareStatement(INSERTINFO))
		{
			statement.setInt(1, account.getLastAccess());
			statement.setString(2, account.getLogin());
			statement.setString(3, account.getLastIP());
			statement.execute();
		}
		catch (Exception e)
		{
			_log.error("", e);
		}
	}
}
