import java.util.Date;

public class Scontrino {
	int ido;
	Date datachiusura = new Date();
	double tot;
	
	public int getIdo() {
		return ido;
	}
	public void setIdo(int ido) {
		this.ido = ido;
	}
	public Date getDatachiusura() {
		return datachiusura;
	}
	public void setDatachiusura(Date datachiusura) {
		this.datachiusura = datachiusura;
	}
	public double getTot() {
		return tot;
	}
	public void setTot(double tot) {
		this.tot = tot;
	}
	
	public Scontrino(int ido, Date datachiusura, double tot) {
		super();
		this.ido = ido;
		this.datachiusura = datachiusura;
		this.tot = tot;
	}
	
	@Override
	public String toString() {
		return "Scontrino [ido=" + ido + ", datachiusura=" + datachiusura + ", tot=" + tot + "]";
	}
	
}