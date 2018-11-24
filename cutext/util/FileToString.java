/*********************************************************************************************
This resource was developed by the Centro Nacional de Investigaciones Oncológicas (CNIO) 
in the framework of the "Plan de Impulso de las Tecnologías del Lenguaje” driven by the 
Secretaría de Estado para la Sociedad de la Información y Agenda Digital.

Copyright (C) 2017 Secretaría de Estado para la Sociedad de la Información y la Agenda Digital (SESIAD)
 
This program is free software; you can redistribute it and/or
modify it under the terms of the MIT License see LICENSE.txt file.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*********************************************************************************************/


/**
 *
 * @author Jesús Santamaría
 */
 




package cutext.util;





import java.io.*;
import java.util.*;
import java.nio.charset.*;
import java.nio.file.*;



public class FileToString
{
	
	
	private static final long serialVersionUID = -7149755349268484907L;

	//call: String fstr = FileToString.oneLiner(rute, Charset.forName(StandardCharsets.UTF_8.name()));
	public static String oneLiner(String path, Charset encoding) throws IOException
	{
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static List<String> asStringList(String path, Charset encoding)// throws IOException
	{
		List<String> lines = null;
		try
		{
			lines = Files.readAllLines(Paths.get(path), encoding);
		}
		catch(IOException e)
		{
			System.out.println("IOException");
			e.printStackTrace();
			//return null;
		}
		return lines;
	}

	public static String oneLiner(String file) throws IOException
	{
		String content = new String(Files.readAllBytes(Paths.get(file)));
		return content;
	}

	public static String withCharset(String file, Charset cs) throws IOException
	{
		// No real need to close the BufferedReader/InputStreamReader
		// as they're only wrapping the stream
		FileInputStream stream = new FileInputStream(file);
		try
		{
			Reader reader = new BufferedReader(new InputStreamReader(stream, cs));
			StringBuilder builder = new StringBuilder();
			char[] buffer = new char[8192];
			int read;
			while((read = reader.read(buffer, 0, buffer.length)) > 0)
			{
				builder.append(buffer, 0, read);
			}
			return builder.toString();
		}
		finally
		{
			// Potential issue here: if this throws an IOException,
			// it will mask any others. Normally I'd use a utility
			// method which would log exceptions and swallow them
			stream.close();
		}
	}
/*
	public static String leanSolution(String file)
	{
		Scanner scanner = new Scanner(new File(file));
		String text = scanner.useDelimiter("\\A").next();
		scanner.close(); // Put this call in a finally block
	}

	public static String leanSolution(String file, String charset)
	{
		Scanner scanner = new Scanner(new File(file), charset);
		String text = scanner.useDelimiter("\\A").next();
		scanner.close(); // Put this call in a finally block
	}
*/
	public static String classic(String file) throws FileNotFoundException, IOException
	{
		String sFile = "";
		String str;
		FileReader f = new FileReader(file);
		BufferedReader b = new BufferedReader(f);
		while((str = b.readLine())!=null)
		{
			sFile += str;
		}
		b.close();
		return sFile;
	}

	public static void stop()
	{
		Scanner inEscaner = new Scanner(System.in);
		String inKeyboard = inEscaner.nextLine();
	}

	public static void main(String[] args) throws IOException
	{
		List<String> list = FileToString.asStringList("." + Estaticos.FILE_SEP + "file.txt", StandardCharsets.UTF_8);
		for(int i = 0; i < list.size(); i++)
		{
			String s = list.get(i);
			System.out.println("---------- " + i + " --------------\n" + s);
		}
	}


}






