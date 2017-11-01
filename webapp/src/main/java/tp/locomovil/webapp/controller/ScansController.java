package tp.locomovil.webapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.service.ScanService;
import tp.locomovil.model.Scan;
import tp.locomovil.webapp.dto.ScanDTO;
import tp.locomovil.webapp.forms.FormScan;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/scans")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class ScansController {
	@Autowired
	private ScanService scanService;

	@Context
	private UriInfo uriContext;

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postScan(final FormScan f) {
		Scan s = scanService.saveScan(f.toScan());

		return Response.ok().entity(new ScanDTO(s)).build();
	}


}
