package l2s.gameserver.network.l2.c2s;

import l2s.commons.network.PacketReader;
import l2s.gameserver.model.Player;
import l2s.gameserver.network.l2.GameClient;

public class ExLetterCollectorTakeReward implements IClientIncomingPacket
{
	private int _wordId;

	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		_wordId = packet.readD();
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player player = client.getActiveChar();
		if(player == null)
			return;
		if(_wordId == -1)
			return;
		/*if(player.getLevel() < LetterCollectRewardHolder.getInstance().LETTER_COLLECTOR_MIN_LEVEL)
			return;
		
		int[] letters = LetterCollectData.getInstance().getLetterCollectDataById(_wordId);
		if(letters == null || letters.length == 0)
			return;
		
		for(int letter : letters) // проверим наличие букв 
			if(!ItemFunctions.haveItem(player, letter, 1))
				return;
		
		for(int letter : letters) // удаляем буквы
			ItemFunctions.deleteItem(player, letter, 1, "LetterCollectorTakeReward");
		//выдаем награду 
		ItemChance reward = LetterCollectRewardHolder.getInstance().getRandomReward(_wordId);
		if(reward == null)
			return;
		
		player.sendPacket(new SystemMessage(SystemMessage.YOU_GET_THE_LETTER_COLLECTOR_S_REWARD));
		ItemFunctions.addItem(player, reward.getId(), reward.getCount(), "LetterCollectorTakeReward");*/
	}
}