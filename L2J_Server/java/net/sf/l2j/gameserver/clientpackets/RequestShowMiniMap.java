/*
 * This program is free software; you can redistribute it and/or modify
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
package net.sf.l2j.gameserver.clientpackets;

import java.nio.ByteBuffer;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.ShowMiniMap;

/**
 * sample
 *
 * format d
 *
 * @version $Revision: 1 $ $Date: 2005/04/10 00:17:44 $
 */
public class RequestShowMiniMap extends ClientBasePacket
{
	private static final String _C__cd_REQUESTSHOWMINIMAP = "[C] cd RequestShowMiniMap";

	/**
	 */
	public RequestShowMiniMap(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
	}

	@Override
	final void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		activeChar.sendPacket(new ShowMiniMap(1665));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__cd_REQUESTSHOWMINIMAP;
	}
}
