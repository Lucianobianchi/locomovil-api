package tp.locomovil.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.LocationService;
import tp.locomovil.inter.ScanService;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.webapp.dto.ScanListDTO;
import tp.locomovil.webapp.forms.FormScan;

import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/scans")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class ScansController {
	@Autowired
	private ScanService scanService;

	@Context
	private UriInfo uriContext;

	// TODO si es que lo necesitamos
//	@GET
//	@Path("/{id}")
//	public Response getScansByLocation(@PathParam("id") long mapId,
//		@NotNull @QueryParam("x_coord") double xCoord,
//		@NotNull @QueryParam("y_coord") double yCoord) {
//		final Location l = new Location(xCoord, yCoord);
//		final List<Scan> scans = scanService.getScansForLocation(mapId, l);
//
//		if (scans.isEmpty())
//			return Response.status(NOT_FOUND).build();
//
//		return Response.ok(new ScanListDTO(scans)).build();
//	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postScan(final FormScan f) {
		Scan s = scanService.saveScan(f.toScan());

		return Response.ok().entity(s).build();
	}


}
