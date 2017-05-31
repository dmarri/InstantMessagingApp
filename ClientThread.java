import javax.crypto.Cipher;
import java.io.*;
import java.net.Socket;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;

/**
 * Created by Jiadi on 11/19/2016.
   Edited by James on 11/23/2016.
   Edited by James on 11/28/2016.
 */
public class ClientThread extends Thread {
    private BufferedReader input = null;
    private PrintStream output = null;
    private Socket socket = null;
    private ArrayList<ClientThread> clients;
    private String username;
    private PublicKey key1;
    private PrivateKey key2;
    private HashMap<ClientThread, PublicKey> keys1 = new HashMap<>();

    public ClientThread(Socket socket, ArrayList<ClientThread> clients, PublicKey key1, PrivateKey key2,
                        HashMap<ClientThread, PublicKey> keys1) {
        this.socket = socket;
        this.clients = clients;
        this.key1 = key1;
        this.key2 = key2;
        this.keys1 = keys1;
    }

    public void run() {
        String msg;
        String ServerKey1;
        String ServerKey2;
        
        try {
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintStream(socket.getOutputStream());

            // Get the username from the user
            output.println("## Please enter your username: ");
            username = input.readLine();

            // If the username already exists, enter a new one
            while (userExists() || username.equals("-leave") == true) {
                output.println("## Username already exists or is invalid. Please enter a new one: ");
                username = input.readLine();
            }

            // Welcome message to the user
            System.out.println();
            output.println("## Welcome to chat room, " + username + ". To leave chat room, type -leave \n");
            
            ServerKey2 = Base64.getEncoder().encodeToString(key2.getEncoded());
            ServerKey1 = key1.toString();
            
            output.println("Server's key 1 for this user: ");
            output.println(ServerKey2);
            output.println("Server's key 2 for this user: ");
            output.println(ServerKey1);

            // Other users will know this user is online
            toOther("## " + username + " has arrived. \n");

            while (true) {
                // Ask the receiver's username from the user
                output.println("## Send to: ");
                String user = input.readLine();
                
                if (user.equals("-leave") == true)
                {
                    break;
                }
                
                ClientThread thread = findUser(user);
                System.out.println();

                // If the receiver does not exist, ask again
                while (thread == null) {
                    output.println("## " + user + " not found. ");
                    output.println("## Send to: ");
                    user = input.readLine();
                    thread = findUser(user);
                    System.out.println();
                }

                // Get the public key of the receiver
                PublicKey key = keys1.get(thread);

                // Encrypt the message
                output.println("## Message: ");
                msg = input.readLine();
                byte[] msgBytes = msg.getBytes();
                byte[] encBytes = encrypt(msgBytes, key);

                // Send the message to the receiver
                // thread.output.println("<" + username + ">: " + byteToString(thread.decrypt(encBytes)));

                // Send the message to all users
                toAll(username, encBytes);
                
                System.out.println(this.username + " to " + thread.getUsername() + ":");
                System.out.println("Cipher Text:" + byteToString(encBytes));


                // The user leaves the room if he / she types "-leave"
                if (msg.startsWith("-leave")) {
                    break;
                }
            }

            // Inform the other users that this user is leaving
            toOther("## " + username + " has left. ");
            output.println("## Press [Enter] to exit.");

            // Release the slot
            clients.remove(this);
            keys1.remove(this);

            input.close();
            output.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return this.username;
    }
    

    // Check if a username already exists
    private boolean userExists() {
        for (ClientThread c : clients) {
            if (c != this && c.getUsername().equals(username)) {
                return true;
            }
        }

        return false;
    }

    // Find a user with the given username
    private ClientThread findUser(String user) {
        for (ClientThread c : clients) {
            if (c.getUsername().equals(user)) {
                return c;
            }
        }

        return null;
    }

    // Send a message to all users except me
    private void toOther(String msg) {
        for (ClientThread c : clients) {
            if (c != this) {
                c.output.println(msg);
            }
        }
    }

    // Send a message to all users
    private void toAll(String username, byte[] msgBytes){
        for (ClientThread c : clients) {
            try {
                c.output.println("<" + username + ">: " + byteToString(c.decrypt(msgBytes)));
            } catch (Exception e) {
                c.output.println("## Server cannot decrypt for this user! ");
            }
        }
    }

    // Encrypt a message with public key
    private byte[] encrypt(byte[] bytes, PublicKey key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(bytes);
    }

    // Decrypt a message with private key
    private byte[] decrypt(byte[] bytes) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.DECRYPT_MODE, this.key2);
        return cipher.doFinal(bytes);
    }

    private String byteToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }
}