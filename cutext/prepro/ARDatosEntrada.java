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
* Se encarga del almacenamiento y recuperacion de los datos de usuario
******************************************************************************************/



package cutext.prepro;



import java.util.*;
import java.io.*;



public class ARDatosEntrada
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	ObjectOutputStream deEscribir; //para guardar de
	ObjectInputStream deLeer;   //recuperar de

	public ARDatosEntrada(String ar)
	{
		if(ar.equals("Guardar"))
		{
			try
			{
				deEscribir = new ObjectOutputStream(new FileOutputStream(DatosEntrada.N + DatosEntrada.out));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(ar.equals("Recuperar"))
		{
			try
			{
				deLeer = new ObjectInputStream(new FileInputStream(DatosEntrada.N + DatosEntrada.out));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public ARDatosEntrada(String ar, String nombre)
	{
		if(ar.equals("Guardar"))
		{
			try
			{
				deEscribir = new ObjectOutputStream(new FileOutputStream(nombre + DatosEntrada.out));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
		else if(ar.equals("Recuperar"))
		{
			try
			{
				deLeer = new ObjectInputStream(new FileInputStream(nombre + DatosEntrada.out));
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	//Guardar de
	public void guardar(DatosEntrada de)
	{
		try
		{
			deEscribir.writeObject(de);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	//Recuperar de
	public DatosEntrada recuperar()
	{
		try
		{
			return (DatosEntrada)deLeer.readObject();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}


