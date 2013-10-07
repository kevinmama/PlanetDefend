package starsector.mod.pld.misc;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.LinkedList;

import org.lazywizard.lazylib.CollectionUtils;
import org.lazywizard.lazylib.campaign.MessageUtils;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.Event;
import starsector.mod.pld.domain.PLDFleet;
import starsector.mod.pld.domain.PLDStation;
import starsector.mod.pld.event.IFleetEvent;
import starsector.mod.pld.event.IStationEvent;
import starsector.mod.pld.event.PLDEventType;
import starsector.mod.pld.event.SalaryEvent;
import starsector.mod.pld.event.StationSpawnedFleetsEvent;


/**
 * notify useful message to user
 * @author fengyuan
 *
 */
public class MessageNotifier extends BaseEventListener{
	
	public MessageNotifier() {
		setName("MessageNotifier");
		listen(
		PLDEventType.STATION_BEGIN_SPAWN_FLEET,
		PLDEventType.STATION_SPAWNED_FLEET,
		PLDEventType.STATION_SUPPLY_SHORTAGE,
		PLDEventType.STATION_SUPPLY_RUN_OUT,
		PLDEventType.SALARY
		);
	}
	
	@Override
	public void handle(Event event) {
		String format = null;
		String message = null;							// 0
		PLDStation station = null;		// 1
		PLDFleet fleet = null;			// 2
		
		if (event instanceof IStationEvent){
			station = ((IStationEvent) event).getStation();
		}
		
		if (event instanceof IFleetEvent){
			fleet = ((IFleetEvent)event).getFleet();
		}
		
		Enum<?> type = event.getEventType();
		if (type instanceof PLDEventType){
			switch ((PLDEventType)type) {
			
			case STATION_SUPPLY_SHORTAGE:
				format = "{1} in short of supply"; break;
			case STATION_SUPPLY_RUN_OUT:
				format = "{1} run out of supply"; break;
			case STATION_BEGIN_SPAWN_FLEET:
				format = "{1} begin spawn fleet"; break;
			case STATION_SPAWNED_FLEET:{
				Collection<PLDFleet> fleets = ((StationSpawnedFleetsEvent)event).getFleets();
				LinkedList<String> fleetNames = new LinkedList<String>();
				for (PLDFleet pldFleet : fleets) {
					String fleetName = pldFleet.getQualifiedName();
					fleetNames.add(fleetName);
				}
				message = CollectionUtils.implode(fleetNames);
				format = "{1} spawned fleets {0}";
				break;
			} 
			case SALARY:{
				SalaryEvent sev = ((SalaryEvent)event);
				message = "you get " + sev.getSalary() + " credits and pay " + sev.getPay() + " credits.";
				if (sev.isIndebt()){
					message += " You don't have enouth credits to pay you man";
				}
				format = "{0}"; 
				break;
			}
			default:
				// ignore other message
				return;
			}
		}
		
		String stationName = station != null ? station.getStation().getFullName() : null; // 1
		String fleetName = fleet != null ? fleet.getFleet().getFullName() : null;		// 2
		
		String msg = MessageFormat.format(format, new Object[]{
				message,		// 0
				stationName,	// 1 
				fleetName		// 2
		});
		
		MessageUtils.showMessage(msg);
	}
}
