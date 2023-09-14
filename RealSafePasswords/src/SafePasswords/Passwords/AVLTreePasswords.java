package SafePasswords.Passwords;

import java.util.*;

public class AVLTreePasswords<V extends Comparable<V>> {
    int _value;
    String _website;
    V _password;
    AVLTreePasswords<V> _leftChild;
    AVLTreePasswords<V> _rightChild;
    int _height;
    int _size;

    public AVLTreePasswords(int value, String website, V password) {
        _value = value;
        _website = website;
        _password = password;
        _leftChild = new AVLTreePasswords<>();
        _rightChild = new AVLTreePasswords<>();
        _height = 0;
        _size = 1;
    }

    public AVLTreePasswords() {
        _value = -1;
        _website = null;
        _leftChild = null;
        _rightChild = null;
        _height = -1;
        _size = 0;
    }

    public AVLTreePasswords<V> insert(int element, String website, V password) {
        if (getValue() == -1) {
            // Empty tree, create a new node with the given value
            setValue(element);
            setPassword(password);
            setWebsite(website);
            setRightChild(new AVLTreePasswords<>());
            setLeftChild( new AVLTreePasswords<>());
        } else if (element < getValue()) {
            // Element is less than current node, insert into left subtree
            if (getLeft() == null) {
                // Left child is null, create a new node with the given value
                AVLTreePasswords<V> node = new AVLTreePasswords<>(element, website, password);
                setLeftChild(node);
            } else {
                // Left child exists, recursively insert into left subtree
                setLeftChild(_leftChild.insert(element, website, password));
            }
        } else if (element > getValue()) {
            // Element is greater than or equal to current node, insert into right subtree
            if (getRight() == null) {
                // Right child is null, create a new node with the given value
                AVLTreePasswords<V> node = new AVLTreePasswords<>(element, website, password);
                setRightChild(node);
            } else {
                // Right child exists, recursively insert into right subtree
                setRightChild(_rightChild.insert(element, website, password));
            }
        }
        else {
            setPassword(password);
        }
        _size = size();
        return balance();
    }

    public AVLTreePasswords<V> rotateRight() {
        if (getValue() != 0) {
            AVLTreePasswords<V> newRoot = getLeft();
            AVLTreePasswords<V> rightSubtree = newRoot.getRight();

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


    public AVLTreePasswords<V> rotateLeft() {
        if (getValue() != 0) {
            AVLTreePasswords<V> newRoot = getRight();
            AVLTreePasswords<V> leftSubtree = newRoot.getLeft();

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


    private AVLTreePasswords<V> balance() {
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


    public AVLTreePasswords<V> remove(int element) {
        if (getValue() == -1) {
            return null;
        }
        return remove(this, element);
    }

    private AVLTreePasswords<V> remove(AVLTreePasswords<V> currentTree, int element) {
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
                return currentTree.getRight().balance();
            }
            else if (currentTree.getLeft().isEmpty()) {
                return currentTree.getRight();
            }
            else if (currentTree.getRight().isEmpty()) {
                return currentTree.getLeft();
            }
            AVLTreePasswords<V> temp = currentTree.getRight().findMin1();
            currentTree.setValue(temp.getValue());
            currentTree.setWebsite(temp.getWebsite());
            currentTree.setPassword(temp.getPassword());
            currentTree.setRightChild(remove(currentTree.getRight(), temp.getValue()));
        }
        _size = size();
        return currentTree.balance();
    }


    public AVLTreePasswords<V> findMin1() {
        AVLTreePasswords<V> current = this;
        while (current.getLeft() != null && current.getLeft().getValue() != -1) {
            current = current.getLeft();
        }
        return current;
    }


    public AVLTreePasswords<V> search(int value) {
        AVLTreePasswords<V> currentTree = this;
        if (getValue() == -1) {
            return new AVLTreePasswords<>();
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

    public Set<String> keySet() {
        AVLTreePasswords<V> current = this;
        if (current.getPassword() == null) {
            return null;
        }
        int i = 0;
        Set<String> keySet = new HashSet<>();
        List<AVLTreePasswords<V>> traversal = BFS(current);

        for (AVLTreePasswords<V> avlTree : traversal) {
            keySet.add(avlTree.getWebsite());
        }

        return keySet;
    }

    private List<AVLTreePasswords<V>> BFS(AVLTreePasswords<V> root) {
        List<AVLTreePasswords<V>> traversal = new ArrayList<>();

        if (root == null) {
            return traversal;
        }

        Queue<AVLTreePasswords<V>> queue = new LinkedList<>();
        queue.offer(root);

        while (!queue.isEmpty()) {
            int size = queue.size();

            for (int i = 0; i < size; i++) {
                AVLTreePasswords<V> curr = queue.poll();
                traversal.add(curr);

                if (curr.getLeft().getWebsite() != null) {
                    queue.offer(curr.getLeft());
                }

                if (curr.getRight().getWebsite() != null) {
                    queue.offer(curr.getRight());
                }
            }
        }

        return traversal;
    }
    public List<String> checkDuplicate(String password) {
        AVLTreePasswords<V> current = this;
        if (current.getPassword() == null) {
            return null;
        }
        List<String> duplicates = new ArrayList<>();
        List<AVLTreePasswords<V>> traversal = BFS(current);

        for (AVLTreePasswords<V> avlTree : traversal) {
            if (avlTree.getPassword().equals(password)) {
                duplicates.add(avlTree.getWebsite());
            }
        }

        return duplicates;
    }


    // Setters
    public void setValue(int value) {
        _value = value;
    }


    public void setLeftChild(AVLTreePasswords<V> leftChild) {
        _leftChild = leftChild;
    }

    public void setRightChild(AVLTreePasswords<V> rightChild) {
        _rightChild = rightChild;
    }

    public void setWebsite(String website) {
        _website = website;
    }

    public void setPassword(V password) {
        _password = password;
    }
    // Getters
    public int getValue() {
        return _value;
    }

    public AVLTreePasswords<V> getLeft() {
        return _leftChild;
    }

    public AVLTreePasswords<V> getRight() {
        return _rightChild;
    }

    public String getWebsite() {
        return _website;
    }

    public V getPassword() {
        return _password;
    }

}
