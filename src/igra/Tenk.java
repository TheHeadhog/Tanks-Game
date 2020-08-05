package igra;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

enum Smer{LEVO,DESNO,GORE,DOLE};
public class Tenk extends Figura implements Runnable {
	Random rnd=new Random();
	private Smer smer=Smer.DESNO;
	Thread nit;
	public Tenk(Polje polje) {
		super(polje);
		// TODO Auto-generated constructor stub
	}
	public void pokreniNit() {
		nit=new Thread(this);
		nit.start();
	}
	public void zaustaviNit() {
		nit.interrupt();
	}
	@Override
	public void run() {
		try {
			while(!nit.isInterrupted()) {
					Thread.sleep(500);
					int i=0,j=0;
					boolean posted =true;
					while(posted) {
						int a=rnd.nextInt(4);
						if(a==0) {smer=Smer.LEVO; i=-1;j=0;}
						else if (a==1) {smer=Smer.DESNO; i=1;j=0;}
						else if (a==2) {smer=Smer.GORE; i=0;j=-1;}
						else if (a==3) {smer=Smer.DOLE; i=0;j=1;}
						Polje pomPolje=polje.dohvRelativno(i, j);
						if(pomPolje!=null && pomPolje.mozeSeNaciFigura())
							{Mreza.staraPolja.add(polje);pomeriFiguru(pomPolje); posted = false; }
						
					}
			}
		}
		catch (InterruptedException e) {
			
		}
		catch (NullPointerException e) {
			System.out.println(polje);
			System.out.println(smer);
		}
	}
	@Override
	public void iscrtaj() {
		Graphics g = polje.getGraphics();
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, polje.getWidth(), polje.getHeight());
		g.drawLine(0, polje.getHeight(), polje.getWidth(), 0);	
	}

}
