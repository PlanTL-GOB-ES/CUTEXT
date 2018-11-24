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
* Es un ArrayList del objeto Token
******************************************************************************************/


package cutext.prepro;



import java.util.*;
import java.io.*;




public class SimpliFrase implements Cloneable, Serializable
{
	//static final long serialVersionUID = 4290328235763872433L;// version compatible con: fusion/out/terminesp_hashtables_final_spa.ser
	private static final long serialVersionUID = -7149755349268484907L;
	//private static final long serialVersionUID = -8392416382922755039L;
	
	

	String lemma;
	String words;
	int tamanyo;
	int frecuencia;
	double cvalue;
	int t; //suma de frecuencias como anidado de candidatos mayores
	int c; //numero de terminos de candidatos mayores
	public static final String COD = "CODIGO";
	public static final String FRE = "FRECUENCIA";

	public SimpliFrase clone()
	{
		SimpliFrase frase = null;
		try
		{
			frase = (SimpliFrase)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:

		return frase;
	}




	public SimpliFrase(String words, String lemma, int tamanyo, int frecuencia, double cvalue, int t, int c)
	{
		this.words = words;
		this.lemma = lemma;
		this.tamanyo = tamanyo;
		this.frecuencia = frecuencia;
		this.cvalue = cvalue;
		this.t = t;
		this.c = c;
	}
    
    public SimpliFrase()
	{
		this.words = null;
		this.lemma = null;
		this.tamanyo = -1;
		this.frecuencia = -1;
		this.cvalue = 0.0;
		this.t = 0;
		this.c = 0;
	}




	/*=========================
		GET
	=========================*/



	public String getWords()
	{
		return this.words;
	}
	
	public String getLemma()
	{
		return this.lemma;
	}

	public int tamanyo()
	{
		return this.tamanyo;
	}

	public int getFrecuencia()
	{
		return frecuencia;
	}

	public double getCvalue()
	{
		return cvalue;
	}

	public int getT()
	{
		return t;
	}

	public int getC()
	{
		return c;
	}


	/*=========================
		SET
	=========================*/



	public void setWords(String words)
	{
		this.words = words;
	}
	
	public void setLemma(String lemma)
	{
		this.lemma = lemma;
	}
	
	public void setTamanyo(int tamanyo)
	{
		this.tamanyo = tamanyo;
	}

	public void setFrecuencia(int frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	public void setCvalue(double cvalue)
	{
		this.cvalue = cvalue;
	}

	public void setT(int t)
	{
		this.t = t;
	}

	public void setC(int c)
	{
		this.c = c;
	}




	/*=========================
		Otros
	=========================*/


	//No se ha modificado ni t ni c (= 0)
	public boolean esPrimeraVez()
	{
		return ((this.getT() == 0) && (this.getC() == 0));
	}

	//No tiene contenido
	public boolean vacia()
	{
		return this.tamanyo() == 0;
	}






	public String toString()
	{
		if(this == null)
			return "NULL";
		String frase = "[frec: " + this.getFrecuencia() + "]-[t: " + this.getT() + "]-[c: " + this.getC() + "]-[c-value: " + this.getCvalue() + "]\n";
		frase += this.getWords() + "|" + this.getLemma() + "\n";
		return frase;
	}

}








class SimpliFraseComparatorSize implements Comparator<SimpliFrase>
{
	@Override
	//length in reverse order: from highest to lowest
	public int compare(SimpliFrase o1, SimpliFrase o2)
	{
		return Integer.compare(o2.tamanyo(), o1.tamanyo());
	}
}







class SimpliFraseComparatorCvalue implements Comparator<SimpliFrase>
{
	@Override
	//cvalue in reverse order: from highest to lowest
	public int compare(SimpliFrase o1, SimpliFrase o2)
	{
		return Double.compare(o2.getCvalue(), o1.getCvalue());
	}
}
















