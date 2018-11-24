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
 

/**************************************************************************************************
* Representa el conjunto de los w (nombres, adjetivos, verbos) de un determinado termino.
***************************************************************************************************/



package cutext.prepro;



import java.util.*;
import java.io.*;




public class ConjuntoW implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	ArrayList array = new ArrayList();

	Frase termino;


	public ConjuntoW clone()
	{
		ConjuntoW f = null;
		try
		{
			f = (ConjuntoW)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		f.array = (ArrayList)f.array.clone();
		f.termino = (Frase)f.termino.clone();

		return f;
	}




	public ConjuntoW(Frase termino)
	{
		this.termino = termino;
	}



	public void anyadir(Token t)
	{
		array.add(t);
	}

	public void anyadir(int i, Token t)
	{
		array.add(i, t);
	}

	public Token getW(int indice)
	{
		return (Token)array.get(indice);
	}

	public int tamanyo()
	{
		return array.size();
	}

	public boolean estaVacio()
	{
		return array.isEmpty();
	}	

	public Token eliminar(int elemento)
	{
		return (Token)array.remove(elemento);
	}	

	public void limpiar()
	{
		array.clear();
	}





	public Frase getTermino()
	{
		return termino;
	}

	public void setTermino(Frase termino)
	{
		this.termino = termino;
	}








	public String aString()
	{
		if(this == null)
			return "NULL";
		if(this.tamanyo() == 0)
			return "W SET IS EMPTY";
		String array = this.termino.aString() + "\n\n";
		for(int i = 0; i < tamanyo(); i++)
		{
			Token w = getW(i);
			array += w.aString() + "\n";
		}
		return array;
	}
	

	
}













