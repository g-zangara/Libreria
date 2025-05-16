# Gestore di Libreria Personale

## 📚 Descrizione del Progetto

**Gestore di Libreria Personale** è un'applicazione Java sviluppata per aiutare gli utenti a gestire una collezione personale di libri. Utilizza Java 17 in versione "vanilla" e Swing per l'interfaccia grafica. Il progetto implementa numerosi design pattern per garantire una struttura solida, estendibile e manutenibile.

## 🎯 Obiettivi

* Gestione completa di una libreria virtuale personale
* Interfaccia grafica intuitiva e responsive
* Persistenza dei dati su file (JSON, CSV)
* Applicazione dei principali design pattern
* Testing completo delle funzionalità principali

---

## 📊 Architettura e Design Pattern

Il progetto segue il pattern **MVC (Model-View-Controller)** e fa uso dei seguenti design pattern:

* **Singleton**: per la gestione centralizzata dei dati
* **Command**: per le operazioni Undo/Redo
* **Strategy**: per l'ordinamento dinamico dei libri
* **DAO**: per la persistenza dei dati in diversi formati (JSON, CSV)

---

## 🔍 Funzionalità Principali

### 📖 Gestione Libri

* Aggiunta, modifica e rimozione di libri
* Attributi: titolo, autore, ISBN, genere, valutazione (1-5 stelle), stato di lettura (Letto, In lettura, Da leggere)
* Validazione dei dati in input

### 🔎 Ricerca, Filtri e Ordinamento

* Ricerca per titolo, autore o ISBN
* Filtri per genere, autore, stato lettura e valutazione
* Ordinamento per titolo (A-Z/Z-A), autore e valutazione

### 📀 Persistenza Dati

* Salvataggio e caricamento in formato JSON e CSV
* Gestione robusta degli errori di I/O

### 💻 Interfaccia Grafica (Swing)

* GUI user-friendly e responsive
* Operazioni drag-and-drop
* Feedback visivo per le operazioni

### 🔄 Undo/Redo

* Sistema completo di annullamento/ripetizione per tutte le operazioni

### ✅ Testing

* Test unitari con JUnit 5
* Test per Command, Controller, DAO, Model, Strategy
* Classe `RunAllTests.java` per eseguire l'intera suite

---

## 📂 Struttura dei Package

```
src
├── model
│   ├── Libro.java
│   └── StatoLettura.java
├── view
│   ├── LibroView.java
│   └── DialogAggiungiModificaLibro.java
├── controller
│   ├── GestoreLibreria.java
│   └── LibroController.java
├── dao
│   ├── LibroDAO.java
│   ├── JsonLibroDAO.java
│   └── CsvLibroDAO.java
├── command
│   ├── Command.java
│   ├── CommandManager.java
│   ├── AggiungiLibroCommand.java
│   ├── ModificaLibroCommand.java
│   └── EliminaLibroCommand.java
├── strategy
│   ├── OrdinatoreLibroStrategy.java
│   ├── OrdinaTitoloAZStrategy.java
│   ├── OrdinaTitoloZAStrategy.java
│   ├── OrdinaAutoreAZStrategy.java
│   ├── OrdinaAutoreZAStrategy.java
│   ├── OrdinaValutazioneAscStrategy.java
│   └── OrdinaValutazioneDescStrategy.java
├── test
│   ├── command/
│   ├── controller/
│   ├── dao/
│   ├── model/
│   ├── strategy/
│   └── RunAllTests.java
└── Librerira.java
```

---

## 🛠️ Requisiti Tecnici

* Java 17 (Standard Edition)
* IDE consigliato: IntelliJ IDEA, Eclipse o NetBeans
* Librerie: Solo API Java standard (JUnit 5 per i test)

---

## ✨ Come Eseguire il Progetto

### 1. Clona il repository

```bash
git clone https://github.com/g-zangara/Libreria.git
cd Libreria
```

### 2. Importa il progetto in IntelliJ IDEA (o altro IDE)

### 3. Aggiungi JUnit 5 al progetto - Su IntelliJ IDEA

Per usare JUnit in un progetto Java vanilla su IntelliJ IDEA, **non serve installare un plugin separato**: JUnit è già supportato nativamente da IntelliJ. Devi solo aggiungere la libreria JUnit al tuo progetto.


Se **non** stai usando Maven o Gradle (Java vanilla):

* Vai su: `File > Project Structure > Modules > Dependencies`
* Clicca su `+` > `Library` > `From Maven...`
* Cerca:

  ```
  org.junit.jupiter:junit-jupiter:5.9.0
  ```
* IntelliJ scaricherà la libreria e la aggiungerà al classpath

### 4. Compila ed esegui l'applicazione

Esegui `Libreria.java` per avviare l'interfaccia grafica principale.

### 5. Esegui i test

Puoi eseguire i test in due modi:

* Cliccando sull'icona verde accanto al metodo `@Test` o alla classe di test
* Oppure: tasto destro sulla classe di test > `Run 'RunAllTests.main()'`

Oppure esegui direttamente:

```bash
java test.RunAllTests
```

---

## 📅 Stato di Lettura e Valutazione

* **Stati possibili**: `Letto`, `In lettura`, `Da leggere`
* **Valutazione**: da 1 a 5 stelle con feedback visivo nella GUI

---

## 📄 Licenza

Questo progetto è distribuito sotto licenza **MIT**. Sentiti libero di utilizzarlo, modificarlo e condividerlo.

---

## 👤 Autori

Progetto sviluppato a scopo didattico come esercitazione di progettazione software in Java. Include best practice OOP, uso avanzato di Swing e design pattern.
