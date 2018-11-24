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




public class Frase implements Cloneable, Serializable
{
	//static final long serialVersionUID = 4290328235763872433L;// version compatible con: fusion/out/terminesp_hashtables_final_spa.ser
	private static final long serialVersionUID = -7149755349268484907L;
	//private static final long serialVersionUID = -8392416382922755039L;
	
	

	ArrayList terminos = new ArrayList();
	int longitud;
	int frecuencia;
	double cvalue;
	int t; //suma de frecuencias como anidado de candidatos mayores
	int c; //numero de terminos de candidatos mayores
	public static final String COD = "CODIGO";
	public static final String FRE = "FRECUENCIA";

	public Frase clone()
	{
		Frase frase = null;
		try
		{
			frase = (Frase)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		frase.terminos = (ArrayList)frase.terminos.clone();

		return frase;
	}




	public Frase()
	{
		this.longitud = -1;
		this.frecuencia = -1;
		this.cvalue = 0.0;
		this.t = 0;
		this.c = 0;
	}

	public void anyadir(Token t)
	{
		terminos.add(t);
	}

	public void anyadir(int i, Token t)
	{
		terminos.add(i, t);
	}

	public Token getToken(int indice)
	{
		return (Token)terminos.get(indice);
	}

	public int tamanyo()
	{
		return terminos.size();
	}

	public boolean estaVacio()
	{
		return terminos.isEmpty();
	}	

	public Token eliminar(int elemento)
	{
		return (Token)terminos.remove(elemento);
	}	

	public void limpiar()
	{
		terminos.clear();
	}



	/*=========================
		GET
	=========================*/



	public int getLongitud()
	{
		return longitud;
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



	public void setLongitud(int longitud)
	{
		this.longitud = longitud;
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


	//this es subfrase de frase (si son iguales se considera subfrase)
	public boolean esSubFrase(Frase frase)
	{
		return (this.getToken(0).getCodigo() >= frase.getToken(0).getCodigo() && this.getToken(this.tamanyo()-1).getCodigo() <= frase.getToken(frase.tamanyo()-1).getCodigo());
	}

	public boolean estaPalabra(String palabra)
	{
		palabra = palabra.toLowerCase();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			String p = token.getPalabra().toLowerCase();
			if(p.equals(palabra))
				return true;
		}
		return false;
	}

	public boolean estaLema(String lema)
	{
		lema = lema.toLowerCase();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			String l = token.getLema().toLowerCase();
			if(l.equals(lema))
				return true;
		}
		return false;
	}

	public int indicePalabra(String palabra)
	{
		palabra = palabra.toLowerCase();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			String p = token.getPalabra().toLowerCase();
			if(p.equals(palabra))
				return i;
		}
		return -1;
	}

	public int indiceLema(String lema)
	{
		lema = lema.toLowerCase();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			String l = token.getLema().toLowerCase();
			if(l.equals(lema))
				return i;
		}
		return -1;
	}

	public int indiceLema(String lema, int fromIndex)
	{
		lema = lema.toLowerCase();
		for(int i = fromIndex; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			String l = token.getLema().toLowerCase();
			if(l.equals(lema))
				return i;
		}
		return -1;
	}

	public int indiceDe(Token t)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			if(token.igual(t))
				return i;
		}
		return -1;
	}

	//Devuelve la Frase con los Tokens desde codigoInicio a codigoFin
	public Frase subFrase(int codigoInicio, int codigoFin)
	{
		//check the limits
		if((codigoInicio < this.getToken(0).getCodigo()) || (codigoFin > this.getToken(this.tamanyo()-1).getCodigo()))
			return null;

		Frase subfrase = new Frase();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token t = getToken(i);
			if(t.getCodigo() == codigoInicio)
			{
				//for(int j = i; j <= codigoFin; j++)
				for(int j = i; j < tamanyo(); j++)
				{
					t = getToken(j);
					subfrase.anyadir(t);
					if(t.getCodigo() == codigoFin)
						break;
				}
				return subfrase;
			}
		}
		return subfrase;
	}

	//Anyade una Frase a lo que ya tenga
	public void anyadir(Frase nueva)
	{
		for(int i = 0; i < nueva.tamanyo(); i++)
		{
			Token t = (nueva.getToken(i)).clone();
			//anyadir(i + tamanyo(), t); //anyadir al final
			anyadir(t);
		}
	}

	//Combina dos Frases sin Tokens repetidos
	public Frase combinar(Frase frase)
	{
		Frase fCombinada = (Frase)frase.clone();
		for(int i = 0; i < tamanyo(); i++)
		{
			Token t = (Token)getToken(i).clone();
			if(!fCombinada.esta(t))
			{
				fCombinada.anyadir(t);
			}
		}
		//ordenar por codigo
		fCombinada = fCombinada.ordenar(COD);
		return fCombinada;
	}

	public Token getToken(Token token)
	{
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Token t = this.getToken(i);
			if(t.igual(token))
				return (Token)t.clone();
		}
		return null; //token is not in this
	}

	public boolean esta(Token token)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Token t = getToken(i);
			if(t.igual(token))
				return true;
		}
		return false;
	}

	//ordena por codigo o frecuencia
	public Frase ordenar(String orden)
	{
		Frase ordenada = (Frase)this.clone();
		if(orden.equals(COD))
			return ordenarPorCodigo(ordenada, 0, tamanyo()-1);
		return ordenarPorFrecuencia(ordenada, 0, tamanyo()-1);
	}

	private Frase ordenarPorCodigo(Frase frase, int izq, int der)
	{
		Token tPivote = (Token)frase.getToken(izq).clone();
		int pivote = tPivote.getCodigo();
		int i = izq;
		int j = der;
		Token aux;

		while(i < j)
		{
			while(frase.getToken(i).getCodigo() <= pivote && i < j) i++;
			while(frase.getToken(j).getCodigo() > pivote) j--;
			if (i < j) //intercambiar i,j
			{
				aux = (Token)frase.eliminar(i).clone();
			        frase.anyadir(i, (Token)frase.eliminar(j).clone());
			        frase.anyadir(j, aux);
     			}
   		}
		Token tizq = (Token)frase.eliminar(izq);
		frase.anyadir(izq, (Token)frase.eliminar(j).clone());
		frase.anyadir(j, tPivote);
		if(izq < j-1)
			return ordenarPorCodigo(frase, izq, j-1);
   		if(j+1 < der)
      			return ordenarPorCodigo(frase, j+1, der);
		return frase;
	}

	private Frase ordenarPorFrecuencia(Frase frase, int izq, int der)
	{
		Token tPivote = (Token)frase.getToken(izq).clone();
		int pivote = tPivote.getFrecuencia();
		int i = izq;
		int j = der;
		Token aux;

		while(i < j)
		{
			while(frase.getToken(i).getFrecuencia() <= pivote && i < j) i++;
			while(frase.getToken(j).getFrecuencia() > pivote) j--;
			if (i < j) //intercambiar i,j
			{
				aux = (Token)frase.eliminar(i).clone();
			        frase.anyadir(i, (Token)frase.eliminar(j).clone());
			        frase.anyadir(j, aux);
     			}
   		}
		Token tizq = (Token)frase.eliminar(izq);
		frase.anyadir(izq, (Token)frase.eliminar(j).clone());
		frase.anyadir(j, tPivote);
		if(izq < j-1)
			return ordenarPorFrecuencia(frase, izq, j-1);
   		if(j+1 < der)
      			return ordenarPorFrecuencia(frase, j+1, der);
		return frase;
	}

	//Todos sus Token son iguales
	public boolean igual(Frase frase)
	{
		if(tamanyo() != frase.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frase.getToken(i).igual(getToken(i)))
				return false;
		}
		return true;
	}

	//Palabra, Pos, y Codigo son iguales en todos los Token
	public boolean igualPalabraPosCodigo(Frase frase)
	{
		if(tamanyo() != frase.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frase.getToken(i).igualPalabraPosCodigo(getToken(i)))
				return false;
		}
		return true;
	}

	//Palabra y Pos son iguales en todos los Token
	public boolean igualPalabraPos(Frase frase)
	{
		if(tamanyo() != frase.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frase.getToken(i).igualPalabraPos(getToken(i)))
				return false;
		}
		return true;
	}

	//Lema, Pos, y Codigo son iguales en todos los Token
	public boolean igualLemaPosCodigo(Frase frase)
	{
		if(tamanyo() != frase.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frase.getToken(i).igualLemaPosCodigo(getToken(i)))
				return false;
		}
		return true;
	}

	//Lema y Pos son iguales en todos los Token
	public boolean igualLemaPos(Frase frase)
	{
		if(tamanyo() != frase.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frase.getToken(i).igualLemaPos(getToken(i)))
				return false;
		}
		return true;
	}

	//veces que this aparece en el corpus
	public void setFrecuencia(Frase corpus)
	{
		//convertir Lemas de this y corpus a String
		String lemasFrase = this.lemasAstring();
		String lemasCorpus = corpus.lemasAstring();

		int cont = 0;
		int indice = lemasCorpus.indexOf(lemasFrase, 0);
		while(indice != -1)
		{
			cont++;
			indice = lemasCorpus.indexOf(lemasFrase, indice + 1);
		}
		this.setFrecuencia(cont);
	}

	//Devuelve un String con todos los Lemas, que no son <unknown>, separados por espacios y en minusculas (e e cummings)
	public String lemasAstring()
	{
		String lemas = "";
		for(int i = 0; i < this.tamanyo(); i++)
		{
			String lema = this.getToken(i).getLema();
			lemas = lemas + lema.toLowerCase() + " ";
		}
		return lemas.trim();
	}

	//Devuelve un String con todas las etiquetas POS separados por espacios
	public String posAstring()
	{
		String poss = "";
		for(int i = 0; i < this.tamanyo(); i++)
		{
			String pos = this.getToken(i).getPos();
			poss = poss + pos + " ";
		}
		return poss.trim();
	}

/**** antiguo: por palabras iguales
	//veces que this aparece en el corpus
	public void setFrecuencia(Frase corpus)
	{
		//convertir Palabras de this y corpus a String
		String palabrasFrase = this.palabrasAstring();
		String palabrasCorpus = corpus.palabrasAstring();

		int cont = 0;
		int indice = palabrasCorpus.indexOf(palabrasFrase, 0);
		while(indice != -1)
		{
			cont++;
			indice = palabrasCorpus.indexOf(palabrasFrase, indice + 1);
		}
		this.setFrecuencia(cont);
	}

	//Devuelve un String con todas las Palabras separadas por espacios y en minusculas (e e cummings)
	public String palabrasAstring()
	{
		String palabras = "";
		for(int i = 0; i < this.tamanyo(); i++)
		{
			String palabra = this.getToken(i).getPalabra();
			palabras = palabras + palabra.toLowerCase() + " ";
		}
		return palabras.trim();
	}
*/


	//Devuelve un String con todas las Palabras separadas por espacios
	public String wordsToString()
	{
		String palabras = "";
		for(int i = 0; i < this.tamanyo(); i++)
		{
			String palabra = this.getToken(i).getPalabra();
			palabras = palabras + palabra + " ";
		}
		return palabras.trim();
	}






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



	
	
	//convert Frase into SimpliFrase
	public SimpliFrase toSimpliFrase()
	{
		SimpliFrase sf = new SimpliFrase();
		sf.setWords(this.wordsToString());
		sf.setLemma(this.lemasAstring());
		sf.setTamanyo(this.tamanyo());
		sf.setFrecuencia(this.getFrecuencia());
		sf.setCvalue(this.getCvalue());
		sf.setT(this.getT());
		sf.setC(this.getC());
		return sf;
	}
	



	public String aString()
	{
		if(this == null)
			return "NULL";
		String frase = "[frec: " + this.getFrecuencia() + "]-[t: " + this.getT() + "]-[c: " + this.getC() + "]-[c-value: " + this.getCvalue() + "]\n";
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			frase += token.aString() + "\n";
		}
		return frase;
	}

	public String aStringLineal()
	{
		if(this == null)
			return "NULL";
		String frase = "";
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			frase += token.getPalabra() + " ";
		}
		/* mostrar el lema debajo de cada palabra
		frase += "\n";
		for(int i = 0; i < tamanyo(); i++)
		{
			Token token = getToken(i);
			frase += token.getLema() + " ";
		}
		*/
		frase += "\n" + "\t" + "* frecuency: " + this.getFrecuencia() + "\n" + "\t" + "* c-value: " + this.getCvalue() + "\n";
		return frase;
	}
	
}








class FraseComparatorSize implements Comparator<Frase>
{
	@Override
	//length in reverse order: from highest to lowest
	public int compare(Frase o1, Frase o2)
	{
		return Integer.compare(o2.tamanyo(), o1.tamanyo());
	}
}







class FraseComparatorCvalue implements Comparator<Frase>
{
	@Override
	//cvalue in reverse order: from highest to lowest
	public int compare(Frase o1, Frase o2)
	{
		return Double.compare(o2.getCvalue(), o1.getCvalue());
	}
}









































