package l2s.gameserver.templates.adenLab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdenLabStageTemplate
{
	private int _id;
	private int _cardCount;
	private int normalEffect;
	public int[] specialOptionsId = new int[2];

	private final Map<Integer, List<CardselectOptionData>> specialOptions = new HashMap<>();
	private PkAdenLabSpecialGradeProb[] _specialGradeProbs;

	public AdenLabStageTemplate(int id, int cardCount)
	{
		_id = id;
		_cardCount = cardCount;
	}

	public int getId()
	{
		return _id;
	}

	public int getCardCount()
	{
		return _cardCount;
	}

	public int getNormalEffect()
	{
		return normalEffect;
	}

	public void setNormalEffect(int effectId)
	{
		normalEffect = effectId;
	}

	public void addSpecialOption(int slotIndex, CardselectOptionData optionData)
	{
		specialOptions.computeIfAbsent(slotIndex, k -> new ArrayList<>()).add(optionData);
	}

	public List<CardselectOptionData> getSpecialOptions(int slotIndex)
	{
		return specialOptions.getOrDefault(slotIndex, new ArrayList<>());
	}

	public void generateSpecialGradeProbs()
	{
		int maxIndex = specialOptions.keySet().stream().max(Integer::compare).orElse(-1);
		_specialGradeProbs = new PkAdenLabSpecialGradeProb[maxIndex + 1];

		for(Map.Entry<Integer, List<CardselectOptionData>> entry : specialOptions.entrySet())
		{
			int index = entry.getKey();
			List<CardselectOptionData> options = entry.getValue();

			int[] probs = new int[options.size()];
			for(int i = 0; i < options.size(); i++)
			{
				probs[i] = (int) (options.get(i).getChance() * 100);
			}

			PkAdenLabSpecialGradeProb gradeProb = new PkAdenLabSpecialGradeProb();
			gradeProb.probs = probs;
			_specialGradeProbs[index] = gradeProb;
		}
	}

	public PkAdenLabSpecialGradeProb[] getSpecialGradeProbs()
	{
		return _specialGradeProbs;
	}

	public List<CardselectOptionData> getOptionsByChance()
	{
		List<CardselectOptionData> selectedOptions = new ArrayList<>();

		for(Map.Entry<Integer, List<CardselectOptionData>> entry : specialOptions.entrySet())
		{
			List<CardselectOptionData> options = entry.getValue();
			if(options == null || options.isEmpty())
				continue;
			double totalChance = options.stream().mapToDouble(CardselectOptionData::getChance).sum();
			double randomValue = Math.random() * totalChance;

			double accumulatedChance = 0;
			for(CardselectOptionData option : options)
			{
				accumulatedChance += option.getChance();
				if(randomValue < accumulatedChance)
				{
					selectedOptions.add(option);
					break;
				}
			}
		}

		return selectedOptions;
	}

	public int[] getSpecialOptionsId()
	{
		return specialOptionsId;
	}
}
