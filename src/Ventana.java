
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComponent;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.io.BufferedReader;
import java.util.ArrayList;
import javax.swing.JLabel;

public class Ventana extends JFrame {
	
	private JFrame instance;
	private JPanel contentPane;
	private int pacman_x, pacman_y;
	private int speedPacman = 25;
	private Color colorPared;
	private Color colorComida;
	public Element pacman,pared1;
	private int puntaje;
	private Graphics graphic;
	private ArrayList<Element> paredes;
	private ArrayList<CircleElement> comidas;
	private JLabel puntajeLabel;

	/**
	 * Create the frame.
	 */
	public Ventana() {
		instance = this;
		puntaje = 0;
		colorComida = Color.white;
		colorPared = Color.decode("#4B87FE");
		pacman_x = 50;
		pacman_y = 50;
		
		// Paredes
		paredes = new ArrayList<Element>();
		paredes.add(new Element(20, 20, 220, 30,colorPared));
		paredes.add(new Element(692, 20, 220, 30, colorPared));
		paredes.add(new Element(20,20,30,680,colorPared));
		paredes.add(new Element(20,240,30,155,colorPared));
		paredes.add(new Element(566,240,30,156,colorPared));
		paredes.add(new Element(20,240,60,155,colorPared));
		paredes.add(new Element(565,240,60,157,colorPared));
		paredes.add(new Element(90,90,30,85,colorPared));
		paredes.add(new Element(216,90,30,134,colorPared));
		paredes.add(new Element(390,90,30,134,colorPared));
		paredes.add(new Element(565,90,30,85,colorPared));
		paredes.add(new Element(90,170,30,85,colorPared));
		paredes.add(new Element(565,170,30,85,colorPared));
		paredes.add(new Element(216,170,130,34,colorPared));
		paredes.add(new Element(490,170,130,34,colorPared));
		paredes.add(new Element(290,170,30,160,colorPared));
		paredes.add(new Element(215,340,30,310,colorPared));
		paredes.add(new Element(20,340,30,155,colorPared));
		paredes.add(new Element(565,340,30,157,colorPared));
		paredes.add(new Element(90,420,30,85,colorPared));
		paredes.add(new Element(565,420,30,85,colorPared));
		paredes.add(new Element(215,420,150,34,colorPared));
		paredes.add(new Element(490,420,150,34,colorPared));
		paredes.add(new Element(292,370,150,34,colorPared));
		paredes.add(new Element(412,370,150,34,colorPared));
		paredes.add(new Element(524,490,30,126,colorPared));
		paredes.add(new Element(90,490,30,126,colorPared));
		paredes.add(new Element(20,370,200,30,colorPared));
		paredes.add(new Element(692,370,200,30,colorPared));
		paredes.add(new Element(20,570,30,702,colorPared));
		
		// Comidas
		/*
		 * Aclaracion: las comidas se leen desde un archivo porque utilice 
		 * un MouseListener para agregar las posiciones de las comidas, es
		 * por eso que se ven desalineadas.
		*/
		rellenarComidas();
		setFocusable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 770, 720);
		setResizable(true);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel BotPanel = new JPanel();
		BotPanel.setBorder(new EmptyBorder(5, 100, 5, 98));
		BotPanel.setBackground(new Color(255, 255, 0));
		contentPane.add(BotPanel, BorderLayout.SOUTH);
		BotPanel.setLayout(new BorderLayout(0, 0));
		
		JButton reiniciarButton = new JButton("REINICIAR");
		reiniciarButton.setFont(new Font("Tahoma", Font.PLAIN, 25));
		reiniciarButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				rellenarComidas();
				restart();
				instance.requestFocus();
				repaint();
			}
		});
		BotPanel.add(reiniciarButton, BorderLayout.EAST);
		
		puntajeLabel = new JLabel("0");
		puntajeLabel.setFont(new Font("Microsoft JhengHei UI Light", Font.BOLD, 30));
		BotPanel.add(puntajeLabel, BorderLayout.WEST);
		
		addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				int xActual = pacman.x;
				int yActual = pacman.y;
				switch(e.getKeyCode()) {
					case 87:
						pacman.y -= speedPacman;
						break;
					case 83:
						pacman.y += speedPacman;
						break;
					case 65:
						pacman.x -= speedPacman;
						break;
					case 68:
						pacman.x += speedPacman;
						break;
				}
				if(pacman.tocando(paredes)) {
					pacman.x = xActual;
					pacman.y = yActual;
				}
				CircleElement element = pacman.tocandoComida(comidas);
				if(element != null) {
					element.comer(graphic,comidas);
					System.out.println("+" + puntaje);
					puntajeLabel.setText("" + puntaje);
				}
				if(pacman.x == -50 && pacman.y == 300) {
					pacman.x = 725;
					pacman.y = 300;
				}
				if(pacman.x == 750 && pacman.y == 300) {
					pacman.x = -25;
					pacman.y = 300;
				}
				System.out.println("X: " + pacman.x + " / Y: " + pacman.y);
				repaint();
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		restart();
		MyGraphics tablero = new MyGraphics();
		getContentPane().add(tablero, BorderLayout.CENTER);
	}
	
	public void restart() {
		pacman = new Element(pacman_x, pacman_y, 40, 40,Color.yellow);
		
		puntaje = 0;
		puntajeLabel.setText("" + puntaje);
	}
	
	public void rellenarComidas() {
		comidas = new ArrayList<CircleElement>();
		InputStream is = this.getClass().getResourceAsStream("resources/comidasCoords.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			String l = br.readLine();
			while(l != null) {
				String[] split = l.split(",");
				int x = Integer.parseInt(split[0]);
				int y = Integer.parseInt(split[1]);
				comidas.add(new CircleElement(x,y,10,10,colorComida));
				l = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public class MyGraphics extends JComponent {

        private static final long serialVersionUID = 1L;

        MyGraphics() {
            setPreferredSize(new Dimension(500, 100));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.black);
            g.fillRect(0,0,getWidth(),getHeight());
            
            g.setColor(pacman.c);
            g.fillArc(pacman.x, pacman.y, pacman.w, pacman.h, 0, 360);
            
            for(Element e : paredes) {
            	g.setColor(e.c);
                g.fillRect(e.x, e.y, e.w, e.h);
            }
            
            for(CircleElement e : comidas) {
            	g.setColor(Color.white);
            	g.fillOval(e.x, e.y, e.w, e.h);
            }
            
            graphic = g;
        }
	}
	
	public class Element {
		
		int x,y,h,w;
		Color c;
		
		public Element(int x, int y, int h, int w, Color c) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.c = c;
		}
		
		public boolean tocando(ArrayList<Element> paredes) {
			for(Element e : paredes) {
				if((this.x < e.x + e.w) &&
						(this.x + this.w > e.x) &&
						(this.y < e.y + e.h) &&
						(this.y + this.h > e.y)) {
						return true;
				}
			}
			return false;
		}
		
		public CircleElement tocandoComida(ArrayList<CircleElement> comidas) {
			for(CircleElement c : comidas) {
				if((this.x < c.x + c.w) &&
						(this.x + this.w > c.x) &&
						(this.y < c.y + c.h) &&
						(this.y + this.h > c.y)) {
						return c;
				}
			}
			return null;
		}
		
	}
	
	public class CircleElement {
		int x,y,h,w;
		Color c;
		
		public CircleElement(int x, int y, int h, int w, Color c) {
			this.x = x;
			this.y = y;
			this.h = h;
			this.w = w;
			this.c = c;
		}
		
		public void comer(Graphics g,ArrayList<CircleElement> comidas) {
			comidas.remove(this);
			g.setColor(Color.black);
			g.fillRect(this.x, this.y, this.w, this.h);
			puntaje+=5;
		}
	}

}
