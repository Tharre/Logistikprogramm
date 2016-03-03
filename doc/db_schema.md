bundesnr
========

**id**, bezeichnung

```sql
CREATE TABLE `bundesnr` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL,
	nr			VARCHAR(255)	NOT NULL
);
```

inventurgruppe
==============

**id**, bezeichnung

```sql
CREATE TABLE `inventurgruppe` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

user
====

**id**, vorname, nachname, cn

```sql
CREATE TABLE `user` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	vorname		VARCHAR(255)	NOT NULL,
	nachname	VARCHAR(255),
	cn			VARCHAR(255)	NOT NULL,
	perm_mask	INT(11)			NOT NULL
);
```

material
========

**id**, bezeichnung, erstelldatum, _bundesnr_id_, _inventurgrp_id_,
_lagerort_id_, _erfasser_id_, gefahrstufe

```sql
CREATE TABLE `material` (
	PRIMARY KEY (id),
	FOREIGN KEY (bundesnr_id)		REFERENCES bundesnr(id),
	FOREIGN KEY (inventurgrp_id)	REFERENCES inventurgruppe(id),
	FOREIGN KEY (erfasser_id)		REFERENCES user(id),
	id				INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung		VARCHAR(255)	NOT NULL,
	erstelldatum	DATETIME		NOT NULL,
	bundesnr_id		INT(11)			NOT NULL,
	inventurgrp_id	INT(11)			NOT NULL,
	erfasser_id		INT(11),
	gefahrstufe		INT(11)			NOT NULL
);
```

lagerort
========

**id**, bezeichnung

```sql
CREATE TABLE `lagerort` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

einheit
=======

**id**, bezeichnung

```sql
CREATE TABLE `einheit` (
	PRIMARY KEY (id),
	id			INT(11)		NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(20)	NOT NULL
);
```

bestand
=======

**id**, _material_id_, _lagerort_id_, menge, meldebestand

```sql
CREATE TABLE `bestand` (
	PRIMARY KEY (id),
	FOREIGN KEY (material_id)	REFERENCES material(id),
	FOREIGN KEY (lagerort_id)	REFERENCES lagerort(id),
	FOREIGN KEY (einheit_id)	REFERENCES einheit(id),
	id				INT(11)			NOT NULL	AUTO_INCREMENT,
	material_id		INT(11)			NOT NULL,
	lagerort_id		INT(11)			NOT NULL,
	einheit_id		INT(11)			NOT NULL,
	menge			DECIMAL(19,4)	NOT NULL,
	meldebestand	DECIMAL(19,4)	NOT NULL
);
```

staat
=====

**id**, bezeichnung

```sql
CREATE TABLE `staat` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

firma
=====

**id**, bezeichnung, erstelldatum, _staat_id_, ort, plz, strasse, mail,
kundennr, sachbearbeiter, telefon, fax, homepage, konditionen, kreditorennr,
umsNr, araNr

```sql
CREATE TABLE `firma` (
	PRIMARY KEY (id),
	FOREIGN KEY (staat_id)	REFERENCES staat(id),
	id				INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung		VARCHAR(255)	NOT NULL,
	erstelldatum	DATETIME		NOT NULL,
	staat_id		INT(11)			NOT NULL,
	ort				VARCHAR(255)	NOT NULL,
	plz				VARCHAR(255)	NOT NULL,
	strasse			VARCHAR(255),
	mail			VARCHAR(255),
	kundennr		VARCHAR(255),
	sachbearbeiter	VARCHAR(255), -- TODO: meh
	telefon			VARCHAR(255),
	fax				VARCHAR(255),
	homepage		VARCHAR(255),
	konditionen		VARCHAR(255),
	kreditorennr	VARCHAR(255),
	umsNr			VARCHAR(255),
	araNr			VARCHAR(255),
	kommentar		VARCHAR(2048)
);
```

firma_material
==============

**firma_id**, **material_id**, preis, mwst, _einheit_id_, artNr

```sql
CREATE TABLE `firma_material` (
	PRIMARY KEY (firma_id, material_id),
	FOREIGN KEY (firma_id)		REFERENCES firma(id),
	FOREIGN KEY (material_id)	REFERENCES material(id),
	FOREIGN KEY (einheit_id)	REFERENCES einheit(id),
	firma_id	INT(11)			NOT NULL,
	material_id	INT(11)			NOT NULL,
	preis		DECIMAL(19,4)	NOT NULL,
	mwst		INT(11)			NOT NULL,
	einheit_id	INT(11)			NOT NULL,
	artNr		VARCHAR(255)
);
```

kostenstelle
============

**id**, bezeichnung

```sql
CREATE TABLE `kostenstelle` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

budget
======

**id**, bezeichnung

```sql
CREATE TABLE `budget` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

banf
====

**id**, _antragsteller_id_, _kostenstelle_id_, erstelldatum, wunschdatum,
kommentar

```sql
CREATE TABLE `banf` (
	PRIMARY KEY (id),
	FOREIGN KEY (antragsteller_id)	REFERENCES user(id),
	FOREIGN KEY (kostenstelle_id)	REFERENCES kostenstelle(id),
	id					INT(11)		NOT NULL	AUTO_INCREMENT,
	antragsteller_id	INT(11)		NOT NULL,
	kostenstelle_id		INT(11)		NOT NULL,
	erstelldatum		DATETIME	NOT NULL,
	wunschdatum			DATETIME,
	kommentar			VARCHAR(2048)
);
```

anschrift
=========

**id**, bezeichnung

```sql
CREATE TABLE `anschrift` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

bestellung
==========

**id**, _firma_id_, _budget_id_, _anschrift_id_, erstelldatum, wnummer,
kommentar

```sql
CREATE TABLE `bestellung` (
	PRIMARY KEY (id),
	FOREIGN KEY (ersteller_id)		REFERENCES user(id),
	FOREIGN KEY (firma_id)			REFERENCES firma(id),
	FOREIGN KEY (budget_id)			REFERENCES budget(id),
	FOREIGN KEY (anschrift_id)		REFERENCES anschrift(id),
	id				INT(11)			NOT NULL	AUTO_INCREMENT,
	ersteller_id	INT(11)			NOT NULL,
	firma_id		INT(11)			NOT NULL,
	budget_id		INT(11)			NOT NULL,
	anschrift_id	INT(11)			NOT NULL,
	erstelldatum	DATETIME		NOT NULL,
	wnummer			VARCHAR(255)	NOT NULL,
	bezahlt			DECIMAL(19,4),
	-- TODO: field for "deleted"
	kommentar		VARCHAR(2048)
);
```

bestbanfstatus
==============

**id**, bezeichnung

```sql
CREATE TABLE `bestbanfstatus` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

## Mögliche Werte
- 1: nicht bestellt
- 2: abgewiesen
- 3: angenommen
- 4: bestellt (aber noch nicht versandt)
- 5: versandt
- 6: abweichend geliefert
- 7: nicht lieferbar
- 8: geliefert

## SQL

```sql
INSERT INTO bestbanfstatus (id, bezeichnung) VALUES
	(1, "nicht bearbeitet"),
	(2, "abgewiesen"),
	(3, "angenommen"),
	(4, "bestellt"),
	(5, "versandt"),
	(6, "abweichend geliefert"),
	(7, "nicht lieferbar"),
	(8, "geliefert");
```

position
========

**id**, _material_id_, menge, _einheit_id_, preis, mwst, _status_, kommentar

```sql
CREATE TABLE `position` (
	PRIMARY KEY (id),
	FOREIGN KEY (material_id)	REFERENCES material(id),
	FOREIGN KEY (einheit_id)	REFERENCES einheit(id),
	FOREIGN KEY (status)		REFERENCES bestbanfstatus(id),
	id			INT(11)			NOT NULL	AUTO_INCREMENT,
	material_id	INT(11)			NOT NULL,
	menge		DECIMAL(19,4)	NOT NULL,
	einheit_id	INT(11)			NOT NULL,
	preis		DECIMAL(19,4),
	mwst		INT(11),
				CONSTRAINT chk_banf
				CHECK((preis IS NOT NULL
						AND mwst IS NOT NULL) OR status <= 2),
	status		INT(11)			NOT NULL,
	kommentar	VARCHAR(255)
);
```

banfposition
============

**banf_id**, **position_id**

```sql
CREATE TABLE `banfposition` (
	PRIMARY KEY (banf_id, position_id),
	FOREIGN KEY (banf_id)		REFERENCES banf(id),
	FOREIGN KEY (position_id)	REFERENCES `position`(id),
	banf_id		INT(11)			NOT NULL,
	position_id	INT(11)			NOT NULL
);
```

bestellposition
===============

**bestell_id**, **position_id**

```sql
CREATE TABLE `bestellposition` (
	PRIMARY KEY (bestell_id, position_id),
	FOREIGN KEY (bestell_id)	REFERENCES bestellung(id),
	FOREIGN KEY (position_id)	REFERENCES `position`(id),
	bestell_id	INT(11)			NOT NULL,
	position_id	INT(11)			NOT NULL
);
```

eigendaten
==========

uid, einkaeufergrp

```sql
CREATE TABLE `eigendaten` (
	uid				VARCHAR(255)	NOT NULL,
	einkaeufergrp	VARCHAR(255)	NOT NULL
);
```

buchungen
=========

**id**, _user_id_, _kostenstelle_id_, datum, _material_id_, menge, _einheit_id_, _firma_id_

```sql
CREATE TABLE `buchungen` (
	PRIMARY KEY (id),
	FOREIGN KEY (user_id)			REFERENCES user(id),
	FOREIGN KEY (kostenstelle_id)	REFERENCES kostenstelle(id),
	FOREIGN KEY (material_id)		REFERENCES material(id),
	FOREIGN KEY (einheit_id)		REFERENCES einheit(id),
	FOREIGN KEY (firma_id)			REFERENCES firma(id),
	id				INT(11)			NOT NULL	AUTO_INCREMENT,
	user_id			INT(11)			NOT NULL,
	kostenstelle_id	INT(11),
	datum			DATETIME		NOT NULL,
	material_id		INT(11)			NOT NULL,
	menge			DECIMAL(19,4)	NOT NULL,
	einheit_id		INT(11), -- TODO: this shouldn't be NULL
	firma_id		INT(11)
);
```
