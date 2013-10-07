package starsector.mod.pld.domain;

import starsector.mod.pld.PLDSettings;

/**
 * Indicate station's supply level. See {@link PLDSettings#STATION_SUPPLY_LEVEL_VECTOR} for
 * details
 * @author fengyuan
 *
 */
public interface StationSupplyLevel {
	int RUN_OUT = 0;
	int SHORTAGE = 1;
	int LOW = 2;
	int NORMAL = 3;
	int HIGH = 4;
	int ABUNDANT =5;
}
