package net.sf.l2j.gameserver.serverpackets;

public class ChooseInventoryItem extends ServerBasePacket
{
	private static final String _S__6F_CHOOSEINVENTORYITEM = "[S] 6f ChooseInventoryItem";

	private int ItemID;

	public ChooseInventoryItem(int Item)
	{
		ItemID = Item;
	}

	{
	}

	@Override
	final void runImpl()
	{
		// no long-running tasks
	}

	@Override
	final void writeImpl()
	{
		writeC(0x6f);
		writeD(ItemID);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see net.sf.l2j.gameserver.serverpackets.ServerBasePacket#getType()
	 */
	@Override
	public String getType()
	{
		return _S__6F_CHOOSEINVENTORYITEM;
	}
}
