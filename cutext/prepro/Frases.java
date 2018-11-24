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
* Es un ArrayList del objeto Frase
******************************************************************************************/



package cutext.prepro;



import java.util.*;
import java.io.*;




public class Frases implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	ArrayList frases = new ArrayList();
	int maximaLongitud;
	int frecuencia;
	double cvalue;



	public Frases clone()
	{
		Frases f = null;
		try
		{
			f = (Frases)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		f.frases = (ArrayList)f.frases.clone();

		return f;
	}




	public Frases()
	{
		maximaLongitud = 0;
		frecuencia = -1;
		cvalue = 0.0;
	}



	public void anyadir(Frase f)
	{
		frases.add(f);
	}

	public void anyadir(int i, Frase f)
	{
		frases.add(i, f);
	}

	public Frase getFrase(int indice)
	{
		return (Frase)frases.get(indice);
	}

	public int tamanyo()
	{
		return frases.size();
	}

	public boolean estaVacio()
	{
		return frases.isEmpty();
	}	

	public Frase eliminar(int elemento)
	{
		return (Frase)frases.remove(elemento);
	}	

	public void limpiar()
	{
		frases.clear();
	}


	/*=========================
		SET
	=========================*/

	public void setMaximaLongitud(int ml)
	{
		this.maximaLongitud = ml;
	}

	public void setFrecuencia(int frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	public void setCvalue(double cvalue)
	{
		this.cvalue = cvalue;
	}



	/*=========================
		GET
	=========================*/

	public int getMaximaLongitud()
	{
		return maximaLongitud;
	}

	public int getFrecuencia()
	{
		return frecuencia;
	}

	public double getCvalue()
	{
		return cvalue;
	}








	/*=========================
		Otros
	=========================*/

	//Devuelve la Frase que coincida (en lema, pos, codigo) con la que se le pasa como argumento
	public Frase getFrase(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			//if(frase.igualPalabraPosCodigo(getFrase(i)))
			if(frase.igualLemaPosCodigo(getFrase(i)))
				return (Frase)getFrase(i).clone();
		}
		return null; //ninguna coincide
	}

	//Todas sus Frase son iguales
	public boolean igual(Frases frases)
	{
		if(tamanyo() != frases.tamanyo())
			return false;
		for(int i = 0; i < tamanyo(); i++)
		{
			if(!frases.getFrase(i).igual(getFrase(i)))
				return false;
		}
		return true;
	}


	//Busca en los lemas de las Frases para cada lema de la Frase
	public void igualarFrecuencias(Frase palabras)
	{
		for(int i = 0; i < palabras.tamanyo(); i++)
		{
			Token tokenP = palabras.getToken(i);
			//String pal = tokenP.getPalabra().toLowerCase();
			String lema = tokenP.getLema().toLowerCase();
			for(int j = 0; j < tamanyo(); j++)
			{
				Frase frase = getFrase(j);
				for(int k = 0; k < frase.tamanyo(); k++)
				{
					Token t = frase.getToken(k);
					//if((t.getPalabra().toLowerCase()).equals(pal))
					if((t.getLema().toLowerCase()).equals(lema))
					{
						t.setFrecuencia(tokenP.getFrecuencia());
					}
				}
			}
		}
	}

	//Fusiona todas las Frases en una sola Frase
	public Frase fusion()
	{
		Frase fusion = new Frase();
		int codigo = 0;
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase frase = getFrase(i);
			for(int j = 0; j < frase.tamanyo(); j++)
			{
				Token token = frase.getToken(j);
				Token t = token.clone();
				t.setCodigo(codigo++);
				fusion.anyadir(t);
			}
		}
		return fusion;
	}

	//Combina dos Frases sin Frase repetidas
	public Frases combinar(Frases frases)
	{
//System.out.println("combinar\n\nthis\n" + this.aString() + "\nfrases\n" + frases.aString());
		Frases fCombinada = (Frases)frases.clone();
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = (Frase)getFrase(i).clone();
//System.out.println("f\n" + f.aString());
			//if(!fCombinada.estaPalabraPosCodigo(f))
			if(!fCombinada.estaLemaPosCodigo(f))
			{
				fCombinada.anyadir(f);
//System.out.println("anyado f\n" + f.aString() + "\nfCombinada\n" + fCombinada.aString());
			}
		}
		//ordenar por codigo
		//fCombinada = fCombinada.ordenar(COD);
		return fCombinada;
	}

	public boolean estaPalabraPosCodigo(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			if(f.igualPalabraPosCodigo(frase))
				return true;
		}
		return false;
	}

	public boolean estaPalabraPos(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			if(f.igualPalabraPos(frase))
				return true;
		}
		return false;
	}

	public boolean estaLemaPosCodigo(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			if(f.igualLemaPosCodigo(frase))
				return true;
		}
		return false;
	}

	public boolean estaLemaPos(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			if(f.igualLemaPos(frase))
				return true;
		}
		return false;
	}

	public boolean esta(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			if(f.igual(frase))
				return true;
		}
		return false;
	}


	//Extrae las Frases que satisfacen el umbral de frecuencias
	public Frases getUmbral()
	{
		//recuperar DatosEntrada para el umbralFrecuencia
		ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		DatosEntrada de = arde.recuperar();

		Frases frases = new Frases();
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Frase f = (Frase)this.getFrase(i).clone();
			//if(f.getFrecuencia() >= Estaticos.UMBRAL_FRECUENCIA)
			if(f.getFrecuencia() >= de.getUmbralFrecuencia())
				frases.anyadir(f);
		}
		return frases;
	}


	//Ordena las Frases de mayor a menor longitud
	public Frases ordenar(Frases frases, int izq, int der)
	{
		Frases sentencesSorted = (Frases)frases.clone();
		Collections.sort(sentencesSorted.frases, new FraseComparatorSize());
		return sentencesSorted;
	}

	public Frases ordenarCvalue(Frases frasesOriginal, int izq, int der)
	{
		Frases sentencesSorted = (Frases)frasesOriginal.clone();
		Collections.sort(sentencesSorted.frases, new FraseComparatorCvalue());
		return sentencesSorted;
	}



	public Frases getSubterminos(Frase frase)
	{
		Frases subterminos = new Frases();
		//String fraseTermino = frase.palabrasAstring();
		String fraseTermino = frase.lemasAstring();
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Frase f = this.getFrase(i);
			//String fTermino = f.palabrasAstring();
			String fTermino = f.lemasAstring();
			int indice = fraseTermino.indexOf(fTermino, 0);
			if((!fraseTermino.equals(fTermino)) && (indice != -1)) //es subtermino, anyadirlo a la lista
			{
				//subterminos.anyadir((Frase)f.clone());
				subterminos.anyadir(f);
			}
		}
		return subterminos;
	}

	public Frases getSuperterminos(Frase frase)
	{
		Frases superterminos = new Frases();
		//String fraseTermino = frase.palabrasAstring();
		String fraseTermino = frase.lemasAstring();
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Frase f = this.getFrase(i);
			//String fTermino = f.palabrasAstring();
			String fTermino = f.lemasAstring();
			int indice = fTermino.indexOf(fraseTermino, 0);
			if((!fraseTermino.equals(fTermino)) && (indice != -1)) //es supertermino, anyadirlo a la lista
			{
				//superterminos.anyadir((Frase)f.clone());
				superterminos.anyadir(f);
			}
		}
		return superterminos;
	}




	//Devuelve un conjunto de Frases que son iguales a la que se le pasa como argumento
	public Frases getGrupoTermino(Frase frase)
	{
		Frases grupo = new Frases();
		//grupo.anyadir((Frase)frase.clone());

		for(int i = 0; i < tamanyo(); i++)
		{
			Frase f = getFrase(i);
			//if(f.igualPalabraPos(frase))
			if(f.igualLemaPos(frase))
				grupo.anyadir((Frase)f.clone());
		}
		return grupo;
	}

	//Asigna a todas las Frase la frecuencia frec
	public void setFrecuencias(int frec)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase frase = getFrase(i);
			frase.setFrecuencia(frec);
		}
	}

	//No tiene contenido
	public boolean vacias()
	{
		return this.tamanyo() == 0;
	}





	public String aString()
	{
		if(this == null)
			return "NULL";
		String frases = "";
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase frase = getFrase(i);
			frases += frase.aString() + "\n";
		}
		return frases;
	}

	public String aStringLineal()
	{
		if(this == null)
			return "NULL";
		String frases = "";
		for(int i = 0; i < tamanyo(); i++)
		{
			Frase frase = getFrase(i);
			frases += frase.aStringLineal() + "\n";
		}
		return frases;
	}
	

	
}

























































