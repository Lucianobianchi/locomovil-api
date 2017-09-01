package tp.locomovil.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tp.locomovil.inter.dao.MapDAO;
import tp.locomovil.inter.dao.ProjectDAO;
import tp.locomovil.inter.service.ProjectMapService;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

import java.util.List;

@Service
public class ProjectMapServiceImpl implements ProjectMapService {

	@Autowired
	private ProjectDAO projectDAO;

	@Autowired
	private MapDAO mapDAO;

	public Project getProjectById (long projectId) {
		return projectDAO.getProjectById(projectId);
	}

	public SMap getMapByName (long projectId, String name) {
		return mapDAO.getMapByName(projectId, name);
	}

	public List<SMap> getMapsInProject(long id) {
		return mapDAO.getMapsByProjectId(id);
	}

	public Project saveProject (String projectName) {
		Project p = projectDAO.getProjectByName(projectName);
		if (p != null)
			return p;
		return projectDAO.createProject(projectName);
	}

	public SMap saveMap (long projectId, String mapName) {
		Project p = projectDAO.getProjectById(projectId);
		if (p == null)
			return null; // Project did not exist

		SMap existing = mapDAO.getMapByName(projectId, mapName);
		if (existing != null)
			return existing;

		return mapDAO.createMap(projectId, mapName);
	}

}
