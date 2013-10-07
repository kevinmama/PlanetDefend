package starsector.mod.pld.rules;

import java.util.Set;

import starsector.mod.nf.event.BaseEventListener;
import starsector.mod.nf.event.CoreEventType;
import starsector.mod.nf.event.Event;
import starsector.mod.nf.support.CargoSupport;
import starsector.mod.pld.PLDSettings;
import starsector.mod.pld.event.SalaryEvent;
import starsector.mod.pld.misc.PLDUtils;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.characters.PersonAPI;


/**
 * process you and your soidler's wage weekly.
 * @author fengyuan
 *
 */
public class RuleSalary extends BaseEventListener{
	
	public RuleSalary() {
		setName(RuleSalary.class.getSimpleName());
		listen(CoreEventType.HEARTBEAT_WEEK);
	}

	@Override
	public void handle(Event event) {
		
		CampaignFleetAPI fleet = Global.getSector().getPlayerFleet();
		PersonAPI player = fleet.getCommander();
		int level = player.getStats().getLevel();
		
		//
		// compute how much you get
		//
		int salary = PLDSettings.SALARY_OFFICER_BASE;
		Set<Integer> set = PLDSettings.SALARY_OFFICER_MULTIPLIER.keySet();
		for (Integer lvbase : set) {
			if (level > lvbase){
				salary += (level - lvbase) * PLDSettings.SALARY_OFFICER_MULTIPLIER.get(lvbase);
			}
		}
		CargoAPI cargo = fleet.getCargo();
		
		//
		// compute how much you paid
		//
		int pay = 0;
		for (int i = 0; i < PLDSettings.SALARY_PEOPLE_VECTOR.length; ++i) {
			pay += PLDUtils.getPeopleByIndex(cargo, i) * PLDSettings.SALARY_PEOPLE_VECTOR[i];
		}
		
		float realGain = CargoSupport.addCredits(cargo, salary-pay, 0, Float.MAX_VALUE);
		
		boolean indebt = salary-pay < realGain;
		eventbus.raise(new SalaryEvent(salary, pay, indebt));
	}
	
}
