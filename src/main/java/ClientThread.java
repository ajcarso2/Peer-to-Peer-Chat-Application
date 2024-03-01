import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

import org.json.*;

/**
 * Client 
 * This is the Client thread class, there is a client thread for each peer we are listening to.
 * We are constantly listening and if we get a message we print it. 
 */

public class ClientThread extends Thread {
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private ServerThread parentServerThread;
	private Socket socket;

	public ClientThread(Socket socket, ServerThread parentServerThread) throws IOException {
		this.socket = socket;
		bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
		this.parentServerThread = parentServerThread;
	}
	public void run() {
		while (true) {
			try {
				String line = bufferedReader.readLine();
				if (line == null) {
					break;
				}
				JSONObject json = new JSONObject(line);
				if (json.has("status") && json.getString("status").equals("newNode")) {
					parentServerThread.addNewNode(json.getString("address"));
				} else {
					System.out.println("[" + json.getString("username") + "]: " + json.getString("content"));
					if (parentServerThread != null) {
						parentServerThread.forwardMessage(json, this);
					}
				}
			} catch (Exception e) {
				interrupt();
				break;
			}
		}
	}


	public Socket getSocket() {
		return socket;
	}

}
