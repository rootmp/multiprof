import java.io.File;
import java.io.FileOutputStream;

public class UpdatesListGenerator
{
	public static void main(String[] args)
	{
		try
		{
			StringBuilder files = new StringBuilder();
			files.append("INSERT INTO installed_updates (`file_name`) VALUES\n");

			File dir = new File(".");
			for(File f : dir.listFiles())
			{
				String name = f.getName();
				if(name.matches(".*?\\.sql"))
				{
					files.append("(\"");
					files.append(name.substring(0, name.length() - 4));
					files.append("\"),\n");
				}
			}

			File file = new File("./updates.txt");
			file.delete();
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(files.toString().getBytes("UTF-8"));
			fos.close();
		}
		catch(Exception e)
		{
		}
    }
}