import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jiadi on 11/19/2016.
   Edited by James on 11/23/2016.
   Edited by James on 11/28/2016.
 */
public class Server {
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Create an empty ServerSocket and an empty Socket
        ServerSocket server = null;
        Socket socket;

        // Keep all users in a ArrayList
        ArrayList<ClientThread> clients = new ArrayList<>();
        // Keep all users' keys in HashMaps
        HashMap<ClientThread, PublicKey> keys1 = new HashMap<>();
        HashMap<ClientThread, PrivateKey> keys2 = new HashMap<>();

        // The port number must be the same as Client's
        int port = 1112;
        System.out.println("The server is running.");
        System.out.println("Press [Ctrl-C] to terminate the server. ");

        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(512);  // key length must be >= 512
        KeyPair kp;
        PublicKey key1;
        PrivateKey key2;

        // Create a socket server with the port number
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                socket = server.accept();

                // Generate a public/private key pair for the user
                kp = kpg.generateKeyPair();
                key1 = kp.getPublic();
                key2 = kp.getPrivate();

                // Create a new Client when a socket connection is established
                ClientThread client = new ClientThread(socket, clients, key1, key2, keys1);

                // Save the user and the user's key
                clients.add(client);
                keys1.put(client, key1);              
                client.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}