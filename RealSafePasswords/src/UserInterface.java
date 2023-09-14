import SafePasswords.Accounts.AVLTreeAccounts;
import SafePasswords.Accounts.AccountManager;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class UserInterface {
    private final AccountManager<String> _accountManager; // Add a field to store the instance
    private boolean transfer = true;
    public UserInterface(AccountManager<String> accountManager) { // Accept the instance as a constructor argument
        _accountManager = accountManager;
    }

    public synchronized void userInterface() {
        Scanner scanner = new Scanner(System.in);
        boolean exiting = false;
        boolean setMaster = false;


        System.out.println("Welcome to SafePasswords. Please enter your name.");
        String name = scanner.nextLine();
        AVLTreeAccounts<String> account = _accountManager.get(name);
        if (account == null || account.getName() == null) {
            System.out.println("No account with this name is registered. Would you like to register? (Y/N)");
            String input = scanner.nextLine().toLowerCase();
            if (input.equals("n")) {
                exiting = true;  // Exit the loop and end the program
                transfer = false;
                System.out.println("Thank you for using SafePasswords. Exiting complete.");
            } else {
                System.out.println("Please choose a master password.");
                String masterPassword = scanner.nextLine();
                _accountManager.put(name, masterPassword);
                System.out.println("You are now registered.");
                account = _accountManager.get(name);
                setMaster = true;
            }
        }

        if (exiting) {
            while (transfer) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
            }
            transfer = true;

            AccountManager<String> returnPacket = _accountManager;
            notifyAll();
            return;
        }
        if (setMaster) {
            System.out.println("Authorization complete.");
            commandLoop(exiting, scanner, account);
        }
        else {
            System.out.println("Please enter your master password.");
            String masterPassword = scanner.nextLine();
            while (!masterPassword.equals(account.getMasterPassword())) {
                System.out.println("Incorrect. Please try again.");
                masterPassword = scanner.nextLine();
            }
            System.out.println("Authorization complete.");
            commandLoop(exiting, scanner, account);
        }

    }
    public synchronized void commandLoop(boolean exiting, Scanner scanner, AVLTreeAccounts<String> account) {
            while (!exiting) {
                System.out.println("Please choose a command: 1. Add new website, 2. Delete website, 3. Get websites, " +
                        "4. Get password, 5. Check duplicate passwords, 6. Generate random password, 7. Exit");
                String[] commands = {"1", "2", "3", "4", "5", "6", "7"};
                String action = scanner.nextLine();

                if (action.equals("7")) {
                    exiting = true;
                    transfer = false;
                    System.out.println("Thank you for using SafePasswords. Exiting complete.");
                }
                if (action.equals("1")) {
                    System.out.println("Please enter the website.");
                    String website = scanner.nextLine();
                    System.out.println("Please enter your desired password.");
                    String password = scanner.nextLine();
                    account.setPasswords(account.getPasswords().insert(_accountManager.hash(website), website, password));
                    System.out.println("New password added.");
                }
                if (action.equals("2")) {
                    System.out.println("Please enter the website you would like to remove.");
                    String website = scanner.nextLine();
                    account.setPasswords(account.getPasswords().remove(_accountManager.hash(website)));
                    if (account.getPasswords() == null) {
                        System.out.println("Website does not exist.");
                    } else {
                        System.out.println("Website deleted.");
                    }
                }
                if (action.equals("3")) {
                    Set<String> websites = account.getPasswords().keySet();
                    if (websites == null) {
                        System.out.println("You have no websites registered.");
                    } else {
                        System.out.println("Your websites:");
                        for (int i = 0; i < websites.size(); i++) {
                            System.out.println(websites.toArray()[i]);
                        }
                    }
                }
                if (action.equals("4")) {
                    System.out.println("Please enter the website you would like to search for.");
                    String website = scanner.nextLine();
                    Object password = account.getPasswords().search(_accountManager.hash(website)).getPassword();
                    if (password == null) {
                        System.out.println("Website does not exist.");
                    } else {
                        System.out.println(website + " : " + password);
                    }
                }
                if (action.equals("5")) {
                    System.out.println("Please enter the password you would like to check.");
                    String password = scanner.nextLine();
                    List<String> duplicates = account.getPasswords().checkDuplicate(password);
                    if (duplicates == null) {
                        System.out.println("No website uses that password.");
                    } else {
                        System.out.println("Websites using that password:");
                        for (int i = 0; i < duplicates.toArray().length; i++) {
                            System.out.println(duplicates.get(i));
                        }
                    }
                }

                if (action.equals("6")) {
                    int length = Integer.parseInt(scanner.nextLine());
                    System.out.println("Please enter the length you wish this password to be.");
                    System.out.println(_accountManager.generateRandomPassword(length));
                }

                if (!Arrays.asList(commands).contains(action)) {
                    System.out.println("Command not found.");
                }
            }
            while (transfer) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    System.err.println("Thread Interrupted");
                }
            }
            transfer = true;

            AccountManager<String> returnPacket = _accountManager;
            notifyAll();
        }
    }