package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "listaEquipos")
public class Equipos implements Serializable {
	private static final long serialVersionUID = 1L;
	List<Equipo> equipos = new ArrayList<>();

	public Equipos() {

	}
	public Equipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}

	@XmlElementWrapper(name = "equipos")
	@XmlElement(name = "equipo")
	public List<Equipo> getEquipos() {
		return equipos;
	}
	public void setEquipos(List<Equipo> equipos) {
		this.equipos = equipos;
	}

	public void addEquipo(Equipo eq) {
		this.equipos.add(eq);
	}
}