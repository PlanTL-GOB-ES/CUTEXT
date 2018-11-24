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
 


/******************************************************************************************
* Se encarga de la escritura en un fichero
******************************************************************************************/


package cutext.util;



import java.util.*;
import java.io.*;


public class Fichero
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	String dir = "." + Estaticos.FILE_SEP + "out";
	String fich = "fich.dat";
	PrintWriter salida = null;
	File f = new File(dir);

	public Fichero()
	{
		if(!f.exists())
			f.mkdirs();
		try
		{
			salida = new PrintWriter(new BufferedWriter(new FileWriter(dir + Estaticos.FILE_SEP + fich)));
		}
		catch(IOException e)
		{
			System.err.println("Error de entrada salida");
		}
	}

	public Fichero(String directorio, String fichero)
	{
		dir = directorio;
		fich = fichero;
		f = new File(dir);
		if(!f.exists())
			f.mkdirs();
		try
		{
			salida = new PrintWriter(new BufferedWriter(new FileWriter(dir + Estaticos.FILE_SEP + fich)));
		}
		catch(IOException e)
		{
			System.err.println("Error de entrada salida");
		}
	}
	
	public void escribir(String texto)
	{
		salida.print(texto);
	}

	public void cerrar()
	{
		salida.close();
	}

	public String getName()
	{
		return this.f.getName();
	}

}


















