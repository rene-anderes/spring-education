Einfache Beispielapplikation "Kochbuch"

Technologie
	Spring DI
	Spring REST
	Spring HATEOAS
	Spring Data (JPA)
	Spring Testframework
	BeanValidation 1.0

Projekt bauen (war File erstellen)
	mvn clean install
	
Dabei werden alle Test's mittels der In-Memory-Datenbank von Apache Derby durchgeführt.


Projekt auf Web-App-Server deployen (z.B. Tomcat):
Vorbereitung:
1) Es wird eine Derby-Datenbank benötigt. Dazu wird der Derby Network-Server gestartet. 
	Unter Windows mit folgendem Aufruf: [DERBY_HOME]\bin\startNetworkServer.bat

2) mvn flyway:clean 
3) mvn flyway:migrate
4) mvn clean install
5) war-File auf Server deployen

Danach ist die REST-Schnittstelle mittels Pfad 'http://localhost:8080/spring-jpa-rest/recipes' erreichbar.
