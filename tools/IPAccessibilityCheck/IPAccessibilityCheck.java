
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Bonux
**/
public class IPAccessibilityCheck
{
    public static void main(String[] args)
	{
		String ip = args[0];
		int port = Integer.parseInt(args[1]);
		if(checkOpenPort(ip, port))
		{
			System.out.println("The port in '" + ip + "' is busy! Release the port and try again.");
			return;
		}

		boolean success = false;
		ServerSocket ss = null;
		try
		{
			ss = new ServerSocket(port);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			if(checkOpenPort(ip, port))
				success = true;

			try
			{
				ss.close();
			}
			catch(Exception e)
			{
				//
			}
		}

		if(success)
			System.out.println("Success! '" + ip + "' is your IP and port '" + port + "' is available.");
		else
			System.out.println("Failed! '" + ip + "' is not your IP or port '" + port + "' is not available.");
    }

	private static boolean checkOpenPort(String ip, int port)
	{
		Socket socket = null;
		try
		{
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 1000);
		}
		catch(Exception e)
		{
			return false;
		}
		finally
		{
			try
			{
				socket.close();
			}
			catch(Exception e)
			{
				//
			}
		}
		return true;
	}
}