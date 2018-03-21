## REST-Schnittstelle mittels JWT absichern
Spring 5 und Spring Security 5

Beispielapplikation die zeigt, wie man eine REST API mittel JWT, Spring Security 5 und Spring 5 erstellen und absichern kann.

### Employees
Zur Demonstration gibt es eine REST API

**URL: /employees/{Mitarbeitername}**

Es kann ein beliebiger Mitarbeitername gewählt werden

Methode: GET
* Liefert ein JSON mit Vor- und Nachname zurück.
* Braucht keinen JWT

Methode: DELETE
* Braucht einen gültigen JWT, Role 'ROLE_ADMIN'
* Liefert Status 200 zurück
* Löscht den entsprechenden Mitarbeiter

Methode: POST
* Braucht einen gültigen JWT, Role 'ROLE_ADMIN'
* Erstellt den neuen Mitarebieter
* Liefert Status 201 zurück
