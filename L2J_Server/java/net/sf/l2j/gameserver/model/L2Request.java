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
package net.sf.l2j.gameserver.model;

import net.sf.l2j.gameserver.ThreadPoolManager;
import net.sf.l2j.gameserver.clientpackets.ClientBasePacket;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * This class manages requests (transactions) between two L2PcInstance.
 *
 * @author kriau
 */
public class L2Request
{
	private static final int REQUEST_TIMEOUT = 15; // in secs

	protected L2PcInstance _player;

	protected L2PcInstance _partner;

	protected boolean _isRequestor;

	protected boolean _isAnswerer;

	protected ClientBasePacket _requestPacket;

	public L2Request(L2PcInstance player)
	{
		_player = player;
	}

	protected void Clear()
	{
		_partner = null;
		_requestPacket = null;
		_isRequestor = false;
		_isAnswerer = false;
	}

	/**
	 * Set the L2PcInstance member of a transaction (ex : FriendInvite,
	 * JoinAlly, JoinParty...).<BR>
	 * <BR>
	 */
	private synchronized void setPartner(L2PcInstance partner)
	{
		_partner = partner;
	}

	/**
	 * Return the L2PcInstance member of a transaction (ex : FriendInvite,
	 * JoinAlly, JoinParty...).<BR>
	 * <BR>
	 */
	public L2PcInstance getPartner()
	{
		return _partner;
	}

	/**
	 * Set the packet incomed from requestor.<BR>
	 * <BR>
	 */
	private synchronized void setRequestPacket(ClientBasePacket packet)
	{
		_requestPacket = packet;
	}

	/**
	 * Return the packet originally incomed from requestor.<BR>
	 * <BR>
	 */
	public ClientBasePacket getRequestPacket()
	{
		return _requestPacket;
	}

	/**
	 * Checks if request can be made and in success case puts both PC on request
	 * state.<BR>
	 * <BR>
	 */
	public synchronized boolean setRequest(L2PcInstance partner,
			ClientBasePacket packet)
	{
		if (partner == null)
		{
			_player.sendPacket(new SystemMessage(
					SystemMessage.YOU_HAVE_INVITED_THE_WRONG_TARGET));
			return false;
		}
		if (partner.getRequest().isProcessingRequest())
		{
			SystemMessage sm = new SystemMessage(
					SystemMessage.S1_IS_BUSY_TRY_LATER);
			sm.addString(partner.getName());
			_player.sendPacket(sm);
			sm = null;
			return false;
		}
		if (isProcessingRequest())
		{
			_player.sendPacket(
					new SystemMessage(SystemMessage.WAITING_FOR_ANOTHER_REPLY));
			return false;
		}

		_partner = partner;
		_requestPacket = packet;
		setOnRequestTimer(true);
		_partner.getRequest().setPartner(_player);
		_partner.getRequest().setRequestPacket(packet);
		_partner.getRequest().setOnRequestTimer(false);
		return true;
	}

	private void setOnRequestTimer(boolean isRequestor)
	{
		_isRequestor = isRequestor ? true : false;
		_isAnswerer = isRequestor ? false : true;
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				Clear();
			}
		}, REQUEST_TIMEOUT * 1000);

	}

	/**
	 * Clears PC request state. Should be called after answer packet
	 * receive.<BR>
	 * <BR>
	 */
	public void onRequestResponse()
	{
		if (_partner != null)
		{
			_partner.getRequest().Clear();
		}
		Clear();
	}

	/**
	 * Return True if a transaction is in progress.<BR>
	 * <BR>
	 */
	public boolean isProcessingRequest()
	{
		return _partner != null;
	}
}
