package tp.locomovil.inter;

import tp.locomovil.model.Location;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;

import java.util.List;

public interface ScanService {
	Scan saveScan(Scan scan);

	Project saveProject(String projectName);

	SMap saveMap(long projectId, String mapName);

	List<Scan> getScansForLocation (long mapId, Location location);

	List<Scan> getScansForId(long mapId);

	List<SMap> getMapsInProject(long id);

	SMap getMapByName (long projectId, String name);
}
