import static spark.Spark.delete;
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
import com.fasterxml.jackson.core.JsonGenerator;

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

		port(8081);

		get("/", (request, response) -> {
			return "SERVER ONLINE";
		});
		
		// GET - mostra tutti i prodotti
		get("/prodotto", (request, response) -> {
			String query;
			JsonGenerator g;
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
		post("/prodotto/add_prodotto", (request, response) -> {
			/*int idp = Integer.parseInt(request.queryParams("idp"));  Chiedi ad Andre se e' necessario */
			String nome = request.queryParams("nome");
			int giacenza = Integer.parseInt(request.queryParams("giacenza"));
			String prezzoString = request.queryParams("prezzo");
			Double prezzo = Double.parseDouble(prezzoString);
			String tipo = request.queryParams("tipo");
			Prodotto p = new Prodotto(0, nome, giacenza, prezzo, tipo);
			//System.out.println(p.toString());    
			String query = String.format(
					"INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ('%s',%d,%s,'%s');",
					nome, giacenza, prezzoString, tipo);
			//System.out.println(query);
			db.executeUpdate(query);
			response.status(201);
			return om.writeValueAsString(p);
		});
		
		// POST - inserisce nuovo ordine
		post("/prodotto/add_ordine", (request, response) -> {
			int idc = Integer.parseInt(request.queryParams("idcameriere"));
			int ntavolo = Integer.parseInt(request.queryParams("ntavolo"));
			//int pronto = Integer.parseInt(request.queryParams("pronto"));
			String query = String.format(
					"INSERT INTO ordine_corrente (idcameriere,ntavolo) VALUES (%d,%d);",
					idc,ntavolo);
			//System.out.println(query);
			db.executeUpdate(query);
			response.status(201);
			Ordine o = new Ordine(ntavolo, idc, 0);
			//new Ordine(0, idc, ntavolo, 0, 0);// CONTINUA QUAAAAAAAAAAAAAAAAA (BEPIS)
			return om.writeValueAsString(o);
		});
				
		
/*INIZIATO DA QUI*/		
		// GET - mostra prodotti in base al tipo passato
		// "http://sbaccioserver.ddns.net:8081/prodotto/tipo"
		get("/prodotto/:tipo", (request, response) -> {
			String tipo = request.params(":tipo");
			String query;

			query = String.format("SELECT * FROM prodotto "
					+ "WHERE TIPO = '%s';",tipo);
			ResultSet rs = db.executeQuery(query);

			ArrayList<Prodotto> p = new ArrayList<Prodotto>();
			while (rs.next()) {
				p.add(new Prodotto(rs.getInt("idp"), rs.getString("nome"),
						rs.getInt("giacenza"), rs.getDouble("prezzo"), rs.getString("tipo")));
			}
			/*Prodotto p = new Prodotto(rs.getInt("idp"), rs.getString("nome"),
						rs.getInt("giacenza"), rs.getDouble("prezzo"), rs.getString("tipo"));*/
			return om.writeValueAsString(p);
		});

		// GET - mostra tutti gli ordini aperti (PER SALA)
		// "http://sbaccioserver.ddns.net:8081/ordine_aperto"
		get("/ordine_aperto", (request, response) -> {
			String query;

			query = String.format("SELECT * FROM ordine_corrente");
			ResultSet rs = db.executeQuery(query);

			ArrayList<Ordine> o = new ArrayList<Ordine>();
			while (rs.next()) {
				o.add(new Ordine(rs.getInt("ntavolo"), rs.getInt("idcameriere"),
						rs.getInt("pronto")));
			}
			return om.writeValueAsString(o);
		});

		// GET - mostra tutti gli ordini aperti che non sono pronti(pronto = 0) (PER CUCINA)
		// "http://sbaccioserver.ddns.net:8081/ordine_in_preparazione"
		get("/ordine_in_preparazione", (request, response) -> {
			String query;

			query = String.format("SELECT * FROM ordine_corrente WHERE pronto = 0");
			ResultSet rs = db.executeQuery(query);

			ArrayList<Ordine> o = new ArrayList<Ordine>();
			while (rs.next()) {
				o.add(new Ordine(rs.getInt("ntavolo"), rs.getInt("idcameriere"),
						rs.getInt("pronto")));
			}
			return om.writeValueAsString(o);
		});
		
		/*// GET - (chiamato dopo click di chiusura ordine) mostra l'ordine legato ad un tavolo (che sarà poi da cancellare e droppare)
		// "http://sbaccioserver.ddns.net:8081/ordine_corrente/numerotavolo
		get("/ordine_corrente/:ntavolo", (request, response) -> {
			int ntavolo = Integer.parseInt(request.params("ntavolo"));
			String query;

			query = String.format("SELECT * FROM ordine_corrente "
					+ "WHERE ntavolo = %d;",ntavolo);
			ResultSet rs = db.executeQuery(query);
			if (rs.next() == false) {
				response.status(404);
				return om.writeValueAsString("{status: failed}");
			}
			Ordine o = new Ordine(rs.getInt("ntavolo"), rs.getInt("idcameriere"),
					rs.getInt("pronto"));
			return om.writeValueAsString(o);
		});*/
		
		// POST - inserisce scontrino da view di appoggio e lo mostra
		post("/prodotto/add_scontrino", (request, response) -> {
			int ntavolo = Integer.parseInt(request.queryParams("ntavolo"));
			String query = String.format(
					"INSERT INTO scontrino (ntavolo,idcameriere, tot) "
					+ "SELECT ntavolo, idcameriere, sum(tot_parz)"
					+ "FROM totali_parziali WHERE ntavolo = %d", ntavolo);			
			db.executeUpdate(query);
			response.status(201);
			
			query = String.format(
					"SELECT *"
					+ "FROM scontrino WHERE ntavolo = %d"
					+ "ORDER BY datachiusura DESC"
					+ "LIMIT 1", ntavolo);	
			ResultSet rs = db.executeQuery(query);
			if (rs.next() == false) {
				response.status(404);
				return om.writeValueAsString("{status: failed}");
			}
			Scontrino s = new Scontrino (rs.getInt("ntavolo"), rs.getInt("idcameriere"),
					rs.getDate("datachiusura"), rs.getDouble("tot")); 
			return om.writeValueAsString(s);
		});
		
		// DELETE - drop da db dell'ordine legato al tavolo per cui bisogna fare scontrino
		// "http://sbaccioserver.ddns.net:8081/drop_ordine/numerotavolo
		delete("/drop_ordine/:ntavolo", (request, response) -> {
			int ntavolo = Integer.parseInt(request.params("ntavolo"));
			String query;

			query = String.format("SELECT * FROM ordine_corrente WHERE ntavolo = %d;",ntavolo);
			ResultSet rs = db.executeQuery(query);
			if (rs.next() == false) {
				response.status(404);
				return om.writeValueAsString("{status: failed}");
			}

			query = String.format("DELETE FROM ordine_corrente WHERE ntavolo = '%d'", ntavolo);
			db.executeUpdate(query);
			return om.writeValueAsString("{status: ok}");
		});
				
/*FINITO QUI*/

	}

	public static void main(String[] args) {
		new ServerRest().run();
		System.out.println("");
	}

}