package banking;

import java.util.Random;
import java.util.Scanner;

enum MenuOpt{
    CREATE(1),
    LOG(2),
    EXIT(3),
    DEFAULT(4);
    int value ;
    MenuOpt(int value){
        this.value=value;
    }

    public int getValue() {
        return value;
    }
}
enum BankingSystem{
    BIN("400000");
    String value;
    BankingSystem(String value){
        this.value = value;
    }
}
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        showMenu();
        int inputUser=inputUserMenu(scanner);
        menuBank(inputUser);
    }
    public static void showMenu(){
        System.out.printf("1. Create an account%n2. Log into account%n0. Exit%n");

    }
    public static int inputUserMenu(Scanner scanner){
        return scanner.nextInt();
    }
    public static void menuBank(int userInput){

        MenuOpt option = menu(userInput);
        switch (option){
            case CREATE -> System.out.println("TODO create method");
            case LOG -> System.out.println("TODO Log method");
            case EXIT -> System.out.println("TODO exit");
            default -> System.out.println("Invalid input!");
        }
    }
    public static MenuOpt menu(int userInput){
        switch (userInput) {
            case 1 -> {
                return MenuOpt.CREATE;
            }
            case 2 -> {
                return MenuOpt.LOG;
            }
            case 3 -> {
                return MenuOpt.EXIT;
            }
            default -> {
                return MenuOpt.DEFAULT;
            }
        }
    }
    public static String randomNumberAccountGenerate(){
        Random random = new Random();
        return " ";
    }
}