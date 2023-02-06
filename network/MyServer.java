
import java.io.*;
import java.text.*;
import java.util.*;
import java.net.*;
//----------------------------------------------
 class ManageClient extends Thread
{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    // Constructor
    public ManageClient(Socket s, DataInputStream dis, DataOutputStream dos)
    {
        this.s = s;
        this.dis = dis;
        this.dos = dos;
    }

    @Override
    public void run()
    {
        String received;
        while (true)
        {
            try {

                // Ask user what he/she wants to send the data
                // to the particular Client (that thread will send)
                dos.writeUTF("Connection Going on (write 'exit' to close)");

                // receive the answer from client (from the same thread)
                received = dis.readUTF();

                if(received.equals("Exit"))
                {
                    System.out.println("Client " + this.s + " sends exit...");
                    System.out.println("Closing Client's connection.");
                    this.s.close();
                    System.out.println("Client Connection closed");
                    break;
                }
                else
                {
                    System.out.println("Data Recieved from Client: "+received);
                }
                appendDataToFile("log.txt", received + "\n");
                dos.writeUTF("Server: Send Data:");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try
        {
            // Closing Resources
            this.dis.close();
            this.dos.close();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void appendDataToFile(String fileName, String str)
    {
        try {
            // Open a file in append mode by creating an
            // object of BufferedWriter class, Filename is in by accepting parameter using variable (filename)
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, true));

            // Writing on output stream into the file as a appending mode
            bufferedWriter.write(str);
            // Closing the connection
            bufferedWriter.close();
        }
        catch (IOException e) // Catch block to handle the exceptions
        {
            System.out.println("exception occurred" + e); // Display message when exception occurs
        }
    }
}



//----------------------------------------------
// Server class
public class MyServer
{
    public static void main(String[] args) throws IOException
    {
        // server is listening on port 7050
        ServerSocket serverSocket = new ServerSocket(7050);
        System.out.println("Server is Started At PORT : 7050");
        // running infinite loop for getting
        // client request

        while (true)
        {
            Socket socket = null;
            try
            {
                // socket object to receive incoming client requests
                socket = serverSocket.accept();

                System.out.print("A new client is connected : " + socket);

                // obtaining input and out streams
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

                System.out.println(", Assigning New Thread To This CLIENT...");

                // create a new thread object
                Thread t = new ManageClient(socket, dis, dos);

                // Invoking the start() method
                t.start();
            }
            catch (Exception e){
                socket.close();
                e.printStackTrace();
            }
        }
    }
}