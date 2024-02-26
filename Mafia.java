/* This class is a subclass of the avl tree class. It overrides some methods 
*  for printing cases which is special to this project. It also handles dividing and getting same ranked members
*  which are not implemented in the standard avl tree implementation.
*/
public class Mafia extends AvlTree<Person> {
  private WriteHelper helper;

  public Mafia(WriteHelper helper) {
    this.helper = helper;
  }

  // If the element that is going to be inserted is less than the current node
  // call insert recursively for left node,
  // if greater than the current node call insert recursivelt for right node,
  // otherwise it is already in tree do nothing.
  // After insertion balance the tree.
  protected Node<Person> insert(Person x, Node<Person> node) {
    if (node == null) {
      return new Node<Person>(x, null, null);
    }
    helper.write(String.format("%s welcomed %s\n", node.data.getName(), x.getName()));
    if (x.compareTo(node.data) < 0) {
      node.left = insert(x, node.left);
    } else if (x.compareTo(node.data) > 0) {
      node.right = insert(x, node.right);
    }

    return balance(node);
  }

  public void remove(Person x) {
    root = remove(x, root, false);
  }

  // If the element that is going to be removed has only left or right child just
  // replace it with this child.
  // If it has no chid, replace it with null.
  // If it has two children, find the smallest element in the right subtree(the
  // smallest element that is bigger than the current element), change the current
  // element to this element,
  // remove this element in its original place.
  private Node<Person> remove(Person x, Node<Person> node, boolean infoPrinted) {
    if (node == null) {
      return node;
    }
    if (x.compareTo(node.data) < 0) {
      node.left = remove(x, node.left, infoPrinted);
    } else if (x.compareTo(node.data) > 0) {
      node.right = remove(x, node.right, infoPrinted);
    } else if (node.left != null && node.right != null) {
      Node<Person> min = findMin(node.right);
      helper.write(String.format("%s left the family, replaced by %s\n", node.data.getName(), min.data.getName()));
      node.data = min.data;
      node.right = remove(node.data, node.right, true);
    } else if (node.left != null) {
      if (!infoPrinted) {
        helper
            .write(
                String.format("%s left the family, replaced by %s\n", node.data.getName(), node.left.data.getName()));
      }
      node = node.left;
    } else if (node.right != null) {
      if (!infoPrinted) {
        helper
            .write(
                String.format("%s left the family, replaced by %s\n", node.data.getName(), node.right.data.getName()));
      }
      node = node.right;
    } else {
      if (!infoPrinted) {
        helper
            .write(String.format("%s left the family, replaced by nobody\n", node.data.getName()));
      }
      node = null;
    }
    return balance(node);
  }

  public void intelTarget(Person x, Person y) {
    Node<Person> target = intelTarget(x, y, root);
    helper.write(String.format("Target Analysis Result: %s %.3f\n", target.data.getName(), target.data.getGms())
        .replace(",", "."));
  }

  // It checks if a node contains the both member. If it contains, its lower
  // ranked members also may contain the both member.
  // It goes until the current node stops being superior to the both nodes.
  private Node<Person> intelTarget(Person x, Person y, Node<Person> node) {
    if (contains(x, node) && contains(y, node)) {
      Node<Person> leftNode = intelTarget(x, y, node.left);
      Node<Person> rightNode = intelTarget(x, y, node.right);
      if (leftNode != null) {
        return leftNode;
      } else if (rightNode != null) {
        return rightNode;
      }
      return node;
    }
    return null;
  }

  public void intelDivide() {
    if (root == null) {
      helper.write(String.format("Division Analysis Result: %d\n", 0));
      return;
    }
    int[] result = intelDivide(root);
    helper.write(String.format("Division Analysis Result: %d\n", result[1]));
  }

  // It goes until the leaf nodes. And adds them to the target list. Than starts
  // returning and goes up along the tree. If the both children of a node are not
  // added to the target list, this node is added to the target list. Otherwise it
  // is not added.
  private int[] intelDivide(Node<Person> node) {
    if (node.left == null && node.right == null) {
      return new int[] { 1, 1 };
    }
    int[] left = intelDivide(node.left);
    int[] right = intelDivide(node.right);
    if (left[0] == 0 && right[0] == 0) {
      return new int[] { 1, left[1] + right[1] + 1 };
    }
    return new int[] { 0, left[1] + right[1] };
  }

  // This is similar to finding the height of a node but itstarts counting from
  // the root instead of counting from the leaves. Helper function for monitoring
  // same ranked members.
  private int findRank(Person x, Node<Person> node, int rank) {
    if (x.compareTo(node.data) < 0) {
      return findRank(x, node.left, rank + 1);
    } else if (x.compareTo(node.data) > 0) {
      return findRank(x, node.right, rank + 1);
    } else {
      return rank;
    }
  }

  public void monitorRanks(Person x) {
    int rank = findRank(x, root, 0);
    helper.write("Rank Analysis Result: ");
    String result = monitorRanks(root, rank);
    helper.write(result.trim());
    helper.write("\n");
  }

  // Public version of the method finds the rank of the wanted person and pass
  // this information to the private implementation.
  // Start from the root node, traverse the nodes in order. When you pass to a
  // child node than its rank is parentRank + 1. In each passage like this
  // decrease the rank parameter by one. When 0 rank is reached then you have a
  // member with same rank in the request.
  private String monitorRanks(Node<Person> node, int rank) {
    if (node == null || rank < 0) {
      return "";
    }
    if (rank == 0) {

      return String.format("%s %.3f ", node.data.getName(), node.data.getGms())
          .replace(",", ".");
    }
    String left = monitorRanks(node.left, rank - 1);
    String right = monitorRanks(node.right, rank - 1);
    return left + right;
  }

  // Used it for test purposes. Traverses the tree in-order.
  protected void printTree(Node<Person> node) {
    if (node != null) {
      printTree(node.left);
      System.out.println(node.data.getName() + " " + node.data.getGms());
      printTree(node.right);
    }
  }
}
