package PL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author DEI-ESINF
 * @param <E>
 */
public class BST<E extends Comparable<E>> implements BSTInterface<E> {

    /**
     * Nested static class for a binary search tree node.
     */
    protected static class Node<E> {

        private E element;          // an element stored at this node
        private Node<E> left;       // a reference to the left child (if any)
        private Node<E> right;      // a reference to the right child (if any)

        /**
         * Constructs a node with the given element and neighbors.
         *
         * @param e the element to be stored
         * @param leftChild reference to a left child node
         * @param rightChild reference to a right child node
         */
        public Node(E e, Node<E> leftChild, Node<E> rightChild) {
            element = e;
            left = leftChild;
            right = rightChild;
        }

        // accessor methods
        public E getElement() {
            return element;
        }

        public Node<E> getLeft() {
            return left;
        }

        public Node<E> getRight() {
            return right;
        }

        // update methods
        public void setElement(E e) {
            element = e;
        }

        public void setLeft(Node<E> leftChild) {
            left = leftChild;
        }

        public void setRight(Node<E> rightChild) {
            right = rightChild;
        }
    }

    //----------- end of nested Node class -----------
    protected Node<E> root = null;     // root of the tree

    /* Constructs an empty binary search tree. */
    public BST() {
        root = null;
    }

    /*
    * @return root Node of the tree (or null if tree is empty)
     */
    public Node<E> root() {
        return root;
    }

    /*
    * Verifies if the tree is empty
    * @return true if the tree is empty, false otherwise
     */
    @Override
    public boolean isEmpty() {
        return root == null;
    }

    /*
    * Inserts an element in the tree.
     */
    @Override
    public void insert(E element) {
        if (root == null) {
            root = new Node<>(null, null, null);
            root.setElement(element);
        } else {
            root = insert(element, root);

        }

    }

    private Node<E> insert(E element, Node<E> node) {
        if (element.compareTo(node.element) < 0) {
            if (node.left == null) {
                node.setLeft(new Node<>(element, null, null));
            } else {
                insert(element, node.left);
            }
        }
        if (element.compareTo(node.element) > 0) {
            if (node.right == null) {
                node.setRight(new Node<>(element, null, null));
            } else {
                insert(element, node.right);
            }
        }
        if (element.equals(node.element)) {
            return node;
        }
        return node;
    }

    /**
     * Removes an element from the tree maintaining its consistency as a Binary
     * Search Tree.
     */
    @Override
    public void remove(E element) {
        root = remove(element, root());
    }

    private Node<E> remove(E element, Node<E> node) {

        if (node == null) {
            return null;    //throw new IllegalArgumentException("Element does not exist");
        }
        if (element.compareTo(node.getElement()) == 0) {
            // node is the Node to be removed
            if (node.getLeft() == null && node.getRight() == null) { //node is a leaf (has no childs)
                return null;
            }
            if (node.getLeft() == null) {   //has only right child
                return node.getRight();
            }
            if (node.getRight() == null) {  //has only left child
                return node.getLeft();
            }
            E min = smallestElement(node.getRight());
            node.setElement(min);
            node.setRight(remove(min, node.getRight()));
        } else if (element.compareTo(node.getElement()) < 0) {
            node.setLeft(remove(element, node.getLeft()));
        } else {
            node.setRight(remove(element, node.getRight()));
        }

        return node;
    }

    /*
    * Returns the number of nodes in the tree.
    * @return number of nodes in the tree
     */
    @Override
    public int size() {
        return size(root);
    }

    private int size(Node<E> node) {
        if (node == null) {
            return 0;
        } else {
            return 1 + size(node.left) + size(node.right);
        }
    }


    /*
    * Returns the height of the tree
    * @return height 
     */
    @Override
    public int height() {
        return height(root);
    }

    /*
    * Returns the height of the subtree rooted at Node node.
    * @param node A valid Node within the tree
    * @return height 
     */
    protected int height(Node<E> node) {
        if (node == null) {
            return -1;
        } else {
            return 1 + Math.max(height(node.left), height(node.right));
        }
    }

    /**
     * Returns the smallest element within the tree.
     *
     * @return the smallest element within the tree
     */
    @Override
    public E smallestElement() {
        return smallestElement(root);
    }

    protected E smallestElement(Node<E> node) {
        if (node.getLeft() == null) {
            return node.element;
        }
        return smallestElement(node.getLeft());

    }

    public E find(E element) {
        Node<E> node = find(element, root);
        if (node == null) {
            return null;
        }
        return node.getElement();
    }

    /**
     * Returns the Node containing a specific Element, or null otherwise.
     *
     * @param element the element to find
     * @param node
     * @return the Node that contains the Element, or null otherwise
     *
     * This method despite not being essential is very useful. It is written
     * here in order to be used by this class and its subclasses avoiding
     * recoding. So its access level is protected
     */
    protected Node<E> find(E element, Node<E> node) {
        if (node == null) {
            return null;
        }
        if (node.getElement().compareTo(element) == 0) {
            return node;
        }

        if (element.compareTo(node.getElement()) < 0) {
            return find(element, node.getLeft());
        }

        return find(element, node.getRight());

    }


    /*
   * Returns an iterable collection of elements of the tree, reported in in-order.
   * @return iterable collection of the tree's elements reported in in-order
     */
    @Override
    public Iterable<E> inOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null) {
            inOrderSubtree(root, snapshot);   // fill the snapshot recursively
        }
        return snapshot;
    }

    /**
     * Adds elements of the subtree rooted at Node node to the given snapshot
     * using an in-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void inOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        inOrderSubtree(node.getLeft(), snapshot);
        snapshot.add(node.getElement());
        inOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Returns an iterable collection of elements of the tree, reported in
     * pre-order.
     *
     * @return iterable collection of the tree's elements reported in pre-order
     */
    @Override
    public Iterable<E> preOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null) {
            preOrderSubtree(root, snapshot);   // fill the snapshot recursively
        }
        return snapshot;
    }

    /**
     * Adds elements of the subtree rooted at Node node to the given snapshot
     * using an pre-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void preOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }

        snapshot.add(node.getElement());
        preOrderSubtree(node.getLeft(), snapshot);
        preOrderSubtree(node.getRight(), snapshot);
    }

    /**
     * Returns an iterable collection of elements of the tree, reported in
     * post-order.
     *
     * @return iterable collection of the tree's elements reported in post-order
     */
    @Override
    public Iterable<E> posOrder() {
        List<E> snapshot = new ArrayList<>();
        if (root != null) {
            posOrderSubtree(root, snapshot);   // fill the snapshot recursively
        }
        snapshot.add(root.getElement());
        return snapshot;
    }

    /**
     * Adds positions of the subtree rooted at Node node to the given snapshot
     * using an post-order traversal
     *
     * @param node Node serving as the root of a subtree
     * @param snapshot a list to which results are appended
     */
    private void posOrderSubtree(Node<E> node, List<E> snapshot) {
        if (node == null) {
            return;
        }
        if (node.left != null) {
            posOrderSubtree(node.getLeft(), snapshot);
            snapshot.add(node.left.getElement());
        }
        if (node.right != null) {
            posOrderSubtree(node.getRight(), snapshot);
            snapshot.add(node.right.getElement());
        }
    }

    /*
    * Returns a map with a list of nodes by each tree level.
    * @return a map with a list of nodes by each tree level
     */
    @Override
    public Map<Integer, List<E>> nodesByLevel() {
        int height = height(root);
        Map<Integer, List<E>> result = new HashMap<>();

        processBstByLevel(root, result, 0);

        return result;
    }

    private void processBstByLevel(Node<E> node, Map<Integer, List<E>> result, int level) {

        if (null == node) {
            return;
        }

        List<E> lresult;
        if (result.size() == level) {
            lresult = new LinkedList<>();
            result.put(level, lresult);
        } else {
            lresult = result.get(level);
        }

        lresult.add(node.getElement());
        processBstByLevel(node.getLeft(), result, level + 1);
        processBstByLevel(node.getRight(), result, level + 1);
    }

//#########################################################################
    /**
     * Returns a string representation of the tree. Draw the tree horizontally
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringRec(root, 0, sb);
        return sb.toString();
    }

    private void toStringRec(Node<E> root, int level, StringBuilder sb) {
        if (root == null) {
            return;
        }
        toStringRec(root.getRight(), level + 1, sb);
        if (level != 0) {
            for (int i = 0; i < level - 1; i++) {
                sb.append("|\t");
            }
            sb.append("|-------" + root.getElement() + "\n");
        } else {
            sb.append(root.getElement() + "\n");
        }
        toStringRec(root.getLeft(), level + 1, sb);
    }

} //----------- end of BST class -----------
