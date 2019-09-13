import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
/*
import static spark.Spark.put;
import static spark.Spark.delete;
*/
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;


public class ServerRest {
	static Logger logger = LoggerFactory.getLogger(ServerRest.class);
	ObjectMapper om = new ObjectMapper();
	DBManager db;

	public void dbConnection() {
		try {
			db = new DBManager(DBManager.JDBCDriver, DBManager.JDBCURL);
			db.executeQuery("SELECT * FROM ordine LIMIT 1");
		} catch (ClassNotFoundException e) {
			System.out.println("Missign lib...");
			throw new RuntimeException();
		} catch (SQLException e) {
			System.out.println("Errore collegamento db");
			}
	}
	 
	public void run() {
		dbConnection();

		port(8080);

		get("/", (request, response) -> {
			return "SERVER ONLINE";
		});
		
		// GET - mostra tutti i prodotti
		get("/prodotto", (request, response) -> {
			String query;

			query = String.format("SELECT * FROM prodotto");
			ResultSet rs = db.executeQuery(query);

			ArrayList<Prodotto> l = new ArrayList<Prodotto>();
			while (rs.next()) {
				l.add(new Prodotto(rs.getInt("idp"), rs.getString("nome"),
						rs.getInt("giacenza"), rs.getDouble("prezzo"), rs.getString("tipo")));
			}
			return om.writeValueAsString(l);
		});
		
		// POST - inserisce nuovo prodotto 
		post("/prodotto/add", (request, response) -> {
			int idp = Integer.parseInt(request.queryParams("idp")); /* Chiedi ad Andre se e' necessario */
			String nome = request.queryParams("nome");
			int giacenza = Integer.parseInt(request.queryParams("giacenza"));
			Double prezzo = Double.parseDouble(request.queryParams("prezzo"));
			String prezzoString = request.queryParams("prezzo");
			String tipo = request.queryParams("tipo");
			Prodotto p = new Prodotto(idp, nome, giacenza, prezzo, tipo);
			//System.out.println(p.toString());    
			String query = String.format(
					"INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ('%s',%d,%s,'%s');",
					nome, giacenza, prezzoString, tipo);
			//System.out.println(query);
			db.executeUpdate(query);
			response.status(201);
			return om.writeValueAsString(p);
		});
		
		/*INIZIATO DA QUI*/		
		// GET - mostra prodotti in base al tipo passato
		// "http://sbaccioserver.ddns.net:8080/prodotto/ --da provare"
		get("/prodotto/:tipo", (request, response) -> {
			String tipo = request.queryParams(":tipo");
			String query;

			query = String.format("SELECT * FROM prodotto "
					+ "WHERE TIPO = '%s';",tipo);
			ResultSet rs = db.executeQuery(query);

			ArrayList<Prodotto> p = new ArrayList<Prodotto>();
			while (rs.next()) {
				p.add(new Prodotto(rs.getInt("idp"), rs.getString("nome"),
						rs.getInt("giacenza"), rs.getDouble("prezzo"), rs.getString("tipo")));
			}
			return om.writeValueAsString(p);
		});

		// GET - mostra tutti gli ordini aperti(chiuso = 0) (PER SALA)
		// "http://sbaccioserver.ddns.net:8080/ordine_aperto"
		get("/ordine_aperto", (request, response) -> {
			String query;

			query = String.format("SELECT * FROM ordine WHERE chiuso = 0");
			ResultSet rs = db.executeQuery(query);

			ArrayList<Ordine> o = new ArrayList<Ordine>();
			while (rs.next()) {
				o.add(new Ordine(rs.getInt("ido"), rs.getInt("idcameriere"),
						rs.getInt("ntavolo"), rs.getBoolean("pronto"), rs.getBoolean("chiuso")));
			}
			return om.writeValueAsString(o);
		});

		// GET - mostra tutti gli ordini aperti e non pronti(chiuso = 0 e pronto = 0) (PER CUCINA)
		// "http://sbaccioserver.ddns.net:8080/ordine_in_preparazione"
		get("/ordine_in_preparazione", (request, response) -> {
			String query;

			query = String.format("SELECT * FROM ordine WHERE chiuso = 0 AND pronto = 0");
			ResultSet rs = db.executeQuery(query);

			ArrayList<Ordine> o = new ArrayList<Ordine>();
			while (rs.next()) {
				o.add(new Ordine(rs.getInt("ido"), rs.getInt("idcameriere"),
						rs.getInt("ntavolo"), rs.getBoolean("pronto"), rs.getBoolean("chiuso")));
			}
			return om.writeValueAsString(o);
		});

		// GET - mostra ordini in base al numero del tavolo  che hanno chiuso = 0 (e pronto = 1)
		// "http://sbaccioserver.ddns.net:8080/ordine/ --da provare"
		get("/ordine/:ntavolo", (request, response) -> {
			String ntavolo = request.queryParams(":ntavolo");
			String query;

			query = String.format("SELECT * FROM ordine "
					+ "WHERE chiuso = 0 AND pronto = 1 AND ntavolo = %d;",ntavolo);
			ResultSet rs = db.executeQuery(query);

			ArrayList<Ordine> o = new ArrayList<Ordine>();
			while (rs.next()) {
				o.add(new Ordine(rs.getInt("ido"), rs.getInt("idcameriere"),
						rs.getInt("ntavolo"), rs.getBoolean("pronto"), rs.getBoolean("chiuso")));
			}
			return om.writeValueAsString(o);
		});

		// POST - crea scontrino cassa (da parametro ido passato e preso a query prima (via client))
		// "http://sbaccioserver.ddns.net:8080/scontrino/add  --da provare
		post("/scontrino/add/:ido", (request, response) -> {
			int ido = Integer.parseInt(request.queryParams(":ido")); 

			String query = String.format(
					"INSERT INTO scontrino (ido,tot) "
							+ "select ido, sum(totali_parziali)     "
							+ "from totali_parziali"
							+ "where ido = %d;", ido);
			//System.out.println(query);
			db.executeUpdate(query);
			response.status(201);
			return om.writeValueAsString("ok");
		});

		// GET - mostra scontrino in base al ido passato e preso da query prima(via client)
		// "http://sbaccioserver.ddns.net:8080/ordine/ --da provare"
		get("/scontrino/:ido", (request, response) -> {
			int ido = Integer.parseInt(request.queryParams(":ido"));
			String query;

			query = String.format("SELECT * FROM scontrino "
					+ "WHERE ido = %d;",ido);
			ResultSet rs = db.executeQuery(query);
			if (rs.next() == false) {
				response.status(404);
				return om.writeValueAsString("{status: failed}");
			}

			Scontrino s = new Scontrino(rs.getInt("ido"), rs.getDate("datachiusura"),
					rs.getDouble("tot"));
			return om.writeValueAsString(s);
		});
/*FINITO QUI*/

	}

	public static void main(String[] args) {
		new ServerRest().run();
	}

}





