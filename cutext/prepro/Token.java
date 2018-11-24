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
*  atributos: la palabra, la etiqueta POS, el codigo, y la frecuencia
*******************************************************************************************/


package cutext.prepro;




import java.util.*;
import java.io.*;


public class Token implements Cloneable, Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	//private static final long serialVersionUID = 2921756743815702524L;
	
	String palabra;
	String pos;
	String lema;
	int codigo;
	int frecuencia;
	double peso;
	int offset;

	public Token clone()
	{
		Token t = null;
		try
		{
			t = (Token)super.clone();
		}
		catch(CloneNotSupportedException e)
		{
			e.printStackTrace(System.err);
		}
		//Hay que clonar las referencias:

		return t;
	}

	public Token()
	{
		inicializar();
	}

	public Token(String palabra, String pos, String lema, int codigo, int frecuencia, double peso, int offset)
	{
		inicializar();
		this.palabra = palabra;
		this.pos = pos;
		this.lema = lema;
		this.codigo = codigo;
		this.frecuencia = frecuencia;
		this.peso = peso;
		this.offset = offset;
	}

	/*=========================
		SET
	=========================*/

	public void setPalabra(String palabra)
	{
		this.palabra = palabra;
	}

	public void setPos(String pos)
	{
		this.pos = pos;
	}

	public void setLema(String lema)
	{
		this.lema = lema;
	}

	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}

	public void setFrecuencia(int frecuencia)
	{
		this.frecuencia = frecuencia;
	}

	public void setPeso(double peso)
	{
		this.peso = peso;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}


	/*=========================
		GET
	=========================*/

	public String getPalabra()
	{
		return palabra;
	}

	public String getPos()
	{
		return pos;
	}

	public String getLema()
	{
		return lema;
	}

	public int getCodigo()
	{
		return codigo;
	}

	public int getFrecuencia()
	{
		return frecuencia;
	}

	public double getPeso()
	{
		return peso;
	}

	public int getOffset()
	{
		return offset;
	}






	/*=========================
		Otros
	=========================*/


	public boolean igual(Token token)
	{
		int compararPesos = Double.compare(getPeso(), token.getPeso());

		return ((getPalabra().toLowerCase().equals(token.getPalabra().toLowerCase())) && 
			(getPos().equals(token.getPos())) && 
			(getLema().toLowerCase().equals(token.getLema().toLowerCase())) && 
			(getCodigo() == token.getCodigo()) && 
			(getFrecuencia() == token.getFrecuencia()) && 
			(compararPesos == 0) && 
			(getOffset() == token.getOffset())
			);

	}

	public boolean igualPalabraPosCodigo(Token token)
	{
		return ((getPalabra().toLowerCase().equals(token.getPalabra().toLowerCase())) && 
			(getPos().equals(token.getPos())) && 
			(getCodigo() == token.getCodigo()));
	}

	public boolean igualPalabraPos(Token token)
	{
		return ((getPalabra().toLowerCase().equals(token.getPalabra().toLowerCase())) && 
			(getPos().equals(token.getPos())));
	}

	public boolean igualLemaPosCodigo(Token token)
	{
		return ((getLema().toLowerCase().equals(token.getLema().toLowerCase())) && 
			(getPos().equals(token.getPos())) && 
			(getCodigo() == token.getCodigo()));
	}

	public boolean igualLemaPos(Token token)
	{
		return ((getLema().toLowerCase().equals(token.getLema().toLowerCase())) && 
			(getPos().equals(token.getPos())));
	}

	//Convierte a String
	public String aString()
	{
		if(this == null)
			return "NULL";

		String astring = palabra + " " + pos + " " + String.valueOf(codigo) + " " + String.valueOf(frecuencia) + " " + String.valueOf(peso) + " " + String.valueOf(offset);

		return astring;
	}

	public void inicializar()
	{
		palabra = null;
		pos = null;
		lema = null;
		codigo = -1;
		frecuencia = -1;
		peso = 0.0;
		offset = -1;
	}


}








