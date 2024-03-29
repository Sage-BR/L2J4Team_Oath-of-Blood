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
import java.util.logging.Logger;

import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.AskJoinPledge;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class ...
 *
 * @version $Revision: 1.3.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class RequestJoinPledge extends ClientBasePacket
{
	private static final String _C__24_REQUESTJOINPLEDGE = "[C] 24 RequestJoinPledge";

	static Logger _log = Logger.getLogger(RequestJoinPledge.class.getName());

	private final int _target;

	private final int _pledgetype;

	public RequestJoinPledge(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_target = readD();
		_pledgetype = readD();
	}

	@Override
	void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (!(L2World.getInstance()
				.findObject(_target) instanceof L2PcInstance))
		{
			activeChar.sendPacket(new SystemMessage(
					SystemMessage.YOU_HAVE_INVITED_THE_WRONG_TARGET));
			return;
		}

		L2PcInstance target = (L2PcInstance) L2World.getInstance()
				.findObject(_target);
		L2Clan clan = activeChar.getClan();
		if (!clan.CheckClanJoinCondition(activeChar, target, _pledgetype)
				|| !activeChar.getRequest().setRequest(target, this))
		{
			return;
		}

		SystemMessage sm = new SystemMessage(
				SystemMessage.S1_HAS_INVITED_YOU_TO_JOIN_THE_CLAN_S2);
		sm.addString(activeChar.getName());
		sm.addString(activeChar.getClan().getName());
		target.sendPacket(sm);
		sm = null;
		AskJoinPledge ap = new AskJoinPledge(activeChar.getObjectId(),
				activeChar.getClan().getName());
		target.sendPacket(ap);
	}

	public int getPledgeType()
	{
		return _pledgetype;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__24_REQUESTJOINPLEDGE;
	}
}
