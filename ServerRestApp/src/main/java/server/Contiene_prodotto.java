package server;

public class Contiene_prodotto extends Prodotto {
	int ntavolo;
	int quantita;
	int pronto;
	public Contiene_prodotto(int ntavolo,int quantita,int pronto,
			int idp,String nome, int giacenza, double prezzo,String tipo) {
		super(idp,nome,giacenza,prezzo,tipo);
		this.ntavolo=ntavolo;
		this.quantita=quantita;
		this.pronto=pronto;
	}
	public int getNtavolo() {
		return ntavolo;
	}
	public void setNtavolo(int ntavolo) {
		this.ntavolo = ntavolo;
	}
	public int getQuantita() {
		return quantita;
	}
	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}
	public int getPronto() {
		return pronto;
	}
	public void setPronto(int pronto) {
		this.pronto = pronto;
	}
	
}
