## REST API für JWT und User-Management
Mit Spring 5 und Spring Security 5

Beispielapplikation für JWT und Authentifizierung

### JWT
URL: /users/token
Methode: POST
* benötigt eine Authentifizierung mittels HTTP-Basic, Rolle "ROLE_USER"
* liefert einen Json-Web-Token zurück

### User-Management
**URL: /users**
Methode: POST
* neuer Benutzer speichern

**URL: /users/username**
Methode: GET
* benötigt eine Authentifizierung mittels HTTP-Basic, Rolle "ROLE_ADMIN"
* Benutzerinformationen

Methode: PUT
* benötigt eine Authentifizierung mittels HTTP-Basic, Rolle "ROLE_ADMIN"
* Benutzerdaten aktualisieren

Methode: DELETE
* benötigt eine Authentifizierung mittels HTTP-Basic, Rolle "ROLE_ADMIN"
* Benutzer löschen

