import java.io.PrintWriter;

public class Header
{
    String[] headerLines;

    Header()
    {
        headerLines = new String[] {
                "HTTP/1.1 200 OK\r\n",
                "Content-Type: text/html\r\n",
                "Connection: close\r\n"
        };
    }

    Header(String contentType)
    {
        headerLines = new String[] {
                "HTTP/1.1 200 OK\r\n",
                "Content-Type: " + contentType + "\r\n",
                "Connection: close\r\n",
        };
    }

    Header(String contentType, String error)
    {
        headerLines = new String[] {
                "HTTP/1.1 " + error + "\r\n",
                "Content-Type: text/html\r\n",
                "Connection: close\r\n",
        };
    }

    Header(String contentType, String error, boolean noClose)
    {
        headerLines = new String[] {
                "HTTP/1.1 " + error + "\r\n",
                "Content-Type: text/html\r\n",
        };
    }

    public void printHeader(PrintWriter out)
    {
        for(int i=0; i<headerLines.length; i++)
        {
            out.print(headerLines[i]);
        }
        out.print("\r\n");
        out.flush();
    }
}
