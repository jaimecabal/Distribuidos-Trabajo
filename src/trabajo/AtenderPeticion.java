package trabajo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
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

public class AtenderPeticion extends Thread {
	private Socket s;
	private String peticion;
	private String HOMEDIR = "./web";

	public AtenderPeticion(Socket s) {
		this.s = s;
		try {
			BufferedReader is = new BufferedReader(new InputStreamReader(s.getInputStream()));
			this.peticion = is.readLine();
			System.out.println(peticion);
		} catch (IOException io) {
			io.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (this.peticion.startsWith("PUT")) {
			try {
				BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

				String[] peticionProcesada = peticion.split(" ");
//				PUT NombreEntrenador foto.png xxx xxx xxx xxx xxx xxx

				int pkmns[] = { Integer.parseInt(peticionProcesada[3]), Integer.parseInt(peticionProcesada[4]),
						Integer.parseInt(peticionProcesada[5]), Integer.parseInt(peticionProcesada[6]),
						Integer.parseInt(peticionProcesada[7]), Integer.parseInt(peticionProcesada[8]) };

				addEquipo(peticionProcesada[1], pkmns, peticionProcesada[2]);

				// Si todo sale bien:
				bwr.write("OK \r\n");
				bwr.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (this.peticion.startsWith("REMOVE")) {
			try {
				BufferedWriter bwr = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				String[] peticionProcesada = peticion.split(" ");
				// REMOVE NombreEntrenador
				removeEquipo(peticionProcesada[1]);

				// Si todo sale bien:
				bwr.write("OK \r\n");
				bwr.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			File fich = buscaFichero(this.peticion);
			try {
				if (fich.exists()) {
					String cType = "";
					if (fich.getName().endsWith(".css")) {
						cType = "text/css";
					} else {
						cType = URLConnection.guessContentTypeFromName(fich.getName());
					}
					if (this.peticion.startsWith("HEAD ")) {
						sendMIMEHeading(this.s.getOutputStream(), 200, cType, fich.length());
					} else {
						if (fich.isFile()) {
							try (BufferedInputStream dis = new BufferedInputStream(new FileInputStream(fich))) {
								sendMIMEHeading(this.s.getOutputStream(), 200, cType, fich.length());

								int bytesLeidos;
								byte[] buff = new byte[1024 * 32];
								System.out.println(peticion);

								while ((bytesLeidos = dis.read(buff)) != -1) {
									s.getOutputStream().write(buff, 0, bytesLeidos);
								}

								s.getOutputStream().flush();
							}
						} else {
							String error = makeHTMLErrorText(501, "No implementado");
							sendMIMEHeading(this.s.getOutputStream(), 501, "text/css", error.length());
							s.getOutputStream().write(error.getBytes());
							s.getOutputStream().flush();
						}
					}
				} else {
					String error = makeHTMLErrorText(404, "Pagina no encontrada");
					sendMIMEHeading(this.s.getOutputStream(), 404, "text/html", error.length());
					s.getOutputStream().write(error.getBytes());
					s.getOutputStream().flush();
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

	/*
	 * 
	 * METODOS DEL TRABAJO
	 * 
	 */
	public static void removeEquipo(String nom) {
		try {
			// Creamos un arbol DOM con los equipos que estan en el fichero equipos.xml
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document docEquipos = db.parse("./web/equipos.xml");
			Element raizEquipo = docEquipos.getDocumentElement();
			NodeList lEquipos = raizEquipo.getElementsByTagName("equipo");

			for (int i = lEquipos.getLength() - 1; i >= 0; i--) {
				Element e = (Element) lEquipos.item(i);
				String nomEntrenador = e.getAttribute("nombreEntrenador");
				System.out.println("Nombre entrenador actual: " + nomEntrenador);
				if (nomEntrenador.equals(nom)) {
					System.out.println("Entrenador encontrado, borrando");
					raizEquipo.removeChild(e);
					break;
				}
			}

			// Transformamos el DOM a XML
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(docEquipos);
			StreamResult result = new StreamResult(new File("./web/equipos.xml"));
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

	public static void addEquipo(String nombreEntrenador, int[] array, String foto) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Creamos un Arbol DOM a partir del fichero pokemon.xml
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document docu = db.parse("./web/pokemon.xml");
			Element raiz = docu.getDocumentElement();

			// Creamos un Objeto equipo que es el que a??adiremos a equipos.xml
			Equipo eq = new Equipo();
			eq.setNombreEntrenador(nombreEntrenador);
			NodeList listaPkmn = raiz.getElementsByTagName("pokemon");
			// Solo tengo que acceder a los numeros que el usuario indique
			/*
			 * Venusaur Arbok Sandslash Vileplume Shellder Electrode
			 */
			List<Pokemon> equipo = new ArrayList<>();

			// Esto es para pasar el Arbol DOM a XML
			// Creamos otro Arbol DOM para los equipos
			DocumentBuilder dbEquipos = dbf.newDocumentBuilder();
			Document docEquipos = db.parse("./web/equipos.xml");
			Element raizEquipo = docEquipos.getDocumentElement();

			// Creamos el nuevo equipo
			Element equipoNuevo = docEquipos.createElement("equipo");
			// A??adimos los atributos
			equipoNuevo.setAttribute("nombreEntrenador", nombreEntrenador);
			//Comprobamos si la foto existe, y sino se pone una por defecto:
			
			equipoNuevo.setAttribute("photo", checkFoto(foto));
			for (int i = 0; i < array.length; i++) {
				// Ahora habria que a??adir los 6 Pokemon del equipo
				Element nuevoPkmn = docEquipos.createElement("pokemon");
				Element pkmnActual = (Element) listaPkmn.item(array[i] - 1);

				System.out.println(pkmnActual.getElementsByTagName("name").item(0).getTextContent());

				Element number = docEquipos.createElement("number");
				number.setTextContent(pkmnActual.getElementsByTagName("number").item(0).getTextContent());

				Element name = docEquipos.createElement("name");
				name.setTextContent(pkmnActual.getElementsByTagName("name").item(0).getTextContent());

				Element classification = docEquipos.createElement("classification");
				classification
						.setTextContent(pkmnActual.getElementsByTagName("classification").item(0).getTextContent());

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

			// Transformamos el DOM a XML
			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(docEquipos);
			StreamResult result = new StreamResult(new File("./web/equipos.xml"));
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

	private static String checkFoto(String foto) {
		File locFoto = new File("./web/images/trainer-img/"+foto);
		if(locFoto.exists()) {
			return foto;
		} else {
			return "default.png";
		}
	}

	public static void verEquipos() {
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse("./web/equipos.xml");

			Element raiz = doc.getDocumentElement(); // El elemento raiz es equipos
			NodeList nl = raiz.getElementsByTagName("equipo");

			List<Equipo> lEq = new ArrayList<>();
			for (int i = 0; i < nl.getLength(); i++) {
				Element nodoActual = (Element) nl.item(i);
				Equipo eq = new Equipo();
				eq.setNombreEntrenador(nodoActual.getAttribute("nombreEntrenador"));
				// Recorremos la lista de equipos
				NodeList lPkmn = nodoActual.getElementsByTagName("pokemon");
				List<Pokemon> equipo = new ArrayList<>();
				for (int j = 0; j < lPkmn.getLength(); j++) {
					// Creamos un array de cadenas para poder guardar los contenidos de texto del
					// XML

					// Casteamos el Nodo actual a un Element para poder sacar sus Elementos y los
					// guardamos en el array
					Element pkmnActual = (Element) lPkmn.item(j);
					String[] pkmnString = new String[10];
					pkmnString[0] = pkmnActual.getElementsByTagName("number").item(0).getTextContent();
					pkmnString[1] = pkmnActual.getElementsByTagName("name").item(0).getTextContent();
					pkmnString[2] = pkmnActual.getElementsByTagName("classification").item(0).getTextContent();
					pkmnString[3] = pkmnActual.getElementsByTagName("height").item(0).getTextContent();
					pkmnString[4] = pkmnActual.getElementsByTagName("weight").item(0).getTextContent();
					pkmnString[5] = pkmnActual.getElementsByTagName("hit_points").item(0).getTextContent();
					pkmnString[6] = pkmnActual.getElementsByTagName("attack").item(0).getTextContent();
					pkmnString[7] = pkmnActual.getElementsByTagName("defense").item(0).getTextContent();
					pkmnString[8] = pkmnActual.getElementsByTagName("special").item(0).getTextContent();
					pkmnString[9] = pkmnActual.getElementsByTagName("speed").item(0).getTextContent();

					// Creamos un objeto Pokemon nuevo con los valores que hemos guardado en el
					// array
					Pokemon pkmnNuevo = new Pokemon(pkmnString[0], pkmnString[1], pkmnString[2], pkmnString[3],
							pkmnString[4], Integer.parseInt(pkmnString[5]), Integer.parseInt(pkmnString[6]),
							Integer.parseInt(pkmnString[7]), Integer.parseInt(pkmnString[8]),
							Integer.parseInt(pkmnString[9]));
					equipo.add(pkmnNuevo);
				}
				eq.setEquipo(equipo);
				lEq.add(eq);
			}
			for (Equipo eq : lEq) {
				System.out.println(eq.toString());
			}

		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * < --------------------- METODOS DADOS EN LA PRACTICA --------------------- >
	 */
	private File buscaFichero(String m) {
		String fileName = "";
		if (m.startsWith("GET ")) {
			// A partir de una cadena de mensaje (m) correcta (comienza por GET)
			fileName = m.substring(4, m.indexOf(" ", 5));
			if (fileName.equals("/")) {
				fileName += "index.html";
			}
		}
		if (m.startsWith("HEAD ")) {
			// A partir de una cadena de mensaje (m) correcta (comienza por HEAD)
			fileName = m.substring(6, m.indexOf(" ", 7));
			if (fileName.equals("/")) {
				fileName += "index.html";
			}
		}
		return new File(HOMEDIR, fileName);
	}

	private void sendMIMEHeading(OutputStream os, int code, String cType, long fSize) {
		PrintStream dos = new PrintStream(os);
		dos.print("HTTP/1.1 " + code + " ");
		if (code == 200) {
			dos.print("OK\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + cType + "\r\n");
			dos.print("\r\n");
		} else if (code == 404) {
			dos.print("File Not Found\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + "text/html" + "\r\n");
			dos.print("\r\n");
		} else if (code == 501) {
			dos.print("Not Implemented\r\n");
			dos.print("Date: " + new Date() + "\r\n");
			dos.print("Server: Cutre http Server ver. -6.0\r\n");
			dos.print("Connection: close\r\n");
			dos.print("Content-length: " + fSize + "\r\n");
			dos.print("Content-type: " + "text/html" + "\r\n");
			dos.print("\r\n");
		}
		dos.flush();
	}

	private String makeHTMLErrorText(int code, String txt) {
		StringBuffer msg = new StringBuffer("<HTML>\r\n");
		msg.append(" <HEAD>\r\n");
		msg.append(" <TITLE>" + txt + "</TITLE>\r\n");
		msg.append(" </HEAD>\r\n");
		msg.append(" <BODY>\r\n");
		msg.append(" <H1>HTTP Error " + code + ": " + txt + "</H1>\r\n");
		msg.append(" </BODY>\r\n");
		msg.append("</HTML>\r\n");
		return msg.toString();
	}

}