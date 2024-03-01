import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * SERVER
 * This is the ServerThread class that has a socket where we accept clients contacting us.
 * We save the clients ports connecting to the server into a List in this class.
 * When we wand to send a message we send it to all the listening ports
 */

public class ServerThread extends Thread {
	private Set<String> processedMessageIds = Collections.synchronizedSet(new HashSet<>());
	private Set<ClientThread> clientThreads = Collections.synchronizedSet(new HashSet<>());
	private ServerSocket serverSocket;

	public ServerThread(String portNum) throws IOException {
		serverSocket = new ServerSocket(Integer.valueOf(portNum));
	}

	public void run() {
		try {
			while (true) {
				Socket sock = serverSocket.accept();
				handleNewNode(sock);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void forwardMessage(JSONObject message, ClientThread sender) {
		try {
			String messageId = message.getString("id");

			if (!processedMessageIds.contains(messageId)) {
				processedMessageIds.add(messageId);
				String senderAddress = sender.getSocket().getRemoteSocketAddress().toString();
				senderAddress = senderAddress.startsWith("/") ? senderAddress.substring(1) : senderAddress;
				for (ClientThread ct : clientThreads) {
					Socket s = ct.getSocket();
					String receiverAddress = s.getRemoteSocketAddress().toString();

					if (!receiverAddress.equals(senderAddress)) {
						PrintWriter out = new PrintWriter(s.getOutputStream(), true);
						out.println(message.toString());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void handleNewNode(Socket sock) throws IOException {
		ClientThread ct = new ClientThread(sock, this);
		clientThreads.add(ct);
		ct.start();
	}

	public void addNewNode(String address) {
		// Do nothing, since we don't need to connect back to the new node.
	}

	public synchronized void handleOfflineNode(ClientThread clientThread) {
		clientThreads.remove(clientThread);
	}
}

