package trabajo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import clases.Equipo;
import clases.Pokemon;

public class Pruebas {
	public static void main(String[] args) {
		String nombreEntrenador = "Javi";
		int array[] = { 3, 24, 28, 45, 90, 101 };
		String foto = "default.png";
		addEquipo(nombreEntrenador,array, foto);
	}
	public static void addEquipo(String nombreEntrenador, int[] array, String foto) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			//Creamos un Arbol DOM a partir del fichero pokemon.xml
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document docu = db.parse("./web/pokemon.xml");
			Element raiz = docu.getDocumentElement();
			
			//Creamos un Objeto equipo que es el que añadiremos a equipos.xml
			Equipo eq = new Equipo();
			eq.setNombreEntrenador(nombreEntrenador);
			NodeList listaPkmn = raiz.getElementsByTagName("pokemon");
			// Solo tengo que acceder a los numeros que el usuario indique
			/*
			 * Venusaur Arbok Sandslash Vileplume Shellder Electrode
			 */
			List<Pokemon> equipo = new ArrayList<>();

			//Esto es para pasar el Arbol DOM a XML
			//Creamos otro Arbol DOM para los equipos
			DocumentBuilder dbEquipos = dbf.newDocumentBuilder();
			Document docEquipos = db.parse("./web/equipos.xml");
			Element raizEquipo = docEquipos.getDocumentElement();
			
			//Creamos el nuevo equipo
			Element equipoNuevo = docEquipos.createElement("equipo");
			//Añadimos los atributos
			equipoNuevo.setAttribute("nombreEntrenador", nombreEntrenador);
			equipoNuevo.setAttribute("photo", foto);
			for (int i = 0; i < array.length; i++) {
				//Ahora habria que añadir los 6 Pokemon del equipo
				Element nuevoPkmn = docEquipos.createElement("pokemon");
				Element pkmnActual = (Element) listaPkmn.item(array[i] - 1);
				
				System.out.println(pkmnActual.getElementsByTagName("name").item(0).getTextContent());
				
				Element number = docEquipos.createElement("number");
				number.setTextContent(pkmnActual.getElementsByTagName("number").item(0).getTextContent());
				
				Element name = docEquipos.createElement("name");
				name.setTextContent(pkmnActual.getElementsByTagName("name").item(0).getTextContent());
				
				Element classification = docEquipos.createElement("classification");
				classification.setTextContent(pkmnActual.getElementsByTagName("classification").item(0).getTextContent());
				
				Element height = docEquipos.createElement("height");
				height.setTextContent(pkmnActual.getElementsByTagName("height").item(0).getTextContent());
				
				Element weight = docEquipos.createElement("weight");
				weight.setTextContent(pkmnActual.getElementsByTagName("weight").item(0).getTextContent());
				
				Element hit_points = docEquipos.createElement("hit_points");
				hit_points.setTextContent(pkmnActual.getElementsByTagName("hit_points").item(0).getTextContent());
				
				Element attack = docEquipos.createElement("attack");
				attack.setTextContent(pkmnActual.getElementsByTagName("attack").item(0).getTextContent());
				
				Element defense = docEquipos.createElement("defense");
				defense.setTextContent(pkmnActual.getElementsByTagName("defense").item(0).getTextContent());
				
				Element speed = docEquipos.createElement("speed");
				speed.setTextContent(pkmnActual.getElementsByTagName("speed").item(0).getTextContent());
				
				nuevoPkmn.appendChild(number);
				nuevoPkmn.appendChild(name);
				nuevoPkmn.appendChild(classification);
				nuevoPkmn.appendChild(height);
				nuevoPkmn.appendChild(weight);
				nuevoPkmn.appendChild(hit_points);
				nuevoPkmn.appendChild(attack);
				nuevoPkmn.appendChild(defense);
				nuevoPkmn.appendChild(speed);
				
				equipoNuevo.appendChild(nuevoPkmn);
			}
			raizEquipo.appendChild(equipoNuevo);			
			
			//Transformamos el DOM a XML
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(docEquipos);
			StreamResult result = new StreamResult(new File("./web/equipos-Pruebas.xml"));
			transformer.transform(source, result);
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}