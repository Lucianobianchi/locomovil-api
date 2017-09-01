package tp.locomovil.inter.service;

import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

import java.util.List;

public interface LocationService {

	Location getApproximateLocation(Scan locationScan, List<Scan> calibrationScans);
}
