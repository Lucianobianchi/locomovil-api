package tp.locomovil.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.service.LocationService;
import tp.locomovil.inter.service.ScanService;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.webapp.dto.LocationDTO;
import tp.locomovil.webapp.forms.FormScan;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

@Path("/location")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class LocationController {
	@Autowired
	private LocationService locationService;

	@Autowired
	private ScanService scanService;

	@Context
	private UriInfo uriContext;

	@POST
	@Path("/map")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationByMap(final FormScan f) {
		Scan queryScan = f.toScan();
		List<Scan> calibrationScans = scanService.getScansForMapId(f.getMapId());
		Location approximateLocation = locationService.getApproximateLocation(queryScan, calibrationScans);

		if (approximateLocation == null)
			return Response.status(Response.Status.NOT_FOUND).build();

		return Response.ok().entity(new LocationDTO(approximateLocation)).build();
	}

	@POST
	@Path("/project")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocationByProject(final FormScan f) {
		Scan queryScan = f.toScan();
		List<Scan> calibrationScans = scanService.getScansForProjectId(queryScan.getProjectId());
		if (calibrationScans.isEmpty())
			return Response.status(Response.Status.NOT_FOUND).build();

		Location approximateLocation = locationService.getApproximateLocation(queryScan, calibrationScans);
		return Response.ok().entity(new LocationDTO(approximateLocation)).build();
	}


}
