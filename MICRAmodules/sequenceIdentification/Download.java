import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/* Download files from NCBI */

public class Download{

	String id;
	String ext;

	Download(String id,String ext){
		
		this.id=id;
		
		this.ext=ext;
		try{
			getFile();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

	void getFile() throws IOException{
		InputStream input = null;
		FileOutputStream writeFile = null;

		try{
		
		 URL url = new URL("ftp://ftp.ncbi.nih.gov/sra/wgs_aux/"+id.charAt(0)+id.charAt(1)+"/"+id.charAt(2)+id.charAt(3)+"/"+id.substring(0,id.indexOf("."))+"/"+id+ext);
		System.out.println("ftp://ftp.ncbi.nih.gov/sra/wgs_aux/"+id.charAt(0)+id.charAt(1)+"/"+id.charAt(2)+id.charAt(3)+"/"+id.substring(0,id.indexOf("."))+"/"+id+ext);


		    URLConnection connection = url.openConnection();
		    int fileLength = connection.getContentLength();

		    input = connection.getInputStream();
		    String fileName = id+ext;
		    writeFile = new FileOutputStream(fileName);
		    byte[] buffer = new byte[1024];
		    int read;

		    	while ((read = input.read(buffer)) > 0)
		        	writeFile.write(buffer, 0, read);
		   		writeFile.flush();
			}
		catch (IOException e)
		{
		    System.out.println("Error while trying to download the file.");
		    e.printStackTrace();
		}
		finally
		{
		    try
		    {
		        writeFile.close();
		        input.close();
		    }
		    catch (IOException e)
		    {
		        e.printStackTrace();
		    }
		}
	}

   
}
