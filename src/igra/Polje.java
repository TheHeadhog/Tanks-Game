package igra;

import java.awt.Canvas;

@SuppressWarnings("serial")
public class Polje extends Canvas {
	private Mreza mreza;
	private int[] pozicija =new int[2];
	public Mreza getMreza() {
		return mreza;
	}

	public Polje(Mreza mreza) {
		super();
		this.mreza = mreza;
		addMouseListener(mreza);
	}
	
	public int[] getPozicija(){
		return pozicija;
	}
	
	public void setPozicija(int i, int j) {
		pozicija[0]=i;
		pozicija[1]=j;
	}
	
	public Polje dohvRelativno(int i,int j) {
		if (pozicija[0]+i>=0&&pozicija[1]+j>=0 && pozicija[0]+i<17 && pozicija[1]+j<17)
			return mreza.getPolja()[pozicija[0]+i][pozicija[1]+j];
			else return null;
	}
	
	public boolean mozeSeNaciFigura() {
		if(this instanceof Trava) return true;
		return false;
	}
}
