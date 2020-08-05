package igra;

import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("serial")
public class Mreza extends Panel implements Runnable,MouseListener {
	Random rnd= new Random();
	private Polje polja[][]=new Polje[17][17];
	private CopyOnWriteArrayList<Igrac> igraci=new CopyOnWriteArrayList<Igrac>();
	private CopyOnWriteArrayList<Novcic> novcici=new CopyOnWriteArrayList<Novcic>();
	private CopyOnWriteArrayList<Tenk> tenkovi = new CopyOnWriteArrayList<Tenk>();
	private int brPoena=0;
	static CopyOnWriteArrayList<Polje> staraPolja=new CopyOnWriteArrayList<Polje>();
	private Igra igra;
	Polje old;
	Thread nit;
	public Mreza(Igra igra) {
		this.igra=igra;
		for(int i=0;i<17;i++) {
			for(int j=0;j<17;j++) {
				double choice = rnd.nextDouble();
				if (choice>0.8) polja[i][j]=new Zid(this);
				else polja[i][j]=new Trava(this);
				add(polja[i][j]);
				polja[i][j].setPozicija(i, j);
			}
		}
		addKeyListener(new KeyIgrac(this));
	}
	public Mreza(Igra igra, int dimenzija) {
		this.igra=igra;
		polja=new Polje[dimenzija][dimenzija];
	}

	public Polje[][] getPolja() {
		return polja;
	}

	public CopyOnWriteArrayList<Igrac> getIgraci() {
		return igraci;
	}

	public CopyOnWriteArrayList<Novcic> getNovcici() {
		return novcici;
	}

	public CopyOnWriteArrayList<Tenk> getTenkovi() {
		return tenkovi;
	}
	
	@Override
	public void run() {
		try {
			igra.brPoena.setText("Poena:"+brPoena);
			while(!nit.isInterrupted()){
				repaint();
				Thread.sleep(40);
				Igrac igrac=igraci.get(0);
				for (int i=0;i<novcici.size();i++) {
					synchronized (novcici.get(i)) {
						if(novcici.get(i).polje.equals(igrac.polje)) {
							novcici.remove(novcici.get(i));
							brPoena++;
							igra.brPoena.setText("Poena:"+brPoena);
							if(novcici.size()==0) zaustaviNit();
						}	
					}	
				}					
				for (Tenk tenk : tenkovi) {
					if(tenk.polje.equals(igrac.polje)) {
						igraci.remove(igrac);
						zaustaviNit();
					}
				}
			}
		}
		catch (InterruptedException e) {
		}
		repaint();
		nit=null;

	}
	public void inicijalizujMrezu() {
		brPoena=0;
		if (nit!= null) {zaustaviNit(); try{nit.join();}catch(InterruptedException e) {} }
		if (igra.rezim == Rezim.IGRANJE) {
			int brNovcica=Integer.parseInt(igra.brNovcica.getText());
			int brTenkova=brNovcica/3;
			int i,j;
			while(brNovcica>0) {
				i=rnd.nextInt(17); j=rnd.nextInt(17);
				if( polja[i][j] instanceof Trava) {
					boolean moze=true;
					for (Novcic novcic : novcici) {
						int itemp=novcic.getPolje().getPozicija()[0];
						int jtemp=novcic.getPolje().getPozicija()[1];
						if(i==itemp&&j==jtemp) { moze=false;break;}
					}
					if (moze) {
						novcici.add(new Novcic(polja[i][j]));
						brNovcica--;
					}
				}
			}
			while (brTenkova>0) {
				i=rnd.nextInt(17); j=rnd.nextInt(17);
				if( polja[i][j] instanceof Trava) {
					tenkovi.add(new Tenk(polja[i][j]));
					brTenkova--;
				}
			}
			boolean flag=true;
			while(flag) {
				flag = false;
				i=rnd.nextInt(17); j=rnd.nextInt(17);
				if( polja[i][j] instanceof Trava) {
					for (Novcic novcic : novcici) {
						if(novcic.polje.equals(polja[i][j])&& !flag) {flag=true; break;  }
					}
					for (Tenk tenk : tenkovi) {
						if(tenk.polje.equals(polja[i][j]) && !flag) {flag=true; break;}
					}
					if(flag==false)
						igraci.add(new Igrac(polja[i][j]));
				}
				else flag=true;
			}
			old=igraci.get(0).polje;
			nit=new Thread(this);
			nit.start();
			for (Tenk tenk : tenkovi) tenk.pokreniNit();
			requestFocus();
		}
		
	}
	
	public void zaustaviNit() {
		for (Tenk tenk : tenkovi) {
			tenk.zaustaviNit();
			Mreza.staraPolja.add(tenk.polje);
			tenkovi.remove(tenk);
		}
		synchronized (novcici) {
			for (Novcic novcic : novcici) {

				Mreza.staraPolja.add(novcic.polje);
				novcici.remove(novcic);
			}
		}	
		for (Igrac igrac : igraci) {
			Mreza.staraPolja.add(igrac.polje);
			igraci.remove(igrac);
		}
		nit.interrupt();
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if (igra.rezim==Rezim.IZMENA) {
			if(igra.cbg.getSelectedCheckbox().getLabel().equals("Trava")&& e.getSource() instanceof Zid) {
				Polje p = (Polje)e.getSource();
				int i,j;
				i=p.getPozicija()[0];
				j=p.getPozicija()[1];
				p.setVisible(false);
				remove(i*17+j);
				polja[i][j]=new Trava(this);
				polja[i][j].setPozicija(i, j);
				add(polja[i][j],i*17+j);
				revalidate();
				p.setVisible(true);
				polja[i][j].repaint();
			}
			if(igra.cbg.getSelectedCheckbox().getLabel().equals("Zid")&& e.getSource() instanceof Trava){
				Polje p = (Polje)e.getSource();
				int i,j;
				i=p.getPozicija()[0];
				j=p.getPozicija()[1];
				p.setVisible(false);
				remove(i*17+j);
				polja[i][j]=new Zid(this);
				polja[i][j].setPozicija(i, j);
				add(polja[i][j],i*17+j);
				revalidate();
				p.setVisible(true);
				polja[i][j].repaint();
			}
			this.repaint();
		}
		requestFocus();
		
	}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (Polje polje : Mreza.staraPolja) {
			polje.repaint();
			Mreza.staraPolja.remove(polje);
		}
		for (Igrac igrac : igraci) igrac.iscrtaj();
		for (Novcic novcic : novcici) novcic.iscrtaj();
		for (Tenk tenk : tenkovi) tenk.iscrtaj();
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

}
