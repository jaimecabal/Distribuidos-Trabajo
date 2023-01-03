package trabajo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(8080);
			ExecutorService pool = Executors.newCachedThreadPool();
			System.out.println("Servidor lanzado");
			while(true) {
				Socket s = ss.accept();
				pool.execute(new AtenderPeticion(s));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}