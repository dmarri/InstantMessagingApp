import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by Jiadi on 11/19/2016.
   Edited By James on 11/23/2016.
   Edited by James on 11/28/2016.
 */
public class Client implements Runnable {
    static BufferedReader input = null;
    static boolean flag = false;

    public static void main(String[] args) {
        Socket socket = null;
        PrintStream output = null;
        BufferedReader userIP = null;
        BufferedReader serverIP = null;
        String host = null;
        
        // The port number must be the same as Server's
        int port = 1112;

        System.out.println("What is the IP Address of the Server?");
        System.out.println("If the Client is running on the same computer as the Server, type: localhost \n");
        System.out.println("Note: The firewall on the Server's computer may block incoming connections.");
        System.out.print("Please allow the IP address of this Client's computer on the Server's firewall for the Client to connect to the Server. \n");
        
        //Allows user to input the IP address of the Server. This will allow the user to run the Client program on a different computer.
        serverIP = new BufferedReader(new InputStreamReader(System.in));
      
        //for(int i = 0;i < 1000; i++)
        //{

        try {
            host = serverIP.readLine();
            socket = new Socket(host, port);
            userIP = new BufferedReader(new InputStreamReader(System.in));
            output = new PrintStream(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (socket != null) {
            Thread thread = new Thread(new Client());
            thread.start();

            try {
                while (!flag) {
                    output.println(userIP.readLine());
                }

                output.close();
                input.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
       //}
    }

    @Override
    public void run() {
        String msg;

        try {
            msg = input.readLine();

            while (msg != null) {
                System.out.println(msg);
                msg = input.readLine();
            }

            flag = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
