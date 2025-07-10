package l2s.gameserver.model.entity.events;

/**
 * @author VISTALL
 * @date 13:03/10.12.2010
 */
public enum EventType
{
	SIEGE_EVENT, // Siege of Castle, Fortress, Clan Hall, Dominion
	PVP_EVENT, // Kratei Cube, Cleft, Underground Coliseum
	MAIN_EVENT, // 1 - Underground Coliseum Event Runner, 2 - Kratei Cube Runner
	BOAT_EVENT, //
	FUN_EVENT, //
	SHUTTLE_EVENT,
	FIGHT_CLUB_EVENT, // Fight Club Event System
	CUSTOM_PVP_EVENT; // TvT, Hasher and etc.

	public static final EventType[] VALUES = values();
}
