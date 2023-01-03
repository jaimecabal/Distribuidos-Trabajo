package clases;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

public class Equipo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String nombreEntrenador;
	private List<Pokemon> equipo = new ArrayList<>();

	public Equipo(String nombre, ArrayList<Pokemon> equipo) {
		this.nombreEntrenador = nombre;
		this.equipo = equipo;
	}
	public Equipo() {

	}

	@XmlAttribute
	public String getNombreEntrenador() {
		return nombreEntrenador;
	}
	public void setNombreEntrenador(String nombreEntrenador) {
		this.nombreEntrenador = nombreEntrenador;
	}

	@XmlElementWrapper(name = "equipo")
	@XmlElement(name = "pokemon")
	public List<Pokemon> getEquipo() {
		return equipo;
	}
	public void setEquipo(List<Pokemon> equipo) {
		this.equipo = equipo;
	}
	@Override
	public String toString() {
		String str = "Nombre: " + nombreEntrenador + "\r\n";

		for(Pokemon p : equipo) {
			str += p.toString() + "\r\n";
		}

		return str;
	}
}