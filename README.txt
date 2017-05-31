/**
 * Created by Zack on 11/27/2016
 */



README:



To run the project:



1. Download the zip file and extract it into a common directory.



2. It is recommended that you use the command prompt (terminal in Ubuntu works extremely well and is easy to use), if you choose to use an IDE, it needs to be one that can run multiple Java files at the same time (Clients and Server). Make sure your version of java (and compiler) are updated, this is easy to do online. (Most of the instructions below reference Ubuntu commands, but it is possible to run this program on any IDE that can open multiple Java files (run multiple programs) at once. As long as you can get more than one program running at the same time (One server and two or more clients) then you will be able to see the program work.



3. Once you have the terminal open and in the file directory of
your choice, make sure that files are compiled (they come precompiled,
but you can delete them and compile them on your own if you wish). To compile in the Ubuntu terminal would mean to call "javac Server.java" doing this for each of the java file names in the folder except putting the file name where Server.java is. If you are using an IDE, just make sure all the files are compiled (as stated above, they come precompiled but feel free to recompile).



4. After compilation verification, run the Server.java 
(in Ubuntu terminal this would be done by inputting "java Server" in
the correct directory on the terminal). Once you run the Server, that
terminal should say "The server is running" and tell you how
to terminate the server. You do not need to do anything else to 
get the server running. In an IDE, this would be done by running the Server.java file.



5. After the server is running, use a separate terminal/command prompt/program execution. Run the Client.java to create the first client on the server. (To run the first client, simply use this second terminal window and type "java Client" in the correct directory. - Ubuntu). To run a client on an IDE, simply run the Client.java file.



6. Once the client is running, the first thing you will see is prompting you to input the IP address of the Server. If you are running this program all on the same computer, the input to that should be "localhost". If you are running the program on different computers, you have to make sure a connection can be made, so be sure that the firewall is not blocking incoming connections to the server from clients. This is done by allowing the IP address of each client through the server's firewall.



7. Once you have inputted localhost or the IP of the server, the client program will ask you for your name (this specific client's name). You can input anything as your name except "-leave" because that is a command to leave the chatroom. Type "A" for this username. Next, you will be welcomed to the chat room and given a private key and public key. After this, you will be prompted with "Send to:". This prompt is asking who you would like to send a message to. Because there are no other clients on the server, you have no one that you can send to yet. You can try to input different names to send to, but you will be told that the user is not found.



8. Now that you have one client running, open a third terminal/program execution and run another instance of Client to complete a message. Follow the same steps as above to get the second client running. Use "B" for the name of this client. Once B has arrived, A will be notified. Now, from A's terminal, type "B" (this will be inputting for the send to prompt). After you type "B" you will be prompted to type a message, type "Hello B".



9. On B's terminal you should see "<A>: Hello B". This means you have successfully completed a message from A to B. On A's terminal you will see that after you typed "Hello B", a message of "Cannot decrypt!" appeared. This is because the server is sending the message to all clients in the current chatroom (for testing purposes) and because this message was not intended for A, A could not decrypt it. After sending a message, the program prompts again for "Send to: " followed by "Message: ". Now let's make a third client to complete the chat room.



10. Now that we have two clients running, make a third client to show just how effective the chat room is. Follow the steps on client creation from above to get a third client running, name it "C".



11. From C's terminal/execution window, type "A" in the send to prompt, followed by a message of "B cannot see". Notice that A can see the message from C, but B "Cannot decrypt!" and cannot see the message. Congratulations, you have successfully run the prototype program!







Test Cases:





Test case 1:  An attacker performs a history-based attack where they try to create all possible ciphertext, corresponding key and plaintext and matches that up with what they have eavesdropped. This attack should not work because our system will have keys which are long (bit-length) enough to make this attack fail.



Test case 1 implementation and testing: Although we do not act as an attacker in our program, we prove that this test case is satisfied by showing the length of the keys given to the clients. In a real-time chat room, we would not tell the client's their private key/public key, this would work under the table. But, to prove this test case is being completed, we have outputted the keys to the client upon client creation in the chat room. As you can see from running the chat room, the client keys are extremely long. It would be infeasible for an attacker to create all possible keys with ciphertext and plaintexts, because all possible keys would be an absurdly large number. To be specific, the bit length of our keys is: (insert bit length of keys here)







Test case 2: Have third user intercept a message (between 1 & 2) or get a
message from the server not intended for them, but show they cannot decrypt it correctly.



Test case 2 implementation and testing: While running the program, we had you send a message from C to A, "B cannot see". This test case was satisfied through this message, because B was unable to see the message between C and A. The reason for having the server send all messages out to all clients is to prove that only the intended client can decyrpt a message. Because A was the intended client, no other client could decrypt the message to them from C. B was unable to decrypt the message, and was prompted with "Cannot decrypt". Originally, we wanted to show that trying to decrypt a message with the wrong key (B's key for the message from C to A) would give B some random useless characters as the message, but this was not possible to show because decrypting with the wrong key gives out an exception in Java, so we had to put the warning in to not break the program. If you want to only send a message to the specified receiver, and do not want to send messages to all clients (showing that their decryption fails), simply comment out the "thread.output" line below, and comment in the "toAll" line in "ClientThread.java".



// Send the message to the receiver

// thread.output.println("<" + username + ">: " + byteToString(thread.decrypt(encBytes)));



// Send the message to all users

toAll(username, encBytes);







Test case 3: Have a new user register on the system/server and generate a key for the user, and show that this private key is different than any other user's private key.



Test case 3 implementation and testing: While running the program, we intentionally displayed the generated keys for each client when a client was created. Through displaying this, it allows whoever is running the program to compare the keys that each client is assigned. The person running the program can create as many clients as they wish, and none of the private keys should match. We do this by: (insert how this is done)


