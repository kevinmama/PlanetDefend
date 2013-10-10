package starsector.mod.nf;

import starsector.mod.nf.cmd.KeyboardCommand;
import starsector.mod.nf.cmd.ToggleDebugCmd;
import starsector.mod.nf.debug.DebugManager;
import starsector.mod.nf.debug.CallDebugMenuDialogCmd;
import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.EventBus;
import starsector.mod.nf.log.AppenderType;
import starsector.mod.nf.log.Logger;
import starsector.mod.nf.log.MessageLogAppender;

import com.fs.starfarer.api.campaign.SectorAPI;


/**
 * Nebular Fantasy Framework. 
 * holds global objects and provide API to access underlying infrastructure.
 * @author fengyuan
 *
 */
public class NF{
	
	//=============================================
	// global settings
	//=============================================
	
	static NF INS;
	
	private static Logger log;
	
	//=============================================
	// global objects
	//=============================================
	
	/**
	 * the event bus, most important object in nebular fantasy framework
	 */
	private EventBus eventbus;
	
	/**
	 * the debug manager, help to execute various debug action.
	 */
	private DebugManager debugManager;
	
	/**
	 * keyboard event command executor 
	 */
	private KeyboardCommandExecutor keyboardCommandExecutor;
	
	/**
	 * get the NebularFantasy event bus.
	 * @return
	 */
	public static EventBus getEventbus() {
		return INS.eventbus;
	}
	
	public static DebugManager getDebugManager() {
		return INS.debugManager;
	}

	/**
	 * init global settings. This can be called when application start.
	 */
	public static void init(){
		log = Logger.getLogger(NF.class, AppenderType.LOG4J);
		log.info("Nebular Fantasy Framework initialized, debugging=" + NFSettings.debugging);
	}
	
	private NF(){}
	
	/**
	 * create objects before sector generation.
	 * Some object may be used in sector generation (eg. eventbus) 
	 * After sector generated, client code should raise SECTOR_INIT event to
	 * make the framework initialization completed.
	 */
	public static NF create(){
		INS = new NF();
		INS._create();
		return INS;
	}
	
	/**
	 * create objects
	 */
	private void _create(){
		
		//
		// create global objects
		//
		debugManager = new DebugManager();
		eventbus = new EventBus();
		
		//
		// register core event source
		//
		
		BaseEventListener[] eventListeners = {
				new Daemon(this), // make sure this object at the begining of the listener chain
				new InputEventSource(),
				keyboardCommandExecutor = new KeyboardCommandExecutor(),
				new FirstHeartbeatHook(),
		};
		
		for (BaseEventListener listener : eventListeners) {
			listener.register(eventbus);
		}
		
		//
		// register keyboard command
		//
		KeyboardCommand[] cmds = {
				new ToggleDebugCmd(),
				new CallDebugMenuDialogCmd(),
//				new CheckFrendlyFleetCmd(),
//				new LevelUpCmd(),
		};
		for (KeyboardCommand cmd : cmds) {
			keyboardCommandExecutor.registerCommand(cmd);
		}
	}
	
	/**
	 * method will be called after sector generated.
	 * @param sector
	 */
	void afterGenerateSector(SectorAPI sector){
		Heartbeat heartbeat = new Heartbeat(eventbus);
		sector.addScript(heartbeat);
		log.info("heartbeat started");
	}
	
	/**
	 * when game loaded, some static object breaks. Fix it
	 */
	void reload(){
		NFSettings.reload();
		INS = this;
	}
	
	/**
	 * register command trigger by keyboard
	 * @param cmd
	 */
	public void registerKeyboardCommand(KeyboardCommand cmd){
		keyboardCommandExecutor.registerCommand(cmd);
	}
	
}
