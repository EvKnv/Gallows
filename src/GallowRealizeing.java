import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GallowRealizeing {

    static String separator = File.separator;

    static File file = new File("Gallows" +separator+ "Source" +separator+ "WordsStorage.txt");

    static ArrayList <String> storageWords = new ArrayList<>();
    static private Random randomNumber = new Random();

    static private int number = 0;
    static private int life = 0;
    static private boolean checkLife = false;
    static private int MAX_MISTAKES_AMOUNT = 4;

    static Scanner dataScannerEntered = new Scanner(System.in);
    static String word = new String();
    static StringBuilder cloneOfWord = new StringBuilder();
    static private String[][] gallow = new String[7][7];

    static char usedLetters[] = new char[33];
    static private int usedLetterCounter = 0;

    private static int helpLetter = 3;

    public static void main(String[] args) throws FileNotFoundException {
        do {
            additionWordsInGame(file);
            startLoopGame();
        }while(restartGame());
        System.out.println("Игра окончена!");
    }

    public static void additionWordsInGame(File file) throws FileNotFoundException {
        Scanner scannerFiles = new Scanner(file);
        while(scannerFiles.hasNextLine()) {
            storageWords.add(scannerFiles.nextLine());
        }
        scannerFiles.close();
    }

    public static String additionDataInGame() throws FileNotFoundException {
        boolean result = false;
        String incertData;

        do {
            incertData = new String(dataScannerEntered.nextLine());

            for (char a : incertData.toCharArray()) {
                if (incertData.length() == 1 && Character.UnicodeBlock.of(a) == Character.UnicodeBlock.CYRILLIC || a == '1' || a == '0' || a == '3') {
                    result = true;
                    break;
                }
                System.out.println("Введите один киррилический символ или цифру 3(Подсказка)");
                break;
            }
        }while(result == false);

        return incertData;
    }

    public static void startLoopGame() throws FileNotFoundException {
        System.out.println("-----------------------------------------");
        System.out.println("Новая игра \n");
        restartUsedLettersStorage();
        life = 0;
        usedLetterCounter = 0;
        helpLetter = 3;
        number = randomNumber.nextInt(storageWords.size());
        gallowBuilding(life);
        findWord();
    }

    public static void restartUsedLettersStorage() {
        for(int i = 0; i < usedLetters.length; i++) {
            usedLetters[i] = ' ';
        }
    }

    public static void findWord() throws FileNotFoundException  {
        word = new String(storageWords.get(number).trim());
        crateCloneOfWord();
    }

    public static void crateCloneOfWord() throws FileNotFoundException  {
        cloneOfWord = new StringBuilder();
        for(int i = 0; i < word.length(); i++) {
            cloneOfWord = cloneOfWord.append('*');
        }
        guessingLetter();
    }

    public static void guessingLetter() throws FileNotFoundException  {
        gameInfo();

        char enteredLetter = choisePlayer();

        enteredLetter = gameOption(enteredLetter);

        usedLetters[usedLetterCounter] = enteredLetter;
        usedLetterCounter++;

        for(int i = 0; i < word.length(); i++) {
            if(word.charAt(i) == enteredLetter) {
                cloneOfWord = cloneOfWord.insert(i, enteredLetter).deleteCharAt(i+1);
                checkLife = true;
            }
        }
        checkLifeCounter();

        if(checkGameEnding()) {
            guessingLetter();
        }
    }

    public static char choisePlayer() throws FileNotFoundException {
        char newLetter = ' ';

        while(checkRepeatUsedLetter(newLetter)) {
            String choise = new String(additionDataInGame().toLowerCase());
            newLetter = choise.charAt(0);
        }
        return newLetter;
    }

    public static char gameOption(char enteredLetter) throws FileNotFoundException {
        if(enteredLetter == '3') {
            if(helpLetter == 0) {
                System.out.println("Вы использовали все подсказки!");
                guessingLetter();
            }
            helpLetter--;
            return enteredLetter = openOneLetterInMask();
        }
        return enteredLetter;
    }

    public static void checkLifeCounter() {
        if(checkLife == false) {
            life++;
        }
        checkLife = false;
        gallowBuilding(life);
    }

    public static boolean checkGameEnding() {
        String checkCloneOfWord = new String(cloneOfWord);

        if(life == MAX_MISTAKES_AMOUNT) {
            showGallow();
            System.out.println("Вы проиграли! \n" + "Заданное слово было: " + word + ".\n");
            System.out.println("-----------------------------------------");
            return false;
        }
        if(word.equals(checkCloneOfWord) == true) {
            System.out.println(word);
            System.out.println("Вы победили! \n");
            System.out.println("-----------------------------------------");
            return false;
        }
        return true;
    }

    public static boolean restartGame() throws FileNotFoundException {
        System.out.println("Начать игру заново? \n" + "1 - Начать заново. \n" + "0 - Закончить игру.");
        String digit;
        do {
            digit = new String(additionDataInGame());
            if(digit.equals("1")){
                return true;
            }
            if(digit.equals("0")){
                return false;
            }
        }while(digit.equals("1") || digit.equals("0"));
        return false;
    }

    public static void gallowBuilding(int life) {
        switch(life) {
            case 0:
                for(int i = 0; i < gallow.length; i++) {
                    for(int b = 0; b < gallow.length; b++) {
                        gallow[i][b] = " ";
                    }
                }
                break;
            case 1:
                gallow[0][0] = " ";
                gallow[1][0] = "|";
                gallow[2][0] = "|";
                gallow[3][0] = "|";
                gallow[4][0] = "|";
                gallow[5][0] = "|";
                gallow[6][0] = "|";
                break;
            case 2:
                gallow[0][1] = "_";
                gallow[0][2] = "_";
                gallow[0][3] = "_";
                gallow[0][4] = "_";
                break;
            case 3:
                gallow[1][4] = "|";
                break;
            case 4:
                gallow[2][4] = "O";
                gallow[3][4] = "|";
                gallow[4][4] = "|";
                gallow[3][3] = "/";
                gallow[3][5] = "|";
                gallow[5][4] = "|";
                break;
        }
    }

    public static void gameInfo() {
        System.out.println("-----------------------------------------");
        int numberOfAttmepts = MAX_MISTAKES_AMOUNT - life;

        System.out.print("Вы вводили буквы: ");
        for(char c  : usedLetters) {
            System.out.print(c + " ");
        }
        System.out.printf("\n" + "Осталось попыток-" + numberOfAttmepts + "\n");
        showGallow();
        System.out.println("\n" + cloneOfWord);
        System.out.println("Введите одну букву");
        System.out.println("Для использования подсказки введите 3");
        System.out.println("Осталось подсказок-" + helpLetter );
        System.out.println("-----------------------------------------");
    }

    public static void showGallow() {
        for(int i = 0; i < 7; i++) {
            for(int b = 0; b < 7; b++) {
                System.out.printf(gallow[i][b]);
            }
            System.out.println();
        }
    }

    public static boolean checkRepeatUsedLetter(char newLetter) {
        for(int i = 0; i < usedLetters.length; i++) {
            if(newLetter == ' ') {
                return true;
            }
            if(usedLetters[i] == newLetter) {
                System.out.println("Вы уже вводили эту букву!");
                return true;
            }
        }
        return false;
    }

    public static char openOneLetterInMask() {
        int numberOpenletter;
        do {
            numberOpenletter = randomNumber.nextInt(word.length());
        }while(cloneOfWord.charAt(numberOpenletter) != '*');
        return word.charAt(numberOpenletter);
    }
}


