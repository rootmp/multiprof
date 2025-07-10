/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package l2s.gameserver.taskmanager;

import java.util.concurrent.Future;

import l2s.commons.threading.SteppingRunnableQueueManager;
import l2s.gameserver.ThreadPoolManager;
import l2s.gameserver.model.Creature;

public class DecayTaskManager extends SteppingRunnableQueueManager
{
	private DecayTaskManager()
	{
		super(500L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(this, 500L, 500L);
		ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> purge(), 60000L, 60000L);
	}

	public Future<?> addDecayTask(Creature actor, long delay)
	{
		return schedule(() -> actor.doDecay(), delay);
	}

	public static DecayTaskManager getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final DecayTaskManager INSTANCE = new DecayTaskManager();
	}
}