package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.*;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {
	@Autowired
	private ScanDAO scanDAO;

	@Autowired
	private WifiDAO wifiDAO;

	@Autowired
	private ScanService scanService;

	// No sirve pa' nada
	public Location getApproximateLocation (List<WifiData> wifiScans) {
		return new Location(100, 100);
	}
	
	public Location getApproximateLocation(Scan queryScan, List<Scan> calibrationScans) {
		int maxCoincidences = 0;

		double minDistance = Double.MAX_VALUE;
		Scan nearestScan = calibrationScans.get(0);

		List<WifiData> queryWifis = queryScan.getWifis();

		for (Scan calibrationScan: calibrationScans) {
			int coincidences = 0;
			double auxDistance = 0;
			for (WifiData calibrationWifi: calibrationScan.getWifis()) {
				int index = queryWifis.indexOf(calibrationWifi);
				if (index >= 0) {
					coincidences++;
					auxDistance += Math.pow(queryWifis.get(index).getLevel() - calibrationWifi.getLevel(), 2);
				}
			}
			if (auxDistance < minDistance && coincidences >= maxCoincidences) {
				minDistance = auxDistance;
				maxCoincidences = coincidences;
				nearestScan = calibrationScan;
			}
		}

		return new Location(nearestScan.getUserCoordX(), nearestScan.getUserCoordY());
	}
}
