public class AvlTree<E extends Comparable<? super E>> {
  protected Node<E> root;
  protected static final int ALLOWED_IMBALANCE = 1;

  // Used for node implementation and contains data, left child, right child and
  // height information. Height is needed for balancing the tree.
  protected static class Node<E> {
    int height;
    Node<E> right;
    Node<E> left;
    E data;

    Node(E data, Node<E> left, Node<E> right) {
      height = 0;
      this.data = data;
      this.right = right;
      this.left = left;
    }

    Node(E data) {
      height = 0;
      this.data = data;
      this.right = null;
      this.left = null;
    }
  }

  public AvlTree() {
    root = null;
  }

  protected boolean isEmpty() {
    return root == null;
  }

  protected int height(Node<E> node) {
    return node == null ? -1 : node.height;
  }

  // Prints the tree in-order.
  public void printTree() {
    if (isEmpty()) {
      System.out.println("Empty tree");
    } else {
      printTree(root);
    }
  }

  protected void printTree(Node<E> node) {
    if (node != null) {
      printTree(node.left);
      System.out.println(node.data);
      printTree(node.right);
    }
  }

  // Compares the current node with the given node. If the given node is bigger
  // than the current node than it should be in the right subtree of the current
  // node, if it is less than the current node it should be in left subtree of the
  // current node, otherwise it is already in the tree. Balance after inserting
  // the item.
  public void insert(E x) {
    root = insert(x, root);
  }

  protected Node<E> insert(E x, Node<E> node) {
    if (node == null) {
      return new Node<E>(x, null, null);
    }

    if (x.compareTo(node.data) < 0) {
      node.left = insert(x, node.left);
    } else if (x.compareTo(node.data) > 0) {
      node.right = insert(x, node.right);
    }

    return balance(node);
  }

  public void remove(E x) {
    remove(x, root);
  }

  // If the element that is going to be removed has only left or right child just
  // replace it with this child.
  // If it has no chid, replace it with null.
  // If it has two children, find the smallest element in the right subtree(the
  // smallest element that is bigger than the current element), change the current
  // element to this element,
  // remove this element in its original place.
  protected Node<E> remove(E x, Node<E> node) {
    if (node == null) {
      return node;
    }
    if (x.compareTo(node.data) < 0) {
      node.left = remove(x, node.left);
    } else if (x.compareTo(node.data) > 0) {
      node.right = remove(x, node.right);
    } else if (node.left != null && node.right != null) {
      node.data = findMin(node.right).data;
      node.right = remove(node.data, node.right);
    } else if (node.left != null) {
      node = node.left;
    } else {
      node = node.right;
    }
    return balance(node);
  }

  public boolean contains(E x) {
    return contains(x, root);
  }

  // Compare the current node with the given node. If it is bigger than the
  // current node than go to the right child of the current node, if it is less
  // than the current node than go to the left child of the current node. If you
  // reach a null node than the given element is not in the tree.
  protected boolean contains(E x, Node<E> node) {
    if (node == null) {
      return false;
    }
    if (x.compareTo(node.data) < 0) {
      return contains(x, node.left);
    } else if (x.compareTo(node.data) > 0) {
      return contains(x, node.right);
    }
    return true;
  }

  public E findMin() {
    if (isEmpty()) {
      throw new RuntimeException("The tree is empty.");
    }
    return findMin(root).data;
  }

  // Start from the root and always go to the left child until there is no left
  // child left.
  protected Node<E> findMin(Node<E> node) {
    if (node == null) {
      return null;
    }
    if (node.left == null) {
      return node;
    }
    return findMin(node.left);
  }

  public E findMax() {
    if (isEmpty()) {
      throw new RuntimeException("The tree is empty.");
    }
    return findMax(root).data;
  }

  // Start from the root and always go to the right child until there is no right
  // child left.
  protected Node<E> findMax(Node<E> node) {
    if (node == null) {
      return null;
    }
    if (node.right == null) {
      return node;
    }
    return findMax(node.right);
  }

  /*
   * ************A
   * **********B
   * ********C
   * When some subtree looks like this we need to make a right rotation. (It is
   * hard to draw children of B and C so they are not shown.)
   */
  protected Node<E> rotateRight(Node<E> node) {
    Node<E> leftChild = node.left;
    node.left = leftChild.right;
    leftChild.right = node;
    node.height = Math.max(height(node.left), height(node.right)) + 1;
    leftChild.height = Math.max(height(leftChild.left), node.height) + 1;
    return leftChild;
  }

  /*
   * ************A
   * **************B
   * ****************C
   * When some subtree looks like this we need to make a right rotation.
   */
  protected Node<E> rotateLeft(Node<E> node) {
    Node<E> rightChild = node.right;
    node.right = rightChild.left;
    rightChild.left = node;
    node.height = Math.max(height(node.left), height(node.right)) + 1;
    rightChild.height = Math.max(height(rightChild.right), node.height) + 1;
    return rightChild;
  }

  /*
   * ***********A
   * ********B
   * **********C
   * When some subtree looks like this we need to make a left rotation first and
   * then a right rotation.
   */
  protected Node<E> rotateLeftRight(Node<E> node) {
    node.left = rotateLeft(node.left);
    return rotateRight(node);
  }

  /*
   * ***********A
   * **************B
   * *************C
   * When some subtree looks like this we need to make a right rotation first and
   * then a left rotation.
   */
  protected Node<E> rotateRightLeft(Node<E> node) {
    node.right = rotateRight(node.right);
    return rotateLeft(node);
  }

  // This checks the situation of the subtree. It calls the appropriate function
  // according to the drawings in the comments.
  protected Node<E> balance(Node<E> node) {
    if (node == null) {
      return node;
    }
    if (height(node.left) - height(node.right) > ALLOWED_IMBALANCE) {
      if (height(node.left.left) >= height(node.left.right)) {
        node = rotateRight(node);
      } else {
        node = rotateLeftRight(node);
      }
    } else if (height(node.right) - height(node.left) > ALLOWED_IMBALANCE) {
      if (height(node.right.right) >= height(node.right.left)) {
        node = rotateLeft(node);
      } else {
        node = rotateRightLeft(node);
      }
    }
    node.height = Math.max(height(node.left), height(node.right)) + 1;
    return node;
  }

}