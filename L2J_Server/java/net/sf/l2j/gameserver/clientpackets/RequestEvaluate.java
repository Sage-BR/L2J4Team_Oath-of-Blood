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
import net.sf.l2j.gameserver.serverpackets.SystemMessage;
import net.sf.l2j.gameserver.serverpackets.UserInfo;

public class RequestEvaluate extends ClientBasePacket
{
	private static final String _C__B9_REQUESTEVALUATE = "[C] B9 RequestEvaluate";

	// private static Logger _log =
	// Logger.getLogger(RequestEvaluate.class.getName());

	@SuppressWarnings("unused")
	private final int _targetid;

	public RequestEvaluate(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_targetid = readD();
	}

	@Override
	void runImpl()
	{
		SystemMessage sm;
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;

		if (!(activeChar.getTarget() instanceof L2PcInstance))
		{
			sm = new SystemMessage(SystemMessage.TARGET_IS_INCORRECT);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		if (activeChar.getLevel() < 10)
		{
			sm = new SystemMessage(
					SystemMessage.ONLY_LEVEL_SUP_10_CAN_RECOMMEND);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		if (activeChar.getTarget() == activeChar)
		{
			sm = new SystemMessage(SystemMessage.YOU_CANNOT_RECOMMEND_YOURSELF);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		if (activeChar.getRecomLeft() <= 0)
		{
			sm = new SystemMessage(
					SystemMessage.NO_MORE_RECOMMENDATIONS_TO_HAVE);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		L2PcInstance target = (L2PcInstance) activeChar.getTarget();

		if (target.getRecomHave() >= 255)
		{
			sm = new SystemMessage(
					SystemMessage.YOU_NO_LONGER_RECIVE_A_RECOMMENDATION);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		if (!activeChar.canRecom(target))
		{
			sm = new SystemMessage(SystemMessage.THAT_CHARACTER_IS_RECOMMENDED);
			activeChar.sendPacket(sm);
			sm = null;
			return;
		}

		activeChar.giveRecom(target);

		sm = new SystemMessage(SystemMessage.YOU_HAVE_RECOMMENDED);
		sm.addString(target.getName());
		sm.addNumber(activeChar.getRecomLeft());
		activeChar.sendPacket(sm);

		sm = new SystemMessage(SystemMessage.YOU_HAVE_BEEN_RECOMMENDED);
		sm.addString(activeChar.getName());
		target.sendPacket(sm);
		sm = null;

		activeChar.sendPacket(new UserInfo(activeChar));
		target.broadcastUserInfo();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__B9_REQUESTEVALUATE;
	}
}
