package tp.locomovil.inter;

import tp.locomovil.model.Location;
import tp.locomovil.model.WifiData;

import java.util.List;


public interface WifiDAO {
	List<WifiData> getWifiScanInCoordinates(Location location, double precision);

	void saveWifiData(int wifiScanId, WifiData wifi);
}
