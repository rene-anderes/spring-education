Maven Archetype für Spring (DI), Junit 5. Mockito

Archetype bauen:

Vom Maven-Projektordner den folgenden Befehl eingeben:
	mvn clean archetype:create-from-project

Da diese Goal default sind, kann auch einfach mvn eingegeben werden.	

Im gleichen Projekt unter “target/generated-sources/archetype” findet sich nun das generierte archetype-Projekt.

Im generierten Archetype können nun noch weitere Modifikationen eingebaut werden. Beispielsweise: Variablen für Versionen, Artefaktname, schon mal allgemeine Hilfsklasen oder Testklassen eintragen.

Wenn das neue Archetype-Projekt so weit fertig ist, kann es mit “mvn install” in das lokale Repository importiert werden.
Der Befehl muss aus “Ursprungsprojekt/target/generated-sources/archetype” aufgerufen werden.