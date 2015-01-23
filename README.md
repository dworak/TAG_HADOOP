Na komputerach roboczych zainstalowany jest Hadoop w wersji 2.5.1 w katalogu /usr/local/hadoop-2.5.1. Procesy HDFS i YARN powinny być uruchomione.
Jako wstępne ćwiczenie możesz uruchomić przykład z wykładu (i jednocześnie tutoriala) WordCount:

cd /usr/local/hadoop-2.5.1
bin/hadoop jar /usr/local/hadoop-2.5.1/share/hadoop/mapreduce/hadoop-mapreduce-examples-2.5.1.jar wordcount /input/tag/logs/small.log [katalog_wyjsciowy]

Po zakończeniu obliczeń sprawdź wyniki:
bin/hdfs dfs -cat [katalog_wyjsciowy]/*

UWAGA: Dobre miejsce na katalogi wyjściowe to /tmp. Zadania mogą również próbować tworzyć katalog wynikowy i próba zapisania do już istniejącego katalogu może skończyć się niepowodzeniem.

W czasie wykonywania zadania można wejść na strony z informacjami o systemie:

    HDFS - http://grid223-20:50070/
    YARN - http://grid223-20:8088


Opis zadania zaliczeniowego:

Treść zadania jest bardzo zbliżona do przykładu WordCount. Możesz zacząć pracę od niego.

Do kompilacji projektu (do archiwum JAR) potrzebne będą biblioteki (archiwa JAR) z podkatalogów /usr/local/hadoop-2.5.1/share/hadoop/*. Dla ułatwienia można wykorzystać gotowy projekt środowiska NetBeans (patrz poniżej).

    Na systemie HDFS w katalogu '/input/tag/logs' znajdują się 3 pliki z wygenerowanymi wpisami dziennika zdarzeń: huge.log, big.log, medium.log i small.log.
    Powyższe pliki zawierają wygenerowane logi serwerowe dla 4 użytkowników (bob, alice, user1, user2) w formacie (pola oddzielone są tabulatorem):
    nazwa_hosta uzytkownik data nazwa_operacji wynik_operacji czas_operacji.
    Napisz i uruchom program MapReduce, który dla podanego jako parametr użytkownika policzy łączne czasy jego operacji na każdym unikalnym serwerze (hoście).
    Zacznij ćwiczenie od najmniejszego pliku.
    Jako zaliczenie należy przeanalizować plik huge.log.
    Napisz i uruchom drugie obliczenia MapReduce (może być w tym samym programie), które dla podanego jako parametr użytkownika policzy łączne czasy jego operacji w każdym dniu. Zadanie powinno wykorzystać kilka reduktorów (Job.setNumReduceTasks), ale wynik powinien być umieszczony w jednym pliku ! W tym celu nie ustawiaj jednak liczby reduktorów na 1, ale stwórz 2. zadanie, które przetworzy wyniki pierwszego w celu ich scalenia.
    Jako format wyjściowy pierwszego zadania (i wejściowy drugiego) użyj SequenceFileOutputFormat !

Uruchamianie projektu NetBeans:

    Uruchom środowisko NetBeans poprzez polecenie:
    /usr/local/netbeans/bin/netbeans &
    Pobierz archiwum z projektem tag-hadoop-netbeans-project.zip
    Rozpakuj archiwum (np. do /home/student/NetBeansProjects) i otwórz projekt w IDE (File/Open project...);
    W projekcie jest już kod źródłowy WordCount i ustawione niezbędne biblioteki potrzebne do realizacji zadania. Budowanie projektu tworzy w podkatalogu projektu ./dist JAR z kodem gotowym do uruchomienia.
    Skompilowany program można uruchomić poprzez hadoop jar podając nazwę klasy, np.: hadoop jar tag-hadoop.jar tag.hadoop.WordCount /input/tag/logs/ [katalog_wyjsciowy]
    
998 cd Pulpit/Dworak/tag-hadoop/
999 usr/local/hadoop-2.5.1/bin/hadoop jar dist/tag-hadoop.jar /input/tag/logs/huge.log /tmp/dworak12_v3
1000 /usr/local/hadoop-2.5.1/bin/hadoop jar dist/tag-hadoop.jar /input/tag/logs/huge.log /tmp/dworak12_v3
1001 /usr/local/hadoop-2.5.1/bin/hadoop jar dist/tag-hadoop.jar /input/tag/logs/huge.log /tmp/dworak12_v3 bob
1002* /usr/local/hadoop-2.5.1/bin/hadoop jar dist/tag-hadoop.jar /input/tag/logs/huge.log /tmp/dworak12 bob
1003 /usr/local/hadoop-2.5.1/bin/hdfs dfs -cat /tmp/dworak12_v3 | less
1004 /usr/local/hadoop-2.5.1/bin/hdfs dfs -cat /tmp/dworak12_v3/* | less
1005 /usr/local/hadoop-2.5.1/bin/hdfs dfs -cat /tmp/dworak12_v3_output/* | less
1006 history 


