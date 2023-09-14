package SafePasswords.Accounts;

import java.util.*;

public class AccountManager<V extends Comparable<V>> {
        private static final String MASTER_PASSWORD = "HEishope2023!";
        AVLTreeAccounts<V>[] _accounts;

        public AccountManager() {
                _accounts = new AVLTreeAccounts[50];
        }
        public void put(String name, String password) {
                int priority = hash(name);

                AVLTreeAccounts<V> current = getAccounts()[priority];

                if (current == null) {
                        getAccounts()[priority] = new AVLTreeAccounts<>(priority, name, password);
                        return;
                }
                current.insert(priority, name, password);
        }


        public AVLTreeAccounts<V> get(String name) {
                if (getAccounts() == null) {
                        return null;
                }
                int i = 0;
                return get(i, name);
        }

        private AVLTreeAccounts<V> get(int i, String name) {
                if (i >= getAccounts().length) {
                        return null;
                }

                AVLTreeAccounts<V> current = getAccounts()[i];

                if (current != null) {
                        AVLTreeAccounts<V> returnedName = current.search(hash(name));
                        if (returnedName.getName() != null) {
                                return returnedName;
                        }
                }

                return get(i + 1, name);
        }



        public int accountsSize() {
                if (getAccounts() == null) {
                        return 0;
                }
                int i = 0;
                int size = 0;
                return accountsSize(i, size);
        }

        private int accountsSize(int i, int size) {
                AVLTreeAccounts<V> current = getAccounts()[i];
                size += current.size();
                if (i == getAccounts().length - 1) {
                        return size;
                }
                return accountsSize(i + 1, size);
        }

        public Set<String> keySet() {
                if (getAccounts() == null) {
                        return null;
                }
                int i = 0;
                Set<String> keySet = new HashSet<>();
                return keySet(i, keySet);
        }

        private Set<String> keySet(int i, Set<String> keySet) {
                if (i == getAccounts().length - 1) {
                        return keySet;
                }
                AVLTreeAccounts<V> current = getAccounts()[i];
                List<AVLTreeAccounts<V>> traversal = BFS(current);

                for (AVLTreeAccounts<V> avlTree : traversal) {
                        keySet.add(avlTree.getName());
                }

                return keySet(i + 1, keySet);
        }

        public AVLTreeAccounts<V> removeAccount(String website) {
                AVLTreeAccounts<V> current = getAccounts()[hash(website)];
                AVLTreeAccounts<V> removedNode = current.remove(hash(website));
                getAccounts()[hash(website)] = removedNode;

                return removedNode;
        }

        public boolean checkMasterPassword(String enteredPassword) {
                return enteredPassword.equals(MASTER_PASSWORD);
        }

        public String generateRandomPassword(int length) {
                int leftLimit = 48; // numeral '0'
                int rightLimit = 122; // letter 'z'
                Random random = new Random();

                return random.ints(leftLimit, rightLimit + 1)
                        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                        .limit(length)
                        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                        .toString();
        }


        public AVLTreeAccounts<V>[] getAccounts() {
                return _accounts;
        }

        public int hash(String website) {
                int hash = 0;
                for (int i = 0; i < website.length(); i++) {
                        hash = (((hash << 5) - hash) + website.charAt(i)) % 50;
                }
                return hash;
        }


        private List<AVLTreeAccounts<V>> BFS(AVLTreeAccounts<V> root) {
                List<AVLTreeAccounts<V>> traversal = new ArrayList<>();

                if (root == null) {
                        return traversal;
                }

                Queue<AVLTreeAccounts<V>> queue = new LinkedList<>();
                queue.offer(root);

                while (!queue.isEmpty()) {
                        int size = queue.size();

                        for (int i = 0; i < size; i++) {
                                AVLTreeAccounts<V> curr = queue.poll();
                                traversal.add(curr);

                                if (curr.getLeft().getName() != null) {
                                        queue.offer(curr.getLeft());
                                }

                                if (curr.getRight().getName() != null) {
                                        queue.offer(curr.getRight());
                                }
                        }
                }
                return traversal;
        }
}
