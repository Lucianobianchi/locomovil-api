package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.ScanDAO;
import tp.locomovil.inter.ScanService;
import tp.locomovil.inter.WifiDAO;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;

@Service
public class ScanServiceImpl implements ScanService {

	@Autowired
	private ScanDAO scanDAO;

	@Autowired
	private WifiDAO wifiDAO;

//	public List<Scan> getScansForLocation(Location location) {
//	}

	public Scan saveScan(Scan scan) {
		int wifiId = scanDAO.saveScan(scan);

		for (WifiData w: scan.getWifiScans()) {
			wifiDAO.saveWifiData(wifiId, w);
		}

		return scan;
	}
}
