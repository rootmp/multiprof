package l2s.gameserver.templates.beatyshop;

import gnu.trove.map.TIntObjectMap;
import l2s.gameserver.model.base.ClassType;
import l2s.gameserver.model.base.Race;
import l2s.gameserver.model.base.Sex;

public class BeautySetTemplate
{
	private final Race _race;
	private final Sex _sex;
	private final ClassType _class;
	private final TIntObjectMap<BeautyStyleTemplate> _hairs;
	private final TIntObjectMap<BeautyStyleTemplate> _faces;

	public BeautySetTemplate(final Race race, final Sex sex, final ClassType _class, final TIntObjectMap<BeautyStyleTemplate> hairs, final TIntObjectMap<BeautyStyleTemplate> faces)
	{
		_race = race;
		_sex = sex;
		this._class = _class;
		_hairs = hairs;
		_faces = faces;
	}

	public Race getRace()
	{
		return this._race;
	}

	public Sex getSex()
	{
		return this._sex;
	}

	public ClassType getClassType()
	{
		return this._class;
	}

	public BeautyStyleTemplate[] getHairs()
	{
		return _hairs.values(new BeautyStyleTemplate[_hairs.size()]);
	}

	public BeautyStyleTemplate getHair(final int id)
	{
		return _hairs.get(id);
	}

	public BeautyStyleTemplate[] getFaces()
	{
		return _faces.values(new BeautyStyleTemplate[_faces.size()]);
	}

	public BeautyStyleTemplate getFace(final int id)
	{
		return _faces.get(id);
	}
}
