import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Cree
{
    private final int PORT;
    ServerSocket serverSocket;
    OutputStream out;
    BufferedReader in;

    public static void main(String[] args)
    {
        Cree creeServer = new Cree(8080);

        if(!creeServer.initialize(creeServer.PORT))
        {
            Errors.throwErr(Errors.INIT);
        }

        System.out.println("Server is running.");

        while(true)
        {
            Socket clientSocket;
            try
            {
                clientSocket = creeServer.serverSocket.accept();

                Thread newClient = new Thread(new ClientHandler(creeServer.serverSocket, clientSocket));
                newClient.start();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Errors.pErr(Errors.CONNECT);
                continue;
            }
        }

        /*try
        {
            creeServer.serverSocket.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/
    }

    Cree(int port)
    {
        PORT = port;
    }

    private boolean initialize(int port)
    {
        try
        {
            serverSocket = new ServerSocket(port);
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}

class ClientHandler implements Runnable
{
    ServerSocket serverSocket;
    Socket clientSocket;
    OutputStream out;
    BufferedReader in;

    ClientHandler(ServerSocket serverSocket, Socket clientSocket)
    {
        this.serverSocket = serverSocket;
        this.clientSocket = clientSocket;
    }

    public void run()
    {
        System.out.println("Accepted client connection in thread " + this);

        try
        {
            out = clientSocket.getOutputStream();
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Connected to client.");
        }
        catch(Exception e)
        {
            Errors.pErr(Errors.CONNECT);
        }

        if(in != null)
        {
            Content toSend = new Content();

            if(!(toSend.getContentPath(in) && toSend.checkIfFileExists()))
            {
                toSend.path = "404.html";
            }

            toSend.transferContent(out);

            try
            {
                out.flush();
                out.close();
                in.close();
                clientSocket.close();
            }
            catch(Exception e)
            {
                Errors.pErr(Errors.DISCONNECT);
            }

            System.out.println("Closed connection.");
        }
    }
}