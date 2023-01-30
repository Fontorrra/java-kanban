package node;

public class Node<T> {
    public Node<T> prev;
    public Node<T> next;
    public T item;

    public Node(T item) {
        this.item = item;
        prev = null;
        next = null;
    }

    public Node(Node<T> prev, Node<T> next, T item) {
        this.prev = prev;
        this.next = next;
        this.item = item;
    }
}
