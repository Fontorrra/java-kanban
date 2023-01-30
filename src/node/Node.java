package node;

public class Node<T> {
    private Node<T> prev;
    private Node<T> next;
    private T item;

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

    public Node<T> getPrev() {
        return prev;
    }

    public void setPrev(Node<T> prev) {
        this.prev = prev;
    }

    public Node<T> getNext() {
        return next;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public T getItem() {
        return item;
    }

    public void setItem(T item) {
        this.item = item;
    }
}
