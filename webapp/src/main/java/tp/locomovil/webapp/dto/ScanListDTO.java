package tp.locomovil.webapp.dto;

import tp.locomovil.model.Scan;

import java.util.List;

public class ScanListDTO {
	List<Scan> scans;

	public ScanListDTO () {
	}

	public ScanListDTO (List<Scan> scans) {
		this.scans = scans;
	}

	public List<Scan> getScans () {
		return scans;
	}

	public void setScans (List<Scan> scans) {
		this.scans = scans;
	}
}
