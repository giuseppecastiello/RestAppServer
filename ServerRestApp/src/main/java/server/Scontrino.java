package server;
//import java.util.Date;

public class Scontrino {
	int ntavolo;
	int idcameriere;
	String datachiusura;
	double tot;

	public Scontrino(int ntavolo, int idcameriere,String datachiusura, double tot) {
		super();
		this.idcameriere = idcameriere;
		this.ntavolo = ntavolo;
		this.datachiusura = datachiusura;
		this.tot = tot;
	}
	
	public double getTotale() {
		return tot;
	}


	public void setTotale(double tot) {
		this.tot = tot;
	}


	public String getDatachiusura() {
		return datachiusura;
	}

	public void setDatachiusura(String datachiusura) {
		this.datachiusura = datachiusura;
	}

	public int getIdcameriere() {
		return idcameriere;
	}

	public void setIdcameriere(int idcameriere) {
		this.idcameriere = idcameriere;
	}

	public int getNtavolo() {
		return ntavolo;
	}

	public void setNtavolo(int ntavolo) {
		this.ntavolo = ntavolo;
	}

	@Override
	public String toString() {
		return "Scontrino [ntavolo=" + ntavolo + ", idcameriere=" + idcameriere + ", datachiusura=" + datachiusura
				+ ", totale=" + tot + "]";
	}

	
}