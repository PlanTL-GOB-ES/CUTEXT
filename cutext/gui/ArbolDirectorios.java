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
 


/********************************************************************************************
* Genera el arbol de directorios del sistema de ficheros del usuario.
********************************************************************************************/


package cutext.gui;

import cutext.util.*;


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;
import java.util.*;
import javax.swing.border.*;








public class ArbolDirectorios extends JDialog implements TreeSelectionListener, TreeExpansionListener
{
	
	private static final long serialVersionUID = -7149755349268484907L;
	
	private JScrollPane scrlDir;
	private JScrollPane scrlFiles;
	private JTree treeDir;
	private DefaultListModel mdlFiles;
	private JList lstFiles;
	private JButton aceptar;
	private JButton cancelar;
	private JTextField jtf;
	private String stringValueChanged;
	private ArrayList ejlist;
	private Object listaObjetos[];

	public ArbolDirectorios(ArrayList ejlist, JTextField tf, JFrame parent)
	{
		super(parent, "Select the directory", true);
		jtf = tf;
		this.ejlist = ejlist;

		this.setLayout(new BorderLayout());

		File f = new File(Estaticos.FILE_SEP);
		DefaultMutableTreeNode top = new DefaultMutableTreeNode(f);

		populateNode(top, f);
		
		treeDir = new JTree(top);
		treeDir.getSelectionModel().addTreeSelectionListener(this);
		treeDir.addTreeExpansionListener(this);
		scrlDir = new JScrollPane(treeDir);
		scrlDir.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		mdlFiles = new DefaultListModel();
		lstFiles = new JList(mdlFiles);
		scrlFiles = new JScrollPane(lstFiles);
		scrlFiles.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		aceptar = new JButton("OK");
		cancelar = new JButton("Cancel");

		//Add components
		Container cp = getContentPane();
		cp.setLayout(null);

		scrlDir.setBounds(0, 0, 200, 200); //x, y, ancho, alto
		scrlFiles.setBounds(0+200+1, 0, 200, 200); //x, y, ancho, alto
		aceptar.setBounds(40, 290, 100, 30); //x, y, ancho, alto
		cancelar.setBounds(40+100+30, 290, 100, 30); //x, y, ancho, alto

		//Add to container
		cp.add(scrlDir);
		cp.add(scrlFiles);
		cp.add(aceptar);
		cp.add(cancelar);

		//Registrar Oyentes
		aceptar.addActionListener(aLaceptar);
		cancelar.addActionListener(aLcancelar);
		lstFiles.addListSelectionListener(lsLlstFiles);

		this.setSize(450, 400); //ancho, alto
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setLocationByPlatform(true);
		this.setVisible(true);
	}

	private boolean populateNode(DefaultMutableTreeNode node, File f)
	{
		node.removeAllChildren();
		return populateNode(node, f, 2);
	}

	private boolean populateNode(DefaultMutableTreeNode node, File f, int depth)
	{
		File files[] = f.listFiles(new FileFilter()
				{
					public boolean accept(File pathname)
					{
						return pathname.isDirectory();
					}
				});

		if(files != null && depth > 0)
		{
			for(int i = 0; i < files.length; i++)
			{
				DefaultMutableTreeNode curr = new 
					DefaultMutableTreeNode(files[i].getName());
				populateNode(curr, files[i], depth - 1);
				node.add(curr);
			}
		}
		return true;
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		TreePath path = e.getPath();

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
					treeDir.getLastSelectedPathComponent();
		if(node == null)
			return;

		Object array[] = path.getPath();
		stringValueChanged = new String();
		stringValueChanged = array[0].toString();
		for(int i = 1; i < array.length; i++)
		{
			if(i == (array.length - 1))
				stringValueChanged = stringValueChanged + array[i].toString();
			else
				stringValueChanged = stringValueChanged + array[i].toString() + Estaticos.FILE_SEP;
		}

		File f = new File(stringValueChanged);
		File files[] = f.listFiles(new FileFilter()
				{
					public boolean accept(File pathname)
					{
						return pathname.isFile();
					}
				});
		mdlFiles.removeAllElements();
		if(files != null)
		{
			for(int i = 0; i < files.length; i++)
			{
				mdlFiles.addElement(files[i].getName());
			}
		}
	}

	public void treeExpanded(TreeExpansionEvent event)
	{
		TreePath path = event.getPath();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

		if(node == null)
			return;
		
		setCursor(new Cursor(Cursor.WAIT_CURSOR));

		Object array[] = path.getPath();
		String fConBarra = array[0].toString();
		for(int i = 1; i < array.length; i++)
		{
			if(i == (array.length - 1))
				fConBarra = fConBarra + array[i].toString();
			else
				fConBarra = fConBarra + array[i].toString() + Estaticos.FILE_SEP;
		}
		
		File f = new File(fConBarra);
		populateNode(node, f);

		JTree tree = (JTree)event.getSource();
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		model.nodeStructureChanged(node);

		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void treeCollapsed(TreeExpansionEvent event)
	{
	}

	//Eventos
	ActionListener aLaceptar = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			if(treeDir.getLastSelectedPathComponent() == null)
				JOptionPane.showMessageDialog(null, 
						"You must select a directory", 
						"Error", 
						JOptionPane.ERROR_MESSAGE);
			else
			{
				JOptionPane.showMessageDialog(null, 
						treeDir.getLastSelectedPathComponent().toString(), 
						"Directory", 
						JOptionPane.INFORMATION_MESSAGE);
				jtf.setText(stringValueChanged);
				if(listaObjetos != null)
				{
					for(int i = 0; i < listaObjetos.length; i++)
						ejlist.add((String)listaObjetos[i]);
				}
				dispose();
			}
		}
	};

	ActionListener aLcancelar = new ActionListener()
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	};

	ListSelectionListener lsLlstFiles = new ListSelectionListener()
	{
		public void valueChanged(ListSelectionEvent e)
		{
			if(ejlist != null)
				listaObjetos = lstFiles.getSelectedValues();
		}
	};


	
}






