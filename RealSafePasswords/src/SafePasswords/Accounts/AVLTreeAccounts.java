package SafePasswords.Accounts;

import SafePasswords.Passwords.AVLTreePasswords;

public class AVLTreeAccounts<V extends Comparable<V>> {
    int _value;
    String _name;
    String _masterPassword;
    AVLTreePasswords<V> _passwords;
    AVLTreeAccounts<V> _leftChild;
    AVLTreeAccounts<V> _rightChild;
    int _height;
    int _size;

    public AVLTreeAccounts(int value, String name, String password) {
        _value = value;
        _name = name;
        _masterPassword = password;
        _passwords = new AVLTreePasswords<>();
        _leftChild = new AVLTreeAccounts<>();
        _rightChild = new AVLTreeAccounts<>();
        _height = 0;
        _size = 1;
    }

    public AVLTreeAccounts() {
        _value = -1;
        _name = null;
        _masterPassword = null;
        _passwords = null;
        _leftChild = null;
        _rightChild = null;
        _height = -1;
        _size = 0;
    }

    public AVLTreeAccounts<V> insert(int element, String name, String password) {
        if (getValue() == 0) {
            // Empty tree, create a new node with the given value
            setValue(element);
            setRightChild(new AVLTreeAccounts<>());
            setLeftChild( new AVLTreeAccounts<>());
        } else if (element < getValue()) {
            // Element is less than current node, insert into left subtree
            if (getLeft() == null) {
                // Left child is null, create a new node with the given value
                AVLTreeAccounts<V> node = new AVLTreeAccounts<>(element, name, password);
                setLeftChild(node);
            } else {
                // Left child exists, recursively insert into left subtree
                setLeftChild(_leftChild.insert(element, name, password));
            }
        } else {
            // Element is greater than or equal to current node, insert into right subtree
            if (getRight() == null) {
                // Right child is null, create a new node with the given value
                AVLTreeAccounts<V> node = new AVLTreeAccounts<>(element, name, password);
                setRightChild(node);
            } else {
                // Right child exists, recursively insert into right subtree
                setRightChild(_rightChild.insert(element, name, password));
            }
        }
        _size = size();
        return balance();
    }

    public AVLTreeAccounts<V> rotateRight() {
        if (getValue() != 0) {
            AVLTreeAccounts<V> newRoot = getLeft();
            AVLTreeAccounts<V> rightSubtree = newRoot.getRight();

            // Perform the rotation
            newRoot.setRightChild(this);
            setLeftChild(rightSubtree);


            // Update heights
            height();
            newRoot.height();

            return newRoot;
        }
        return null;
    }


    public AVLTreeAccounts<V> rotateLeft() {
        if (getValue() != 0) {
            AVLTreeAccounts<V> newRoot = getRight();
            AVLTreeAccounts<V> leftSubtree = newRoot.getLeft();

            // Perform the rotation
            newRoot.setLeftChild(this);
            setRightChild(leftSubtree);


            // Update heights
            height();
            newRoot.height();

            return newRoot;
        }
        return null;
    }


    public boolean isEmpty() {
        return size() == 0;
    }


    public int height() {
        int leftHeight = getLeft() != null ? getLeft().height() : -1;
        int rightHeight = getRight() != null ? getRight().height() : -1;
        _height = Math.max(leftHeight, rightHeight) + 1;
        return _height;
    }


    public int size() {
        if (getValue() == -1) {
            return 0;
        } else {
            int leftSize = (getLeft() == null) ? 0 : getLeft().size();
            int rightSize = (getRight() == null) ? 0 : getRight().size();
            return 1 + leftSize + rightSize;
        }
    }


    public int getBalanceFactor() {
        int leftHeight = getLeft() != null ? getLeft().height() : -1;
        int rightHeight = getRight() != null ? getRight().height() : -1;
        return leftHeight - rightHeight;
    }


    private AVLTreeAccounts<V> balance() {
        height();

        // Check if the tree is balanced
        int balanceFactor = getBalanceFactor();
        if (balanceFactor > 1) {
            // Left subtree is heavier, perform a right rotation or a left-right rotation
            if (getLeft().getBalanceFactor() < 0) {
                setLeftChild(getLeft().rotateLeft());
            }
            return rotateRight();
        } else if (balanceFactor < -1) {
            // Right subtree is heavier, perform a left rotation or a right-left rotation
            if (getRight().getBalanceFactor() > 0) {
                setRightChild(getRight().rotateRight());
            }
            return rotateLeft();
        } else {
            // The tree is already balanced, return the current node
            return this;
        }
    }


    public AVLTreeAccounts<V> remove(int element) {
        if (getValue() == -1) {
            return null;
        }
        return remove(this, element);
    }

    private AVLTreeAccounts<V> remove(AVLTreeAccounts<V> currentTree, int element) {
        if (currentTree == null) {
            return null;
        }

        if (element < currentTree.getValue()) {
            currentTree.setLeftChild(remove(currentTree.getLeft(), element));
        }
        else if (element > currentTree.getValue()) {
            currentTree.setRightChild(remove(currentTree.getRight(), element));
        }
        else {
            if (currentTree.getLeft().isEmpty() && currentTree.getRight().isEmpty()) {
                return null;
            }
            else if (currentTree.getLeft().isEmpty()) {
                return currentTree.getRight();
            }
            else if (currentTree.getRight().isEmpty()) {
                return currentTree.getLeft();
            }
            AVLTreeAccounts<V> temp = currentTree.getRight().findMin1();
            currentTree.setValue(temp.getValue());
            currentTree.setName(temp.getName());
            currentTree.setRightChild(remove(currentTree.getRight(), temp.getValue()));
        }
        _size = size();
        return currentTree.balance();
    }



    public AVLTreeAccounts<V> findMin1() {
        AVLTreeAccounts<V> current = this;
        while (current.getLeft() != null && current.getLeft().getValue() != -1) {
            current = current.getLeft();
        }
        return current;
    }


    public AVLTreeAccounts<V> search(int value) {
        AVLTreeAccounts<V> currentTree = this;
        if (getValue() == -1) {
            return currentTree;
        }
        if (value < getValue()) {
            currentTree = getLeft().search(value);
        }
        else if (value > getValue()) {
            currentTree = getRight().search(value);
        }
        return currentTree;
    }


    public boolean contains(int element) {
        if (getValue() == -1) {
            // Empty tree, element not found
            return false;
        } else if (element == getValue()) {
            // Element is equal to current node, element found
            return true;
        } else if (element < getValue()) {
            // Element is less than current node, search in left subtree
            if (getLeft() == null) {
                // Left child is null, element not found
                return false;
            } else {
                // Left child exists, recursively search in left subtree
                return _leftChild.contains(element);
            }
        } else {
            // Element is greater than current node, search in right subtree
            if (getRight() == null) {
                // Right child is null, element not found
                return false;
            } else {
                // Right child exists, recursively search in right subtree
                return _rightChild.contains(element);
            }
        }
    }



    // Setters
    public void setValue(int value) {
        _value = value;
    }

    public void setPasswords(AVLTreePasswords<V> passwords) {
        _passwords = passwords;
    }

    public void setLeftChild(AVLTreeAccounts<V> leftChild) {
        _leftChild = leftChild;
    }

    public void setRightChild(AVLTreeAccounts<V> rightChild) {
        _rightChild = rightChild;
    }

    public void setName(String name) { _name = name;}
    public void setMasterPassword(String masterPassword) {
        _masterPassword = masterPassword;
    }

    // Getters
    public int getValue() {
        return _value;
    }

    public AVLTreeAccounts<V> getLeft() {
        return _leftChild;
    }

    public AVLTreeAccounts<V> getRight() {
        return _rightChild;
    }

    public String getName() {
        return _name;
    }

    public String getMasterPassword() {
        return _masterPassword;
    }

    public AVLTreePasswords<V> getPasswords() { return _passwords;}

}
