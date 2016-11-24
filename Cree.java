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
                System.out.println("Accepted client connection.");

                creeServer.out = clientSocket.getOutputStream();
                creeServer.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                System.out.println("Connected to client.");
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Errors.pErr(Errors.CONNECT);
                continue;
            }

            Content toSend = new Content();

            if(!(toSend.getContentPath(creeServer.in) && toSend.checkIfFileExists()))
            {
                toSend.path = "404.html";
            }

            toSend.transferContent(creeServer.out);

            try
            {
                creeServer.out.flush();
                creeServer.out.close();
                creeServer.in.close();
                clientSocket.close();
            }
            catch(Exception e)
            {
                e.printStackTrace();
                Errors.pErr(Errors.DISCONNECT);
                continue;
            }


            System.out.println("Closed connection.");
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
