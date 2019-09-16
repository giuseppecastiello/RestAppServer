DROP DATABASE IF EXISTS DBRestApp;

CREATE DATABASE DBRestApp;
USE DBRestApp;

CREATE TABLE prodotto(
idp INT PRIMARY KEY AUTO_INCREMENT,
nome VARCHAR(50) NOT NULL,
giacenza INT NOT NULL, /* Quantità disponibile in magazzino */
prezzo DOUBLE NOT NULL,
tipo VARCHAR(20) NOT NULL /* Antipasto, Primo piatto, Secondo piatto, Dolce */
);

CREATE TABLE ordine_corrente(
ntavolo INT PRIMARY KEY,
idcameriere INT,
pronto BIT DEFAULT 0 NOT NULL /* 1 = è pronto in cucina */
);

CREATE TABLE contiene(
ntavolo INT,
idp INT,
quantita INT NOT NULL,
PRIMARY KEY (ntavolo,idp),
FOREIGN KEY (ntavolo) REFERENCES ordine_corrente(ntavolo) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (idp) REFERENCES prodotto(idp) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE scontrino(
ntavolo INT,
datachiusura TIMESTAMP DEFAULT NOW(),
idcameriere int,
tot DOUBLE NOT NULL,
PRIMARY KEY (ntavolo,datachiusura)
/*FOREIGN KEY (ntavolo) REFERENCES ordine_corrente(ntavolo) ON UPDATE NO ACTION ON DELETE NO ACTION*/
);

CREATE TABLE ha_contenuto(
ntavolo INT,
idp INT,
quantita INT NOT NULL,
PRIMARY KEY (ntavolo,idp),
FOREIGN KEY (ntavolo) REFERENCES scontrino(ntavolo) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (idp) REFERENCES prodotto(idp) ON UPDATE NO ACTION ON DELETE NO ACTION
);

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Arancini di riso",5,5.00,"Antipasto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Impepata di cozze",3,8.00,"Antipasto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Crocchette di patate",10,4.00,"Antipasto");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tagliatelle al ragù",5,8.00,"Primo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tortellini in brodo",4,10.00,"Primo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tortelloni ricotta e spinaci",6,9.00,"Primo_piatto");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tagliata di manzo",8,16.00,"Secondo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Orata al sale grosso",4,18.00,"Secondo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Arrosto di vitello",5,12.00,"Secondo_piatto");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Zuppa Inglese",5,4.00,"Dolce");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Profitterol",5,4.00,"Dolce");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Sorbetto",7,3.00,"Dolce");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Acqua naturale 1L",20,2.00,"Bibita_analcolica");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Coca Cola 0.66L",20,3.00,"Bibita_analcolica");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Estathe 0.66L",10,2.50,"Bibita_analcolica");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Prosecco 0.75L",15,7.50,"Vino");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Rosso 0.75L",20,8.50,"Vino");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Bianco 0.75L",10,8.50,"Vino");

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Becks 0.66L",15,3.50,"Birra");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Moretti 0.66L",15,3.50,"Birra");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Ichnusa 0.66L",15,4.00,"Birra");

INSERT INTO ordine_corrente (ntavolo,idcameriere) VALUES (1,1); 
INSERT INTO ordine_corrente (ntavolo,idcameriere) VALUES (2,1); 
INSERT INTO ordine_corrente (ntavolo,idcameriere) VALUES (3,2); 

INSERT INTO contiene VALUES (1,1,2);
INSERT INTO contiene VALUES (1,2,2);
INSERT INTO contiene VALUES (1,3,1);
INSERT INTO contiene VALUES (1,20,1);
/*DOBBIAMO MODIFICARE LA GIACENZA SOTTRAENDO LA QUANTITA CHE E' STATA UTILIZZATA DALLA CUCINA*/

INSERT INTO contiene VALUES (2,3,2);
INSERT INTO contiene VALUES (2,5,1);
INSERT INTO contiene VALUES (2,18,1);
INSERT INTO contiene VALUES (2,7,2);

INSERT INTO contiene VALUES (3,7,2);
INSERT INTO contiene VALUES (3,2,2);
INSERT INTO contiene VALUES (3,15,2);
INSERT INTO contiene VALUES (3,19,1);

/*GENERO LO SCONTRINO ANDANDO A VEDERE I PRODOTTI CHE CONTENEVA L'ORDINE CORRENTE*/
CREATE VIEW totali_parziali as
select o.ntavolo,o.idcameriere, (p.prezzo * c.quantita) as tot_parz
from ordine_corrente o join contiene c on o.ntavolo=c.ntavolo
join prodotto p on p.idp=c.idp;

/*
INSERT INTO ordine_corrente (ntavolo,idcameriere) VALUES (4,2); 
INSERT INTO contiene VALUES (4,1,2);

SELECT *
FROM totali_parziali;
*/

INSERT INTO scontrino (ntavolo,idcameriere,tot) 
select ntavolo,idcameriere, sum(tot_parz)
from totali_parziali
group by ntavolo;/*where ntavolo=ntavolo;*/

/*drop view totali_parziali;*/

/*COPIO IL DETTAGLIO DELL'ORDINE CORRENTE COME DATI DELLO SCONTRINO*/
INSERT INTO ha_contenuto (ntavolo,idp,quantita)
select ntavolo,idp,quantita
from contiene;

/*POSSO ORA ELIMINARE I DATI DELL'ORDINE CORRENTE CHE A LORO VOLTA ELIMINERANNO I RECORD DI CONTIENE*/
DELETE FROM ordine_corrente
WHERE ntavolo<1000;

/*
select *
from ordine_corrente o join contiene c on o.ntavolo=c.ntavolo
join prodotto p on p.idp=c.idp;

select *
from scontrino s join ha_contenuto h on s.ntavolo=h.ntavolo
join prodotto p on p.idp=h.idp;

SELECT *
FROM scontrino
WHERE ntavolo = 3
ORDER BY datachiusura DESC
LIMIT 1;

select *
from totali_parziali;

select ido, sum(tot_parz)
from totali_parziali;

select *
from scontrino;

delete from prodotto
where idp=1 or idp=2 or idp=3;

select *
from prodotto;

*/