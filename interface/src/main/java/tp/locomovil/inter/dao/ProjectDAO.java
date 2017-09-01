package tp.locomovil.inter.dao;

import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;

public interface ProjectDAO {
	Project createProject(String name);

	Project getProjectByName (String name);

	Project getProjectById (long id);
}
