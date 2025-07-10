import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.util.Scanner;

/**
 * @author Bonux
**/
public class IpToByteArray
{
	private static final String IPADDRESS_PATTERN = "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
	private static final String DOMAIN_PATTERN = "^(?!:\\/\\/)([a-zA-Z0-9-_]+\\.)*[a-zA-Z0-9][a-zA-Z0-9-_]+\\.[a-zA-Z]{2,11}?$";

    public static void main(String[] args)
	{
		int bindingType = 0;
		
		Scanner sc = new Scanner(System.in);
		while(bindingType != 1 && bindingType != 2)
		{
			System.out.print("Select binding type: 1 - IP, 2 - Domain: ");
			bindingType = sc.nextInt();
		}
		while(true)
		{
			if(bindingType == 1)
			{
				System.out.print("Enter IP: ");
				String ip = sc.next();
				if(ip.matches(IPADDRESS_PATTERN))
				{
					byte[] bytes = ip.getBytes();
					String result = "CLIENTS.put(new String(new byte[]{ ";
					System.out.print("Result: ");
					for(int i = 0; i < bytes.length; i++)
					{
						result += byteToHex(bytes[i]);
						if(i != (bytes.length - 1))
							result += ", ";
					}
					result += " }), -1);\t// ";
					System.out.println(result);
					StringSelection ss = new StringSelection(result);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
					System.out.println("Result copied to clipboard!");
					break;
				}
				else
				{
					System.out.println("Wrong IP[" + ip + "] format! Try again.");
				}
			}
			else
			{
				System.out.print("Enter domain: ");
				String domain = sc.next();
				if(domain.matches(DOMAIN_PATTERN))
				{
					byte[] bytes = domain.getBytes();
					String result = "CLIENTS.put(new String(new byte[]{ ";
					System.out.print("Result: ");
					for(int i = 0; i < bytes.length; i++)
					{
						result += byteToHex(bytes[i]);
						if(i != (bytes.length - 1))
							result += ", ";
					}
					result += " }), -1);\t// ";
					System.out.println(result);
					StringSelection ss = new StringSelection(result);
					Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
					System.out.println("Result copied to clipboard!");
					break;
				}
				else
				{
					System.out.println("Wrong DOMAIN[" + domain + "] format! Try again.");
				}
			}
		}
    }

	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

	public static String byteToHex(byte b)
	{
		String hex = "0x";
		int v = b & 0xFF;
		hex += hexArray[v >>> 4];
		hex += hexArray[v & 0x0F];
		return hex;
	}
}