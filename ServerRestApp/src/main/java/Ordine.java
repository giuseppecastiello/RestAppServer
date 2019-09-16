public class Ordine {
	int ntavolo;
	int idcameriere;
	int pronto;
	

	public Ordine(int ntavolo, int idcameriere, int pronto) {
		super();
		this.idcameriere = idcameriere;
		this.ntavolo = ntavolo;
		this.pronto = pronto;
		
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

	public int isPronto() {
		return pronto;
	}

	public void setPronto(int pronto) {
		this.pronto = pronto;
	}

	@Override
	public String toString() {
		return "Ordine [ntavolo=" + ntavolo + ", idcameriere=" + idcameriere + ", pronto=" + pronto + "]";
	}

	
}