package banking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

enum MenuOpt {
    CREATE(1),
    LOG(2),
    EXIT(0),
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

    Connect conn;
    Main(String fileName){
        this.conn=new Connect(fileName);
        conn.createTable();
    }
    public static void main(String[] args) {
        //String fileName = args.length == 0 ? "database.db" : args[1];
        String fileName = "card.s3db";
        Main main = new Main(fileName);
        int inputUser;
        do {
            main.showMenu();
            inputUser = main.inputUserMenu();
            main.menuBank(inputUser);
        } while (inputUser != 0);


        System.out.println("Bye!");

    }
    public  void showMenu() {
        System.out.printf("1. Create an account%n2. Log into account%n0. Exit%n");
    }

    public  int inputUserMenu() {
        return scanner.nextInt();
    }

    public void menuBank(int userInput) {

        MenuOpt option = menu(userInput);
        switch (option) {
            case CREATE -> createBankAccount();
            case LOG -> logAccount();
            case EXIT -> System.out.println("TODO exit");
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
    public void createBankAccount() {

        boolean validCard;
        boolean lunh;
        String cardNumber;
        String pinNumber;

        do {
            cardNumber = BankingSystem.BIN.value.concat(randomNumberAccountGenerate());
            validCard = searchAccount(cardNumber);
            lunh = luhnAlgorithm(cardNumber);
            pinNumber = randomPinGenerate();
        } while (validCard || !lunh);

        conn.insert(cardNumber,pinNumber);
        accounts.add(new Account(cardNumber, pinNumber));
        showSuccesFullMessage(cardNumber, pinNumber);
    }
    public static void showSuccesFullMessage(String cardNumber, String pin) {
        System.out.printf("Your card has been created%nYour card number:%n%s%nYour card PIN:%n%s%n", cardNumber, pin);
    }
    public static boolean searchAccount(String cardNumber) {
        for (Account x : accounts) {
            if (cardNumber.equals(x.getCardNumber())) {
                return true;
            }
        }
        return false;
    }
    public static boolean searchPinNumber(String pinNumber) {
        for (Account x : accounts) {
            if (pinNumber.equals(x.getCardPIN())) {
                return true;
            }
        }
        return false;
    }

    public static String randomNumberAccountGenerate() {
        Random random = new Random();
        int max = 90000;
        int min = 80000;

        int account1 = random.nextInt(max - min + 1) + min;
        int account2 = random.nextInt(max - min + 1) + min;

        return account1 + "" + account2;
    }

    public static String randomPinGenerate() {
        Random random = new Random();
        return String.valueOf(random.nextInt(9999 - 1000 + 1) + 1000);
    }

    public  void logAccount() {
        Scanner sc = new Scanner(System.in);
        System.out.printf("Enter your card number:%n");
        String cardiNumber = sc.nextLine();
        System.out.printf("Enter your pin number:%n");
        String pinNumber = sc.nextLine();

        if (searchAccount(cardiNumber) && searchPinNumber(pinNumber)) {
            System.out.printf("You have successfully logged in!%n");
            int opt;
            do {
                opt = menuUserLogged(cardiNumber);
            } while (opt>0 && opt<4);

        } else {
            System.out.printf("Wrong card number or PIN!%n");
        }
    }

    public int menuUserLogged(String cardNumber) {
        System.out.printf("1. Balance%n2. Add income%n3. Do transfer%n4. Close account%n5. Log out%n0. Exit%n");
        int opt = scanner.nextInt();
        switch (opt) {
            case 1:
                balance(cardNumber);

                break;
            case 2: setBalance(cardNumber);
                break;
            case 3:// TODO: do transfer
                break;
            case 4: //TODO: close account
                break;
            case 5:
                System.out.println("You have successfully logged out!");
                break;
            case 0:
                System.out.println("Bye!");
                System.exit(0);
        }
        return opt;
    }

    public void balance(String cardNumber) {
        /*
        Account account = accounts.stream()
                .filter(x -> x.getCardNumber().equals(cardNumber))
                .findFirst()
                .orElse(null);
        System.out.printf("Balance: %d%n", account.getBalance());

         */
        System.out.printf("Balance: %d%n",conn.selectBalancebyCardNumber(cardNumber));
    }
    public void setBalance(String cardNumber){
        System.out.println("Enter income:");
        int income = scanner.nextInt();

        conn.updateBalance(cardNumber,income);
        System.out.println("Income was added!");

    }


    public static boolean luhnAlgorithm(String numberCard) {
        String[] array = numberCard.split("");
        int[] numbers = changeValues(array);
        int sum = 0;
        for (int i = 0; i < numbers.length - 1; i++) {
            numbers[i] = (i+1) % 2 != 0 ? numbers[i] * 2: numbers[i];
            numbers[i] = numbers[i] > 9 ? numbers[i]-9 : numbers[i];
            sum += numbers[i];
        }
        sum += numbers[numbers.length - 1];
        return sum % 10 == 0;
    }

    public static int[] changeValues(String[] array) {
        int[] numbers = new int[array.length];

        for (int i = 0; i < array.length; i++) {
            numbers[i] = Integer.parseInt(array[i]);
        }
        return numbers;
    }
}
class Connect{
    private String url;
    Connect(String fileName){
        this.url="jdbc:sqlite:" + fileName;
    }


    public void createTable() {
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("CREATE TABLE IF NOT EXISTS card (" +
                            "id INTEGER PRIMARY KEY," +
                            "number TEXT NOT NULL," +
                            "pin TEXT NOT NULL," +
                            "balance INTEGER DEFAULT 0" +
                            ");");
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void insert(String cardNumber,String pinNumber) {
        try (Connection connection = DriverManager.getConnection(url)) {
            if (connection != null) {
                String query = "INSERT INTO card (number, pin) VALUES (?, ?);";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, cardNumber);
                    preparedStatement.setString(2, pinNumber);
                    preparedStatement.executeUpdate();
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public  void updateBalance(String cardNumber, int balance){
        String sql = "UPDATE card SET balance = ? "
                + "WHERE number = ?";

        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            // set the corresponding param
            preparedStatement.setInt(1, balance);
            preparedStatement.setString(2, cardNumber);
            // update
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    public int selectBalancebyCardNumber(String cardNumber){
        String sql = "SELECT balance "
                + "FROM card WHERE number > ?";
        int balance = 0;
        try (Connection connection = DriverManager.getConnection(url);
             PreparedStatement preparedStatement = connection.prepareStatement(sql)){

            // set the value
            preparedStatement.setString(1,cardNumber);
            //
            ResultSet rs  = preparedStatement.executeQuery();

            // loop through the result set
            while (rs.next()) {

                balance=rs.getInt("balance");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return balance;
    }

}
class Account {
    private String cardNumber;
    private String cardPIN;
    private int balance;

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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
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
