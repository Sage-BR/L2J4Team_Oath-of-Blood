/* This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.cache;

import javolution.util.FastMap;
import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;

/**
 *
 * @author -Nemesiss-
 */
public class WarehouseCacheManager
{
	private static WarehouseCacheManager _instance;

	protected final FastMap<L2PcInstance, Long> _CachedWh;

	protected final long _CacheTime;

	public static WarehouseCacheManager getInstance()
	{
		if (_instance == null)
			_instance = new WarehouseCacheManager();
		return _instance;
	}

	private WarehouseCacheManager()
	{
		_CacheTime = Config.WAREHOUSE_CACHE_TIME * 60 * 1000;
		_CachedWh = new FastMap<L2PcInstance, Long>().setShared(true);
		ThreadPoolManager.getInstance()
				.scheduleAiAtFixedRate(new CacheScheduler(), 120000, 60000);
	}

	public void addCacheTask(L2PcInstance pc)
	{
		_CachedWh.put(pc, System.currentTimeMillis());
	}

	public void remCacheTask(L2PcInstance pc)
	{
		_CachedWh.remove(pc);
	}

	public class CacheScheduler implements Runnable
	{
		@Override
		public void run()
		{
			long cTime = System.currentTimeMillis();
			for (L2PcInstance pc : _CachedWh.keySet())
			{
				if (cTime - _CachedWh.get(pc) > _CacheTime)
				{
					pc.clearWarehouse();
					_CachedWh.remove(pc);
				}
			}
		}
	}
}
