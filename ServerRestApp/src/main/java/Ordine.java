public class Ordine {
	int ido;
	int idcameriere;
	int ntavolo;
	int pronto;
	int chiuso;

	public Ordine(int ido, int idcameriere, int ntavolo, int pronto, int chiuso) {
		super();
		this.ido = ido;
		this.idcameriere = idcameriere;
		this.ntavolo = ntavolo;
		this.pronto = pronto;
		this.chiuso = chiuso;
	}
	
	public int getIdo() {
		return ido;
	}

	public void setIdo(int ido) {
		this.ido = ido;
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

	public int isChiuso() {
		return chiuso;
	}

	public void setChiuso(int chiuso) {
		this.chiuso = chiuso;
	}

	@Override
	public String toString() {
		return "Ordine [ido=" + ido + ", idcameriere=" + idcameriere + ", ntavolo=" + ntavolo + ", pronto=" + pronto
				+ ", chiuso=" + chiuso + "]";
	}
	
	
}