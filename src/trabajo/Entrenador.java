package trabajo;

import java.util.Scanner;

public class Entrenador {
	public static void main(String[] args) {
		// Mandar el nombre del entrenador, la foto y los pokemon que se quiere mandar
		Scanner scan = new Scanner(System.in);
		//La peticion sera: PUT NombreEntrenador foto.png xxx xxx xxx xxx xxx xxx
		String peticion = "PUT";
		System.out.println("Introduce el nombre del entrenador nuevo: ");
		peticion += " " + scan.nextLine();
		
		System.out.println("Introduce la foto que se quiere usar"); // Aqui habria que hacer una comprobacion de que la
																	// foto esta disponible
		peticion += " " + scan.nextLine();
		System.out.println("Introduce los pokemon para añadir al equipo");
		for (int i = 0; i < 6; i++) {
			int numPkmn = scan.nextInt();
			if(numPkmn < 0 || numPkmn > 151) {
				System.out.println("Este Pokemon no esta en nuestra lista");
				i--;
			} else {
				peticion += " " + numPkmn;
			}
		}
//		System.out.println(peticion);
		
	}
}