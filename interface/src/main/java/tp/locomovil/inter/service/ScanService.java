package tp.locomovil.inter.service;

import tp.locomovil.model.Location;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;

import java.util.List;

public interface ScanService {
	Scan saveScan(Scan scan);

	List<Scan> getScansForLocation (long mapId, Location location);

	List<Scan> getScansForMapId (long mapId);

	List<Scan> getScansForProjectId (long projectId);

}
