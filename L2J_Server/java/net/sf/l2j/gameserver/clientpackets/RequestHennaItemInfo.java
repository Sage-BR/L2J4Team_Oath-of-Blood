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
import net.sf.l2j.gameserver.datatables.HennaTable;
import net.sf.l2j.gameserver.model.L2HennaInstance;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.HennaItemInfo;
import net.sf.l2j.gameserver.templates.L2Henna;

/**
 * This class ...
 *
 * @version $Revision$ $Date$
 */
public class RequestHennaItemInfo extends ClientBasePacket
{
	private static final String _C__BB_RequestHennaItemInfo = "[C] bb RequestHennaItemInfo";

	// private static Logger _log =
	// Logger.getLogger(RequestHennaItemInfo.class.getName());
	private final int SymbolId;
	// format cd

	/**
	 * packet type id 0xbb format: cd
	 *
	 * @param decrypt
	 */
	public RequestHennaItemInfo(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		SymbolId = readD();
	}

	@Override
	void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		L2Henna template = HennaTable.getInstance().getTemplate(SymbolId);
		if (template == null)
		{
			return;
		}
		L2HennaInstance temp = new L2HennaInstance(template);

		HennaItemInfo hii = new HennaItemInfo(temp, activeChar);
		activeChar.sendPacket(hii);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__BB_RequestHennaItemInfo;
	}
}
