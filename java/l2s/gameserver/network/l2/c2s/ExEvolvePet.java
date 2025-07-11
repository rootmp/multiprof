package l2s.gameserver.network.l2.c2s;
import l2s.commons.network.PacketReader;
import l2s.commons.util.Rnd;
import l2s.gameserver.data.xml.holder.NpcHolder;
import l2s.gameserver.model.Player;
import l2s.gameserver.model.Servitor;
import l2s.gameserver.model.instances.PetInstance;
import l2s.gameserver.model.items.ItemInstance;
import l2s.gameserver.network.l2.GameClient;

/**
 * Written by Eden, on 24.02.2021
 */
public class ExEvolvePet implements IClientIncomingPacket
{
	@Override
	public boolean readImpl(GameClient client, PacketReader packet)
	{
		return true;
	}

	@Override
	public void run(GameClient client) throws Exception
	{
		final Player activeChar = client.getActiveChar();
		if (activeChar == null)
			return;

		final PetInstance pet = activeChar.getPet();
		if (pet == null)
			return;

		final boolean isAbleToEvolveLevel1 = pet.getLevel() >= 40 && pet.getEvolveLevel() == 0;
		final boolean isAbleToEvolveLevel2 = pet.getLevel() >= 76 && pet.getEvolveLevel() == 1;

		if (pet.getPetType() != 0 && !pet.isMounted() && !activeChar.isMounted() && !pet.isDead() && !activeChar.isDead() && !pet.isHungry() && !activeChar.isDecontrolled() && !activeChar.isInDuel() && !activeChar.isSitting() && !activeChar.isFishing() && !activeChar.isInCombat() && !pet.isInCombat())
		{
			if (isAbleToEvolveLevel1 && activeChar.consumeItem(94096, 1, true))
			{
				doEvolve(activeChar, pet, Servitor.EvolveLevel.First, Rnd.nextBoolean() ? 2 : 1);
			}
			else if (isAbleToEvolveLevel2 && activeChar.consumeItem(94117, 1, true))
			{
				doEvolve(activeChar, pet, Servitor.EvolveLevel.Second, 2);
			}
		}
		else
		{
			activeChar.sendMessage("You can't evolve in this time"); // todo system msg conditions
		}
	}

	private void doEvolve(Player activeChar, PetInstance pet, Servitor.EvolveLevel evolveLevel, int incPetId)
	{
		final ItemInstance controlItem = pet.getControlItem();
		PetInstance evolved = PetInstance.restore(controlItem, NpcHolder.getInstance().getTemplate(pet.getNpcId() + incPetId), activeChar);
		if (evolved == null)
		{
			activeChar.sendMessage("Something went wrong. Pet evolve rejected");
			return;
		}
		pet.unSummon(false);
		activeChar.setPet(evolved);
		evolved.setTitle(Servitor.TITLE_BY_OWNER_NAME);

		activeChar.storeEvolvedPets(evolveLevel.ordinal(), pet.getNpcId() + incPetId, controlItem.getItemId());
		if (!evolved.isRespawned())
		{
			evolved.store();
		}

		evolved.getInventory().restore();
		evolved.setEvolveLevel(evolveLevel);
		evolved.setReflection(activeChar.getReflection());
		evolved.spawnMe(pet.getLoc());
		evolved.setRunning();
		evolved.setFollowMode(true);
		evolved.getInventory().validateItems();
	}
}
