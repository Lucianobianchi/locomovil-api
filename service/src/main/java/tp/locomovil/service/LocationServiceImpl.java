package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.*;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;

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

	public Location getApproximateLocation(Scan queryScan) {
		List<Scan> allScans = scanService.getScansForId(queryScan.getMapId());
		int maxIntersectionSize = 0;
		for (Scan s: allScans) {
			int size = intersectionSize(queryScan.getWifis(), s.getWifis(), new Comparator<WifiData>() {
				public int compare (WifiData o1, WifiData o2) {
					return o1.getBSSID().compareTo(o2.getBSSID());
				}
			});

			maxIntersectionSize = (size > maxIntersectionSize) ? size : maxIntersectionSize;
		}

		final int finalMaxIntersectionSize = maxIntersectionSize;
		allScans.removeIf(new Predicate<Scan>() {
			public boolean test (Scan scan) {
				return scan.getWifis().size() < finalMaxIntersectionSize;
			}
		});

		double distance = Double.MAX_VALUE;
		Scan nearestScan = allScans.get(0);
		for (Scan s: allScans) {
			double auxDistance = 0;
			for (WifiData w: s.getWifis()) {
				List<WifiData> queryWifis = queryScan.getWifis();
				int index = queryWifis.indexOf(w);
				if (index >= 0)
					auxDistance += Math.pow(queryWifis.get(index).getLevel() - w.getLevel(), 2);
			}
			if (auxDistance < distance) {
				distance = auxDistance;
				nearestScan = s;
			}
		}

		return new Location(nearestScan.getUserCoordX(), nearestScan.getUserCoordY());
	}

	private int intersectionSize(List<WifiData> l1, List<WifiData> l2, Comparator<WifiData> comparator) {
		int size = 0;
		for (WifiData w1: l1) {
			for (WifiData w2: l2) {
				if (comparator.compare(w1, w2) == 0)
					size++;
			}
		}
		return size;
	}
}
