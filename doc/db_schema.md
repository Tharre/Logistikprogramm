Bundesnr
========

**id**, bezeichnung

```sql
CREATE TABLE `Bundesnr` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Inventurgruppe
==============

**id**, bezeichnung

```sql
CREATE TABLE `Inventurgruppe` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

User
====

**id**, vorname, nachname, cn

```sql
CREATE TABLE `User` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	vorname		VARCHAR(255)	NOT NULL,
	nachname	VARCHAR(255),
	cn			VARCHAR(255)	NOT NULL,
	perm_mask	INT(11)			NOT NULL
);
```

Material
========

**id**, bezeichnung, erstelldatum, _bundesnr_id_, _inventurgrp_id_,
_lagerort_id_, _erfasser_id_, gefahrstufe

```sql
CREATE TABLE `Material` (
	PRIMARY KEY (id),
	FOREIGN KEY (bundesnr_id)		REFERENCES Bundesnr(id),
	FOREIGN KEY (inventurgrp_id)	REFERENCES Inventurgruppe(id),
	FOREIGN KEY (erfasser_id)		REFERENCES User(id),
	id				INT(11)			NOT NULL,
	bezeichnung		VARCHAR(255)	NOT NULL,
	erstelldatum	DATETIME		NOT NULL,
	bundesnr_id		INT(11)			NOT NULL,
	inventurgrp_id	INT(11)			NOT NULL,
	erfasser_id		INT(11)			NOT NULL,
	gefahrstufe		INT(11)			NOT NULL
);
```

Lagerort
========

**id**, bezeichnung

```sql
CREATE TABLE `Lagerort` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Einheit
=======

**id**, bezeichnung

```sql
CREATE TABLE `Einheit` (
	PRIMARY KEY (id),
	id			INT(11)		NOT NULL,
	bezeichnung	VARCHAR(20)	NOT NULL
);
```

Bestand
=======

**id**, _material_id_, _lagerort_id_, menge, meldebestand

```sql
CREATE TABLE `Bestand` (
	PRIMARY KEY (id),
	FOREIGN KEY (material_id)	REFERENCES Material(id),
	FOREIGN KEY (lagerort_id)	REFERENCES Lagerort(id),
	FOREIGN KEY (einheit_id)	REFERENCES Einheit(id),
	id				INT(11)			NOT NULL,
	material_id		INT(11)			NOT NULL,
	lagerort_id		INT(11)			NOT NULL,
	einheit_id		INT(11)			NOT NULL,
	menge			DECIMAL(11,3)	NOT NULL,
	meldebestand	DECIMAL(11,3)	NOT NULL
);
```

Staat
=====

**id**, bezeichnung

```sql
CREATE TABLE `Staat` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Firma
=====

**id**, bezeichnung, erstelldatum, _staat_id_, ort, plz, strasse, mail,
kundennr, sachbearbeiter, telefon, fax, homepage, konditionen, kreditorennr,
umsNr, araNr

```sql
CREATE TABLE `Firma` (
	PRIMARY KEY (id),
	FOREIGN KEY (staat_id)	REFERENCES Staat(id),
	id				INT(11)			NOT NULL,
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
	araNr			VARCHAR(255)
);
```

Firma_Material
==============

**firma_id**, **material_id**, preis, mwst, _einheit_id_, artNr

```sql
CREATE TABLE `Firma_Material` (
	PRIMARY KEY (firma_id, material_id),
	FOREIGN KEY (firma_id)		REFERENCES Firma(id),
	FOREIGN KEY (material_id)	REFERENCES Material(id),
	FOREIGN KEY (einheit_id)	REFERENCES Einheit(id),
	firma_id	INT(11)			NOT NULL,
	material_id	INT(11)			NOT NULL,
	preis		DECIMAL(11,3)	NOT NULL,
	mwst		INT(11)			NOT NULL,
	einheit_id	INT(11)			NOT NULL,
	artNr		VARCHAR(255)
);
```

Kostenstelle
============

**id**, bezeichnung

```sql
CREATE TABLE `Kostenstelle` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Budget
======

**id**, bezeichnung

```sql
CREATE TABLE `Budget` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Wnummer
=======

**id**, bezeichnung

```sql
CREATE TABLE `Wnummer` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

Bestbanf
========

**id**, _antragsteller_id_, _kostenstelle_id_, erstelldatum, wunschdatum,
_budget_id_, _wnummer_id_, kommentar

```sql
CREATE TABLE `Bestbanf` (
	PRIMARY KEY (id),
	FOREIGN KEY (antragsteller_id)	REFERENCES User(id),
	FOREIGN KEY (kostenstelle_id)	REFERENCES Kostenstelle(id),
	FOREIGN KEY (budget_id)			REFERENCES Budget(id),
	FOREIGN KEY (wnummer_id)		REFERENCES Wnummer(id),
	id					INT(11)		NOT NULL,
	antragsteller_id	INT(11)		NOT NULL,
	kostenstelle_id		INT(11)		NOT NULL,
	erstelldatum		DATETIME	NOT NULL,
	wunschdatum			DATETIME	NOT NULL,
	budget_id			INT(11),
	wnummer_id			INT(11),
	kommentar			VARCHAR(2048)
);
```

Bestbanfstatus
==============

**id**, bezeichnung

```sql
CREATE TABLE `Bestbanfstatus` (
	PRIMARY KEY (id),
	id			INT(11)			NOT NULL,
	bezeichnung	VARCHAR(255)	NOT NULL
);
```

## Mögliche Werte
- 1: nicht bestellt
- 2: abgewiesen
- 3: bestellt (aber noch nicht versandt)
- 4: versandt
- 5: abweichend geliefert
- 6: nicht lieferbar
- 7: geliefert

## SQL

```sql
INSERT INTO Bestbanfstatus (id, bezeichnung) VALUES
	(1, "nicht bestellt"),
	(2, "abgewiesen"),
	(3, "bestellt"),
	(4, "versandt"),
	(5, "abweichend geliefert"),
	(6, "nicht lieferbar"),
	(7, "geliefert");
```

Bestbanfpos
===========

**bestbanf_id**, **pos**, _material_id_, menge, _einheit_id_, _status_,
_firma_id_, preis, mwst, bezahlt, kommentar

```sql
CREATE TABLE `Bestbanfpos` (
	PRIMARY KEY (bestbanf_id, pos),
	FOREIGN KEY (bestbanf_id)	REFERENCES Bestbanf(id),
	FOREIGN KEY (material_id)	REFERENCES Material(id),
	FOREIGN KEY (einheit_id)	REFERENCES Einheit(id),
	FOREIGN KEY (status)		REFERENCES Bestbanfstatus(id),
	FOREIGN KEY (firma_id)		REFERENCES Firma(id),
	bestbanf_id		INT(11)			NOT NULL,
	pos				INT(11)			NOT NULL,
	material_id		INT(11)			NOT NULL,
	menge			DECIMAL(11,3)	NOT NULL,
	einheit_id		INT(11)			NOT NULL,
	status			INT(11)			NOT NULL,
	firma_id		INT(11),
	preis			DECIMAL(11,3),
	mwst			INT(11),
					CONSTRAINT chk_banf
					CHECK((firma_id IS NOT NULL
							AND preis IS NOT NULL
							AND mwst IS NOT NULL) OR status <= 2),
	bezahlt			DECIMAL(11,3),
					CONSTRAINT chk_bestellung
					CHECK(bezahlt IS NULL OR status >= 3),
	kommentar		VARCHAR(255)
);
```

Eigendaten
==========

uid, einkaeufergrp

```sql
CREATE TABLE `Eigendaten` (
	uid				VARCHAR(255)	NOT NULL,
	einkaeufergrp	VARCHAR(255)	NOT NULL
);
```

Buchungen
=========

**id**, _user_id_, _kostenstelle_id_, datum, _material_id_, menge, _einheit_id_

```sql
CREATE TABLE `Buchungen` (
	PRIMARY KEY (id),
	FOREIGN KEY (user_id) REFERENCES User(id),
	FOREIGN KEY (kostenstelle_id) REFERENCES Kostenstelle(id),
	FOREIGN KEY (material_id)	REFERENCES Material(id),
	FOREIGN KEY (einheit_id)	REFERENCES Einheit(id),
	id				INT(11)			NOT NULL,
	user_id			INT(11)			NOT NULL,
	kostenstelle_id	INT(11)			NOT NULL,
	datum			DATETIME		NOT NULL,
	material_id		INT(11)			NOT NULL,
	menge			DECIMAL(11,3)	NOT NULL,
	einheit_id		INT(11)			NOT NULL
);
```
