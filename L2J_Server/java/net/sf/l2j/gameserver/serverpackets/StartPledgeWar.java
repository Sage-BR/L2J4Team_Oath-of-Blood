package net.sf.l2j.gameserver.serverpackets;

public class StartPledgeWar extends ServerBasePacket
{
	private static final String _S__65_STARTPLEDGEWAR = "[S] 65 StartPledgeWar";

	private String _pledgeName;

	private String _char;

	public StartPledgeWar(String pledge, String charName)
	{
		_pledgeName = pledge;
		_char = charName;
	}

	@Override
	final void runImpl()
	{
	}

	@Override
	final void writeImpl()
	{
		writeC(0x65);
		writeS(_char);
		writeS(_pledgeName);
	}

	@Override
	public String getType()
	{
		return _S__65_STARTPLEDGEWAR;
	}
}