package l2s.gameserver.network.l2.s2c;
import l2s.commons.network.PacketWriter;

import java.util.List;

import l2s.gameserver.Config;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.tables.FakePlayersTable;
import l2s.gameserver.utils.BypassStorage.BypassType;

public class ShowBoardPacket implements IClientOutgoingPacket
{
	public static L2GameServerPacket CLOSE = new ShowBoardPacket();

	private static final String[] DIRECT_BYPASS = new String[]
	{
		"bypass _bbshome",
		"bypass _bbsgetfav",
		"bypass _bbsloc",
		"bypass _bbsclan",
		"bypass _bbsmemo",
		"bypass _maillist_0_1_0_",
		"bypass _friendlist_0_"
	};

	private boolean _show;
	private String _html;
	private String _fav;

	public static void separateAndSend(String html, Player player)
	{
		if (html == null || html.isEmpty())
			return;

		String fav = "";
		if (player.getSessionVar("add_fav") != null)
			fav = "bypass _bbsaddfav_List";

		html = player.getBypassStorage().parseHtml(html, BypassType.BBS, true);

		html = html.replace("<?copyright?>", Config.BBS_COPYRIGHT);
		html = html.replace("<?total_online?>", String.valueOf(GameObjectsStorage.getPlayers(true, true).size() + FakePlayersTable.getActiveFakePlayersCount()));

		if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoardPacket("101", html, fav));
			player.sendPacket(new ShowBoardPacket("102", "", fav));
			player.sendPacket(new ShowBoardPacket("103", "", fav));
		}
		else if (html.length() < 8180 * 2)
		{
			player.sendPacket(new ShowBoardPacket("101", html.substring(0, 8180), fav));
			player.sendPacket(new ShowBoardPacket("102", html.substring(8180, html.length()), fav));
			player.sendPacket(new ShowBoardPacket("103", "", fav));
		}
		else if (html.length() < 8180 * 3)
		{
			player.sendPacket(new ShowBoardPacket("101", html.substring(0, 8180), fav));
			player.sendPacket(new ShowBoardPacket("102", html.substring(8180, 8180 * 2), fav));
			player.sendPacket(new ShowBoardPacket("103", html.substring(8180 * 2, html.length()), fav));
		}
		else
			throw new IllegalArgumentException("Html is too long!");
	}

	public static void separateAndSend(String html, List<String> arg, Player player)
	{
		String fav = "";
		if (player.getSessionVar("add_fav") != null)
			fav = "bypass _bbsaddfav_List";

		html = player.getBypassStorage().parseHtml(html, BypassType.BBS, true);

		html = html.replace("<?copyright?>", Config.BBS_COPYRIGHT);
		html = html.replace("<?total_online?>", String.valueOf(GameObjectsStorage.getPlayers(true, true).size() + FakePlayersTable.getActiveFakePlayersCount()));

		if (html.length() < 8180)
		{
			player.sendPacket(new ShowBoardPacket("1001", html, fav));
			player.sendPacket(new ShowBoardPacket("1002", arg, fav));
		}
		else
			throw new IllegalArgumentException("Html is too long!");
	}

	private ShowBoardPacket(String id, String html, String fav)
	{
		_show = true;
		_html = id + "\u0008";
		if (html != null)
			_html += html;
		_fav = fav;
	}

	private ShowBoardPacket(String id, List<String> arg, String fav)
	{
		_show = true;
		_html = id + "\u0008";
		for (String a : arg)
			_html += a + " \u0008";
	}

	private ShowBoardPacket()
	{
		_show = false;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_show); // c4 1 to show community 00 to hide
		if (_show)
		{
			for (String bbsBypass : DIRECT_BYPASS)
				packetWriter.writeS(bbsBypass);
			packetWriter.writeS(_fav);
			packetWriter.writeS(_html);
		}
	}
}