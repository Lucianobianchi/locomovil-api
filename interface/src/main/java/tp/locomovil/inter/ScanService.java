package tp.locomovil.inter;

import tp.locomovil.model.Location;
import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;

import java.util.List;

public interface ScanService {
	Scan saveScan(Scan scan);

	SMap saveMap(String mapName);

	SMap getMapByName(String mapName);

	SMap getMapById (long id);

	List<Scan> getScansForLocation (long mapId, Location location);

	List<Scan> getScansForId(long mapId);
}
