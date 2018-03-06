package tp.locomovil.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.dao.*;
import tp.locomovil.inter.service.ScanService;
import tp.locomovil.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

	@Autowired
	private NeuralNetDAO neuralNetDAO;

	// TODO: es raro que se repite esto del LEVEL_SORT en varios lugares.
	private static final Comparator<WifiData> LEVEL_SORT = (o1, o2) -> o2.getLevel() - o1.getLevel();

	private static final int MAX_COINCIDENCES = 5;
	private static final int STRONGEST_AP_NUMBER = 5;

	public Scan saveScan(Scan scan) {
		SMap m = mapDAO.getMapById(scan.getProjectId(), scan.getMapId());
		if (m == null) // Si el mapa no existe retorna null.
			return null;

		// Guardado en base de datos normal
		int wifiId = scanDAO.saveScan(scan);

		for (WifiData w: scan.getWifis()) {
			wifiDAO.saveWifiData(wifiId, w);
		}

		// Redes neuronales
		List<WifiData> wifis = scan.getWifis().stream()
				.sorted(LEVEL_SORT).limit(STRONGEST_AP_NUMBER).collect(Collectors.toList());

		WifiNeuralNet net = neuralNetDAO.getNetworkForAPs(wifis);
		if (net == null) // Crea una nueva
			net = neuralNetDAO.createNetworkForAPs(scan.getProjectId(), scan.getMapId(), wifis);

		// Entrena una existente
		net.train(Collections.singletonList(scan));
		neuralNetDAO.updateNetworkWithId(scan.getMapId(), net);


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
