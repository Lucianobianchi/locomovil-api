package tp.locomovil.inter.service;


import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

import java.util.List;

public interface ProjectMapService {

	Project getProjectById(long projectId);

	List<SMap> getMapsInProject(long id);

	SMap getMapByName (long projectId, String name);

	Project saveProject(String projectName);

	SMap saveMap(long projectId, String mapName);
}
