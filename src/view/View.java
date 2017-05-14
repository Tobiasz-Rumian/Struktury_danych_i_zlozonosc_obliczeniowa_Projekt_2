package view;

import addon.FileChooser;
import addon.Results;
import enums.Task;
import representation.AdjacencyLists;
import representation.AdjacencyMatrix;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Klasa reprezentująca widok
 *
 * @author Tobiasz Rumian.
 */
public class View {

    private static Random random = new Random();//Generator pseudolosowy
    private Results results = new Results();//Obiekt zawierający wyniki testów.
    private AdjacencyMatrix adjacencyMatrix;
    private AdjacencyLists adjacencyLists;

    /**
     * Główna pętla programu, wyświetla główne menu.
     */
    private View() {
        message("Witaj " + System.getProperty("user.name") + "." + "\n" +
                "Uruchomiles projekt nr 2 autorstwa Tobiasza Rumiana." + "\n" +
                "Rozsiadz sie wygodnie i wybierz co chcesz zrobic.", false);
        while (true) {
            message(View.title("menu glowne") +
                    "1. Wyznaczanie minimalnego drzewa rozpinającego\n" +
                    "2. Wyznaczanie najkrótszej ścieżki w grafie\n" +
                    "3. Wyznaczanie maksymalnego przepływu\n" +
                    "4. Wykonaj pełny test\n" +
                    "0. Wyjscie", false);

            switch (select("Podaj numer zadania:", 0, 4)) {
                case 1:selectTask(Task.MST);
                    break;
                case 2: selectTask(Task.NSWG);
                    break;
                case 3: selectTask(Task.MP);
                    break;
                case 4: //TODO:Pełny test;
                    return;
                case 0: return;
            }

        }
    }

    /**
     * Funkcja formatująca tytuł.
     *
     * @param title Tekst do sformatowania.
     * @return Zwraca sformatowany tytuł.
     */
    public static String title(String title) {
        return "===========" + title.toUpperCase() + "===========\n";
    }

    /**
     * Funkcja obsługują wybieranie numeru przez użytkownika.
     * Obsługuje wybór z konsoli.
     *
     * @param message Wiadomość do wyświetlenia użytkownikowi.
     * @param min     Minimalna akceptowalna wartość.
     * @param max     Maksymalna akceptowalna wartość.
     * @return Zwraca odpowiedź użytkownika.
     */
    public static int select(String message, Integer min, Integer max) {
        do {
            try {
                message(message, false);
                Scanner in = new Scanner(System.in);
                int i = Integer.parseInt(in.nextLine());
                if (i <= max && i >= min) return i;
            } catch (NumberFormatException ignored) {
            }
        } while (true);
    }

    /**
     * Funkcja pozwalająca wyświetlić wiadomość na ekranie.
     *
     * @param message Wiadomość do wyświetlenia.
     * @param error   Jeżeli true, wyświetla wiadomość jako błąd.
     */
    public static void message(String message, Boolean error) {
        if (error) System.err.println(message + "\n");
        else System.out.println(message + "\n");
    }

    /**
     * Funkcja pozwalająca na wybranie zadania wykonywanego na strukturze.
     */
    private void selectTask(Task task) {
        do {
            message(
                    View.title("wybor zadania") +
                            "1. Wczytaj z pliku" + "\n" +
                            "2. Wygeneruj losowo" + "\n" +
                            "3. Wyświetl graf" + "\n" +
                            "4. Uruchom algorytm" + "\n" +
                            "0. Wyjscie", false);

            switch (View.select("Podaj numer zadania:", 0, 4)) {
                case 1:

                    loadFromFile(structure);
                    message(structure.show(), false);
                    break;
                case 2:
                    structure.clear();
                    loadFromFile(structure);
                    message(structure.show(), false);
                    break;
                case 3:
                    View.message(View.title("odejmowanie"), false);
                    if (structure.getClass() == Table.class)
                        ((Table) structure).subtract(select("Podaj indeks", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    else if (structure.getClass() == BidirectionalList.class)
                        ((BidirectionalList) structure).subtract(select("Podaj wartość", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    else structure.subtract(place, select("Podaj wartość", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    message(structure.show(), false);
                    break;
                case 4:
                    View.message(View.title("dodawanie"), false);
                    if (structure.getClass() == Table.class)
                        ((Table) structure).add(select("Podaj indeks", Integer.MIN_VALUE, Integer.MAX_VALUE),
                                select("Podaj wartość", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    else if (structure.getClass() == BidirectionalList.class)
                        ((BidirectionalList) structure).add(select("Podaj indeks", Integer.MIN_VALUE, Integer.MAX_VALUE),
                                select("Podaj wartość", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    else structure.add(place, select("Podaj wartość", Integer.MIN_VALUE, Integer.MAX_VALUE));
                    message(structure.show(), false);
                    break;
                case 5:
                    View.message(View.title("znajdowanie"), false);
                    if (structure.find(View.select("Podaj liczbe", Integer.MIN_VALUE, Integer.MAX_VALUE)))
                        message("Liczba znajduje się w strukturze", false);
                    else message("Liczba nie znajduje się w strukturze", true);
                    message(structure.show(), false);
                    break;
                case 6:
                    message(structure.show(), false);
                    break;
                case 7:
                    results.clear();
                    while (true) {
                        message(View.title("test") +
                                "1. Generuj populację struktury\n" +
                                "2. Usun ze struktury\n" +
                                "3. Wyszukaj w strukturze\n" +
                                "4. Ustawienia\n" +
                                "5. Pokaż wyniki\n" +
                                "0. Zakończ test\n", false);
                        int select = View.select("Podaj numer zadania:", 0, 5);
                        if (select == 0) break;
                        if (select == 1 || select == 2) {
                            if (structure.getClass() == Table.class || structure.getClass() == BidirectionalList.class)
                                place = choosePlace("Podaj miejsce, w które chcesz wstawiać");
                            else place = Place.NULL;
                        }
                        test(select, place);
                    }
                    break;
                case 0:
                    return;
            }
        } while (true);
    }

    /**
     * Funkcja zwracająca wartość pseudolosową z podanego przedziału.
     *
     * @param min Minimalna wartość.
     * @param max Maksymalna wartość.
     * @return Zwraca liczbę pseudolosową z podanego przedziału.
     */
    public static Integer getRandom(Integer min, Integer max) {

        return random.nextInt(max - min) + min;
    }

    /**
     * Funkcja pozwalająca na załadowanie danych z pliku tekstowego do struktury.
     * Wyświetla okno pozwalające na wybór pliku.
     * Usuwa pierwszy wyraz, gdyż według specyfikacji projektowej, pierwsza wartość oznacza ilość elementów.
     *
     * @param structure Struktura, do której mają być załadowane dane.
     */
    private void loadFromFile(Task task) {
        FileChooser fileChooser = new FileChooser();
        if (fileChooser.getPath() == null) return;
        ArrayList<String> arrayList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileChooser.getPath()))) {
            stream.filter(x -> !x.equals("")).forEach(arrayList::add);
        } catch (IOException e) {
            e.getMessage();
        }
        String x = arrayList.get(0);
        int graphSize=Integer.parseInt(x.substring(0,x.indexOf(" ")));
        x=x.substring(x.indexOf(" ")+1,x.length());
        int graphOrder=Integer.parseInt(x.substring(0,x.indexOf(" ")));
        x=x.substring(x.indexOf(" ")+1,x.length());
        if(task==Task.NSWG){
            int startVertice = Integer.parseInt(x);
        }else if(task==Task.MP){
            int startVertice=Integer.parseInt(x.substring(0,x.indexOf(" ")));
            x=x.substring(x.indexOf(" ")+1,x.length());
            int endVertice=Integer.parseInt(x.substring(0,x.indexOf(" ")));
        }
        adjacencyLists=new AdjacencyLists(graphOrder);
        adjacencyMatrix=new AdjacencyMatrix(graphOrder);
        arrayList.remove(0);
        for (String s:arrayList) {
            int start=Integer.parseInt(s.substring(0,s.indexOf(" ")));
            s=s.substring(s.indexOf(" ")+1,s.length());
            int end=Integer.parseInt(s.substring(0,s.indexOf(" ")));
            s=s.substring(s.indexOf(" ")+1,s.length());
            int weight=Integer.parseInt(s.substring(0,s.indexOf(" ")));
            adjacencyMatrix.add(start,end,weight);
            adjacencyLists.add(start,end,weight);
        }
        //TODO: Dokończyć, pozostało przekazanie pozostałych parametrów.
    }

    /**
     * Funkcja pozwalająca na wykonanie testów na strukturze.
     *
     * @param task  Numer zadania do wykonania.
     * @param place Miejsce dodania/usunięcia ze struktury (wykorzystywane tylko dla list i tablic).
     */
    /*private void test(int task, Place place) {
        TimeTracker tracker = new TimeTracker();

        String label;
        BigDecimal resultTime = new BigDecimal(0);
        PopulationGenerator populationGenerator;
        message(Messages.messageTest(), false);
        switch (task) {
            case 1://Generuj populację struktury
                for (int i = 0; i < getHowManyRepeats(); i++) {
                    message(showProgress(i, getHowManyRepeats()) + "     " +
                            structure.toString() + "  " + getHowManyElements() + "  " +
                            place.toString() + " Dodawanie", false);

                    populationGenerator = new PopulationGenerator();
                    if (structure.getClass() == Table.class) {
                        Table table = new Table();
                        table.addAll(populationGenerator.getPopulation());
                        table.subtract(Place.END, 0);
                        int rand = random.nextInt();
                        tracker.start();
                        table.add(place, rand);
                        resultTime = resultTime.add(tracker.getElapsedTime());
                    } else {
                        if (place == Place.RANDOM) {
                            for (int j = 0; j < populationGenerator.getPopulation().length - 1; j++)
                                structure.add(Place.END, populationGenerator.getPopulation()[j]);
                            int add = populationGenerator.getPopulation()[populationGenerator.getPopulation().length - 1];
                            tracker.start();
                            structure.add(place, add);
                            resultTime = resultTime.add(tracker.getElapsedTime());
                            structure.clear();
                        } else {
                            for (int j = 0; j < populationGenerator.getPopulation().length - 1; j++)
                                structure.add(place, populationGenerator.getPopulation()[j]);
                            int add = populationGenerator.getPopulation()[populationGenerator.getPopulation().length - 1];
                            tracker.start();
                            structure.add(place, add);
                            resultTime = resultTime.add(tracker.getElapsedTime());
                            structure.clear();
                        }
                    }
                }
                resultTime = resultTime.divide(BigDecimal.valueOf(getHowManyRepeats()), RoundingMode.UP);
                label = structure.toString() + "\t" + "Dodawanie" + "\t" + place.toString() + "\t" + getHowManyElements() + "\t" + getHowManyRepeats();
                message(resultTime.toString(), false);
                results.add(label, resultTime.longValue());
                break;
            case 2://Usun ze struktury
                for (int i = 0; i < getHowManyRepeats(); i++) {
                    message(showProgress(i, getHowManyRepeats()) + "     " +
                            structure.toString() + "  " + getHowManyElements() + "  " +
                            place.toString() + " Odejmowanie", false);

                    populationGenerator = new PopulationGenerator();
                    if (structure.getClass() == Table.class) {
                        Table table = new Table();
                        table.addAll(populationGenerator.getPopulation());
                        tracker.start();
                        table.subtract(place, 0);
                        resultTime = resultTime.add(tracker.getElapsedTime());
                    } else {
                        for (int k = 0; k < populationGenerator.getPopulation().length; k++)
                            structure.add(Place.END, populationGenerator.getPopulation()[k]);
                        int rand = random.nextInt();
                        tracker.start();
                        structure.subtract(place, rand);
                        resultTime = resultTime.add(tracker.getElapsedTime());
                        structure.clear();
                    }
                }
                resultTime = resultTime.divide(BigDecimal.valueOf(getHowManyRepeats()), RoundingMode.UP);
                label = structure.toString() + "\t" + "Usuwanie" + "\t" + place.toString() + "\t" +
                        getHowManyElements() + "\t" + getHowManyRepeats();
                message(resultTime.toString(), false);
                results.add(label, resultTime.longValue());
                break;
            case 3://Wyszukaj w strukturze
                for (int i = 0; i < getHowManyRepeats(); i++) {
                    message(showProgress(i, getHowManyRepeats()) + "     " +
                            structure.toString() + "  " + getHowManyElements() + " Wyszukiwanie", false);

                    populationGenerator = new PopulationGenerator();
                    if (structure.getClass() == Table.class) {
                        Table table = (Table) structure;
                        table.addAll(populationGenerator.getPopulation());
                        int rand = random.nextInt();
                        tracker.start();
                        table.find(rand);
                        resultTime = resultTime.add(tracker.getElapsedTime());
                    } else {
                        for (int k = 0; k < populationGenerator.getPopulation().length; k++)
                            structure.add(Place.END, populationGenerator.getPopulation()[k]);
                        int rand = random.nextInt();
                        tracker.start();
                        structure.find(rand);
                        resultTime = resultTime.add(tracker.getElapsedTime());
                        structure.clear();
                    }
                }
                resultTime = resultTime.divide(BigDecimal.valueOf(getHowManyRepeats()), RoundingMode.UP);
                label = structure.toString() + "\t" + "Wyszukiwanie" + "\t" + "-" + "\t" +
                        getHowManyElements() + "\t" + getHowManyRepeats();
                message(resultTime.toString(), false);
                results.add(label, resultTime.longValue());
                break;
            case 4://Ustawienia
                Settings.message();
                changeSettings();
                break;
            case 5://Pokaż wyniki
                message(results.show(), false);
                break;
            case 0://Zakończ test
                break;
        }
    }*/

    /**
     * Funkcja pozwalająca na wybór, przez użytkownika, miejsca wstawienia danych.
     * Wybór odbywa się w konsoli.
     *
     * @param label Tekst, który ma zostać wyświetlony użytkownikowi.
     * @return Zwraca wybrane miejsce.
     */
    private Place choosePlace(String label) {
        Place place = Place.NULL;
        View.message("Gdzie odjac liczbe?", false);
        View.message("1. Poczatek", false);
        View.message("2. Koniec", false);
        View.message("3. Losowo", false);
        Integer i = View.select(label, 1, 3);
        if (i.equals(1)) place = Place.START;
        else if (i.equals(2)) place = Place.END;
        else if (i.equals(3)) place = Place.RANDOM;
        return place;
    }

    /**
     * Funkcja pozwalająca na zobaczenie postępu.
     *
     * @param now Wartość chwilowa.
     * @param end Wartość końcowa.
     * @return Zwraca postęp w postaci paska oraz procentu w postaci ułamka.
     */
    private String showProgress(Integer now, Integer end) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        Integer percent = (int) ((now * 100.0f) / end);
        for (int i = 0; i <= percent; i++) stringBuilder.append("=");
        for (int i = 0; i <= 100 - percent; i++) stringBuilder.append(" ");
        stringBuilder.append("]");
        stringBuilder.append(" ").append(percent).append("%");
        return stringBuilder.toString();
    }

    /**
     * Funkcja pozwalająca na wykonanie pełnych testów.
     */
    private void fullTest() {
        int[] howMany = {2000, 4000, 6000, 8000, 10000};
        Structure[] structures = {new BstTree()};//new Table(),new BidirectionalList(), new BinaryHeap(), new BstTree()
        Place[] places = {Place.START, Place.END, Place.RANDOM};//Place.START, Place.END, Place.RANDOM
        int[] tests = {1, 2, 3};//1, 2, 3
        for (Structure structure : structures) {
            this.structure = structure;
            for (int test : tests) {
                if ((this.structure.getClass() == Table.class && test != 3) || (this.structure.getClass() == BidirectionalList.class && test != 3)) {
                    for (Place place : places) {
                        for (int how : howMany) {
                            setSettings(how, getHowManyRepeats());
                            test(test, place);
                        }
                        results.save();
                    }
                } else {
                    for (int how : howMany) {
                        setSettings(how, getHowManyRepeats());
                        test(test, Place.NULL);
                    }
                    results.save();
                }
            }
        }
        results.save();
        results.clear();
    }

    public static void main(String[] args) {
        new View();
    }
}
