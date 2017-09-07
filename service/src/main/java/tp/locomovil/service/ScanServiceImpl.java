package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.dao.MapDAO;
import tp.locomovil.inter.dao.ProjectDAO;
import tp.locomovil.inter.dao.ScanDAO;
import tp.locomovil.inter.dao.WifiDAO;
import tp.locomovil.inter.service.ScanService;
import tp.locomovil.model.*;

import java.util.List;

@Service
public class ScanServiceImpl implements ScanService {

	@Autowired
	private ScanDAO scanDAO;

	@Autowired
	private WifiDAO wifiDAO;

	@Autowired
	private MapDAO mapDAO;

	@Autowired
	private ProjectDAO projectDAO;

	public Scan saveScan(Scan scan) {
		// TODO: hay que chequear que el scan pertenezca a algún mapa y proyecto existentes, no dejar que explote
		int wifiId = scanDAO.saveScan(scan);

		for (WifiData w: scan.getWifis()) {
			wifiDAO.saveWifiData(wifiId, w);
		}

		return scan;
	}

	public List<Scan> getScansForMapId (long mapId) {
		List<Scan> scans = scanDAO.getAllScansByMapId(mapId);
		for (Scan s: scans) {
			s.setWifis(wifiDAO.getWifiScanById(s.getWifiScanId()));
		}

		return scans;
	}

	public List<Scan> getScansForProjectId (long projectId) {
		List<Scan> scans = scanDAO.getAllScansByProjectId(projectId);
		for (Scan s: scans) {
			s.setWifis(wifiDAO.getWifiScanById(s.getWifiScanId()));
		}

		return scans;
	}

	public List<Scan> getScansForLocation (long mapId, Location location) {
		List<Scan> scans = scanDAO.getScansByLocation(mapId, location);
		for (Scan s: scans) {
			s.setWifis(wifiDAO.getWifiScanById(s.getWifiScanId()));
		}

		return scans;
	}

}
