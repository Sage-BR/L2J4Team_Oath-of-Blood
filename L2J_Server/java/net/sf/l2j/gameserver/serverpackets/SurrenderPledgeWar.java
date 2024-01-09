package net.sf.l2j.gameserver.serverpackets;

public class SurrenderPledgeWar extends ServerBasePacket
{
	private static final String _S__81_SURRENDERPLEDGEWAR = "[S] 69 SurrenderPledgeWar";

	private String _pledgeName;

	private String _char;

	public SurrenderPledgeWar(String pledge, String charName)
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
		writeC(0x69);
		writeS(_pledgeName);
		writeS(_char);
	}

	@Override
	public String getType()
	{
		return _S__81_SURRENDERPLEDGEWAR;
	}
}