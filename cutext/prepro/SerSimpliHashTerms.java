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
 

package cutext.prepro;








import java.util.*;
import java.io.*;



public class SerSimpliHashTerms
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	ObjectOutputStream deEscribir;
	ObjectInputStream deLeer;

	private final String D = "." + cutext.util.Estaticos.FILE_SEP + "ser" + cutext.util.Estaticos.FILE_SEP;
	private final String N = "hsimpli";

	boolean recovered = false;
	
	public SerSimpliHashTerms(String ar)
	{
		if(ar.equals("Guardar"))
		{
			try
			{
				deEscribir = new ObjectOutputStream(new FileOutputStream(D+N));
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
				deLeer = new ObjectInputStream(new FileInputStream(D+N));
				recovered = true;
			}
			catch(IOException e)
			{
				//e.printStackTrace();
			//----->	System.out.println("\n\t" + D + N + " does not exist.");
				//return new SimpliHashTerms();
				recovered = false;
			}
		}
	}

	public SerSimpliHashTerms(String ar, String ruta)
	{
		if(ar.equals("Guardar"))
		{
			try
			{
				deEscribir = new ObjectOutputStream(new FileOutputStream(ruta));
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
				deLeer = new ObjectInputStream(new FileInputStream(ruta));
				recovered = true;
			}
			catch(IOException e)
			{
				//e.printStackTrace();
			//--->	System.out.println("\n\t" + ruta + " does not exist.");
				//return new SimpliHashTerms();
				recovered = false;
			}
		}
	}


	public void guardar(cutext.prepro.SimpliHashTerms h)
	{
		try
		{
			deEscribir.writeObject(h);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		writeClose();
	}


	public cutext.prepro.SimpliHashTerms recuperar()
	{
		if(!recovered)
		{
		//--->	System.out.print(" Return new hash.");
			readClose();
			return new cutext.prepro.SimpliHashTerms();
		}
		else
		{
			try
			{
				cutext.prepro.SimpliHashTerms h = (cutext.prepro.SimpliHashTerms)deLeer.readObject();
				readClose();
				return h;
			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			catch(ClassNotFoundException e)
			{
				e.printStackTrace();
			}
			readClose();
			return null;//new SimpliHashTerms();
		}
	}
	
	public void writeClose()
	{
		try
		{
			if(this.deEscribir != null)
				this.deEscribir.close();
		}
		catch(IOException e)
		{
			System.err.println("ERROR TO CLOSE WRITER SimpliHashTerms");
			e.printStackTrace();
		}
	}
	
	public void readClose()
	{
		try
		{
			if(this.deLeer != null)
				this.deLeer.close();
		}
		catch(IOException e)
		{
			System.err.println("ERROR TO CLOSE READER SimpliHashTerms");
			e.printStackTrace();
		}
	}

}

















