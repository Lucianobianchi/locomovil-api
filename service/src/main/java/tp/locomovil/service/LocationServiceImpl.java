package tp.locomovil.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.dao.*;
import tp.locomovil.inter.service.LocationService;
import tp.locomovil.inter.service.ScanService;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;
import tp.locomovil.model.WifiNeuralNet;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationServiceImpl implements LocationService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LocationService.class);

	@Autowired
	private ScanDAO scanDAO;

	@Autowired
	private WifiDAO wifiDAO;

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private MapDAO mapDAO;

	@Autowired
	private NeuralNetDAO neuralNetDAO;

	@Autowired
	private ScanService scanService;

	// TODO: Al archivo de configuracion
	private static final int MAX_COINCIDENCES = 5;
	private static final int STRONGEST_AP_NUMBER = 5;

	// De mayor a menor
	private static final Comparator<WifiData> LEVEL_SORT = (o1, o2) -> o2.getLevel() - o1.getLevel();

	@Override
	public Location getLocationByMapKNNAverage (Scan queryScan, int K) {
		List<Scan> calibrationScans = scanService.getScansForMapId(queryScan.getMapId());
		return getLocationKNNAverage(queryScan, calibrationScans, K);
	}

	@Override
	public Location getLocationByProjectKNNAverage (Scan queryScan, int K) {
		List<Scan> calibrationScans = scanService.getScansForProjectId(queryScan.getProjectId());
		return getLocationKNNAverage(queryScan, calibrationScans, K);
	}

	/**
	 * An algorithm to get the user location. Given a current scan of nearby APs, the function
	 * returns a location that is predicted using a multilayer neural network.
	 * The network is selected according to the 5 strongest APs detected.
	 * @param queryScan Current scan by the user, to get the APs intensities to use as input for the network.
	 * @return The approximate location. Returns null if {@param calibrationScans} is empty.
	 */
	@Override
	public Location getLocationMultiLayer (Scan queryScan) {
		List<WifiData> wifis = queryScan.getWifis();
		wifis = wifis.stream().sorted(LEVEL_SORT).limit(STRONGEST_AP_NUMBER).collect(Collectors.toList());
		return getLocationMultiLayer(wifis);
	}

	private Location getLocationMultiLayer (List<WifiData> strongestAPs) {
		// Si con los N mas fuertes no hay ninguna red, pruebo con N-1.
		for (int i = strongestAPs.size(); i > 0; i--) {
			List<WifiData> wifis = strongestAPs.subList(0, i);
			WifiNeuralNet net = neuralNetDAO.getNetworkForAPs(wifis);
			if (net != null)
				return net.getLocationForWifis(wifis);
		}
		return null;
	}

	/**
	 * An algorithm to get the user location. Given a list of scans (that happened during calibration)
	 * and a current scan to compare against the calibration, the function returns a location that
	 * is the average of K-Nearest-Neighbour scans. Due to the nature of this technique, this
	 * algorithm only works when using calibration scans of a certain map, it would not make sense
	 * to calculate averages between scans in different maps.
	 * @param K The amount of different calibrations to be averaged to get the approximate coordinates.
	 * @param queryScan Current scan by the user, to compare against calibrations.
	 * @param calibrationScans List of scans gathered during calibration.
	 * @return The approximate location. Returns null if {@param calibrationScans} is empty.
	 */
	private Location getLocationKNNAverage (Scan queryScan,
			List<Scan> calibrationScans, int K) {
		if (calibrationScans.isEmpty())
			return null;

		int currentMaxCoincidences = 0;

		// PriorityQueue, se insertan ordenados.
		PriorityQueue<DistanceScanPair> scanDistances = new PriorityQueue<>();
		List<WifiData> queryWifis = queryScan.getWifis();
		queryWifis.sort(LEVEL_SORT);

		for (Scan calibrationScan: calibrationScans) {
			double distance = 0;
			int coincidences = 0;
			// Get the distance from a certain calibration
			List<WifiData> calibrationSorted = calibrationScan.getWifis();
			calibrationSorted.sort(LEVEL_SORT);

			for (WifiData calibrationWifi: calibrationSorted) {
				int index = queryWifis.indexOf(calibrationWifi);

				// Se fija si algun APs de la calibración también esta en los de la query.
				if (index >= 0) {
					coincidences++;
					distance += getDistance(queryWifis.get(index), calibrationWifi);
				}
				if (coincidences == MAX_COINCIDENCES)
					break;
			}

			if (coincidences == currentMaxCoincidences) {
				scanDistances.add(new DistanceScanPair(distance, calibrationScan));
			} else if (coincidences > currentMaxCoincidences) {
				currentMaxCoincidences = coincidences;
				scanDistances.clear();
				scanDistances.add(new DistanceScanPair(distance, calibrationScan));
			}
		}

		Scan first = scanDistances.peek().scan;
		String projectName = projectDAO.getProjectById(first.getProjectId()).getName();
		String mapName = mapDAO.getMapById(first.getProjectId(), first.getMapId()).getMapName();

		// Calculate average position between K closest points
		List<Scan> nearestScans = scanDistances.stream().map(sd -> sd.scan).limit(K).collect(Collectors.toList());
		double x = nearestScans.stream().collect(Collectors.averagingDouble(Scan::getUserCoordX));
		double y = nearestScans.stream().collect(Collectors.averagingDouble(Scan::getUserCoordY));

		// precision = distance between estimated position and the farthest point from the K nearest ones
		double precision = getFarthestDistanceFrom(x, y, nearestScans);
		return new Location(projectName, mapName, queryScan.getMACAddress(), x, y, precision);
	}

	private double getFarthestDistanceFrom(double x, double y, List<Scan> scans) {
		double maxDistance = 0;
		for (Scan s: scans) {
			double dist = Math.sqrt(Math.pow(x - s.getUserCoordX(), 2) + Math.pow(y - s.getUserCoordY(), 2));
			if (dist > maxDistance) {
				maxDistance = dist;
			}
		}
		return maxDistance;
	}

	private double getDistance(WifiData w1, WifiData w2) {
		return Math.pow(w1.getLevel() - w2.getLevel(), 2);
	}

	private static class DistanceScanPair implements Comparable<DistanceScanPair>{
		final double distance;
		final Scan scan;

		public DistanceScanPair (double distance, Scan scan) {
			this.distance = distance;
			this.scan = scan;
		}

		@Override
		public int compareTo (DistanceScanPair o) {
			return this.distance - o.distance < 0 ? -1 : 1;
		}
	}

}
