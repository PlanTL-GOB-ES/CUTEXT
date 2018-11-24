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
 


/*********************************************************************************************************
* Es un File con un atributo booleano que indica si se ha llegado, o no, al final del fichero.
**********************************************************************************************************/


package cutext.util;




import java.util.*;
import java.io.*;


public class FicheroLectura
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	File file;
	boolean fin;

	public FicheroLectura(File file)
	{
		this.file = file;
		fin = false;
	}
	
	public void setFin(boolean fin)
	{
		this.fin = fin;
	}

	public void setFile(File file)
	{
		this.file = file;
	}

	public boolean esFin()
	{
		return fin;
	}

	public File getFile()
	{
		return file;
	}
	
}


