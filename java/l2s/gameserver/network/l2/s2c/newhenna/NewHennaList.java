package l2s.gameserver.network.l2.s2c.newhenna;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;
import l2s.gameserver.templates.item.henna.Henna;
import l2s.gameserver.templates.item.henna.HennaPoten;

public class NewHennaList implements IClientOutgoingPacket
{
	private final HennaPoten[] _hennaId;
	private final int _activeslots;
	private final int _dailyStep;
	private final int _dailyCount;
	private int _cSendType = 0;
	private int _nResetCount = 0;
	private int _nResetMaxCount = -1;
	private int _reset_count;

	public NewHennaList(Player player, int cSendType)
	{
		_dailyStep = player.getDyePotentialDailyStep();
		_dailyCount = player.getDyePotentialDailyCount();
		_nResetCount = player.getDyePotentialDailyResetCount();
		_hennaId = player.getHennaPotenList();
		_activeslots = player.getAvailableHennaSlots();

		int reset_count = player.getDyePotentialDailyResetCount();
		_reset_count = 1000;
		if(reset_count >= 3 && reset_count <= 5)
			_reset_count = 2000;
		else if(reset_count >= 6)
			_reset_count = 5000;
		_cSendType = cSendType;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeC(_cSendType);
		packetWriter.writeH(_dailyStep);
		packetWriter.writeH(_dailyCount);

		packetWriter.writeH(_nResetCount);
		packetWriter.writeH(_nResetMaxCount);
		//  var array<_ItemInfo> resetCostList;
		packetWriter.writeD(1);//size
		//for(item : _resetCostList)
		//{
		packetWriter.writeD(91663);
		packetWriter.writeQ(_reset_count);
		//}

		if(_dailyStep != -1)
		{
			packetWriter.writeD(_hennaId.length);
			for(int i = 1; i <= _hennaId.length; i++)
			{
				final HennaPoten hennaPoten = _hennaId[i - 1];
				final Henna henna = _hennaId[i - 1].getHenna();
				int isactiveStep = hennaPoten.getActiveStep();
				packetWriter.writeD(henna != null ? henna.getDyeId() : 0x00);
				packetWriter.writeD(isactiveStep == 0 ? 0 : hennaPoten.getPotenId());
				packetWriter.writeC(_activeslots >= i);
				packetWriter.writeH(hennaPoten.getEnchantLevel());

				packetWriter.writeD(hennaPoten.getEnchantExp());
				if(hennaPoten.getHenna() != null && hennaPoten.getEnchantLevel() == 30 && hennaPoten.getEnchantExp() >= 2500
						&& hennaPoten.getHenna().getPatternLevel() == 30)
					packetWriter.writeH(hennaPoten.getActiveStep() + 1);
				else
					packetWriter.writeH(hennaPoten.getActiveStep());
				packetWriter.writeH(0);//nDailyStep
				packetWriter.writeH(0);//nDailyCount
				packetWriter.writeH(30);//nOpenedSlotStep
			}
		}
		else
			packetWriter.writeD(0);
		return true;
	}
}
