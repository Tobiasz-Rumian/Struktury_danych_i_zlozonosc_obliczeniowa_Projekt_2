package addon;

import view.View;

/**
 * Ustawienia
 *
 * @author Tobiasz Rumian
 */
public class Settings {
    private static Integer howManyElements = 1000;
    private static Integer howManyRepeats = 1000;

    public static Integer getHowManyElements() {
        return howManyElements;
    }

    public static Integer getHowManyRepeats() {
        return howManyRepeats;
    }

    public static String message() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(View.title("ustawienia")).append("\n")
                .append("Ilosc elementów: ").append(howManyElements).append("\n")
                .append("Ilosc powtórzeń: ").append(howManyRepeats).append("\n");
        return stringBuilder.toString();
    }

    public static void changeSettings() {
        View.message("Co chcesz zmienić?", false);
        View.message("1. Ilość elementów\n" + "2. Ilość powtórzeń\n", false);
        switch (View.select("Wybierz liczbę",1,2)){
            case 1:Settings.howManyElements=View.select("Podaj liczbę elementów",1,Integer.MAX_VALUE);
                break;
            case 2:Settings.howManyRepeats=View.select("Podaj liczbę powtórzeń",1,Integer.MAX_VALUE);
                break;
        }
    }
    public static void setSettings(int howManyElements,int howManyRepeats){
        Settings.howManyElements=howManyElements;
        Settings.howManyRepeats=howManyRepeats;
    }
}
