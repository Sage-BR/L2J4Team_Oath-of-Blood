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
import java.sql.PreparedStatement;
import java.sql.SQLException;

import net.sf.l2j.L2DatabaseFactory;
import net.sf.l2j.gameserver.ClientThread;
import net.sf.l2j.gameserver.model.L2Clan;
import net.sf.l2j.gameserver.model.L2ClanMember;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;

/**
 * Format: (ch) dSS
 *
 * @author -Wooden-
 *
 */
public class RequestPledgeSetAcademyMaster extends ClientBasePacket
{
	private static final String _C__D0_19_REQUESTSETPLEADGEACADEMYMASTER = "[C] D0:19 RequestPledgeSetAcademyMaster";

	private String _currPlayerName;

	private int _set; // 1 set, 0 delete

	private String _targetPlayerName;

	/**
	 * @param buf
	 * @param client
	 */
	public RequestPledgeSetAcademyMaster(ByteBuffer buf, ClientThread client)
	{
		super(buf, client);
		_set = readD();
		_currPlayerName = readS();
		_targetPlayerName = readS();
	}

	/**
	 * @see net.sf.l2j.gameserver.clientpackets.ClientBasePacket#runImpl()
	 */
	@Override
	void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		L2Clan clan = activeChar.getClan();
		if (clan == null)
			return;

		if ((activeChar.getClanPrivileges()
				& L2Clan.CP_CL_MASTER_RIGHTS) != L2Clan.CP_CL_MASTER_RIGHTS)
		{
			activeChar.sendPacket(new SystemMessage(
					SystemMessage.YOU_DO_NOT_HAVE_THE_RIGHT_TO_DISMISS_AN_APPRENTICE));
			return;
		}

		L2ClanMember currentMember = clan.getClanMember(_currPlayerName);
		L2ClanMember targetMember = clan.getClanMember(_targetPlayerName);
		if (currentMember == null || targetMember == null)
			return;

		L2ClanMember apprenticeMember, sponsorMember;
		if (currentMember.getPledgeType() == L2Clan.SUBUNIT_ACADEMY)
		{
			apprenticeMember = currentMember;
			sponsorMember = targetMember;
		} else
		{
			apprenticeMember = targetMember;
			sponsorMember = currentMember;
		}

		L2PcInstance apprentice = apprenticeMember.getPlayerInstance();
		L2PcInstance sponsor = sponsorMember.getPlayerInstance();

		SystemMessage sm = null;
		if (_set == 0)
		{
			// test: do we get the current sponsor & apprentice from this packet
			// or no?
			if (apprentice != null)
				apprentice.setSponsor(0);
			else // offline
				apprenticeMember.initApprenticeAndSponsor(0, 0);

			if (sponsor != null)
				sponsor.setApprentice(0);
			else // offline
				sponsorMember.initApprenticeAndSponsor(0, 0);

			saveApprenticeAndSponsor(apprenticeMember.getObjectId(), 0, 0);
			saveApprenticeAndSponsor(sponsorMember.getObjectId(), 0, 0);

			sm = new SystemMessage(
					SystemMessage.S2_CLAN_MEMBER_S1_S_APPRENTICE_HAS_BEEN_REMOVED);
		} else
		{
			if (apprenticeMember.getSponsor() != 0
					|| sponsorMember.getApprentice() != 0
					|| apprenticeMember.getApprentice() != 0
					|| sponsorMember.getSponsor() != 0)
			{
				activeChar.sendMessage("Remove previous connections first.");
				return;
			}
			if (apprentice != null)
				apprentice.setSponsor(sponsorMember.getObjectId());
			else // offline
				apprenticeMember.initApprenticeAndSponsor(0,
						sponsorMember.getObjectId());

			if (sponsor != null)
				sponsor.setApprentice(apprenticeMember.getObjectId());
			else // offline
				sponsorMember.initApprenticeAndSponsor(0,
						sponsorMember.getObjectId());

			// saving to database even if online, since both must match
			saveApprenticeAndSponsor(apprenticeMember.getObjectId(), 0,
					sponsorMember.getObjectId());
			saveApprenticeAndSponsor(sponsorMember.getObjectId(),
					apprenticeMember.getObjectId(), 0);

			sm = new SystemMessage(
					SystemMessage.S2_HAS_BEEN_DESIGNATED_AS_APPRENTICE_OF_CLAN_MEMBER_S1);
		}
		sm.addString(sponsorMember.getName());
		sm.addString(apprenticeMember.getName());
		if (sponsor != activeChar && sponsor != apprentice)
			activeChar.sendPacket(sm);
		if (sponsor != null)
			sponsor.sendPacket(sm);
		if (apprentice != null)
			apprentice.sendPacket(sm);
	}

	/**
	 * @see net.sf.l2j.gameserver.BasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _C__D0_19_REQUESTSETPLEADGEACADEMYMASTER;
	}

	public void saveApprenticeAndSponsor(int objectID, int apprentice,
			int sponsor)
	{
		java.sql.Connection con = null;

		try
		{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(
					"UPDATE characters SET apprentice=?,sponsor=? WHERE obj_Id=?");
			statement.setInt(1, apprentice);
			statement.setInt(2, sponsor);
			statement.setInt(3, objectID);
			statement.execute();
			statement.close();
		} catch (SQLException e)
		{
			// _log.warning("could not set apprentice/sponsor:"+e.getMessage());
		} finally
		{
			try
			{
				con.close();
			} catch (Exception e)
			{
			}
		}
	}

}