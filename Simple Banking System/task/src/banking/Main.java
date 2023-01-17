package banking;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

enum MenuOpt {
    CREATE(1),
    LOG(2),
    EXIT(3),
    DEFAULT(4);
    final int value;

    MenuOpt(int value) {
        this.value = value;
    }


}

enum BankingSystem {
    BIN("400000");
    final String value;

    BankingSystem(String value) {
        this.value = value;
    }
}

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static List<Account> accounts = new ArrayList<>();

    public static void main(String[] args) {
        int inputUser;
        do {
            showMenu();
            inputUser = inputUserMenu();
            menuBank(inputUser);
        } while (inputUser != 0);

    }

    public static void showMenu() {
        System.out.printf("1. Create an account%n2. Log into account%n0. Exit%n");
    }

    public static int inputUserMenu() {
        return scanner.nextInt();
    }

    public static void menuBank(int userInput) {

        MenuOpt option = menu(userInput);
        switch (option) {
            case CREATE -> createBankAccount();
            case LOG -> logAccount();
            case EXIT -> System.out.println("TODO exit");
            default -> System.out.println("Invalid input!");
        }
    }

    public static MenuOpt menu(int userInput) {
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

    public static void createBankAccount() {
        boolean validCard = false;

        String cardNumber;
        String pinNumber;
        do {
            cardNumber = BankingSystem.BIN.value.concat(randomNumberAccountGenerate());
            validCard = searchAccount(cardNumber);
            pinNumber = randomPinGenerate();
        } while (validCard);

        accounts.add(new Account(cardNumber, pinNumber));
        showSuccesFullMessage(cardNumber, pinNumber);
    }

    public static void showSuccesFullMessage(String cardNumber, String pin) {
        System.out.printf("Your card has been created%nYour card number:%n%s%nYour card PIN:%n%s%n", cardNumber, pin);
    }

    public static boolean searchAccount(String cardNumber) {
        for (Account x : accounts) {
            if (x.getCardNumber().equals(cardNumber)) {
                return true;
            }
        }
        return false;
    }

    public static boolean searchPinNumber(String pinNumber) {
        for (Account x : accounts) {
            if (x.getCardPIN().equals(pinNumber)) {
                return true;
            }
        }
        return false;
    }

    public static String randomNumberAccountGenerate() {
        Random random = new Random();
        int max = 11000;
        int min = 10000;

        int account1 = random.nextInt(max - min + 1) + min;
        int account2 = random.nextInt(max - min + 1) + min;

        return account1 + "" + account2;
    }

    public static String randomPinGenerate() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);
    }

    public static void logAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter your card number:%n");
        String cardNumber = sc.nextLine();
        System.out.printf("Enter your pin number:%n");
        String pinNumber = sc.nextLine();

        if (!searchAccount(cardNumber) && searchPinNumber(pinNumber)) {
            System.out.printf("Wrong card number or PIN!%n");
        } else {
            System.out.printf("You have successfully logged in!%n");
            menuUserLogged(cardNumber);
        }
    }

    public static void menuUserLogged(String cardNumber) {
        System.out.printf("1. Balance%n2. Log out%n0. Exit%n");
        switch (scanner.nextInt()) {
            case 1:
                balance(cardNumber);
                break;
            case 2:
                System.out.println("You have successfully logged out!");
            case 0:
                System.out.println("Exit");
        }
    }

    public static void balance(String cardNumber) {
        Account account = accounts.stream()
                .filter(x -> x.getCardNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
        System.out.printf("Balance: %f%n", account.getBalance());
    }
}

class Account {
    private String cardNumber;
    private String cardPIN;
    private double balance;

    public Account(String account, String pin) {
        this.cardNumber = account;
        this.cardPIN = pin;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardPIN() {
        return cardPIN;
    }

    public void setCardPIN(String cardPIN) {
        this.cardPIN = cardPIN;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "account='" + cardNumber + '\'' +
                ", pin='" + cardPIN + '\'' +
                '}';
    }
}