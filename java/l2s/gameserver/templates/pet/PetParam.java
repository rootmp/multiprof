package l2s.gameserver.templates.pet;

public class PetParam
{

	private int _objId;
	private String _name;
	private long _exp;
	private int _sp;
	private int _fed;
	private int _npcid;
	private int _evolve_level;
	private int _random_name;
	private int _pet_index;
	private int _passive_skill;
	private int _passive_skill_level;

	public PetParam(int objId, String name, long exp, int sp, int fed, int npcid, int evolve_level, int random_names, int pet_index, int passive_skill, int passive_skill_level)
	{
		setObjId(objId);
		setName(name);
		setExp(exp);
		setSp(sp);
		setFed(fed);
		setEvolveNpcId(npcid);
		setEvolveLevel(evolve_level);
		setRandomName(random_names);
		setPetIndex(pet_index);
		_passive_skill = passive_skill;
		_passive_skill_level = passive_skill_level;
	}

	public PetParam()
	{
		setObjId(0);
		setName("");
		setExp(0);
		setSp(0);
		setFed(0);
		setEvolveNpcId(-1);
		setEvolveLevel(0);
		setRandomName(0);
		setPetIndex(0);
		setPassiveSkill(0);
		setPassiveSkillLevel(0);
	}

	public boolean isEmpty()
	{
		return getObjectId() == 0;
	}

	public String getName()
	{
		return _name;
	}

	public void setName(String _name)
	{
		this._name = _name;
	}

	public int getEvolveNpcId()
	{
		return _npcid;
	}

	public void setEvolveNpcId(int _npcid)
	{
		this._npcid = _npcid;
	}

	public int getObjectId()
	{
		return _objId;
	}

	public void setObjId(int _objId)
	{
		this._objId = _objId;
	}

	public long getExp()
	{
		return _exp;
	}

	public void setExp(long _exp)
	{
		this._exp = _exp;
	}

	public int getSp()
	{
		return _sp;
	}

	public void setSp(int _sp)
	{
		this._sp = _sp;
	}

	public int getCurrentFed()
	{
		return _fed;
	}

	public void setFed(int _fed)
	{
		this._fed = _fed;
	}

	public int getEvolveLevel()
	{
		return _evolve_level;
	}

	public void setEvolveLevel(int _evolve_level)
	{
		this._evolve_level = _evolve_level;
	}

	public int getRandomName()
	{
		return _random_name;
	}

	public void setRandomName(int _random_names)
	{
		this._random_name = _random_names;
	}

	public int getPetIndex()
	{
		return _pet_index;
	}

	public void setPetIndex(int _pet_index)
	{
		this._pet_index = _pet_index;
	}

	public int getPassiveSkill()
	{
		return _passive_skill;
	}

	public int getPassiveSkillLevel()
	{
		return _passive_skill_level;
	}

	public void setPassiveSkill(int passive_skill)
	{
		_passive_skill = passive_skill;
	}

	public void setPassiveSkillLevel(int level)
	{
		_passive_skill_level = level;
	}
}