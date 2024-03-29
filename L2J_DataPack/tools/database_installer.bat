@echo off
REM ##############################################
REM ## Interactive script setup -  (by TanelTM) ##
REM ##############################################
echo Checking environment...
mysql --help >nul 2>nul
if errorlevel 1 goto nomysql
echo   - MySQL: OK

REM LOGINSERVER
set lsuser=root
set lspass=
set lsdb=l2jdb
set lshost=localhost

REM GAMESERVER
set gsuser=root
set gspass=
set gsdb=l2jdb
set gshost=localhost

set workdir="%cd%"
:loadVars
if not exist vars.txt goto createVars
ren vars.txt *.bat
call vars.bat
ren vars.bat *.txt
cls
goto start
:createVars
echo This is the first time you run database_installer so we need to set it up...
echo.
echo.
echo LoginServer settings
echo --------------------
set /P lsuser="User (default is 'root'): "
set /P lspass="Pass (will be shown and saved as clear text): "
set /P lsdb="Database (default is 'l2jdb'): "
set /P lshost="Host (default is 'localhost'): "
echo.
echo GameServer settings
echo -------------------
set /P gsuser="User (default is 'root'): "
set /P gspass="Pass (will be shown and saved as clear text): "
set /P gsdb="Database (default is 'l2jdb'): "
set /P gshost="Host (default is 'localhost'): "
echo.
echo @set lsuser=%lsuser%>> vars.txt
echo @set lspass=%lspass%>> vars.txt
echo @set lsdb=%lsdb%>> vars.txt
echo @set lshost=%lshost%>> vars.txt
echo @set gsuser=%gsuser%>> vars.txt
echo @set gspass=%gspass%>> vars.txt
echo @set gsdb=%gsdb%>> vars.txt
echo @set gshost=%gshost%>> vars.txt
echo.
echo Script setup complete, press any key to continue...
pause> nul
goto loadVars
:start
REM ############################################
echo.
echo.
echo WARNING: Full install (f) will destroy data in your `accounts` and `gameserver`
echo tables.
echo.
echo Choose upgrade (u) if you already have an `accounts` table but no `gameserver`
echo table (ie. your server is a pre LS/GS split version.)
echo.
echo Choose skip (s) to skip loginserver DB installation and go to gameserver DB 
echo installation/upgrade.
echo.
:asklogin
set loginprompt=x
set /p loginprompt=LOGINSERVER DB install type: (f) full or (u) upgrade or {s} skip or (q) quit? 
if /i %loginprompt%==f goto logininstall
if /i %loginprompt%==u goto loginupgrade
if /i %loginprompt%==s goto gsbackup
if /i %loginprompt%==q goto end
goto asklogin

:logininstall
echo Deleting loginserver tables for new content.
mysql -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% < login_install.sql

:loginupgrade
echo Installing new loginserver content.
mysql -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% < ../sql/accounts.sql
mysql -h %lshost% -u %lsuser% --password=%lspass% -D %lsdb% < ../sql/gameservers.sql

:gsbackup
echo.
echo Making a backup of the original gameserver database.
mysqldump -h %gshost% -u %gsuser% --password=%gspass% %gsdb% > gameserver_backup.sql

echo.
echo.
echo WARNING: A full install (f) will destroy all existing character data.
:asktype
set installtype=x
set /p installtype=GAMESERVER DB install type: (f) full install or (u) upgrade or (s) skip or (q) quit? 
if /i %installtype%==f goto fullinstall
if /i %installtype%==u goto upgradeinstall
if /i %installtype%==s goto experimental
if /i %installtype%==q goto end
goto asktype

:fullinstall
echo Deleting all gameserver tables for new content.
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < full_install.sql

:upgradeinstall
echo Installing new gameserver content.
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/account_data.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/armor.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/auction.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/auction_bid.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/auction_watch.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/auto_chat.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/auto_chat_text.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/boxaccess.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/boxes.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/castle.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/castle_door.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/castle_doorupgrade.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/castle_siege_guards.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/char_templates.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_friends.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_hennas.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_macroses.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_quests.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_recipebook.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_recommends.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_shortcuts.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_skills.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_skills_save.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/character_subclasses.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/characters.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clan_data.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clan_privs.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clan_subpledges.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clan_skills.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clan_wars.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clanhall.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/clanhall_functions.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/class_list.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/cursedWeapons.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/droplist.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/enchant_skill_trees.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/etcitem.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/fish.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/fishing_skill_trees.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/forums.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/games.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/global_tasks.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/helper_buff_list.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/henna.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/henna_trees.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/heroes.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/items.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/itemsonground.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/locations.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/lvlupgain.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/mapregion.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/merchant_areas_list.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/merchant_buylists.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/merchant_lease.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/merchant_shopids.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/merchants.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/minions.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/npc.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/pets.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/pets_stats.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/pledge_skill_trees.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/posts.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/npcskills.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/olympiad_nobles.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/raidboss_spawnlist.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/random_spawn.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/random_spawn_loc.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/seven_signs.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/seven_signs_festival.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/seven_signs_status.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/siege_clans.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/skill_learn.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/skill_spellbooks.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/skill_trees.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/spawnlist.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/teleport.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/topic.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/weapon.sql
mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/zone.sql

:experimental
REM echo.
REM echo.
REM echo WARNING: legacy spawnlist contains more mobs and lesser chests, but many z values are wrong and doesnt match retail in many areas.
:askexp
REM set expprompt=x
REM set /p expprompt=Install experimental gameserver DB tables: (y) yes or (n) no or (q) quit? 
REM if /i %expprompt%==y goto expinstall
REM if /i %expprompt%==n goto newbie_helper
REM if /i %expprompt%==q goto end
REM goto end
:expinstall
REM echo Making a backup of the default gameserver tables.
REM mysqldump -h %gshost% -u %gsuser% --password=%gspass% %gsdb% > experimental_backup.sql
REM echo Installing new content.
REM mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/experimental/npc.sql
REM mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/experimental/npcskills.sql
REM mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% < ../sql/experimental/spawnlist.sql

:newbie_helper
echo.
echo.
echo If you're not that skilled applying changes within 'updates' folder, i can try to do it for you (y). If you wish to do it on your own, choose (n).
echo.
:asknb
set nbprompt=y
set /p nbprompt=Should i parse updates files? (Y/n)
if /i %nbprompt%==y goto nbinstall
if /i %nbprompt%==n goto end
goto asknb
:nbinstall
cd ..\sql\updates\
echo @echo off> temp.bat
if exist errors.txt del errors.txt
for %%i in (*.sql) do echo mysql -h %gshost% -u %gsuser% --password=%gspass% -D %gsdb% ^< %%i 2^>^> errors.txt >> temp.bat
call temp.bat> nul
del temp.bat
type errors.txt|find "doesn't exist"
del errors.txt
cd %workdir%
:end
echo.
echo Script complete.
pause
