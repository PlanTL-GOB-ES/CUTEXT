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
 


/************************************************************************************************************************************
* Se encarga de concatenar todos los ficheros de un directorio en un unico fichero y almacenarlo en otro directorio
*************************************************************************************************************************************/


package cutext.prepro;

import cutext.util.*;

import java.util.*;
import java.io.*;



public class ConcatenarFicheros
{
	private static final long serialVersionUID = -7149755349268484907L;


	String directorioEntrada;
	String directorioSalida;
	String ficheroSalida;
	File listaArchivos[];

	public ConcatenarFicheros(ArrayList lista, String directorioEntrada, String directorioSalida, String ficheroSalida)
	{
		this.directorioEntrada = directorioEntrada;
		this.directorioSalida = directorioSalida;
		this.ficheroSalida = ficheroSalida;
		File ruta = new File(directorioEntrada);
		if((lista == null) || (lista.size() == 0))
			listaArchivos = ruta.listFiles();
		else
			inicializarListaArchivos(lista);
	}

	//Selecciona pdf o texto
	public void inicializarListaArchivos(ArrayList lista)
	{
		listaArchivos = new File[lista.size()];
		for(int i = 0; i < lista.size(); i++)
		{
			String elemento = (String)lista.get(i);
			String archivo = directorioEntrada + Estaticos.FILE_SEP + elemento;
			if(elemento.toLowerCase().endsWith(".pdf"))//convertir a texto
				//archivo = convertirAtexto(directorioEntrada, elemento);
				archivo = pdfToText(directorioEntrada, elemento);

			listaArchivos[i] = new File(archivo);
		}
	}

	public synchronized String convertirAtexto(String directorio, String archivo)
	{
		String archivoTexto = directorio + Estaticos.FILE_SEP + archivo.substring(0, archivo.lastIndexOf(".pdf")) + ".txt";

		Runtime aplicacion = Runtime.getRuntime();
	 	try
		{
			String arg = "java -jar .." + Estaticos.FILE_SEP + "jar_pdf" + Estaticos.FILE_SEP + "pdfbox-app-2.0.5.jar ExtractText " + directorio + Estaticos.FILE_SEP + archivo + " " + archivoTexto;
			Process tt = aplicacion.exec(arg);
			tt.waitFor(); //esperar hasta que acabe
		}
		catch(Exception e)
		{
			System.err.println("ERROR to execute ExtractText from PDF");
			e.printStackTrace();
			System.exit(0);
		}

		return archivoTexto;
	}

	public String pdfToText(String directorio, String archivo)
	{
		String archivoPdf = directorio + Estaticos.FILE_SEP + archivo;
		String archivoTexto = directorio + Estaticos.FILE_SEP + archivo.substring(0, archivo.lastIndexOf(".pdf")) + ".txt";
		String args[] = {archivoPdf, archivoTexto};

		PdfTOtext ptt = new PdfTOtext();
		try
		{
			ptt.startExtraction(args);
		}
		catch(IOException e)
		{
			System.err.println("ERROR to extract text from PDF");
			e.printStackTrace();
			System.exit(0);
		}

		return archivoTexto;
	}



	public void concatenar()
	{
		FileWriter fich = null;
		PrintWriter escritor = null;
		try
		{
			fich = new FileWriter(directorioSalida + Estaticos.FILE_SEP + ficheroSalida);
			escritor = new PrintWriter(fich);

			for(int i = 0; i < listaArchivos.length; i++)
			{
				concatenar(escritor, listaArchivos[i]);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (null != fich)
					fich.close();
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}

	public void concatenar(PrintWriter escritor, File archivo)
	{
		FileReader fr = null;
		BufferedReader lector = null;
		try
		{
			fr = new FileReader (archivo);
			lector = new BufferedReader(fr);

			// Lectura del fichero
			String linea;
			while((linea = lector.readLine()) != null)
			{
				escritor.println(linea);
			}
			//escribir salto de linea
			escritor.println();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if(null != fr)
				{
					fr.close();
				}
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
		}
	}

}














