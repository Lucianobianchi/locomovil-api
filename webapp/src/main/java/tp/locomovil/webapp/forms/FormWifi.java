package tp.locomovil.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;

public class FormWifi {

	@NotBlank
	private String BSSID;

	@NotBlank
	private Integer level;

	@NotBlank
	private Integer frequency;

	public FormWifi () {}

	public String getBSSID() {
		return BSSID;
	}

	public Integer getLevel() {
		return level;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setBSSID (String BSSID) {
		this.BSSID = BSSID;
	}

	public void setLevel (Integer level) {
		this.level = level;
	}

	public void setFrequency (Integer frequency) {
		this.frequency = frequency;
	}
}
