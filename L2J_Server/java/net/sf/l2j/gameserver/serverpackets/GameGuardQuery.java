/**
 *
 */
package net.sf.l2j.gameserver.serverpackets;

/**
 * @author zabbix Lets drink to code!
 */
public class GameGuardQuery extends ServerBasePacket
{
	private static final String _S__F9_GAMEGUARDQUERY = "[S] F9 GameGuardQuery";

	@Override
	public void runImpl()
	{
		// Lets make user as gg-unauthorized
		// We will set him as ggOK after reply fromclient
		// or kick
		getClient().setGameGuardOk(false);
	}

	@Override
	public void writeImpl()
	{
		writeC(0xf9);
	}

	@Override
	public String getType()
	{
		return _S__F9_GAMEGUARDQUERY;
	}
}
