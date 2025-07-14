package l2s.commons.math.random;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.ToDoubleFunction;

import l2s.commons.util.Rnd;

public class RndSelector<E>
{
	private static final BigDecimal CHANCE_WEIGHT_MOD = BigDecimal.valueOf(10_000);

	protected final List<Node<E>> nodes;

	private long totalWeight = 0L;
	private int faultWeight = 0;

	public RndSelector(boolean concurrent)
	{
		nodes = concurrent ? new CopyOnWriteArrayList<>() : new ArrayList<>();
	}

	public RndSelector(int initialCapacity)
	{
		nodes = new ArrayList<>(initialCapacity);
	}

	public RndSelector()
	{
		this(false);
	}

	public boolean isEmpty()
	{
		return nodes.isEmpty();
	}

	public int size()
	{
		return nodes.size();
	}

	public List<Node<E>> getNodes()
	{
		return nodes;
	}

	public void add(E value, double chance)
	{
		int weight = BigDecimal.valueOf(chance).multiply(CHANCE_WEIGHT_MOD).intValue();
		if(weight <= 0 && chance > 0)
		{
			totalWeight++;
			faultWeight++;
		}
		else
		{
			totalWeight += weight;
		}
		nodes.add(new Node<>(value, weight));
	}

	public void addAll(Collection<E> collection, ToDoubleFunction<E> weightFunction)
	{
		for(E e : collection)
		{
			add(e, weightFunction.applyAsDouble(e));
		}
	}

	private E get(long maxWeight, boolean reduce)
	{
		maxWeight -= faultWeight;
		if(maxWeight <= 0)
			return null;

		Collections.shuffle(nodes);

		long r = Rnd.get(maxWeight);
		long weight = 0;
		for(Iterator<Node<E>> it = nodes.iterator(); it.hasNext();)
		{
			Node<E> node = it.next();
			if((weight += node.weight) > r)
			{
				if(reduce)
				{
					totalWeight -= node.weight;
					it.remove();
				}
				return node.value;
			}
		}
		return null;
	}

	/**
	 * Вернет один из елементов или null, null возможен только если сумма весов всех
	 * элементов меньше maxWeight
	 */
	public E chance(double chance)
	{
		return get(BigDecimal.valueOf(chance).multiply(CHANCE_WEIGHT_MOD).intValue(), false);
	}

	/**
	 * Вернет один из елементов или null, null возможен только если сумма шансов
	 * всех элементов меньше 100
	 */
	public E chance()
	{
		return chance(100.);
	}

	/**
	 * Вернет один из елементов
	 */
	public E select()
	{
		return get(totalWeight, false);
	}

	/**
	 * Вернет один из елементов или null и удалит со списка элемент, null возможен
	 * только если сумма весов всех элементов меньше maxWeight
	 */
	public E chanceReduce(double chance)
	{
		return get(BigDecimal.valueOf(chance).multiply(CHANCE_WEIGHT_MOD).intValue(), true);
	}

	/**
	 * Вернет один из елементов или null и удалит со списка элемент, null возможен
	 * только если сумма шансов всех элементов меньше 100
	 */
	public E chanceReduce()
	{
		return chanceReduce(100.);
	}

	/**
	 * Вернет один из елементов и удалит со списка элемент
	 */
	public E selectReduce()
	{
		return get(totalWeight, true);
	}

	public void clear()
	{
		totalWeight = 0L;
		faultWeight = 0;
		nodes.clear();
	}

	public static class Node<T>
	{
		private final T value;
		private final int weight;

		public Node(T value, int weight)
		{
			this.value = value;
			this.weight = weight;
		}

		public T getValue()
		{
			return value;
		}

		public int getWeight()
		{
			return weight;
		}
	}

	public double getWeight()
	{
		return totalWeight;
	}
}