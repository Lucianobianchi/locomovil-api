package tp.locomovil.inter;

import tp.locomovil.model.Location;
import tp.locomovil.model.SMap;

/**
 * Created by Bianchi on 13/8/17.
 */
public interface MapDAO {
	SMap createMap(String name);

	SMap getMapByName (String name);

	SMap getMapById (long id);
}