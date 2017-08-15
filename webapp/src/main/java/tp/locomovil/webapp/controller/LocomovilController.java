package tp.locomovil.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.LocationService;
import tp.locomovil.inter.ScanService;
import tp.locomovil.model.Location;
import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;
import tp.locomovil.service.ScanServiceImpl;
import tp.locomovil.webapp.dto.LocationDTO;
import tp.locomovil.webapp.dto.MapDTO;
import tp.locomovil.webapp.forms.FormMap;
import tp.locomovil.webapp.forms.FormScan;
import tp.locomovil.webapp.forms.FormWifi;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

@Path("/")
@Component
@Produces(value = {MediaType.APPLICATION_JSON})
public class LocomovilController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private ScanService scanService;

	@Context
	private UriInfo uriContext;

//	@GET
//	@Path("/location")
//	@Consumes(MediaType.APPLICATION_JSON)
//	public Response getLocation(final FormScan f) {
//		return Response.ok().build();
//	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/maps/save")
	public Response postMap(final FormMap formMap) {
		// TODO location URI bien
		final String name = formMap.getMapName();
		SMap existingMap = scanService.getMapByName(name);
		if (existingMap != null)
			return Response.ok(uriContext.getBaseUri()).entity(new MapDTO(existingMap)).build();

		SMap newMap = scanService.saveMap(name);

		return Response.created(uriContext.getBaseUri()).entity(new MapDTO(newMap)).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/scans/save")
	public Response postScan(final FormScan f) {
		final Scan.ScanDataBuilder b = new Scan.ScanDataBuilder();
		final List<WifiData> wifis = new LinkedList<WifiData>();

		if (f.getWifis() != null) {
			for (FormWifi w: f.getWifis()) {
				WifiData.WifiDataBuilder wb = new WifiData.WifiDataBuilder();
				wb.frequency(w.getFrequency());
				wb.level(w.getLevel());
				wb.bssid(w.getBSSID());
				wifis.add(wb.build());
			}
		}

		b.userCoordinates(f.getUserCoordX(), f.getUserCoordY());
		b.rotationMatrix(f.getRotationMatrix());
		b.mapId(f.getMapId());
		b.location(f.getLatitude(), f.getLongitude(), f.getAltitude());
		b.locationResolution(f.getLocationResolution());
		b.NTPMillis(f.getNTPMillis());
		b.deviceMillis(f.getDeviceMillis());
		b.geomagneticFieldResolution(f.getGeomagneticResolution());
		b.geomagneticField(f.getGeomagneticX(), f.getGeomagneticY(), f.getGeomagneticZ());
		b.accelerationResolution(f.getAccelerationResolution());
		b.acceleration(f.getAccelerationX(), f.getAccelerationY(), f.getAccelerationZ());
		b.wifis(wifis);

		Scan s = scanService.saveScan(b.build());

		return Response.ok().entity(s).build();
	}

}
