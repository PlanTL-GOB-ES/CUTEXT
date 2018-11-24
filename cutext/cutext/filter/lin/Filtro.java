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
 



/*****************************************************************************************
Filters are based on regular expresions
******************************************************************************************/




package cutext.filter.lin;



import java.util.*;
import java.io.*;

import cutext.prepro.*;
import cutext.util.Tiempo;




public class Filtro implements Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;


	FilterInterface filter = null;

	Frase corpus;

	DatosEntrada datos;

	public Filtro clone()
	{
		Filtro f = null;
		try
		{
			f = (Filtro)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		f.corpus = (Frase)f.corpus.clone();

		return f;
	}

	public Filtro()
	{
	}

	public Filtro(Frase corpus, DatosEntrada datos)
	{
		this.datos = datos;
		this.corpus = corpus;
		
		//select filter by language
		if(this.datos.getIdioma().equals(DatosEntrada.SPA))
			this.filter = new FilterSpa(corpus, datos);
		else if(this.datos.getIdioma().equals(DatosEntrada.ENG))
			this.filter = new FilterEng(corpus, datos);
		else if(this.datos.getIdioma().equals(DatosEntrada.CAT))
			this.filter = new FilterCat(corpus, datos);
		else //if(this.datos.getIdioma().equals(DatosEntrada.GAL))
			this.filter = new FilterGal(corpus, datos);
	}


	public ArrayFrases filtrar()
	{
		ArrayFrases af = this.filter.filtrar();
		return af;
	}








	public static boolean estaEstricto(String pos, String lista[])
	{
		for(int i = 0; i < lista.length; i++)
		{
			if(pos.equals(lista[i]))
				return true;
		}
		return false;
	}

	public static boolean esta(String pos, String lista[])
	{
		//token = token.toUpperCase();
		for(int i = 0; i < lista.length; i++)
		{
			//if(token.equals(lista[i]))
			if(pos.startsWith(lista[i]))
				return true;
		}
		return false;
	}








	public static void main(String args[])
	{
	}








}





























