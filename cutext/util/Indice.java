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
*  atributos: int
*******************************************************************************************/


package cutext.util;




import java.util.*;
import java.io.*;


public class Indice implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	int indice;

	public Indice clone()
	{
		Indice t = null;
		try
		{
			t = (Indice)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:

		return t;
	}

	public Indice(int indice)
	{
		this.indice = indice;
	}

	/*=========================
		SET
	=========================*/

	public void setIndice(int indice)
	{
		this.indice = indice;
	}


	/*=========================
		GET
	=========================*/

	public int getIndice()
	{
		return indice;
	}







	/*=========================
		Otros
	=========================*/


	public boolean igual(Indice indice)
	{
		return (this.getIndice() == indice.getIndice());

	}

	//Convierte a String
	public String aString()
	{
		if(this == null)
			return "NULL";

		String astring = String.valueOf(this.getIndice());

		return astring;
	}




}








