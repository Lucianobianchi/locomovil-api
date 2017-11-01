package tp.locomovil.webapp.dto;

import tp.locomovil.model.Scan;

import java.util.LinkedList;
import java.util.List;

public class ScanListDTO {
	List<ScanDTO> scans;

	public ScanListDTO () {
	}

	public ScanListDTO (List<Scan> scans) {
		this.scans = new LinkedList<>();
		for (Scan s: scans)
			this.scans.add(new ScanDTO(s));
	}

	public List<ScanDTO> getScans () {
		return scans;
	}

	public void setScans (List<ScanDTO> scans) {
		this.scans = scans;
	}
}
