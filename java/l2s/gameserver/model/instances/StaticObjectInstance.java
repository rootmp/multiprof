package l2s.gameserver.model.instances;

import java.util.Collections;
import java.util.List;

import l2s.commons.lang.reference.HardReference;
import l2s.gameserver.ai.CtrlIntention;
import l2s.gameserver.handler.onshiftaction.OnShiftActionHolder;
import l2s.gameserver.model.Creature;
import l2s.gameserver.model.GameObject;
import l2s.gameserver.model.GameObjectsStorage;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.World;
import l2s.gameserver.model.reference.L2Reference;
import l2s.gameserver.network.l2.components.HtmlMessage;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.network.l2.s2c.ShowTownMapPacket;
import l2s.gameserver.network.l2.s2c.StaticObjectPacket;
import l2s.gameserver.templates.StaticObjectTemplate;

public class StaticObjectInstance extends GameObject
{
	private final HardReference<StaticObjectInstance> reference;
	private final StaticObjectTemplate _template;
	private int _meshIndex;

	public StaticObjectInstance(int objectId, StaticObjectTemplate template)
	{
		super(objectId);

		_template = template;
		reference = new L2Reference<StaticObjectInstance>(this);

		GameObjectsStorage.put(this);
	}

	@Override
	public HardReference<StaticObjectInstance> getRef()
	{
		return reference;
	}

	public int getUId()
	{
		return _template.getUId();
	}

	public int getType()
	{
		return _template.getType();
	}

	@Override
	public void onAction(Player player, boolean shift)
	{
		if(shift && OnShiftActionHolder.getInstance().callShiftAction(player, StaticObjectInstance.class, this, true))
			return;

		if(player.getTarget() != this)
		{
			player.setTarget(this);
			return;
		}

		if(!player.checkInteractionDistance(this))
		{
			if(player.getAI().getIntention() != CtrlIntention.AI_INTENTION_INTERACT)
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_INTERACT, this, null);
			return;
		}

		if(_template.getType() == 0) // Arena Board
			player.sendPacket(new HtmlMessage(getUId()).setFile("newspaper/arena.htm"));
		else if(_template.getType() == 2) // Village map
		{
			player.sendPacket(new ShowTownMapPacket(_template.getFilePath(), _template.getMapX(), _template.getMapY()));
			player.sendActionFailed();
		}
		else if(_template.getType() == 4)
		{
			// World Statistic
		}
	}

	@Override
	public List<IClientOutgoingPacket> addPacketList(Player forPlayer, Creature dropper)
	{
		return Collections.<IClientOutgoingPacket> singletonList(new StaticObjectPacket(this));
	}

	@Override
	public boolean isAttackable(Creature attacker)
	{
		return false;
	}

	public void broadcastInfo(boolean force)
	{
		StaticObjectPacket p = new StaticObjectPacket(this);
		for(Player player : World.getAroundObservers(this))
			player.sendPacket(p);
	}

	@Override
	public int getGeoZ(int x, int y, int z)
	{
		return z;
	}

	public int getMeshIndex()
	{
		return _meshIndex;
	}

	public void setMeshIndex(int meshIndex)
	{
		_meshIndex = meshIndex;
	}

	@Override
	public boolean isStaticObject()
	{
		return true;
	}
}