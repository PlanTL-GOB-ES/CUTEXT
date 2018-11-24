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



public class StringToFile
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	public static void stringToFile(String text, String fileName)
	{
		try
		{
			File file = new File(fileName);
			// if file doesnt exists, then create it 
			if(!file.exists())
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}

	public static void stringToFileAppend(String text, String fileName)
	{
		try
		{
			File file = new File(fileName);
			// if file doesnt exists, then create it 
			if(!file.exists())
			{
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true); //true := append
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(text);
			bw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	public static void stringToFileAppend(String text, String fileName, java.nio.charset.Charset encod) //for example: encod = StandardCharsets.ISO_8859_1
	{
		try
		{
			File file = new File(fileName);
			// if file doesnt exists, then create it 
			if(!file.exists())
			{
				file.createNewFile();
			}
			//
			FileOutputStream fos = new FileOutputStream(file.getAbsoluteFile(), true); //true := append
			OutputStreamWriter osw = new OutputStreamWriter(fos, encod);
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(text);
			bw.close();
		}
		catch(IOException e)
		{
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
	}
	
	
	
	
	//This method uses the "try-with-resources statement" which will automatically close the PrintStream
	public static void oneLiner(String text, String fileName)
	{
		try(PrintWriter out = new PrintWriter(fileName))
		{
			out.println(text);
		}
		catch(FileNotFoundException e)
		{
			System.out.println("Error, file not exists: " + e);
			e.printStackTrace();
		}
	}





	public static void stop()
	{
		Scanner inEscaner = new Scanner(System.in);
		String inKeyboard = inEscaner.nextLine();
	}

	public static void main(String[] args) throws IOException
	{
	}


}






