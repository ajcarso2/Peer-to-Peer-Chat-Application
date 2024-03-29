# Peer-to-Peer Chat Application Overview

This Java-based application enables peer-to-peer messaging, allowing users to communicate directly without a centralized server. Utilizing a subscriber model, participants have the flexibility to select peers they wish to receive messages from, enhancing user-driven connectivity and interaction.

## Running the Application

### Compilation
First, compile the Java classes:
```bash
javac ClientThread.java ServerThread.java Peer.java
```

### Execution
Launch the application using:
```bash
java Peer <username> <port> [initialNodeHost:initialNodePort]
```
- **username**: Your chosen username for the chat session.
- **port**: The port your instance listens on for incoming connections.
- **initialNodeHost:initialNodePort**: Optionally, connect to an existing peer by specifying their host and port.

## Key Features

### Node Addition
New participants can join the network by contacting an existing peer, provided during startup. The joining node sends its details in a JSON format with a "newNode" status, prompting the contacted node to update its peer list accordingly.

### Node Removal
To maintain network integrity, nodes monitor peer connectivity. If a peer becomes unresponsive, it's automatically removed from the participant's list of active connections. This ensures the network remains dynamic, allowing for nodes to leave and rejoin seamlessly.

## Development Insights

- **Handling New Nodes**: The `ClientThread` class is designed to process "newNode" messages, invoking `addNewNode()` within `ServerThread` to update peer lists without requiring reciprocal connections.
- **Managing Node Departures**: Enhanced `ClientThread` error handling identifies disconnected peers, leveraging `interrupt()` to cease operations. The `ServerThread` incorporates `handleOfflineNode()` to purge inactive nodes from connectivity records.

## Limitations

- **Security**: The application operates without encryption or authentication, transmitting messages in plaintext.
- **Trustworthiness**: There's an inherent assumption of peer reliability, lacking mechanisms to authenticate message origins.
- **Input Validation**: Absent error handling for incorrect user inputs might lead to operational inconsistencies.
 
 