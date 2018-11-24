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
 



/***************************************************************************************
 Un marco con una zona de texto, una barra de progreso y un botón para poder cancelar
****************************************************************************************/



package cutext.gui;



import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;
import javax.swing.border.*;



public class DialogoEjecucion extends JDialog
{
	private static final long serialVersionUID = -7149755349268484907L;
	
	public static final int ANCHURA_PREFIJADA = 700;
	public static final int ALTURA_PREFIJADA = 500;
	
	private boolean cancelar = false;
	private JTextArea zonaTexto;
	private JScrollPane laminaDesplazamiento;
	private JPanel laminaBotones;
	private JButton botonCancelar;
	private JProgressBar progreso;
	private int unidadProgreso;
	private int indiceProgreso = 0;
	private int progress = 0;
	private boolean progMayorQueUno = false;
	private int presionCancelar = 0;

	public DialogoEjecucion(JFrame parent)
	{
		// el último parámetro se pone a "false" para permitir la simultaneidad con Frame
		super(parent, "Running TermineSP...", false);
		setSize(ANCHURA_PREFIJADA, ALTURA_PREFIJADA);
		
		laminaBotones = new JPanel();
		
		// se añade una barra de progreso
		
		progreso = new JProgressBar(0, 100);
		progreso.setBorder(BorderFactory.createEmptyBorder());

		progreso.setStringPainted(true);

		laminaBotones.add(progreso);

		// se añade un botón "Cancelar", para poder parar la ejecución en cualquier momento

		botonCancelar = new JButton("Cancel");
		laminaBotones.add(botonCancelar);
		botonCancelar.addActionListener(new
			ActionListener()
			{
				public void actionPerformed(ActionEvent evento)
				{
					cancelar = true;
					presionCancelar++;
					if(presionCancelar == 1)
						botonCancelar.setText("Exit");
					
					if(presionCancelar > 1)
						dispose();
				}
			});

		add(laminaBotones, BorderLayout.SOUTH);
		
		// se añade una zona de texto con barras de desplazamiento

		zonaTexto = new JTextArea(8, 40);
		zonaTexto.setEditable(false);
		//poner el nombre de la fuente a "Monospaced" para que todos los caracteres y espacios
		//ocupen lo mismo
		zonaTexto.setFont(new Font("Monospaced", 0, 13));
		laminaDesplazamiento = new JScrollPane(zonaTexto);

		add(laminaDesplazamiento, BorderLayout.CENTER);

	}

	public void calcularUnidadProgreso(int frases)
	{
		double multiplicacion = (double)frases;
		double cociente = (double)(multiplicacion / 100.0);
		if(cociente >= 1.0)
		{
			unidadProgreso = (int)Math.ceil(cociente);
			progMayorQueUno = false;
		}
		else
		{
			unidadProgreso = (int)Math.floor((double)(1.0 / cociente));
			progMayorQueUno = true;
		}
	}

	public boolean estaCancelada()
	{
		return cancelar;
	}

	public void setTexto(String texto)
	{
		//zonaTexto.append("\n\n\t" + texto);
		zonaTexto.append(texto);
	}

	public void insertar(String texto)
	{
		zonaTexto.setText(texto);
	}

	public void reemplazar(String texto, int comienzo, int fin)
	{
		zonaTexto.replaceRange(texto, comienzo, fin);
	}



	public void actualizarProgreso()
	{
		if(!progMayorQueUno)
		{
			indiceProgreso++;
			if(indiceProgreso == unidadProgreso)
			{
				progress++;
				indiceProgreso = 0;
				progreso.setValue(progress);
			}
		}
		else
		{
			indiceProgreso += unidadProgreso;
			progreso.setValue(indiceProgreso);
		}
	}

	public void finalizar()
	{
		if(estaCancelada())
			setTitle("End of TermineSP: Canceled by the user");
		else
			setTitle("End of TermineSP");
		botonCancelar.setText("Exit");
		presionCancelar = 2;
		// si no ha sido cancelada Y no ha llegado al máximo, poner la barra de progreso al máximo
		if((!estaCancelada()) && (progreso.getValue() != progreso.getMaximum()))
			progreso.setValue(progreso.getMaximum());

		//Poner el texto final en blanco con fondo negro
		zonaTexto.setBackground(Color.black);
		zonaTexto.setForeground(Color.white);
	}

}

