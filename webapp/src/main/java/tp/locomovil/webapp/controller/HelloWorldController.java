package tp.locomovil.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tp.locomovil.inter.LocationService;
import tp.locomovil.model.Location;
import tp.locomovil.model.Scan;
import tp.locomovil.model.WifiData;
import tp.locomovil.service.ScanServiceImpl;
import tp.locomovil.webapp.dto.LocationDTO;
import tp.locomovil.webapp.forms.FormScan;
import tp.locomovil.webapp.forms.FormWifi;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("/")
@Component
public class HelloWorldController {

	@Autowired
	private LocationService locationService;

	@Autowired
	private ScanServiceImpl scanServiceImpl;

	@GET
	@Path("test")
	@Produces(value = { MediaType.APPLICATION_JSON, })
	public Response getTest() {
		int s = 1;
		Location l = locationService.getApproximateLocation(null);
		return Response.ok(new LocationDTO(l)).build();
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/save")
	public Response postScan(final FormScan f) {
		final Scan.ScanDataBuilder b = new Scan.ScanDataBuilder();
		final List<WifiData> wifis = new LinkedList<WifiData>();

		if (f.getWifiScans() != null) {
			for (FormWifi w: f.getWifiScans()) {
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

		scanServiceImpl.saveScan(b.build());

		return Response.ok().build();
	}

}
