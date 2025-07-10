package handler.dailymissions;

/**
 * @author nexvill
 */
public class HuntPrimevalIsle extends DailyHunting
{
	private final int[] MONSTER_IDS =
	{
		21962, // Wild Strider
		21963, // Elroki
		21964, // Pachycephalosaurus
		21966, // Ornithomimus
		21968, // Ornithomimus
		21969, // Deinonychus
		21971, // Velociraptor
		21974, // Ornithomimus
		21976, // Deinonychus
		21978, // Tyrannosaurus
		22056, // Pachycephalosaurus
		22057, // Strider
		22058, // Ornithomimus
		22059 // Pterosaur
	};

	@Override
	protected int[] getMonsterIds()
	{
		return MONSTER_IDS;
	}
}
