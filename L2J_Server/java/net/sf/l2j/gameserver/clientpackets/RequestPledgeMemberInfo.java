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
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.PledgeReceiveMemberInfo;

/**
 * Format: (ch) dS
 *
 * @author -Wooden-
 *
 */
public class RequestPledgeMemberInfo extends ClientBasePacket
{
	private static final String _C__D0_1D_REQUESTPLEDGEMEMBERINFO = "[C] D0:1D RequestPledgeMemberInfo";

	@SuppressWarnings("unused")
	private int _unk1;

	private String _player;

	/**
	 * @param buf
	 * @param client
	 */
	public RequestPledgeMemberInfo(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_unk1 = readD();
		_player = readS();
	}

	/**
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
	 */
	@Override
	void runImpl()
	{
		// System.out.println("C5: RequestPledgeMemberInfo d:"+_unk1);
		// System.out.println("C5: RequestPledgeMemberInfo S:"+_player);
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
			return;
		// do we need powers to do that??
		L2Clan clan = activeChar.getClan();
		if (clan == null)
			return;
		L2ClanMember member = clan.getClanMember(_player);
		if (member == null)
			return;
		activeChar.sendPacket(new PledgeReceiveMemberInfo(member));
	}

	/**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_1D_REQUESTPLEDGEMEMBERINFO;
	}

}