#Made by Emperorc
import sys
from net.sf.l2j.gameserver.model.quest import State
from net.sf.l2j.gameserver.model.quest import QuestState
from net.sf.l2j.gameserver.model.quest.jython import QuestJython as JQuest
from net.sf.l2j.gameserver.serverpackets import CreatureSay
from java.util import Iterator

qn = "615_MagicalPowerOfFirePart1"

#NPC
Naran = 31378
Udan = 31379
Asefa_Box = 31559
Eye = 31684

#MOBS
Varka_Mobs = [ 21350, 21351, 21353, 21354, 21355, 21357, 21358, 21360, 21361, \
21362, 21369, 21370, 21364, 21365, 21366, 21368, 21371, 21372, 21373, 21374, 21375 ]
Ketra_Orcs = [ 21324, 21325, 21327, 21328, 21329, 21331, 21332, 21334, 21335, \
21336, 21338, 21339, 21340, 21342, 21343, 21344, 21345, 21346, 21347, 21348, 21349 ]

#ITEMS
Key = 1661
Totem = 7242
Wisdom_Stone = 7081
Totem2 = 7243

def AutoChat(npc,text) :
    chars = npc.getKnownList().getKnownPlayers().values().toArray()
    if chars != None:
       for pc in chars :
          sm = CreatureSay(npc.getObjectId(), 0, npc.getName(), text)
          pc.sendPacket(sm)

class Quest (JQuest) :

 def __init__(self,id,name,descr): JQuest.__init__(self,id,name,descr)

 def onEvent (self, event, st) :
   cond = st.getInt("cond")
   id = st.getInt("id")
   aggro = st.getInt("aggro")
   Thief_Key = st.getQuestItemsCount(Key)
   htmltext = event
   if event == "31378-04.htm" :
       if st.getPlayer().getLevel() >= 74 and st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
            st.set("cond","1")
            st.set("id","2")
            st.set("aggro","0")
            st.set("spawnId","0")
            st.set("spawned","0")
            st.set("npcid","0")
            st.setState(STARTED)
            st.playSound("ItemSound.quest_accept")
            htmltext = "31378-04.htm"
       else :
            htmltext = "31378-02.htm"
            st.exitQuest(1)
   elif event == "31559-03.htm" :
       if Thief_Key:
           st.takeItems(Key,1)
           if aggro == 1 :
               htmltext = "31559-04.htm"
           else :
               htmletext = "31559-03.htm"
               st.giveItems(Totem,1)
               st.set("id","5")
               st.set("cond","3")
               st.playSound("ItemSound.quest_middle")
       else :
           htmltext = "31559-02.htm"
   elif event == "Eye of Udan has despawned" :
        npc1 = st.getPcSpawn().getSpawn(st.getInt("spawnId")).getLastSpawn()
        AutoChat(npc1,"I'll be waiting for your return")
        npc1.reduceCurrentHp(9999999,npc1)
        st.getPcSpawn().removeAllSpawn()
        st.set("spawnId","0")
        st.set("spawned","0")
   return htmltext

 def onTalk (self, npc, player):
    st = player.getQuestState(qn)
    htmltext = "<html><head><body>I have nothing to say you</body></html>"
    if st :
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        id = st.getInt("id")
        aggro = st.getInt("aggro")
        Red_Totem = st.getQuestItemsCount(Totem)
        Stone = st.getQuestItemsCount(Wisdom_Stone)
        if st.getState() == CREATED :
            if npcId == Naran :
                htmltext = "31378-01.htm"
        elif st.getState() == STARTED :
            if npcId == Naran :
                if id == 2 :
                    htmltext = "31378-05.htm"
            elif npcId == Udan :
                if st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
                    if id == 2 :
                        htmltext = "31379-01.htm"
                        st.set("cond","2")
                        st.set("id","3")
                    elif id == 3 :
                        htmltext = "31379-02.htm"
                    elif id == 4 or aggro == 1 :
                        htmltext = "31379-03.htm"
                        st.set("id","3")
                        st.set("aggro","0")
                        st.set("spawnId","0")
                        st.set("spawned","0")
                        st.set("npcid","0")
                    elif id == 5 and Red_Totem :
                        htmltext = "31379-04.htm"
                        st.giveItems(Wisdom_Stone,1)
                        st.takeItems(Totem,1)
                        st.giveItems(Totem2,1)
                        st.unset("id")
                        st.unset("aggro")
                        st.playSound("ItemSound.quest_middle")
                        st.exitQuest(1)
            elif npcId == Asefa_Box :
                    if st.getPlayer().getAllianceWithVarkaKetra() <= -2 :
                        if id == 3 :
                            htmltext = "31559-01.htm"
    return htmltext

 def onAttack (self, npc, player):
   st = player.getQuestState(qn)
   if st :
    if st.getState() == STARTED :
        npcId = npc.getNpcId()
        id = st.getInt("id")
        Red_Totem = st.getQuestItemsCount(Totem)
        if st.getInt("spawned") == 0 and npc.getObjectId() != st.getInt("npcid"):
            if npcId in Ketra_Orcs :
                if id > 2 :
                    xx = int(st.getPlayer().getX())
                    yy = int(st.getPlayer().getY())
                    zz = int(st.getPlayer().getZ())
                    st.set("aggro","1")
                    st.set("cond","1")
                    st.set("id","4")
                    spawnId = st.getPcSpawn().addSpawn(Eye,xx,yy,zz)
                    st.set("spawnId",str(spawnId))
                    st.set("spawned","1")
                    st.set("npcid",str(npc.getObjectId()))
                    AutoChat(st.getPcSpawn().getSpawn(spawnId).getLastSpawn(),"You cannot escape Udan's eyes!")#this is only a temp message until we find out what it actually is! string = 61503
                    st.startQuestTimer("Eye of Udan has despawned",10000)
                    if Red_Totem :
                        st.takeItems(Totem,-1)
    return

 def onKill (self, npc, player):
   st = player.getQuestState(qn)
   if st :
    if st.getState() == STARTED :
        npcId = npc.getNpcId()
        cond = st.getInt("cond")
        id = st.getInt("id")
        Red_Totem = st.getQuestItemsCount(Totem)
        if npcId in Varka_Mobs :
            st.unset("id")
            st.unset("aggro")
            st.exitQuest(1)
            if Red_Totem:
                st.takeItems(Totem,-1)
    return


QUEST       = Quest(615,qn,"Magical Power of Fire - Part 1")
CREATED     = State('Start', QUEST)
STARTED     = State('Started', QUEST)

QUEST.setInitialState(CREATED)
QUEST.addStartNpc(Naran)

QUEST.addTalkId(Naran)
QUEST.addTalkId(Udan)
QUEST.addTalkId(Asefa_Box)

STARTED.addQuestDrop(Asefa_Box,Totem,1)
for mobId in Varka_Mobs:
    QUEST.addKillId(mobId)
for mobId in Ketra_Orcs:
    QUEST.addAttackId(mobId)

print "importing quests: 615: Magical Power of Fire - Part 1"