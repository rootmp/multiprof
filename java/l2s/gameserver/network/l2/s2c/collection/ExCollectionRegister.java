package l2s.gameserver.network.l2.s2c.collection;

import l2s.commons.network.PacketWriter;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.s2c.IClientOutgoingPacket;

public class ExCollectionRegister implements IClientOutgoingPacket
{
	private int _collectionId, _slotId, _item_id, _cEnchant, _nAmount;
	private boolean _bBless;

	public ExCollectionRegister(Player player, int collectionId, int slotId, int item_id, int cEnchant, boolean bBless, int nAmount)
	{
		_collectionId = collectionId;
		_slotId = slotId;
		_item_id = item_id;
		_cEnchant = cEnchant;
		_bBless = bBless;
		_nAmount = nAmount;
	}

	@Override
	public boolean write(PacketWriter packetWriter)
	{
		packetWriter.writeH(_collectionId);
		packetWriter.writeC(1);// bSuccess
		packetWriter.writeC(1);// bRecursiveReward
		packetWriter.writeH(14);//size
		packetWriter.writeC(_slotId);//cSlotIndex 
		packetWriter.writeD(_item_id);//nItemClassID
		packetWriter.writeC(_cEnchant);//cEnchant
		packetWriter.writeC(_bBless);//bBless
		packetWriter.writeC(0);//cBlessCondition
		packetWriter.writeD(_nAmount);//nAmount
		return true;

	}
}