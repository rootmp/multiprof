package l2s.gameserver.network.l2.s2c;

import java.util.Collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.templates.item.support.Ensoul;

/**
 * @author Bonux
**/
public class ExEnsoulResult implements IClientOutgoingPacket
{
	public static final IClientOutgoingPacket FAIL = new ExEnsoulResult();

	private final boolean _success;
	private final Collection<Ensoul> _normalEnsouls;
	private final Collection<Ensoul> _specialEnsouls;

	private ExEnsoulResult()
	{
		_success = false;
		_normalEnsouls = null;
		_specialEnsouls = null;
	}

	public ExEnsoulResult(Collection<Ensoul> normalEnsouls, Collection<Ensoul> specialEnsouls)
	{
		_success = true;
		_normalEnsouls = normalEnsouls;
		_specialEnsouls = specialEnsouls;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_success);
		if(_success)
		{
			packetWriter.writeC(_normalEnsouls.size());
			for(Ensoul ensoul : _normalEnsouls)
				packetWriter.writeD(ensoul.getId());

			packetWriter.writeC(_specialEnsouls.size());
			for(Ensoul ensoul : _specialEnsouls)
				packetWriter.writeD(ensoul.getId());
		}
		return true;
	}
}