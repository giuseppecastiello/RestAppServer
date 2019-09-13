
public class Prodotto {
	int idp;
	String nome;
	int giacenza;
	double prezzo;
	String tipo;
	
	public Prodotto(int idp, String nome, int giacenza, double prezzo, String tipo) {
		super();
		this.idp = idp;
		this.nome = nome;
		this.giacenza = giacenza;
		this.prezzo = prezzo;
		this.tipo = tipo;
	}
	
	public int getIdp() {
		return idp;
	}

	public void setIdp(int idp) {
		this.idp = idp;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getGiacenza() {
		return giacenza;
	}

	public void setGiacenza(int giacenza) {
		this.giacenza = giacenza;
	}

	public double getPrezzo() {
		return prezzo;
	}

	public void setPrezzo(double prezzo) {
		this.prezzo = prezzo;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	@Override
	public String toString() {
		return "Prodotto [idp=" + idp + ", nome=" + nome + ", giacenza=" + giacenza + ", prezzo=" + prezzo + ", tipo="
				+ tipo + "]";
	}


}
