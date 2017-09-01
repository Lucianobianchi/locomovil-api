package tp.locomovil.inter.dao;

import tp.locomovil.model.Location;
import tp.locomovil.model.WifiData;

import java.util.List;


public interface WifiDAO {
	void saveWifiData(int wifiScanId, WifiData wifi);

	List<WifiData> getWifiScanById(long wifiId);

	List<WifiData> getWifiScanInCoordinates(Location location, double precision);
}
