CREATE TABLE user_rodzic (
    id SERIAL PRIMARY KEY,
    imie VARCHAR(50) NOT NULL,
    imie2 VARCHAR(50),
    nazwisko VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    telefon VARCHAR(15)
);

CREATE TABLE user_uczen (
    id SERIAL PRIMARY KEY,
    imie VARCHAR(50) NOT NULL,
    imie2 VARCHAR(50),
    nazwisko VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    id_klasy INT NOT NULL REFERENCES klasa(id),
);

CREATE TABLE rodzic_uczen (
    id_rodzica INT NOT NULL REFERENCES user_rodzic(id) ON DELETE CASCADE,
    id_ucznia INT NOT NULL REFERENCES user_uczen(id) ON DELETE CASCADE,
    PRIMARY KEY (id_rodzica, id_ucznia)
);

CREATE TABLE user_nau (
    id SERIAL PRIMARY KEY,
    imie VARCHAR(50) NOT NULL,
    imie2 VARCHAR(50),
    nazwisko VARCHAR(100) NOT NULL,
    email VARCHAR(100)
);

CREATE TABLE wiadomosc (
    id SERIAL PRIMARY KEY,
    tytul VARCHAR(255) NOT NULL,
    tresc TEXT NOT NULL,
    data TIMESTAMP NOT NULL,
    zalaczniki BYTEA
);

CREATE TABLE wiadomosc_u (
    id_wiadomosci INT NOT NULL REFERENCES wiadomosc(id) ON DELETE CASCADE,
    id_odbiorcy INT NOT NULL REFERENCES user_uczen(id),
    PRIMARY KEY (id_wiadomosci, id_odbiorcy)
);

CREATE TABLE wiadomosc_n (
    id_wiadomosci INT NOT NULL REFERENCES wiadomosc(id) ON DELETE CASCADE,
    id_odbiorcy INT NOT NULL REFERENCES user_nau(id),
    PRIMARY KEY (id_wiadomosci, id_odbiorcy)
);

CREATE TABLE wiadomosc_r (
    id_wiadomosci INT NOT NULL REFERENCES wiadomosc(id) ON DELETE CASCADE,
    id_odbiorcy INT NOT NULL REFERENCES user_rodzic(id),
    PRIMARY KEY (id_wiadomosci, id_odbiorcy)
);

CREATE TABLE klasa (
    id SERIAL PRIMARY KEY,
    nazwa VARCHAR(50) NOT NULL
);

CREATE TABLE lekcja (
    id SERIAL PRIMARY KEY,
    start TIME NOT NULL,
    end TIME NOT NULL,
    dzien DATE NOT NULL,
    id_klasy INT NOT NULL REFERENCES klasa(id),
    id_sali INT NOT NULL REFERENCES sala(id),
    id_nauczyciela INT NOT NULL REFERENCES user_nau(id)
);

CREATE TABLE sala (
    id SERIAL PRIMARY KEY,
    nr_sali VARCHAR(10) NOT NULL
);

CREATE TABLE przedmiot (
    id SERIAL PRIMARY KEY,
    nazwa VARCHAR(100) NOT NULL,
    id_nauczyciela INT NOT NULL REFERENCES user_nau(id)
);

CREATE TABLE sprawdzian (
    id SERIAL PRIMARY KEY,
    kategoria VARCHAR(100) NOT NULL,
    id_nauczyciela INT NOT NULL REFERENCES user_nau(id),
    id_sali INT NOT NULL REFERENCES sala(id),
    id_klasy INT NOT NULL REFERENCES klasa(id),
    id_przedmiotu INT NOT NULL REFERENCES przedmiot(id)
);

CREATE TABLE ocena (
    id SERIAL PRIMARY KEY,
    ocena INT NOT NULL,
    data DATE NOT NULL,
    id_ucznia INT NOT NULL REFERENCES user_uczen(id),
    id_nauczyciela INT NOT NULL REFERENCES user_nau(id),
    id_przedmiotu INT NOT NULL REFERENCES przedmiot(id)
);

CREATE TABLE uwaga (
    id SERIAL PRIMARY KEY,
    tresc TEXT NOT NULL,
    data TIMESTAMP NOT NULL,
    id_ucznia INT NOT NULL REFERENCES user_uczen(id),
    id_nauczyciela INT NOT NULL REFERENCES user_nau(id)
);

CREATE TABLE dzien (
    id SERIAL PRIMARY KEY,
    data DATE NOT NULL
);