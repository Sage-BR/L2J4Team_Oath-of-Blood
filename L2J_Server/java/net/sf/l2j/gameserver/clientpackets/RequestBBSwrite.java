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
import net.sf.l2j.gameserver.communitybbs.CommunityBoard;

/**
 * Format SSSSSS
 *
 * @author -Wooden-
 *
 */
public class RequestBBSwrite extends ClientBasePacket
{
	private static final String _C__22_REQUESTBBSWRITE = "[C] 22 RequestBBSwrite";

	private String _url;

	private String _arg1;

	private String _arg2;

	private String _arg3;

	private String _arg4;

	private String _arg5;

	/**
	 * Format SSSSSS
	 *
	 * @param buf
	 * @param client
	 */
	public RequestBBSwrite(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_url = readS();
		_arg1 = readS();
		_arg2 = readS();
		_arg3 = readS();
		_arg4 = readS();
		_arg5 = readS();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
	 */
	@Override
	void runImpl()
	{
		CommunityBoard.getInstance().handleWriteCommands(getClient(), _url,
				_arg1, _arg2, _arg3, _arg4, _arg5);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__22_REQUESTBBSWRITE;
	}
}