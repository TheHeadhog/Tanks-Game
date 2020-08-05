package igra;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.CopyOnWriteArrayList;

public class KeyIgrac extends KeyAdapter {
	private CopyOnWriteArrayList<Igrac> igraci;
	private Mreza m;
	public KeyIgrac(Mreza m) {
		igraci=m.getIgraci();
		this.m=m;
	}
	@Override
	public void keyPressed(KeyEvent e) {
		if (!m.getIgraci().isEmpty()) {
		int pritisnuto = e.getKeyCode();
		Polje pomPolje=null;
		m.old=m.getIgraci().get(0).polje;
	    switch( pritisnuto ) { 
	        case KeyEvent.VK_W:
	        	pomPolje=igraci.get(0).polje.dohvRelativno(-1,0);
				if(pomPolje!=null && pomPolje.mozeSeNaciFigura())
					{Mreza.staraPolja.add(igraci.get(0).polje); igraci.get(0).pomeriFiguru(pomPolje);}
	            break;
	        case KeyEvent.VK_A :
	        	pomPolje=igraci.get(0).polje.dohvRelativno(0,-1);
				if(pomPolje!=null && pomPolje.mozeSeNaciFigura())
					{Mreza.staraPolja.add(igraci.get(0).polje); igraci.get(0).pomeriFiguru(pomPolje);}
	            break;
	        case KeyEvent.VK_S:
	        	pomPolje=igraci.get(0).polje.dohvRelativno(1,0);
				if(pomPolje!=null && pomPolje.mozeSeNaciFigura())
					{Mreza.staraPolja.add(igraci.get(0).polje); igraci.get(0).pomeriFiguru(pomPolje);}
	            break;
	        case KeyEvent.VK_D :
	        	pomPolje=igraci.get(0).polje.dohvRelativno(0,1);
				if(pomPolje!=null && pomPolje.mozeSeNaciFigura())
					{Mreza.staraPolja.add(igraci.get(0).polje); igraci.get(0).pomeriFiguru(pomPolje);}
	            break;
	     }
	    m.repaint();	
		}
	}
}
