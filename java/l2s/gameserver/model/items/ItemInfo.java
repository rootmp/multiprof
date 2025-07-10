package l2s.gameserver.model.items;

import l2s.gameserver.data.xml.holder.ItemHolder;
import l2s.gameserver.model.base.Element;
import l2s.gameserver.model.enums.ItemLocation;
import l2s.gameserver.templates.item.ItemTemplate;
import l2s.gameserver.templates.item.support.Ensoul;

public class ItemInfo
{
	private int _ownerId;
	private int _lastChange;
	private int _type1;
	private int _objectId;
	private int _itemId;
	private long _count;
	private int _type2;
	private int _customType1;
	private boolean _isEquipped;
	private long _bodyPart;
	private int _enchantLevel;
	private int _customType2;
	private int _variationStoneId;
	private int _variation1Id;
	private int _variation2Id;
	private int _shadowLifeTime;
	private int _equipSlot;
	private ItemLocation _location;
	private int _temporalLifeTime;
	private int[] _enchantOptions = ItemInstance.EMPTY_ENCHANT_OPTIONS;
	private int _visualId;
	private boolean _isBlessed;

	// Attributes
	private int _attrFire;
	private int _attrWater;
	private int _attrWind;
	private int _attrEarth;
	private int _attrHoly;
	private int _attrUnholy;

	private boolean _isBlocked;

	private Ensoul[] _normalEnsouls = ItemInstance.EMPTY_ENSOULS_ARRAY;
	private Ensoul[] _specialEnsouls = ItemInstance.EMPTY_ENSOULS_ARRAY;

	private ItemTemplate _item;

	private PetItemInfo _petInfo;

	public ItemInfo()
	{
		//
	}

	public ItemInfo(ItemInstance item)
	{
		this(item, false);
	}

	public ItemInfo(ItemInstance item, boolean isBlocked)
	{
		setOwnerId(item.getOwnerId());
		setObjectId(item.getObjectId());
		setItemId(item.getItemId());
		setCount(item.getCount());
		setCustomType1(item.getCustomType1());
		setEquipped(item.isEquipped());
		setEnchantLevel(item.getEnchantLevel());
		setCustomType2(item.getCustomType2());
		setVariationStoneId(item.getVariationStoneId());
		setVariation1Id(item.getVariation1Id());
		setVariation2Id(item.getVariation2Id());
		setShadowLifeTime(item.getShadowLifeTime());
		setEquipSlot(item.getEquipSlot());
		setTemporalLifeTime(item.getTemporalLifeTime());
		setEnchantOptions(item.getEnchantOptions());
		setAttributeFire(item.getAttributeElementValue(Element.FIRE, true));
		setAttributeWater(item.getAttributeElementValue(Element.WATER, true));
		setAttributeWind(item.getAttributeElementValue(Element.WIND, true));
		setAttributeEarth(item.getAttributeElementValue(Element.EARTH, true));
		setAttributeHoly(item.getAttributeElementValue(Element.HOLY, true));
		setAttributeUnholy(item.getAttributeElementValue(Element.UNHOLY, true));
		setIsBlocked(isBlocked);
		setVisualId(item.getVisualId());
		setBlessed(item.isBlessed());
		setNormalEnsouls(item.getNormalEnsouls());
		setSpecialEnsouls(item.getSpecialEnsouls());
		setLocation(item.getLocation());
		setPetInfo(item.getPetInfo());
	}

	public ItemTemplate getItem()
	{
		return _item;
	}

	public void setOwnerId(int ownerId)
	{
		_ownerId = ownerId;
	}

	public void setLastChange(int lastChange)
	{
		_lastChange = lastChange;
	}

	public void setType1(int type1)
	{
		_type1 = type1;
	}

	public void setObjectId(int objectId)
	{
		_objectId = objectId;
	}

	public void setItemId(int itemId)
	{
		_itemId = itemId;
		if (itemId > 0)
		{
			_item = ItemHolder.getInstance().getTemplate(getItemId());
		}
		else
		{
			_item = null;
		}
		if (_item != null)
		{
			setType1(_item.getType1());
			setType2(_item.getType2());
			setBodyPart(_item.getBodyPart());
		}
	}

	public void setCount(long count)
	{
		_count = count;
	}

	public void setType2(int type2)
	{
		_type2 = type2;
	}

	public void setCustomType1(int customType1)
	{
		_customType1 = customType1;
	}

	public void setEquipped(boolean isEquipped)
	{
		_isEquipped = isEquipped;
	}

	public void setBodyPart(long bodyPart)
	{
		_bodyPart = bodyPart;
	}

	public void setEnchantLevel(int enchantLevel)
	{
		_enchantLevel = enchantLevel;
	}

	public void setCustomType2(int customType2)
	{
		_customType2 = customType2;
	}

	public void setVariationStoneId(int val)
	{
		_variationStoneId = val;
	}

	public void setVariation1Id(int val)
	{
		_variation1Id = val;
	}

	public void setVariation2Id(int val)
	{
		_variation2Id = val;
	}

	public void setShadowLifeTime(int shadowLifeTime)
	{
		_shadowLifeTime = shadowLifeTime;
	}

	public void setEquipSlot(int equipSlot)
	{
		_equipSlot = equipSlot;
	}

	public void setTemporalLifeTime(int temporalLifeTime)
	{
		_temporalLifeTime = temporalLifeTime;
	}

	public void setIsBlocked(boolean val)
	{
		_isBlocked = val;
	}

	public void setVisualId(int val)
	{
		_visualId = val;
	}

	public void setBlessed(boolean val)
	{
		_isBlessed = val;
	}

	public int getOwnerId()
	{
		return _ownerId;
	}

	public int getLastChange()
	{
		return _lastChange;
	}

	public int getType1()
	{
		return _type1;
	}

	public int getObjectId()
	{
		return _objectId;
	}

	public int getItemId()
	{
		return _itemId;
	}

	public long getCount()
	{
		return _count;
	}

	public int getType2()
	{
		return _type2;
	}

	public int getCustomType1()
	{
		return _customType1;
	}

	public ItemLocation getLocation()
	{
		return _location;
	}

	public void setLocation(ItemLocation location)
	{
		_location = location;
	}

	public boolean isEquipped()
	{
		return _isEquipped;
	}

	public long getBodyPart()
	{
		return _bodyPart;
	}

	public int getEnchantLevel()
	{
		return _enchantLevel;
	}

	public int getVariationStoneId()
	{
		return _variationStoneId;
	}

	public int getVariation1Id()
	{
		return _variation1Id;
	}

	public int getVariation2Id()
	{
		return _variation2Id;
	}

	public int getShadowLifeTime()
	{
		return _shadowLifeTime;
	}

	public int getCustomType2()
	{
		return _customType2;
	}

	public int getEquipSlot()
	{
		return _equipSlot;
	}

	public int getTemporalLifeTime()
	{
		return _temporalLifeTime;
	}

	public boolean isBlocked()
	{
		return _isBlocked;
	}

	public int getVisualId()
	{
		return _visualId;
	}

	public boolean isBlessed()
	{
		return _isBlessed;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		if (getObjectId() == 0)
		{
			return getItemId() == ((ItemInfo) obj).getItemId();
		}
		return getObjectId() == ((ItemInfo) obj).getObjectId();
	}

	@Override
	public int hashCode()
	{
		int hash = getItemId();
		hash = (89 * hash) + getObjectId();
		return hash;
	}

	public int[] getEnchantOptions()
	{
		return _enchantOptions;
	}

	public void setEnchantOptions(int[] enchantOptions)
	{
		_enchantOptions = enchantOptions;
	}

	public int getAttributeFire()
	{
		return _attrFire;
	}

	public void setAttributeFire(int val)
	{
		_attrFire = val;
	}

	public int getAttributeWater()
	{
		return _attrWater;
	}

	public void setAttributeWater(int val)
	{
		_attrWater = val;
	}

	public int getAttributeWind()
	{
		return _attrWind;
	}

	public void setAttributeWind(int val)
	{
		_attrWind = val;
	}

	public int getAttributeEarth()
	{
		return _attrEarth;
	}

	public void setAttributeEarth(int val)
	{
		_attrEarth = val;
	}

	public int getAttributeHoly()
	{
		return _attrHoly;
	}

	public void setAttributeHoly(int val)
	{
		_attrHoly = val;
	}

	public int getAttributeUnholy()
	{
		return _attrUnholy;
	}

	public void setAttributeUnholy(int val)
	{
		_attrUnholy = val;
	}

	private int[] getAttackElementInfo()
	{
		if (getItem().isWeapon())
		{
			if (_attrFire > 0)
			{
				return new int[]
				{
					Element.FIRE.getId(),
					_attrFire
				};
			}
			if (_attrWater > 0)
			{
				return new int[]
				{
					Element.WATER.getId(),
					_attrWater
				};
			}
			if (_attrWind > 0)
			{
				return new int[]
				{
					Element.WIND.getId(),
					_attrWind
				};
			}
			if (_attrEarth > 0)
			{
				return new int[]
				{
					Element.EARTH.getId(),
					_attrEarth
				};
			}
			if (_attrHoly > 0)
			{
				return new int[]
				{
					Element.HOLY.getId(),
					_attrHoly
				};
			}
			if (_attrUnholy > 0)
			{
				return new int[]
				{
					Element.UNHOLY.getId(),
					_attrUnholy
				};
			}
		}
		return new int[]
		{
			Element.NONE.getId(),
			0
		};
	}

	public int getAttackElement()
	{
		return getAttackElementInfo()[0];
	}

	public int getAttackElementValue()
	{
		return getAttackElementInfo()[1];
	}

	public int getDefenceFire()
	{
		return getItem().isWeapon() ? 0 : _attrFire;
	}

	public int getDefenceWater()
	{
		return getItem().isWeapon() ? 0 : _attrWater;
	}

	public int getDefenceWind()
	{
		return getItem().isWeapon() ? 0 : _attrWind;
	}

	public int getDefenceEarth()
	{
		return getItem().isWeapon() ? 0 : _attrEarth;
	}

	public int getDefenceHoly()
	{
		return getItem().isWeapon() ? 0 : _attrHoly;
	}

	public int getDefenceUnholy()
	{
		return getItem().isWeapon() ? 0 : _attrUnholy;
	}

	public Ensoul[] getNormalEnsouls()
	{
		return _normalEnsouls;
	}

	public void setNormalEnsouls(Ensoul[] ensouls)
	{
		_normalEnsouls = ensouls;
	}

	public Ensoul[] getSpecialEnsouls()
	{
		return _specialEnsouls;
	}

	public void setSpecialEnsouls(Ensoul[] ensouls)
	{
		_specialEnsouls = ensouls;
	}

	public PetItemInfo getPetInfo()
	{
		return _petInfo;
	}

	public void setPetInfo(PetItemInfo petInfo)
	{
		_petInfo = petInfo;
	}
}
