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
* Es un ArrayList del objeto Frases: cada elemento es un conjunto de terminos iguales
******************************************************************************************/



package cutext.prepro;



import java.util.*;
import java.io.*;

import cutext.util.*;




public class ArrayFrases implements Cloneable, Serializable
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	
	ArrayList array = new ArrayList();


	public ArrayFrases clone()
	{
		ArrayFrases f = null;
		try
		{
			f = (ArrayFrases)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:
		f.array = (ArrayList)f.array.clone();

		return f;
	}




	public ArrayFrases()
	{
		
	}



	public void anyadir(Frases f)
	{
		array.add(f);
	}

	public void anyadir(int i, Frases f)
	{
		array.add(i, f);
	}

	public Frases getFrases(int indice)
	{
		return (Frases)array.get(indice);
	}

	public int tamanyo()
	{
		return array.size();
	}

	public boolean estaVacio()
	{
		return array.isEmpty();
	}	

	public Frases eliminar(int elemento)
	{
		return (Frases)array.remove(elemento);
	}	

	public void limpiar()
	{
		array.clear();
	}







	//Agrupa por terminos iguales
	public void crear(Frases frases)
	{
		for(int i = 0; i < frases.tamanyo(); i++)
		{
			Frase f = frases.getFrase(i);
			Frases grupoTermino = frases.getGrupoTermino(f);
			if(!esta(grupoTermino))
				anyadir(grupoTermino);
		}
	}

	public boolean esta(Frases frases)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases f = getFrases(i);
			if(f.igual(frases))
				return true;
		}
		return false;
	}

	public void setFrecuencias()
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases frases = getFrases(i);
			frases.setFrecuencia(frases.tamanyo());
			frases.setFrecuencias(frases.tamanyo());
		}
	}

	//Extrae las Frases que satisfacen el umbral de frecuencias
	public ArrayFrases getUmbral(DatosEntrada de)
	{
		//recuperar DatosEntrada y asignar el umbralFrecuencia
		//ARDatosEntrada arde = new ARDatosEntrada("Recuperar", DatosEntrada.N);
		//DatosEntrada de = arde.recuperar();

		ArrayFrases array = new ArrayFrases();
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Frases frases = (Frases)this.getFrases(i).clone();
			//if(frases.getFrecuencia() >= Estaticos.UMBRAL_FRECUENCIA)
			if(frases.getFrecuencia() >= de.getUmbralFrecuencia())
				array.anyadir(frases);
		}
		return array;
	}

	//Devuelve una por cada grupo
	public Frases contraer()
	{
		Frases frases = new Frases();
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases f = getFrases(i);
			//if(!frases.estaLemaPos(f.getFrase(0)))
			//	frases.anyadir((Frase)f.getFrase(0).clone());
			frases.anyadir((Frase)f.getFrase(0).clone());
		}
		return frases;
	}

	//A cada grupo y a cada miembro del mismo grupo le asigna los mismos atributos (maximaLongitud, frecuencia, cvalue)
	public ArrayFrases expandir(Frases frases)
	{
		ArrayFrases af = new ArrayFrases();
		for(int i = 0; i < frases.tamanyo(); i++)
		{
			Frase f = frases.getFrase(i);
			Frases grupo = getGrupoTermino(f);
			if(grupo != null)
				af.actualizar((Frases)grupo.clone(), f, frases.getMaximaLongitud());
		}
		return af;
	}

	//Devuelve el grupo que es igual a la Frase que se le pasa como argumento
	public Frases getGrupoTermino(Frase frase)
	{
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases f = getFrases(i);
			//if(f.getFrase(0).igualPalabraPos(frase))
			if(f.getFrase(0).igualLemaPos(frase))
				return f;
		}
		return null;
	}

	public void actualizar(Frases grupo, Frase frase, int ml)
	{
		//fijar atributos de Frases
		grupo.setMaximaLongitud(ml);
		grupo.setFrecuencia(frase.getFrecuencia());
		grupo.setCvalue(frase.getCvalue());
		for(int i = 0; i < grupo.tamanyo(); i++)
		{
			Frase f = grupo.getFrase(i);
			//fijar atributos de cada Frase
			f.setLongitud(frase.getLongitud());
			f.setFrecuencia(frase.getFrecuencia());
			f.setCvalue(frase.getCvalue());
			f.setT(frase.getT());
			f.setC(frase.getC());
		}
		anyadir(grupo);
	}

	public ArrayFrases eliminarDuplicados()
	{
		ArrayFrases af = new ArrayFrases();
		ListaIndices indices = new ListaIndices();
		for(int i = 0; i < this.tamanyo(); i++)
		{
			Frases frases = this.getFrases(i);
			if(indices.esta(i)) //ya se ha anyadido
				continue;
			ListaIndices lista = this.getIndicesDe(i);
			//af.anyadir(this, lista);
			Frases f = this.getMejor(lista);
			af.anyadir((Frases)f.clone());
			indices.anyadir(lista);
		}
		return af;
	}

	public ListaIndices getIndicesDe(int indice)
	{
		Frase fraseAbuscar = this.getFrases(indice).getFrase(0);
		ListaIndices indices = new ListaIndices();
		indices.anyadir(new Indice(indice));
		for(int i = indice+1; i < this.tamanyo(); i++)
		{
			Frase frase = this.getFrases(i).getFrase(0);
			if(fraseAbuscar.igualLemaPos(frase))
				indices.anyadir(new Indice(i));
		}
		return indices;
	}

	public Frases getMejor(ListaIndices lista)
	{
		Frase fMejor = (Frase)this.getFrases(lista.getIndice(0).getIndice()).getFrase(0).clone();
		int indiceMejor = lista.getIndice(0).getIndice();
		for(int i = 0; i < lista.tamanyo(); i++)
		{
			Frase fActual = (Frase)this.getFrases(lista.getIndice(i).getIndice()).getFrase(0).clone();
			if(fActual.getFrecuencia() >= fMejor.getFrecuencia())
			{
				fMejor = (Frase)fActual.clone();
				indiceMejor = lista.getIndice(i).getIndice();
			}
		}
		return (Frases)this.getFrases(indiceMejor).clone();
	}









	public String aString()
	{
		if(this == null)
			return "NULL";
		String array = "";
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases frases = getFrases(i);
			array += "======= #" + i + " ===========\n" + frases.aString() + "\n";
		}
		return array;
	}

	public String aStringLineal()
	{
		if(this == null)
			return "NULL";
		String array = "";
		for(int i = 0; i < tamanyo(); i++)
		{
			Frases frases = getFrases(i);
			array += "======= #" + i + " ===========\n" + frases.aStringLineal() + "\n";
		}
		return array;
	}
	

	
}













