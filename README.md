# InformatikQ2-Messenger

Ein kleiner und sehr bröckeliger Messenger mit Ende zu Ende Verschlüsselung für die Clients, sowie eine seperate verschlüsselung von Client zu Server.

<h1>Benutzung</h1>

Zum nutzen des Programms wird zunächst eine Instanz vom Server benötigt, welcher auf localhost:1887 läuft.
Anschließend kann man beliebig viele Clients zu dem Server verbinden (aktuell festgelegt auf localhost:1887) und mit einem Aufruf in der Konsole, "verbinde=[ip:port]", kann die Verbindung zu einem externen Client aufgenommen werden.
Der Befehl "nachricht=[Nachricht die verschickt werden soll]" definiert die Nachricht, die verschickt wird. Nach absenden des Befehls (Enter-Taste) wird nun der Empfänger eingegeben, z.B. "127.0.0.1:56373". Den Namen des Clients kann man nach dem Verbinden zum Server aus der Konsole auslesen.
