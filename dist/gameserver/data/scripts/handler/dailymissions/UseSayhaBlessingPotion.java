package handler.dailymissions;

/**
 * @author nexvill
 */
public class UseSayhaBlessingPotion extends UseItem
{
	private final int[] ITEM_IDS =
	{
		49845, // Sayha's Blessing
		91641, // Sayha's Blessing (Sealed)
		92398 // Sayha's Blessing (Time-Limited) (Sealed)
	};

	@Override
	protected int[] getItemIds()
	{
		return ITEM_IDS;
	}
}
