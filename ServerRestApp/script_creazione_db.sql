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

CREATE TABLE ordine(
ido INT PRIMARY KEY AUTO_INCREMENT,
idcameriere INT,
ntavolo INT NOT NULL,
pronto BIT DEFAULT 0 NOT NULL, /* 1 = è pronto in cucina */
chiuso BIT DEFAULT 0 NOT NULL /* 1 = è stato fatto lo scontrino */
);

CREATE TABLE contiene(
ido INT,
idp INT,
quantita INT NOT NULL,
PRIMARY KEY (ido,idp),
FOREIGN KEY (ido) REFERENCES ordine(ido) ON UPDATE CASCADE ON DELETE CASCADE,
FOREIGN KEY (idp) REFERENCES prodotto(idp) ON UPDATE CASCADE ON DELETE CASCADE
);

CREATE TABLE scontrino(
ido INT PRIMARY KEY,
datachiusura TIMESTAMP DEFAULT NOW() NOT NULL,
tot DOUBLE NOT NULL,
FOREIGN KEY (ido) REFERENCES ordine(ido) ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tagliatelle al ragù",5,8.00,"Primo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Tagliata di manzo",8,14.00,"Secondo_piatto");
INSERT INTO prodotto (nome,giacenza,prezzo,tipo) VALUES ("Acqua naturale 1L",20,2.00,"Bibita_analcolica");

INSERT INTO ordine (idcameriere,ntavolo) VALUES (1,1); /*guarda se funziona il default*/

INSERT INTO contiene VALUES (1,1,2);
INSERT INTO contiene VALUES (1,2,2);
INSERT INTO contiene VALUES (1,3,1);

CREATE VIEW totali_parziali as
select o.ido, (p.prezzo * c.quantita) as tot_parz
from ordine o join contiene c on o.ido=c.ido
join prodotto p on p.idp=c.idp
where o.ido = 1;

INSERT INTO scontrino (ido,tot) 
select ido, sum(tot_parz)
from totali_parziali;

drop view totali_parziali;

/*
select *
from ordine o join contiene c on o.ido=c.ido
join prodotto p on p.idp=c.idp
join scontrino s on s.ido=o.ido;

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