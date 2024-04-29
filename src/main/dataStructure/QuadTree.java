package main.dataStructure;

import main.model.Place;

public class QuadTree<T extends Place> {
    private static final int MAX_CAPACITY = 4;
    Node<T> root;

    public QuadTree() {
        this.root = null;
    }

    public QuadTree(Rectangle boundary) {
        this.root = new Node<>(boundary);
    }

    public Node<T> getRoot() {
        return root;
    }

    public boolean insert(T dataToInsert) {
        if (root == null) {
            System.out.println("Root is not set.");
            return false;
        }
        return insert(root, dataToInsert);
    }

    private boolean insert(Node<T> node, T dataToInsert) {
//        if (node == null) {
//            return false;
//        }

        if (!node.contains(dataToInsert)) {
            return false;
        }

        if (node.isLeaf()) {
                if (node.getData()[MAX_CAPACITY - 1] != null) {
                    // Split the leaf node if it's full
                    node.split();
                    // Recursively try to insert into children after splitting
                    node.distributeData();
                    return insert(node, dataToInsert);
                } else {
                    node.insert(dataToInsert);
                    return true;
            }
        } else {
            // If not a leaf, continue searching for appropriate leaf node
            for (Node<T> child : node.getChildren()) {
                if (insert(child, dataToInsert)) {
                    return true;
                }
            }
        }

        return false;
    }


    public T dfs(T dataPoint) {
        return dfs(root, dataPoint);
    }

    private T dfs(Node<T> node, T dataPoint) {
        if (node == null) {
            return null;
        }

        if (node.contains(dataPoint)) {
            if (!node.isLeaf()) {
                for (Node<T> child : node.getChildren()) {
                    T result = dfs(child, dataPoint);
                    if (result != null) {
                        return result;
                    }
                }
            }

            for (T data : node.getData()) {
                if (data.equals(dataPoint)) {
                    return data;
                }
            }
        }
        return null;
    }

    // param: String name / Point location / ServiceType service
    // operation: String remove/edit/findAll
    // boundingBox: Point, distance
    public QuadTree<T> searchElements(Rectangle boundingBox, String param) {
        QuadTree<T> results = new QuadTree<>(boundingBox);
        searchElements(root, param, results);

        return results;
    }

    private void searchElements(Node<T> node, String param, QuadTree<T> results) {
        if (node == null) {
            return;
        }

        Rectangle boundingBox = results.getRoot().getBoundary();

        if (boundingBox.intersects(node.getBoundary())) {
            for (T dataPoint : node.getData()) {
                results.insert(dataPoint);
            }
        } else {
            for (Node<T> child : node.getChildren()) {
                searchElements(child, param, results);
            }
        }
    }



    public boolean remove(T dataToRemove) {
        if (root == null) {
            return false;
        }
        return remove(null, root, dataToRemove);
    }

    private boolean remove(Node<T> parent, Node<T> node, T dataToRemove) {
        return false;
    }

    private void mergeNode(Node<T> parent, Node<T> node) {
        parent.setData(node.getData());
        parent.setChildren(null);
    }

    public void display() {
        if (root == null) {
            System.out.println("Tree is empty");
        } else {
            display(root, 0);
        }
    }

    private void display(Node<T> node, int level) {
        for (int i = 0; i < level; i++) {
            System.out.print("Level " + i + ": ");
        }

        if (node.isLeaf()) {
            System.out.println("Leaf:");
            for (T dataPoint : node.getData()) {
                    System.out.println("  Data point = " + dataPoint);
            }

        } else {
            System.out.println("Node:" + node.getBoundary());
            for (Node<T> child : node.getChildren()) {
                display(child, level + 1);
            }
        }
    }

    public void displayData() {
        if (root == null) {
            System.out.println("Map is empty.");
        } else {
            displayData(root);
        }
    }

    private void displayData(Node<T> node) {
        if (node == null) return;

        // Check each child node for data if the current node is not a leaf
        if (!node.isLeaf()) {
            for (Node<T> child : node.getChildren()) {
                displayData(child);
            }
        } else {
            // This is a leaf node, display all non-null data points
            for (T dataPoint : node.getData()) {
                if (dataPoint != null) {
                    System.out.println(dataPoint);
                }
            }
        }
    }

}
