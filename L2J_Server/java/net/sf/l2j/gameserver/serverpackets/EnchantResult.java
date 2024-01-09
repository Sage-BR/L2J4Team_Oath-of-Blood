package net.sf.l2j.gameserver.serverpackets;

public class EnchantResult extends ServerBasePacket
{
	private static final String _S__81_ENCHANTRESULT = "[S] 81 EnchantResult";

	private int _unknown;

	public EnchantResult(int unknown)
	{
		_unknown = unknown;
	}

	@Override
	final void runImpl()
	{
		// no long-running tasks
	}

	@Override
	final void writeImpl()
	{
		writeC(0x81);
		writeD(_unknown);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__81_ENCHANTRESULT;
	}
}
