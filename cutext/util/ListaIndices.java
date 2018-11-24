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
* Es un ArrayList del objeto Indice
******************************************************************************************/


package cutext.util;


import java.util.*;
import java.io.*;




public class ListaIndices implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	ArrayList lista = new ArrayList();

	public ListaIndices clone()
	{
		ListaIndices li = null;
		try
		{
			li = (ListaIndices)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		li.lista = (ArrayList)li.lista.clone();

		return li;
	}




	public ListaIndices()
	{
	}

	public void anyadir(Indice indice)
	{
		lista.add(indice);
	}

	public void anyadir(int i, Indice indice)
	{
		lista.add(i, indice);
	}

	public Indice getIndice(int indice)
	{
		return (Indice)lista.get(indice);
	}

	public int tamanyo()
	{
		return lista.size();
	}

	public boolean estaVacio()
	{
		return lista.isEmpty();
	}	

	public Indice eliminar(int elemento)
	{
		return (Indice)lista.remove(elemento);
	}	

	public void limpiar()
	{
		lista.clear();
	}



	/*=========================
		GET
	=========================*/





	/*=========================
		SET
	=========================*/







	/*=========================
		Otros
	=========================*/


	public boolean esta(int indice)
	{
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Indice ind = this.getIndice(i);
			if(indice == ind.getIndice())
				return true;
		}
		return false;
	}

	public void anyadir(ListaIndices li)
	{
		for(int i = 0; i < li.tamanyo(); i++)
		{
			Indice indice = li.getIndice(i);
			this.anyadir((Indice)indice.clone());
		}
	}




	public String aString()
	{
		if(this == null)
			return "NULL";
		String as = "";
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Indice indice = getIndice(i);
			as += indice.aString() + " ";
		}
		return as.trim();
	}

	
}













