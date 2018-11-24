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
Asigna el valor de C-Value a cada termino
******************************************************************************************/




package cutext.filter.sta;



import java.util.*;
import java.io.*;

import cutext.prepro.*;
import cutext.util.*;



public class Cvalue implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;

	Frases terminos;
	Frases lista;


	public Cvalue clone()
	{
		Cvalue c = null;
		try
		{
			c = (Cvalue)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		c.terminos = (Frases)c.terminos.clone();
		c.lista = (Frases)c.lista.clone();

		return c;
	}

	public Cvalue(Frases terminos)
	{
		inicializar();
		this.terminos = terminos;
	}

	public void inicializar()
	{
		terminos = null;
		lista = new Frases();
	}


	//------------- SET --------------------


	public void setTerminos(Frases terminos)
	{
		this.terminos = terminos;
	}

	public void setLista(Frases lista)
	{
		this.lista = lista;
	}



	//------------- GET --------------------


	public Frases getTerminos()
	{
		return terminos;
	}

	public Frases getLista()
	{
		return lista;
	}






	//------------- Otros: Fases p.5 del paper --------------------


	public void setCvalue()
	{
		//recuperar DatosEntrada para el umbralCvalue
		ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		DatosEntrada de = arde.recuperar();

		setCvalue(de);
	}

	//Para el test
	public void setCvalue(DatosEntrada de)
	{
		if(terminos.tamanyo() == 0)
			return;

		double cvalue = 0.0;
		//ordenar de mayor a menor longitud
//System.out.println("...Frases sin ordenar..." + terminos.tamanyo() + "\n" + terminos.aString());
		Frases ford = terminos.ordenar(terminos, 0, terminos.tamanyo()-1);
//System.out.println("...Frases ordenadas..." + ford.tamanyo() + "\n" + ford.aString());
		ford.setMaximaLongitud(ford.getFrase(0).tamanyo());
		//coger terminos mayores (de maximaLongitud)
		int ml = ford.getMaximaLongitud();
		for(int i = 0; i < ford.tamanyo(); i++)
		{
			Frase frase = ford.getFrase(i);
			if(frase.tamanyo() == ml) //de maximaLongitud: asignar c-value
			{
				cvalue = Estaticos.log2(frase.tamanyo()) * (double)frase.getFrecuencia(); //English
				if(de.getIdioma().equals("SPANISH") || de.getIdioma().equals("CATALAN") || de.getIdioma().equals("GALICIAN")) //Spanish, catalan, galician
					cvalue = (Estaticos.CTE_I + Estaticos.log2(frase.tamanyo())) * (double)frase.getFrecuencia();
				frase.setCvalue(cvalue);
				//0: d1 == d2; < 0: d1 < d2; > 0: d1 > d2
				if(Double.compare(cvalue, de.getUmbralCvalue()) >= 0)
				{
					lista.anyadir((Frase)frase.clone());
					//para todo subtermino b de a modificar t(b) y c(b)
					Frases subterminos = ford.getSubterminos(frase);
//System.out.println("subterminos del termino:\n" + frase.aString() + "\nSUBTERMINOS:\n" + subterminos.aString());
					modificarTC(subterminos, frase);
//System.out.println("subterminos modificados:\n" + subterminos.aString());
//Scanner entradaEscaner = new Scanner(System.in);
//String entradaTeclado = entradaEscaner.nextLine();
				}
			}
			else //smaller in descending order
			{
				cvalue = Estaticos.log2(frase.tamanyo()) * (double)frase.getFrecuencia(); //suponemos que aparece por primera vez
				if(de.getIdioma().equals("SPANISH") || de.getIdioma().equals("CATALAN") || de.getIdioma().equals("GALICIAN")) //Spanish, catalan, galician
					cvalue = (Estaticos.CTE_I + Estaticos.log2(frase.tamanyo())) * (double)frase.getFrecuencia();
				if(!frase.esPrimeraVez()) //no primera vez: cvalue distinta formula
				{
					cvalue = Estaticos.log2(frase.tamanyo()) * 
						(double)(((double)frase.getFrecuencia()) - (double)((1/(double)frase.getC()) * ((double)frase.getT())));
					if(de.getIdioma().equals("SPANISH") || de.getIdioma().equals("CATALAN") || de.getIdioma().equals("GALICIAN")) //Spanish, catalan, galician
						cvalue = (Estaticos.CTE_I + Estaticos.log2(frase.tamanyo())) * 
							(double)(((double)frase.getFrecuencia()) - (double)((1/(double)frase.getC()) * ((double)frase.getT())));
				}
				frase.setCvalue(cvalue);
				if(Double.compare(cvalue, de.getUmbralCvalue()) >= 0)
				{
					lista.anyadir((Frase)frase.clone());
					//para todo subtermino b de a modificar t(b) y c(b)
					Frases subterminos = ford.getSubterminos(frase);
//System.out.println("subterminos del termino:\n" + frase.aString() + "\nSUBTERMINOS:\n" + subterminos.aString());
					modificarTC(subterminos, frase);
//System.out.println("subterminos modificados:\n" + subterminos.aString());
//Scanner entradaEscaner = new Scanner(System.in);
//String entradaTeclado = entradaEscaner.nextLine();
				}
			}
		}
	}



	public void modificarTC(Frases subterminos, Frase termino)
	{
		//String palabrasTermino = termino.palabrasAstring();
		String lemasTermino = termino.lemasAstring();
		for(int i = 0; i < subterminos.tamanyo(); i++)
		{
			Frase fSubtermino = subterminos.getFrase(i);
			//String palabrasSubtermino = fSubtermino.palabrasAstring();
			String lemasSubtermino = fSubtermino.lemasAstring();
			//int encontrado = palabrasTermino.indexOf(palabrasSubtermino, 0);
			int encontrado = lemasTermino.indexOf(lemasSubtermino, 0);
			if(encontrado != -1) //modificar t y c de fSubtermino
			{
				fSubtermino.setT(fSubtermino.getT() + termino.getFrecuencia());
				fSubtermino.setC(fSubtermino.getC() + 1);
			}
			//buscar superterminos de fSubtermino en subterminos para modificar t y c de fSubtermino
			Frases superterminos = subterminos.getSuperterminos(fSubtermino);
			for(int j = 0; j < superterminos.tamanyo(); j++)
			{
				Frase fSupertermino = superterminos.getFrase(j);
				fSubtermino.setT(fSubtermino.getT() + fSupertermino.getFrecuencia());
				//fSubtermino.setC(fSubtermino.getC() + 1);
			}
		}
	}





}















