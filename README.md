# MailClient - Private Project



## Description

This application can be used to send massive mails per Java mail client and SMTP provider (Magenta, GMAIL, etc.). It is fully configurable via application.properties and via API endpoints. Details can be found in the usage section.

It was realized as a private project to help a friend and family member to spam a lot of mails to multiple receivers via BCC.

It is based on Java 8 and the Spring framework.

## Usage

You have to configure the body, subject and receivers of the mail via REST endpoints.

### Startup

Start the startMailClient.bat via CMD/PowerShell in the root directory. Alternatively you can build the JAR file (mvn package) and go to the target directory and execute following command:

```
java -d64-Xms1G -Xmx5G -XX:MaxMetaspaceSize=1G -XX:NewSize=2G -XX:MaxNewSize=3G -jar mail-client-1.0.0.jar --spring.config.location=TODO:PATH_TO_APPLICATION_DOT_PROPERTIES
```

Closing and stopping the application via: 
* CMD window closing
* STRG + C pressing multiple times and with key Y accepting

### API Documentation

The REST endpoints and an overview is accessable under: http://localhost:8080/swagger-ui.html#/ after startup.

## German

### TODO (Von oben nach unten) ###

#### Voraussetzungen ##

Java 8 muss installiert sein:
	-) Link Allgemein zum Download: https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
	-) Link Windows 32 bit: Windows x86 (.exe)
	-) Link Windows 64 bit: Windows x64 (.exe)
	
Entzippen von:
	-)mail-client-application-<VERSION>.zip
	
#### Verwendung ##

Konfigurieren von anderen Absender/Absender Mail Provider:
	-) Öffnen von application.properties
		-) Änderung von spring.mail.username=<PLATZHALTER>
		-) Änderung von spring.mail.password=<PLATZHALTER>
	-) Wechsel von Mail Provider wie Gmail, Gmx, Outlook, etc:
		-) Änderung von spring.mail.host=<PLATZHALTER>
	-) Ändern des Ordners vom Absender, wo die Kopien gespeichert werden:
		-) spring.mail.folder-name-sent=<PLATZHALTER>
	-) Async Einstellungen wie Thread Anzahl, etc.:
		-)Änderung von async.*=<PLATZHALTER>
	
Starten:
	-) Doppelklick auf die startMailClient.bat, um das Mail Programm zu starten
	-) Im Browser folgende URL öffnen: http://localhost:8080/swagger-ui.html#/
	-) Verschiedene Endpoints mit verschiedener Funktionalität und mit Try it Out und anschließend ausfüllen der Formulare und Execute zum Ausführen:
	-) Öffnen von mail-configuration-controller (Konfigurieren von E-Mail Inhalt)
		-) Ansehen des derzeitigen E-Mail Inhaltes: /mail/configuration
		-) Hinzufügen von Anhängen: /mail/configuration/attachment
		-) Änderung von Betreff und E-Mail Text: /mail/configuration/subjecttext
		-) Zurücksetzen von Betreff, E-Mail Text und Anhängen: /mail/configuration/reset
		-) Ansehen aller Ordner des Absender E-Mail Accounts: /mail/sender/folders
		-) Ansehen des derzeitigen Ordners des Absenders, wo die gesendeten E-Mails gespeichert werden: /mail/sender/folder
		-) Ändern des derzeitigen Ordners des Absenders, wo die gesendeten E-Mails gespeichert werden: /mail/sender/folder/{folderName}
		-) Einstellen des Intervalles zwischen den Versenden von den E-Mails wegen Überlastung: /mail/configuration/async/interval
	-) Öffnen von excel-configuration-controller (Konfigurieren von Excel Empfänger extrahieren)
		-) Ansehen des derzeitigen Excel Extraktion Einstellungen: /excel/configuration
		-) Änderung von Name und Email Excel Spalten Index: /mail/configuration/column/nameemail
		-) Zurücksetzen von Excel Konfiguration: /excel/configuration/reset
	-) Öffnen von mail-send-controller (ACHTUNG: Senden von Mails)
		-) ACHTUNG mit dem Ausführen (Execute) werden echte E-Mails an die Empfänger der Excel Liste gesendet
		-) Asynchrones Senden von den vorher eingestellen Betreff, Text und Anhängen an die E-Mail Adressen aus der Excel (xlsl) Datei - schneller aber auch etwas mehr fehleranfällig: /mail/send/async
		-) Synchrones Senden von den vorher eingestellen Betreff, Text und Anhängen an die E-Mail Adressen aus der Excel (xlsl) Datei - langsamer aber sicher: /mail/send/sync
	
Beenden:
	-) CMD Fenster schließen
	-) STRG + C mehrmals drücken und mit Y bestätigen