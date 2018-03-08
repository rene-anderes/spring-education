## Web-Applikation absichern mit Spring Security 5 ohne Spring MVC

**Ziel**: Wird eine Web-Seite aufgerufen für die eine Berechtigung notwendig ist, wird auf ein entsprechendes Login-Formular umgeleitet (sofern der Benutzer nicht schon angemeldet ist).

Zur Authentifizierung wird ein eigener [AuthenticationProvider](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/authentication/AuthenticationProvider.html) eingesetzt. Der ist hierbei nur sehr schlicht gehalten, um das Prinzip zu verdeutlichen.

### Links:
* [Spring Security Architecture](https://spring.io/guides/topicals/spring-security-architecture/)