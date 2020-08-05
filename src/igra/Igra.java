package igra;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Color;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

enum Rezim{IGRANJE,IZMENA};
@SuppressWarnings("serial")
public class Igra extends Frame implements ActionListener{
	private Mreza mreza;
	Rezim rezim=Rezim.IZMENA;
	CheckboxGroup cbg=new CheckboxGroup();
	Checkbox[] cbs;
	Label brPoena=new Label("Poena:");
	Button pocni= new Button("Pocni");
	TextField brNovcica=new TextField("12");
	public Igra() {
		super("Igra");
		mreza=new Mreza(this);
		setSize(800,600);
		setResizable(true);
		dodajKomponente();
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				if (mreza.nit!=null) mreza.zaustaviNit();
				dispose();
				}
		});
		setVisible(true);
		mreza.requestFocus();
		repaint();
	}
	
	private void dodajKomponente() {
		dodajMeni();
		dodajPanele();
	}

	private void dodajPanele() {
		mreza.setLayout(new GridLayout(17,17));	
		add(mreza,BorderLayout.CENTER);
		Panel panel2 = new Panel(new GridLayout(1,2));
		panel2.add(new Label("Podloga:",Label.CENTER));
		Panel panel3 = new Panel(new GridLayout(2, 1));
		cbs=new Checkbox[2];
		cbs[0]= new Checkbox("Trava", true, cbg);
		cbs[0].setBackground(Color.GREEN);
		cbs[1]= new Checkbox("Zid", false, cbg);
		cbs[1].setBackground(Color.GRAY);
		panel3.add(cbs[0]);
		panel3.add(cbs[1]);
		panel2.add(panel3);
		add(panel2,BorderLayout.EAST);
		Panel panel4 = new Panel();
		panel4.add(new Label("Novcica:"));
		panel4.add(brNovcica);
		panel4.add(brPoena);
		pocni.addActionListener(this);
		panel4.add(pocni);
		pocni.setEnabled(false);
		add(panel4,BorderLayout.SOUTH);
	}

	private void dodajMeni() {
		MenuBar trakaMenija= new MenuBar();
		Menu meni = new Menu("Rezim");
		meni.add("Rezim izmena");
		meni.add("Rezim igranje");
		meni.addActionListener(this);
		trakaMenija.add(meni);
		setMenuBar(trakaMenija);		
	}

	public static void main(String[] args) {
		new Igra();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String komanda=e.getActionCommand();
		if(komanda.equals("Rezim izmena")) {
			rezim=Rezim.IZMENA;
			pocni.setEnabled(false);
			if (mreza.nit!=null) mreza.zaustaviNit();
		}
		if(komanda.equals("Rezim igranje")) {rezim=Rezim.IGRANJE; pocni.setEnabled(true);}
		if(komanda.equals("Pocni")) mreza.inicijalizujMrezu();
	}
}



//.(abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ
