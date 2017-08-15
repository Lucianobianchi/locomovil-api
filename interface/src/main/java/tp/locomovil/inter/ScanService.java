package tp.locomovil.inter;

import tp.locomovil.model.SMap;
import tp.locomovil.model.Scan;

/**
 * Created by Bianchi on 12/8/17.
 */
public interface ScanService {
	Scan saveScan(Scan scan);

	SMap saveMap(String mapName);

	SMap getMapByName(String mapName);
}
