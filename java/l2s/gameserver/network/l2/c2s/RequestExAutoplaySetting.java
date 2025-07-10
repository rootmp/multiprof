package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.gameserver.network.l2.GameClient;


import l2s.gameserver.model.Player;
import l2s.gameserver.model.actor.instances.player.AutoFarm;
import l2s.gameserver.network.l2.s2c.ExAutoplaySetting;

public class RequestExAutoplaySetting implements IClientIncomingPacket
{
	private int _healPercent, _petHealPercent;
	private boolean _farmActivate, _autoPickUpItems, _meleeAttackMode, _politeFarm;;
	private AutoFarm.TargetType _targetType;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		// cchcdch
		int size = readH(); // 16 UNK
		_farmActivate = readC() > 0; // Auto Farm Enabled
		_autoPickUpItems = readC() > 0; // Auto Pick Up items
		try
		{
			_targetType = AutoFarm.TargetType.values()[readH()]; // Target type: 0 - Any target, 1 - Monster, 2 - PC, 3
																	// - NPC
		}
		catch (Exception e)
		{
			return false;
		}
		_meleeAttackMode = readC() > 0;
		_healPercent = packet.readD(); // Auto Heal Percent
		_petHealPercent = packet.readD();
		_politeFarm = readC() > 0;
		return true;
	}

	@Override
	public void run(GameClient client)
	{
		Player player = client.getActiveChar();
		if (player == null)
			return;

		AutoFarm autoFarm = player.getAutoFarm();
		autoFarm.setFarmActivate(_farmActivate);
		autoFarm.setAutoPickUpItems(_autoPickUpItems);
		autoFarm.setTargetType(_targetType);
		autoFarm.setMeleeAttackMode(_meleeAttackMode);
		autoFarm.setHealPercent(_healPercent);
		autoFarm.setPoliteFarm(_politeFarm);
		autoFarm.doAutoFarm();

		player.sendPacket(new ExAutoplaySetting(player));
	}
}