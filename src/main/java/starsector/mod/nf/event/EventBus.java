package starsector.mod.nf.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import starsector.mod.nf.NF;
import starsector.mod.nf.log.Logger;


/**
 * Nebular Framework event bus. You can acquire the global event bus by calling
 * {@link NF#getEventbus()} or create your own event bus.
 * By default, events defined in {@link CoreEventType} will be propagated in the
 * global event bus. You can listen to global event bus and dispatch event to your own.
 * @author fengyuan
 *
 */
public class EventBus{
	
	Logger log = Logger.getLogger(EventBus.class);
	
	/**
	 * keep the event listeners of type
	 */
	private Map<Enum<?>, ConcurrentLinkedQueue<EventListener>> eventListenerMap;
	
	private Queue<Event> eventQueue;
	
	public EventBus() {
		eventListenerMap = new HashMap<Enum<?>, ConcurrentLinkedQueue<EventListener>>();
		eventQueue = new LinkedList<Event>();
	}
	
	public void registerEventListener(Enum<?> type, EventListener listener){
		ConcurrentLinkedQueue<EventListener> list = eventListenerMap.get(type);
		if (list == null){
			list = new ConcurrentLinkedQueue<EventListener>();
			eventListenerMap.put(type, list);
		}
		list.add(listener);
		log.info("register event listener '" + listener.getName() + "' for type '" + type.toString() + "'");
	}
	
//
// unsafe, remove it
//	/**
//	 * remove the listener from the given EventType list.
//	 * @param type
//	 * @param listener
//	 */
//	public void removeEventListener(Enum<?> type, EventListener listener){
//		List<EventListener> list = eventListenerMap.get(type);
//		if (list != null){
//			list.remove(listener);
//		}
//	}
	
	/**
	 * raise a BaseEvent of given type.
	 * the event will be dispatch at next frame.
	 * @param type
	 */
	public void raise(Enum<?> type){
		raise(new BaseEvent(type));
	}
	
	/**
	 * raise an event and trigger the corresponding listener.
	 * the event will be dispatch at next frame.
	 * @param event
	 */
	public void raise(Event event){
		log("raise event", event);
		eventQueue.add(event);
	}
	
	/**
	 * notify listener to handle event immediately. 
	 * @param type
	 */
	public void dispatch(Enum<?> type){
		dispatch(new BaseEvent(type));
	}
	
	/**
	 * notify listener to handle event immediately. 
	 */
	public void dispatch(Event event){
		log("dispatch event", event);
		ConcurrentLinkedQueue<EventListener> queue = eventListenerMap.get(event.getEventType());
		if (queue != null){
			Iterator<EventListener> iter = queue.iterator();
			
			while(iter.hasNext()){
				EventListener listener = iter.next();
				//
				// check listener status
				//
				if (listener.getStatus() == EventListenerStatus.ACTIVE){
					listener.handle(event);
				}
				
				//
				// check status after handling event
				// using disposing and disposed status to avoid concurrency problem.
				//
				if (listener.getStatus() == EventListenerStatus.DISPOSING){
					log.info("remove event listener: " + listener.getName());
					iter.remove();
					listener.setStatus(EventListenerStatus.DISPOSED);
				}
				
				//
				// should stop propagating?
				//
				if (listener.isStopPropagation()){
					break;
				}
			}
		}
	}
	
	/**
	 * dispatch events in queue and notify listeners, called by heartbeat every frame.
	 * You can call this method to dispatch raised events immediately.
	 * when doing this, you may breaks the event propagation order.
	 */
	public void dispatchEvents(){
		while (!eventQueue.isEmpty()){
			Event event = eventQueue.poll();
			dispatch(event);
		}
	}
	
	/**
	 * make event log, ignore some frequently event.
	 * @param msg
	 */
	private void log(String prefix, Event event){
		
		Object eventType = event.getEventType();
		if (eventType instanceof CoreEventType){
			switch ((CoreEventType)eventType) {
			// don't report events
			case HEARTBEAT_FRAME:
			case HEARTBEAT_ADVANCE:
			case HEARTBEAT_HOUR:
			case KEY_PRESS:
			case MOUSE_CLICK:
				return;
			default:
			}
		}
		log.info(prefix + ": " + eventType);
	}
	
	
	
	
	
	/**
	 * some concurrent test
	 * @param args
	 */
	public static void main(String[] args) {
		ConcurrentLinkedQueue<String> l = new ConcurrentLinkedQueue<String>();
		for (int i = 0; i < 10; i++) {
			l.add("e"+i);
		}
		Iterator<String> iter = l.iterator();
		boolean sub = false;
		int i = 0;
		while(iter.hasNext()){
			String e = iter.next();
			System.out.print("i"+i + ":" + e);
			if (i%2==0){
				l.add(e);
				System.out.print(" add");
			}
			if (i%3==0){
				iter.remove();
				System.out.print(" remove");
			}
			++i;
			System.out.println();
			
			if (!sub && i%5==0){
				sub = true;
				Iterator<String> iter1 = l.iterator();
				int j=0;
				while (iter1.hasNext()){
					e = iter1.next();
					System.out.print("j"+j+":"+e);
					if (j%2==0){
						l.add(e);
						System.out.print(" add");
					}
					if (j%3==0){
						iter1.remove();
						System.out.print(" remove");
					}
					++j;
					System.out.println();
				}
			}
			
		}
	}

	
}
