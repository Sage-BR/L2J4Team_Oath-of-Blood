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

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.ai.CtrlEvent;
import net.sf.l2j.gameserver.model.L2CharPosition;
import net.sf.l2j.gameserver.model.L2Character;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.PartyMemberPosition;

/**
 * This class ...
 *
 * @version $Revision: 1.1.2.1.2.4 $ $Date: 2005/03/27 15:29:30 $
 */
public class CannotMoveAnymore extends ClientBasePacket
{
	private static final String _C__36_STOPMOVE = "[C] 36 CannotMoveAnymore";

	private static Logger _log = Logger
			.getLogger(CannotMoveAnymore.class.getName());

	private final int _x;

	private final int _y;

	private final int _z;

	private final int _heading;

	/**
	 * packet type id 0x36
	 *
	 * sample
	 *
	 * 36 a8 4f 02 00 // x 17 85 01 00 // y a7 00 00 00 // z 98 90 00 00 //
	 * heading?
	 *
	 * format: cdddd
	 *
	 * @param decrypt
	 */
	public CannotMoveAnymore(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_x = readD();
		_y = readD();
		_z = readD();
		_heading = readD();
	}

	@Override
	void runImpl()
	{
		L2Character player = getClient().getActiveChar();
		if (player == null)
			return;

		if (Config.DEBUG)
			_log.fine("client: x:" + _x + " y:" + _y + " z:" + _z + " server x:"
					+ player.getX() + " y:" + player.getY() + " z:"
					+ player.getZ());
		if (player.getAI() != null)
		{
			player.getAI().notifyEvent(CtrlEvent.EVT_ARRIVED_BLOCKED,
					new L2CharPosition(_x, _y, _z, _heading));
		}
		if (player instanceof L2PcInstance
				&& ((L2PcInstance) player).getParty() != null)
			((L2PcInstance) player).getParty().broadcastToPartyMembers(
					((L2PcInstance) player),
					new PartyMemberPosition((L2PcInstance) player));

		// player.stopMove();
		//
		// if (Config.DEBUG)
		// _log.fine("client: x:"+_x+" y:"+_y+" z:"+_z+
		// " server x:"+player.getX()+" y:"+player.getZ()+" z:"+player.getZ());
		// StopMove smwl = new StopMove(player);
		// getClient().getActiveChar().sendPacket(smwl);
		// getClient().getActiveChar().broadcastPacket(smwl);
		//
		// StopRotation sr = new StopRotation(getClient().getActiveChar(),
		// _heading);
		// getClient().getActiveChar().sendPacket(sr);
		// getClient().getActiveChar().broadcastPacket(sr);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__36_STOPMOVE;
	}
}
