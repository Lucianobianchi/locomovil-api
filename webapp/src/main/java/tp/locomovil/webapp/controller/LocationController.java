package tp.locomovil.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.LocationService;
import tp.locomovil.inter.ScanService;
import tp.locomovil.model.Location;
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

@Path("/")
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
	@Path("/location")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response getLocation(final FormScan f) {
		Location approximateLocation = locationService.getApproximateLocation(f.toScan());
		return Response.ok().entity(new LocationDTO(approximateLocation)).build();
	}


}
