Planet Defend(PLD) is a mod of starsector (starsector is a STG game By Alex, http://fractalsoftworks.com/).
       

For starsector version 0.6.1a

Current Features:
1. Mother Ship (the only feature activate in compatible mode) 
  1) as a mobile storage station (but consume supplies every day)
  2) player can press 'A' to open the Mother Ship interaction dialog and communicate with Mother Ship
  3) Once fleet 'Mother Ship' is gone, it never come back again, take care of it!
  
Known Bugs:	
1. when player and MotherShip are not in the same location(starSystem/hyperspace), vector-based command not work.
2. after exchange ships, you should pick and drop the new member. If you don't do that ,the fleet data won't be set correctly.

Technical problems:
1. how to exchange ship betweens fleets. I use the dummy neutral station currently. It leads to bug 2.

Ideas/Planning:
1. Army system, a group of fleets, lead by a flag fleet. 
2. Economy system, station produce/consume supplies. 
3. station takeover

Project Dependency:
1. starsector
2. LazyLib ( A light-weight convenient lib by 
3. apache commons (commons-math, commons-lang3, commons-collections)
4. jmockit (only for test)

Build Guide: (assume you have basic knowledge of java development)
(for windows)
1. make sure jdk1.6+ and maven 3+ installed and configured 
2. edit config/gamedir.txt to fit your environment
3. edit %gamedir%/starsector-core/starsector.bat, change the screenshots, mods, saves dir to parent dir. (for debug)
4. open dos prompt, go to the project directory, and run 
   1) configure.bat
   2) debug.bat


--------------------------------------------------------

Nebular Fantasy(NF) is a support framework of PLD. I'd try to make it easy to share between mods.

Current Features:
1. Event Bus: help game objects to communicate each other.
	1) Livecycle Events: notify modder when the static instance should be recreate. 
	2) Heartbeat Events: notify game objects to work periodically 
	3) Input Events: Help to trigger by Keyboard and Mouse
2. menu: help to build interaction dialog menu. 
3. debug command: (not complete): help to show infomation and control objects in game.
4. game object support
    1) random system generation support
    2) cargo operation support
    3) fleet action support
5. misc
	1) auto drive
	2) custom logging

Shortage:
1. none of finished mod to support it. lack of verification
2. In developing, API is not stable currently.
 