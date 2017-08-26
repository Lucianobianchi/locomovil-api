package tp.locomovil.webapp.forms;

import org.hibernate.validator.constraints.NotBlank;

public class FormProject {
	@NotBlank
	String name;

	public FormProject () {
	}

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}
}
