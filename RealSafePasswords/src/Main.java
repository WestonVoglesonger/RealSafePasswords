import SafePasswords.Accounts.AccountManager;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        boolean running = true;
        AccountManager<String> accountManager = new AccountManager<>(); // Create a shared instance
        while (running) {
            UserInterface newThread = new UserInterface(accountManager); // Pass the instance to the thread
            newThread.userInterface();

            // Check if the user wants to restart the process
            System.out.println("Do you want to restart the SafePassword? (Y/N)");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("n")) {
                running = false; // Exit the loop and end the program
            }
        }
    }
}