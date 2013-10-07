package starsector.mod.pld.rules;

import java.util.Collection;

import starsector.mod.nf.event.FirstHeartbeatEventListener;
import starsector.mod.pld.PLD;
import starsector.mod.pld.domain.PLDRegistry;
import starsector.mod.pld.domain.PLDStation;
import starsector.mod.pld.fleets.AsteroidMiningFleetSpawnPoint;


/**
 * initialize station's fleet spawnpoint
 * @author fengyuan
 *
 */
public class RuleStationSpawnpoint extends FirstHeartbeatEventListener{
	
	public RuleStationSpawnpoint() {
		setName(RuleStationSpawnpoint.class.getSimpleName());
	}

	@Override
	protected void doInit() {
		PLDRegistry reg = PLD.getRegistry();
		Collection<PLDStation> stations = reg.getAllStations();
		for (PLDStation station : stations) {
			//
			// registry mining fleet spawn point
			//
			new AsteroidMiningFleetSpawnPoint(station).register(eventbus);
		}
	}

}
