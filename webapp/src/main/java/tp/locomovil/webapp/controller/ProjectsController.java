package tp.locomovil.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.service.ProjectMapService;
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
import java.util.List;
import java.util.Map;

@Path("/projects")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class ProjectsController {
	@Autowired
	private ProjectMapService projectMapService;

	@Context
	private UriInfo uriContext;

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response putProject (final FormProject project) {
		final String name = project.getName();

		Project existingProject = projectMapService.saveProject(name);
		if (existingProject != null) {
			List<SMap> maps = projectMapService.getMapsInProject(existingProject.getId());
			return Response.ok(uriContext.getBaseUri())
					.entity(new ProjectDTO(existingProject, maps, uriContext.getBaseUri())).build();
		}

		Project newProject = projectMapService.saveProject(name);

		ProjectDTO pDTO = new ProjectDTO(newProject, new ArrayList<SMap>(), uriContext.getBaseUri());
		return Response.created(pDTO.getUri()).entity(pDTO).build();
	}

	@GET
	@Path("/{project_id}")
	public Response getProjectById(@PathParam("project_id") long projectId) {
		Project p = projectMapService.getProjectById(projectId);
		if (p == null) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		else {
			List<SMap> maps = projectMapService.getMapsInProject(p.getId());
			return Response.ok(new ProjectDTO(p, maps, uriContext.getBaseUri())).build();
		}
	}

	@GET
	@Path("/{project_id}/maps/{map_id}")
	public Response getMapById(@PathParam("project_id") long projectId, @PathParam("map_id") long mapId) {
		//TODO
		return Response.noContent().build();
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/{project_id}/maps")
	public Response putMap (@PathParam("project_id") long projectId, final FormMap formMap) {
		SMap map = projectMapService.saveMap(projectId, formMap.getName());

		if (map == null) {
			return Response.noContent().build();
		}

		MapDTO mDTO = new MapDTO(map, projectId, uriContext.getBaseUri());
		return Response.created(mDTO.getUrl()).entity(mDTO).build();
	}
}
