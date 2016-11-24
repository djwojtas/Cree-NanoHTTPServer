import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.TreeMap;

public class Content
{
    public String path = "";
    static TreeMap<String, String> contentTypes = new TreeMap<String, String>();

    static
    {
        contentTypes.put("html", "text/html");
        contentTypes.put("png", "image/png");
        contentTypes.put("jpg", "image/jpeg");
        contentTypes.put("jpeg", "image/jpeg");
    }

    public boolean getContentPath(BufferedReader in)
    {
        try
        {
            String head;
            if((head = in.readLine()) != null)
            {
                path = head.split(" ")[1].replaceFirst("\\/", "");
                System.out.println("User requested " + path);
                return true;
            }
            return false;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transferContent(OutputStream out)
    {
        System.out.println("Sending file of MIME " + getContentType());
        new Header(getContentType()).printHeader(new PrintWriter(out, true));

        if(getContentType().equals("text/html"))
        {
            return transferText(out);
        }
        else if(getContentType().equals("image/png"))
        {
            return transferImg(out);
        }
        else if(getContentType().equals("image/jpeg"))
        {
            return transferImg(out);
        }
        try
        {
            out.flush();
        }
        catch(Exception e){}

        return false;
    }

    public boolean transferText(OutputStream out)
    {
        try
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF8"));
            PrintWriter prtOut = new PrintWriter(out, true);

            String line;
            while ((line = in.readLine()) != null) {
                prtOut.println(line);
            }
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean transferImg(OutputStream out)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File(path));
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            out.write(byteArrayOutputStream.toByteArray());
            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkIfFileExists()
    {
        return new File(path).isFile();
    }

    public String getContentType()
    {
        return contentTypes.get(path.replaceFirst(".+\\.", ""));
    }
}
