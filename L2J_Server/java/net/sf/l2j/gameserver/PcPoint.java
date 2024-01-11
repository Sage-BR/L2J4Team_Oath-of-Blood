package net.sf.l2j.gameserver;

import java.util.logging.Logger;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.lib.Rnd;
import net.sf.l2j.gameserver.model.L2World;
import net.sf.l2j.gameserver.model.actor.instance.L2PcInstance;
import net.sf.l2j.gameserver.serverpackets.SystemMessage;


/**
 * @author Anumis
 */

public class PcPoint implements Runnable
{
 Logger _log = Logger.getLogger(PcPoint.class.getName());
 private static PcPoint _instance;

 public static PcPoint getInstance()
 {
 if(_instance == null)
 {
 _instance = new PcPoint();
 }

 return _instance;
 }

 private PcPoint()
 {
 _log.info("PcBang point event started.");
 }

 @Override
 public void run()
 {

 int score = 0;
 for(L2PcInstance activeChar: L2World.getInstance().getAllPlayers())
 {

 if(activeChar.getLevel() > Config.PCB_MIN_LEVEL )
 {
 score = Rnd.get(Config.PCB_POINT_MIN, Config.PCB_POINT_MAX);

 if(Rnd.get(100) <= Config.PCB_CHANCE_DUAL_POINT)
 {
 score *= 2;

 activeChar.addPcBangScore(score);

 SystemMessage sm = new SystemMessage(SystemMessage.DOUBLE_POINTS_YOU_GOT_$51_GLASSES_PC);
 sm.addNumber(score);
 activeChar.sendPacket(sm);
 sm = null;

 activeChar.updatePcBangWnd(score, true, true);
 }
 else
 {
 activeChar.addPcBangScore(score);

 SystemMessage sm = new SystemMessage(SystemMessage.YOU_RECEVIED_$51_GLASSES_PC);
 sm.addNumber(score);
 activeChar.sendPacket(sm);
 sm = null;

 activeChar.updatePcBangWnd(score, true, false);
 }
 }

 activeChar = null;
 }
 }
}