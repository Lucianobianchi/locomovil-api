package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.MapDAO;
import tp.locomovil.inter.ScanDAO;
import tp.locomovil.inter.ScanService;
import tp.locomovil.inter.WifiDAO;
import tp.locomovil.model.Location;
import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

@Service
public class ScanServiceImpl implements ScanService {

	@Autowired
	private ScanDAO scanDAO;

	@Autowired
	private WifiDAO wifiDAO;

	@Autowired
	private MapDAO mapDAO;

//	public List<Scan> getScansForLocation(Location location) {
//	}

	public Scan saveScan(Scan scan) {
		int wifiId = scanDAO.saveScan(scan);

		for (WifiData w: scan.getWifiScans()) {
			wifiDAO.saveWifiData(wifiId, w);
		}

		return scan;
	}

	public SMap saveMap (String mapName) {
		return mapDAO.createMap(mapName);
	}
}
