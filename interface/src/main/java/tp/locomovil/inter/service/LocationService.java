package tp.locomovil.inter.service;

import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import java.util.List;

public interface LocationService {

	/**
	 * An algorithm to get the user location. Given a list of scans (that happened during calibration)
	 * and a current scan to compare against the calibration, the function returns a location that
	 * is the average of K-Nearest-Neighbour scans. Due to the nature of this technique, this
	 * algorithm only works when using calibration scans of a certain map, it would not make sense
	 * to calculate averages between scans in different maps.
	 * @param queryScan Current scan by the user, to compare against calibrations.
	 * @param K the number of nodes to be averaged
	 * @return The approximate location. Returns null if {@param calibrationScans} is empty.
	 */
	Location getLocationByMapKNNAverage (Scan queryScan, int K);

	Location getLocationByProjectKNNAverage (Scan queryScan, int K);

	Location getLocationMultiLayer (Scan queryScan);

}
