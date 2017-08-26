package tp.locomovil.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.LocationService;
import tp.locomovil.inter.ScanService;
import tp.locomovil.model.Project;
import tp.locomovil.model.SMap;
import tp.locomovil.webapp.dto.MapDTO;
import tp.locomovil.webapp.dto.ProjectDTO;
import tp.locomovil.webapp.forms.FormMap;
import tp.locomovil.webapp.forms.FormProject;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Path("/projects")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class ProjectsController {
	@Autowired
	private LocationService locationService;

	@Autowired
	private ScanService scanService;

	@Context
	private UriInfo uriContext;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putProject (final FormProject project) {
		final String name = project.getName();

		Project existingProject = scanService.saveProject(name);
		if (existingProject != null) {
			List<SMap> maps = scanService.getMapsInProject(existingProject.getId());
			return Response.ok(uriContext.getBaseUri()).entity(new ProjectDTO(existingProject, maps)).build();
		}

		Project newProject = scanService.saveProject(name);

		// TODO location URI bien
		return Response.created(uriContext.getBaseUri()).entity(new ProjectDTO(newProject, new ArrayList<SMap>())).build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{project_id}/maps")
	public Response putMap (@PathParam("project_id") long projectId, final FormMap formMap) {
		SMap map = scanService.saveMap(projectId, formMap.getName());

		if (map == null) {
			// FIXME: error, el proyecto no existía
			return Response.ok().build();
		}

		// TODO location URI bien
		return Response.created(uriContext.getBaseUri()).entity(new MapDTO(map)).build();
	}
}
