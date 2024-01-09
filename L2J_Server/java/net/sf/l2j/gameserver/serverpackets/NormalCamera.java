package net.sf.l2j.gameserver.serverpackets;

public class NormalCamera extends ServerBasePacket
{
	private static final String _S__C8_NORMALCAMERA = "[S] C8 NormalCamera";

	public NormalCamera()
	{
	}

	@Override
	public void runImpl()
	{
	}

	@Override
	public void writeImpl()
	{
		writeC(0xc8);
	}

	@Override
	public String getType()
	{
		return _S__C8_NORMALCAMERA;
	}
}
