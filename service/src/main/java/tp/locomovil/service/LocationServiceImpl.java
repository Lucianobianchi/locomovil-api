package tp.locomovil.service;

import org.deeplearning4j.nn.api.NeuralNetwork;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.util.ModelSerializer;
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
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

	private static final int MAX_COINCIDENCES = 5;
	private static final int STRONGEST_AP_NUMBER = 5;

	// Lower level is stronger so this sorts signals from strongest to weakest
	private static final Comparator<WifiData> LEVEL_SORT = (o1, o2) -> o1.getLevel() - o2.getLevel();

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

	@Override
	public Location getLocationMultiLayer (Scan queryScan) {
		List<WifiData> wifis = queryScan.getWifis();
		wifis = wifis.stream().sorted(LEVEL_SORT).limit(STRONGEST_AP_NUMBER).collect(Collectors.toList());
		return getLocationMultiLayer(wifis);
	}

	// Mock -> sin neural nets
	private Location getLocationMultiLayer(List<WifiData> strongestAPs) {
		Scan mockScan = new Scan.ScanDataBuilder().wifis(strongestAPs).build();
		return getLocationByMapKNNAverage(mockScan, 4);
	}

	private Location getLocationMultiLayerNeural(List<WifiData> strongestAPs) {
		WifiNeuralNet net = neuralNetDAO.getNetworkForAPs(strongestAPs);
		// net predict with APs
		double x = 10.234;
		double y = 83.394;
		return new Location(net.getProjectName(), net.getMapName(), "Macaddrr", x, y, x/y);
	}

	private Location getLocationKNNAverage (Scan queryScan,
			List<Scan> calibrationScans, int K) {
		if (calibrationScans.isEmpty())
			return null;

		int maxCoincidences = 0;
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
				if (index >= 0) {
					coincidences++;
					distance += getDistance(queryWifis.get(index), calibrationWifi);
				}
				if (coincidences == MAX_COINCIDENCES)
					break;
			}

			if (coincidences == maxCoincidences) {
				scanDistances.add(new DistanceScanPair(distance, calibrationScan));
			} else if (coincidences > maxCoincidences) {
				maxCoincidences = coincidences;
				scanDistances.clear();
				scanDistances.add(new DistanceScanPair(distance, calibrationScan));
			}
		}

		Scan first = scanDistances.peek().scan;
		String projectName = projectDAO.getProjectById(first.getProjectId()).getName();
		String mapName = mapDAO.getMapById(first.getProjectId(), first.getMapId()).getMapName();

		// Calculate average position between K closest points
		double x = 0, y = 0;
		int sumCount = 0;
		List<Scan> nearestScans = new LinkedList<>();
		for (int i = 0; i < K && !scanDistances.isEmpty(); i++) {
			Scan s = scanDistances.poll().scan;
			nearestScans.add(s);
			x += s.getUserCoordX();
			y += s.getUserCoordY();
			sumCount++;
		}
		x /= sumCount;
		y /= sumCount;

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
		double distance;
		Scan scan;

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
