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
 


/**************************
* Es la clase principal.
***************************/



package cutext.main;



import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;
import javax.swing.border.*;

import cutext.gui.*;
import cutext.prepro.*;





public class Principal implements Serializable
{
	
	private static final long serialVersionUID = -7149755349268484907L;

	DatosEntrada datos;
	DialogoEjecucion marco;
	Frases frases;

	public Principal(DialogoEjecucion marco, DatosEntrada de, Frases frases)
	{
		this.datos = de;
		this.marco = marco;
		this.frases = frases;

		//guardar en memoria los datos de entrada
		//ARDatosEntrada arde = new ARDatosEntrada("Guardar", DatosEntrada.N);
		//arde.guardar(datos);
	}

	public void cutext(String stringSalida, long tiempoInicial)
	{
		if(this.marco == null) //call without Thread
			new Cutext(marco, datos, frases, stringSalida, tiempoInicial); //en el constructor se llama a run()
		else
			new CutextGUI(marco, datos, frases, stringSalida, tiempoInicial); //en el constructor se llama a start()
	}




}
























