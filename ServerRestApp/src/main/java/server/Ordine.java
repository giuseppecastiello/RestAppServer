package server;
public class Ordine {
	int ntavolo;
	int idcameriere;

	public Ordine(int ntavolo, int idcameriere) {
		super();
		this.idcameriere = idcameriere;
		this.ntavolo = ntavolo;
		
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
		return "Ordine [ntavolo=" + ntavolo + ", idcameriere=" + idcameriere + "]";
	}

}