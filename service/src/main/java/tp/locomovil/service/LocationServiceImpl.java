package tp.locomovil.service;

import org.springframework.stereotype.Service;
import tp.locomovil.inter.LocationService;
import tp.locomovil.model.Location;
import tp.locomovil.model.WifiData;

import java.util.List;

@Service
public class LocationServiceImpl implements LocationService {

	public Location getApproximateLocation(List<WifiData> wifiScans) {
		return new Location(100, 100);
	}
}
