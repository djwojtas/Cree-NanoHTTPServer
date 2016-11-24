public class Errors
{
    public static final String[] INIT = {"Error occurred during server initialization", "1"};
    public static final String[] CONNECT = {"Error occurred during connection with user", "2"};
    public static final String[] DISCONNECT = {"Error occurred during disconnecting from user", "3"};

    public static void throwErr(String[] Err)
    {
        System.out.println(" ERROR " + Err[1] + " - " + Err[0]);
        System.exit(Integer.parseInt(Err[1]));
    }

    public static void pErr(String[] Err)
    {
        System.out.println(" ERROR " + Err[1] + " - " + Err[0]);
    }
}