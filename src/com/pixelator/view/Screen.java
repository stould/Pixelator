package com.pixelator.view;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.LineBorder;

import com.pixelator.draws.CanPaint;
import com.pixelator.draws.Circle;
import com.pixelator.draws.Eraser;
import com.pixelator.draws.Line;
import com.pixelator.draws.Pen;
import com.pixelator.draws.Polyline;
import com.pixelator.draws.Square;
import com.pixelator.io.FileGenerator;
import com.pixelator.io.Image;
import com.pixelator.utils.DrawStack;
import com.pixelator.vk.UndoHotKey;

public class Screen {

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Screen window = new Screen();
					window.frmDesenhos.setVisible(true);
				} catch (Exception e) {
					JOptionPane.showMessageDialog(null, "There was a Problem Initializing the Application");
				}
			}
		});
	}

	private JFrame frmDesenhos;
	private DrawStack drawStack;
	private Point p1 = new Point(0, 0);
	private Point p2 = new Point(0, 0);
	private SelectedForm typeDraw = SelectedForm.FREE;
	private JLabel currentCordinatesLabel;
	private Panel panel = new Panel(800, 600);
	private Color color = Color.BLACK;
	private JColorChooser colorChooser;
	private JPanel jPColor;
	private Polyline poly;
	private Circle circle = new Circle(p1, p2, color);
	private Square square = new Square(p1, p2, color);
	private Line line = new Line(p1, p2, color);
	private Eraser eraser = new Eraser(p1, 15, color);
	private CanPaint canPaint = new CanPaint(p1, color, color);
	private Pen pen = new Pen(p1, p2, color);

	/**
	 * Create the application.
	 */
	public Screen() {
		initialize();
		drawStack = new DrawStack();
		colorChooser = new JColorChooser();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmDesenhos = new JFrame();
		frmDesenhos.setIconImage(Toolkit.getDefaultToolkit().getImage(Screen.class.getResource("/com/sun/java/swing/plaf/motif/icons/DesktopIcon.gif")));
		frmDesenhos.setTitle("Desenhos");
		frmDesenhos.setBounds(100, 100, 980, 725);
		frmDesenhos.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmDesenhos.getContentPane().setLayout(null);
		panel.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == 17) {
					UndoHotKey.ctrol = true;
				}
				if (arg0.getKeyCode() == 90) {
					UndoHotKey.z = true;
				}
				if (UndoHotKey.ctrol & UndoHotKey.z) {
					drawStack.pop();
					if (!drawStack.isEmpty()) {
						panel.setBuffer(drawStack.peek());
						panel.repaint();
					} else {
						panel.setBuffer(panel.getDefault());
						panel.repaint();
					}
					if (SelectedForm.POLYLINE == typeDraw && poly.getN() > 0) {
						poly.removeLast();
						if (poly.getN() == 1)
							poly.removeLast();
					}
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 90) {
					UndoHotKey.z = false;
				} else if (e.getKeyCode() == 17) {
					UndoHotKey.ctrol = false;
				}
			}
		});
		panel.setBounds(146, 40, 800, 600);
		panel.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent arg0) {
				updateCoordinates(arg0);
			}

			@Override
			public void mouseDragged(MouseEvent arg0) {
				updateCoordinates(arg0);
				if (typeDraw == SelectedForm.ERASER) {
					eraser.setP1(arg0.getX(), arg0.getY());
					eraser.setSize(Eraser.DEFAULT_SIZE);
					panel.draw(typeDraw, eraser);
				} else if (typeDraw == SelectedForm.LINE) {
					Panel.dragging = true;
					line.setP1(p1.x, p1.y);
					line.setP2(arg0.getX(), arg0.getY());
					line.setColor(color);
					panel.tempDraw(SelectedForm.LINE, line);
				} else if (typeDraw == SelectedForm.SQUARE) {
					Panel.dragging = true;
					square.setP1(p1.x, p1.y);
					square.setP2(arg0.getX(), arg0.getY());
					square.setColor(color);
					panel.tempDraw(SelectedForm.SQUARE, square);
				} else if (typeDraw == SelectedForm.CIRCLE) {
					Panel.dragging = true;
					circle.setP1(p1.x, p1.y);
					circle.setP2(arg0.getX(), arg0.getY());
					circle.setColor(color);
					panel.tempDraw(SelectedForm.CIRCLE, circle);
				} else if (typeDraw == SelectedForm.POLYLINE) {
					Panel.dragging = true;
					poly.setActualX(arg0.getX());
					poly.setActualY(arg0.getY());
					panel.tempDraw(SelectedForm.POLYLINE, poly);
				} else if (typeDraw == SelectedForm.PEN) {
					pen.setP1(p2.x, p2.y);
					pen.setP2(arg0.getX(), arg0.getY());
					pen.setColor(color);
					panel.draw(typeDraw, pen);
				}
			}
		});

		jPColor = new JPanel();
		jPColor.setBackground(Color.BLACK);
		jPColor.setBorder(new LineBorder(new Color(0, 0, 0)));
		jPColor.setBounds(121, 308, 15, 18);
		frmDesenhos.getContentPane().add(jPColor);
		frmDesenhos.getContentPane().add(panel);

		final JButton cleanButton = new JButton("Clean");
		cleanButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				drawStack.clearForms();
				panel.clear();
				if (poly != null)
					poly.clear();
			}
		});
		cleanButton.setBounds(22, 40, 89, 23);
		frmDesenhos.getContentPane().add(cleanButton);

		final JButton lineButton = new JButton("Line");
		lineButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				typeDraw = SelectedForm.LINE;
			}
		});
		lineButton.setBounds(22, 71, 89, 23);
		frmDesenhos.getContentPane().add(lineButton);

		final JButton squareButton = new JButton("Square");
		squareButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeDraw = SelectedForm.SQUARE;
			}
		});
		squareButton.setBounds(22, 139, 89, 23);
		frmDesenhos.getContentPane().add(squareButton);

		final JButton eraserButton = new JButton("Eraser");
		eraserButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeDraw = SelectedForm.ERASER;
			}
		});
		eraserButton.setBounds(22, 241, 89, 23);
		frmDesenhos.getContentPane().add(eraserButton);

		currentCordinatesLabel = new JLabel("");
		currentCordinatesLabel.setBounds(146, 651, 130, 18);
		frmDesenhos.getContentPane().add(currentCordinatesLabel);

		final JButton circleButton = new JButton("Circle");
		circleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				typeDraw = SelectedForm.CIRCLE;
			}
		});
		circleButton.setBounds(22, 173, 89, 23);
		frmDesenhos.getContentPane().add(circleButton);

		final JButton jBColorChooser = new JButton("Color");
		jBColorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				colorChooser();
			}
		});
		jBColorChooser.setBounds(22, 306, 89, 23);
		frmDesenhos.getContentPane().add(jBColorChooser);
		JButton jBCanOfPaint = new JButton("Can Paint");
		jBCanOfPaint.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				typeDraw = SelectedForm.CANPAINT;
			}
		});
		jBCanOfPaint.setBounds(22, 207, 89, 23);
		frmDesenhos.getContentPane().add(jBCanOfPaint);

		final JCheckBox jCBAA = new JCheckBox("Anti-Aliasing");
		jCBAA.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (jCBAA.isSelected()) {
					panel.setAntiAliasing(true);
				} else {
					panel.setAntiAliasing(false);
				}
			}
		});
		jCBAA.setBounds(146, 10, 119, 23);
		frmDesenhos.getContentPane().add(jCBAA);

		JButton jBPolyline = new JButton("Polyline");
		jBPolyline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				poly = new Polyline(color);
				typeDraw = SelectedForm.POLYLINE;
			}
		});
		jBPolyline.setBounds(22, 105, 89, 23);
		frmDesenhos.getContentPane().add(jBPolyline);

		final JSlider jSliderEraserSize = new JSlider();
		jSliderEraserSize.setSnapToTicks(true);
		jSliderEraserSize.setPaintLabels(true);
		jSliderEraserSize.addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent arg0) {
				eraser.setSize(jSliderEraserSize.getValue());
			}
		});
		jSliderEraserSize.setValue(15);
		jSliderEraserSize.setMaximum(120);
		jSliderEraserSize.setMinimum(2);
		jSliderEraserSize.setBounds(22, 275, 89, 24);
		frmDesenhos.getContentPane().add(jSliderEraserSize);

		JButton jBPen = new JButton("Pen");
		jBPen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				typeDraw = SelectedForm.PEN;
			}
		});
		jBPen.setBounds(22, 340, 89, 23);
		frmDesenhos.getContentPane().add(jBPen);

		JMenuBar jMainMenu = new JMenuBar();
		frmDesenhos.setJMenuBar(jMainMenu);
		
		JMenu mnFile = new JMenu("File");
		jMainMenu.add(mnFile);

		JMenu menuOptions = new JMenu("Options");
		jMainMenu.add(menuOptions);

		JMenuItem jIOpMenuSave = new JMenuItem("Save");
		jIOpMenuSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new FileGenerator(new Image(drawStack.peek(), drawStack, panel.getHeight(), panel.getWidth())).save();
			}
		});
		menuOptions.add(jIOpMenuSave);

		JMenuItem jIOpMenuSaveAs = new JMenuItem("Save as..");
		menuOptions.add(jIOpMenuSaveAs);

		JMenu mnFilters = new JMenu("Filters");
		jMainMenu.add(mnFilters);

		JMenuItem menuInverseColorButton = new JMenuItem("Inverse Color");
		menuInverseColorButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				panel.reverseColor();
				drawStack.addForm(panel.deepCopy());
			}
		});
		mnFilters.add(menuInverseColorButton);
		
		JMenu menuHelpButton = new JMenu("Help");
		jMainMenu.add(menuHelpButton);
		
		JMenuItem menuHelpOption = new JMenuItem("Help");
		menuHelpButton.add(menuHelpOption);
		panel.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				updateCoordinates(arg0);
				if (typeDraw != SelectedForm.FREE) {
					if (typeDraw != SelectedForm.ERASER && SelectedForm.CANPAINT != typeDraw) {
						if (typeDraw == SelectedForm.POLYLINE && poly.getN() == 0) {
							poly.add(arg0.getX(), arg0.getY());
						} else {
							if (typeDraw == SelectedForm.PEN) {
								pen.setP1(arg0.getX(), arg0.getY());
								pen.setP2(arg0.getX(), arg0.getY());
								pen.setColor(color);
								panel.draw(typeDraw, pen);
							}
							p1.move(arg0.getX(), arg0.getY());
						}
					} else if (typeDraw == SelectedForm.ERASER) {
						eraser.setP1(arg0.getX(), arg0.getY());
						panel.draw(typeDraw, eraser);
					} else if (typeDraw == SelectedForm.CANPAINT) {
						canPaint.setP(arg0.getX(), arg0.getY());
						canPaint.setActualColor(panel.getPixelColor(arg0.getX(), arg0.getY()));
						canPaint.setReplacementColor(color);
						panel.draw(typeDraw, canPaint);
					}
				}
				if (!panel.isFocusOwner())
					panel.requestFocusInWindow();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				updateCoordinates(arg0);
				if (typeDraw != SelectedForm.FREE) {
					if (typeDraw == SelectedForm.ERASER) {
						drawStack.addForm(panel.deepCopy());
						return;
					} else if (typeDraw == SelectedForm.CANPAINT) {
						drawStack.addForm(panel.deepCopy());
						return;
					} else if (typeDraw == SelectedForm.POLYLINE) {
						Panel.dragging = false;
						poly.add(arg0.getX(), arg0.getY());
						poly.setActualX(arg0.getX());
						poly.setActualY(arg0.getY());
						panel.draw(typeDraw, poly);
						drawStack.addForm(panel.deepCopy());
						if (poly.getX()[0] == arg0.getX() && poly.getY()[0] == arg0.getY() && poly.getN() > 2)
							poly = new Polyline(color);
					} else {
						p2.move(arg0.getX(), arg0.getY());
						if (p1 == null)
							return;
						if (Math.abs(p2.x - p1.x) + Math.abs(p2.y - p1.y) == 0 && typeDraw != SelectedForm.ERASER) {
							return;
						}
						if (typeDraw == SelectedForm.LINE) {
							line.setP1(p1.x, p1.y);
							line.setP2(p2.x, p2.y);
							line.setColor(color);
							panel.draw(typeDraw, line);
						} else if (typeDraw == SelectedForm.SQUARE) {
							square.setP1(p1.x, p1.y);
							square.setP2(p2.x, p2.y);
							square.setColor(color);
							panel.draw(typeDraw, square);
						} else if (typeDraw == SelectedForm.CIRCLE) {
							circle.setP1(p1.x, p1.y);
							circle.setP2(p2.x, p2.y);
							circle.setColor(color);
							panel.draw(typeDraw, circle);
						} else if (typeDraw == SelectedForm.PEN) {

						}
						drawStack.addForm(panel.deepCopy());
						Panel.dragging = false;
					}
				}
			}
		});

	}

	public void updateCoordinates(MouseEvent arg0) {
		int x = arg0.getX(), y = arg0.getY();
		x = x > 800 ? 800 : x < 0 ? 0 : x;
		y = y > 600 ? 600 : y < 0 ? 0 : y;
		currentCordinatesLabel.setText(x + " " + y);
	}

	public void colorChooser() {
		colorChooser.setColor(JColorChooser.showDialog(frmDesenhos, "Change Line's Color ", Color.BLACK));
		color = colorChooser.getColor();
		jPColor.setBackground(color);
	}
}