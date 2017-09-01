package tp.locomovil.inter.dao;

import tp.locomovil.model.Location;
import tp.locomovil.model.SMap;

import java.util.List;


public interface MapDAO {
	SMap createMap(long projectId, String name);

	SMap getMapById (long projectId, long id);

	SMap getMapByName (long projectId, String name);

	List<SMap> getMapsByProjectId (long projectId);
}
