CREATE TABLE IF NOT EXISTS Zemlja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    kod_drzave VARCHAR(3) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS VrstaPutovanja (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS Korisnik (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    uloga VARCHAR(50) NOT NULL DEFAULT 'KORISNIK'
);

CREATE TABLE IF NOT EXISTS Aktivnost (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    opis VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS TuristickiVodic (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ime VARCHAR(50) NOT NULL,
    prezime VARCHAR(50) NOT NULL,
    kontakt VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS Destinacija (
    id INT AUTO_INCREMENT PRIMARY KEY,
    naziv VARCHAR(100) NOT NULL,
    opis VARCHAR(1000) NOT NULL,
    latitude DOUBLE,
    longitude DOUBLE,
    preporucenoGodisnjeDoba VARCHAR(50),
    putanjaFotografija VARCHAR(500),
    zemljaId INT NOT NULL,
    vrstaPutovanjaId INT NOT NULL,
    FOREIGN KEY (zemljaId) REFERENCES Zemlja(id),
    FOREIGN KEY (vrstaPutovanjaId) REFERENCES VrstaPutovanja(id)
);

CREATE TABLE IF NOT EXISTS DestinacijaAktivnost (
    destinacijaId INT NOT NULL,
    aktivnostId INT NOT NULL,
    PRIMARY KEY (destinacijaId, aktivnostId),
    FOREIGN KEY (destinacijaId) REFERENCES Destinacija(id),
    FOREIGN KEY (aktivnostId) REFERENCES Aktivnost(id)
);

CREATE TABLE IF NOT EXISTS DestinacijaVodic (
    destinacijaId INT NOT NULL,
    vodicId INT NOT NULL,
    PRIMARY KEY (destinacijaId, vodicId),
    FOREIGN KEY (destinacijaId) REFERENCES Destinacija(id),
    FOREIGN KEY (vodicId) REFERENCES TuristickiVodic(id)
);