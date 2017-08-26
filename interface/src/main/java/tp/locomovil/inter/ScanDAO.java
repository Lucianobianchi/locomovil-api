package tp.locomovil.inter;

import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;

import java.util.List;


public interface ScanDAO {
	int saveScan(Scan scan);

	List<Scan> getScansByLocation(long mapId, Location location);

	List<Scan> getAllScansByMapId(long mapId);

	List<Scan> getAllScansByProjectId(long projectId);
}
