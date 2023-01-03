package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pokedex")
public class Pokedex implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<Pokemon> pkdx = new ArrayList<>();

	public Pokedex() {
	}

	public List<Pokemon> getPkdx() {
		return pkdx;
	}

	@XmlElementWrapper(name = "pokedex")
	@XmlElement(name = "pokemon")
	public void setPkdx(List<Pokemon> pkdx) {
		this.pkdx = pkdx;
	}

	public Pokedex(List<Pokemon> pkdx) {
		this.pkdx = pkdx;
	}
}