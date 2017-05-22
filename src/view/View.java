package view;

import addon.FileChooser;
import addon.Results;
import addon.TimeTracker;
import enums.Algorithm;
import enums.Task;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * Klasa reprezentująca widok
 *
 * @author Tobiasz Rumian.
 */
public class View {
    private Results results = new Results();//Obiekt zawierający wyniki testów.
    private addon.Task task;

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
                    "3. Wykonaj pełny test\n" +
                    "0. Wyjscie", false);

            switch (select("Podaj numer zadania:", 0, 3)) {
                case 1: task = new addon.Task(Task.MST);
                    break;
                case 2: task = new addon.Task(Task.NSWG);
                    break;
                case 3: createTests();
                    return;
                case 0: return;
            }
            selectTask();
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
    private static int select(String message, Integer min, Integer max) {
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
    private static void message(String message, Boolean error) {
        if (error) System.err.println(message + "\n");
        else System.out.println(message + "\n");
    }

    /**
     * Funkcja pozwalająca na wybranie zadania wykonywanego na strukturze.
     */
    private void selectTask() {
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
                    task.clear();
                    loadFromFile();
                    message(task.toString(), false);
                    break;
                case 2:
                    task.clear();
                    int graphOrder = select("Podaj ilość wierzchołków", 0, Integer.MAX_VALUE);
                    task.generateRandomGraph(graphOrder, select("Podaj gęstość", 0, 100));
                    if (task.getTypeOfTask() == Task.NSWG)
                        task.setStartVertex(select("Podaj wierzchołek początkowy", 0, graphOrder - 1));
                    message(task.toString(), false);
                    break;
                case 3:
                    message(task.toString(), false);
                    break;
                case 4:
                    message(task.getAlgorithm(chooseAlgorithm()), false);
                    break;
                case 0:
                    return;
            }
        } while (true);
    }

    /**
     * Funkcja pozwalająca na załadowanie danych z pliku tekstowego do struktury.
     * Wyświetla okno pozwalające na wybór pliku.
     */
    private void loadFromFile() {
        FileChooser fileChooser = new FileChooser();
        if (fileChooser.getPath() == null) return;
        ArrayList<String> arrayList = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(fileChooser.getPath()))) {
            stream.filter(x -> !x.equals("")).forEach(arrayList::add);
        } catch (IOException e) {
            e.getMessage();
        }
        String x = arrayList.get(0);
        task.setGraphSize(Integer.parseInt(x.substring(0, x.indexOf(" "))));
        x = x.substring(x.indexOf(" ") + 1, x.length());
        if (task.getTypeOfTask() == Task.MST) task.createStructures(Integer.parseInt(x));
        else {
            task.createStructures(Integer.parseInt(x.substring(0, x.indexOf(" "))));
            x = x.substring(x.indexOf(" ") + 1, x.length());
            task.setStartVertex(Integer.parseInt(x));
        }
        arrayList.remove(0);
        for (String s : arrayList) {
            int start = Integer.parseInt(s.substring(0, s.indexOf(" ")));
            s = s.substring(s.indexOf(" ") + 1, s.length());
            int end = Integer.parseInt(s.substring(0, s.indexOf(" ")));
            s = s.substring(s.indexOf(" ") + 1, s.length());
            int weight = Integer.parseInt(s);
            task.addToStructures(start, end, weight);
        }
    }

    /**
     * Funkcja pozwalająca na wykonanie testów na strukturze.
     * @param algorithm Wykorzystany algorytm.
     * @param graphOrder Ilość wierzchołków w grafie.
     * @param density Gęstosć grafu.
     * @param matrix true jeżeli algorytm jest testowany dla macierzy.
     * @return Zwraca czas wykonania algorytmu.
     */
    private BigDecimal test(Algorithm algorithm, int graphOrder, int density, boolean matrix) {
        TimeTracker timeTracker = new TimeTracker();
        addon.Task task;
        Random random=new Random();
        if (algorithm == Algorithm.PRIM || algorithm == Algorithm.KRUSKAL) task = new addon.Task(Task.MST);
        else task = new addon.Task(Task.NSWG);
        task.generateRandomGraph(graphOrder, density);
        if (task.getTypeOfTask() == Task.NSWG) task.setStartVertex(random.nextInt(graphOrder - 1));
        timeTracker.start();
        task.testAlgorithm(algorithm, matrix);
        return timeTracker.getElapsedTime();

    }

    /**
     * Funkcja pozwalająca na wykonanie pełnych testów.
     */
    private void createTests() {
        final int howManyRepeats = 100;
        int[] graphOrders = {10, 30, 50, 70, 100};
        int[] densitys = {25, 50, 75, 99};
        boolean[] matrixes = {true, false};
        String label;
        Algorithm[] algorithms = {Algorithm.PRIM, Algorithm.KRUSKAL, Algorithm.DIJKSTR, Algorithm.BELLMAN_FORD};//Algorithm.PRIM, Algorithm.KRUSKAL, Algorithm.DIJKSTR, Algorithm.BELLMAN_FORD
        BigDecimal time;

        for (Algorithm algorithm : algorithms) {
            for (int graphOrder : graphOrders) {
                for (int density : densitys) {
                    for (boolean matrix : matrixes) {
                        time = new BigDecimal(0).setScale(0,RoundingMode.CEILING);
                        for (int i = 0; i < howManyRepeats; i++) {
                            time = time.add(test(algorithm, graphOrder, density, matrix));
                            System.gc();
                            showProgress(i, howManyRepeats,time.longValue(), "     " +
                                    algorithm.toString() + "  " + density + "  " +
                                    graphOrder + "  " + matrix + "  ");
                        }
                        time = time.divide(BigDecimal.valueOf(howManyRepeats), RoundingMode.UP);
                        label = algorithm.toString() + "\t" + graphOrder + "\t" + density + "\t" + matrix;
                        message(time.toString(), false);
                        results.add(label, time.longValue());
                    }
                }
            }
            results.save();
        }
        results.save();
        results.clear();
    }

    /**
     * Funkcja pozwalająca na wybór, przez użytkownika, algorytmu.
     * Wybór odbywa się w konsoli.
     *
     * @return Zwraca wybrany algorytm.
     */
    private Algorithm chooseAlgorithm() {
        View.message("Dostępne algorytmy", false);
        View.message("1. " + task.getAvailableAlgorithms()[0].toString(), false);
        View.message("2. " + task.getAvailableAlgorithms()[1].toString(), false);
        return task.getAvailableAlgorithms()[View.select("Podaj numer wybranego algorytmu", 1, 2) - 1];
    }

    /**
     * Funkcja pozwalająca na zobaczenie postępu.
     * @param now Wartość chwilowa.
     * @param end Wartość końcowa.
     * @param time Czas wykonywania obliczenia.
     * @param label Dodatkowa informacja do wyświetlenia.
     */
    private void showProgress(int now, int end, long time,String label) {
        StringBuilder string = new StringBuilder(140);
        int percent = (now * 100 / end);
        string
                .append('\r')
                .append(String.join("", Collections.nCopies(percent == 0 ? 2 : 2 - (int) (Math.log10(percent)), " ")))
                .append(String.format(" %d%% [", percent))
                .append(String.join("", Collections.nCopies(percent, "=")))
                .append('>')
                .append(String.join("", Collections.nCopies(100 - percent, " ")))
                .append(']')
                .append(String.format(" %d/%d    %d    %s", now, end,time,label));
        System.out.print(string);
    }

    public static void main(String[] args) {
        new View();
    }
}
