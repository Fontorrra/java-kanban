package manager.history;

import node.Node;
import task.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    HashMap<Integer, Node<Task>> mapOfNodes;

    Node<Task> head;
    Node<Task> tail;

    public InMemoryHistoryManager() {
        mapOfNodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Node<Task> node = mapOfNodes.get(task.getId());
        if (node == null) {
            linkLast(task);
        }
        else {
            removeNode(node);
            linkLast(task);
        }
    }

    @Override
    public void remove(int id) {
        Node<Task> node = mapOfNodes.get(id);
        if (node != null) removeNode(node);
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    void linkLast(Task task) {
        if (head == null) {
            Node<Task> node = new Node<>(task);
            head = node;
            tail = node;
            mapOfNodes.put(task.getId(), head);
        }

        else {
            Node<Task> newNode = new Node<>(task);
            tail.setNext(newNode);
            newNode.setPrev(tail);
            tail = newNode;
            mapOfNodes.put(newNode.getItem().getId(), newNode);
        }
    }

    void removeNode(Node<Task> node) {
        mapOfNodes.remove(node.getItem().getId());
        if (head == tail) {
            head = null;
            tail = null;
        }

        else if (node == head) {
            if (node.getNext() == tail) {
                tail.setPrev(null);
                head = tail;
                return;
            }
            head = node.getNext();
        }

        else if (node == tail) {
            if (node.getPrev() == head) {
                head.setNext(null);
                tail = head;
                return;
            }
            tail = node.getPrev();
            tail.setNext(null);
        }

        else {
            node.getPrev().setNext(node.getNext());
            node.getNext().setPrev(node.getPrev());
        }
    }

    List<Task> getTasks() {
        Node<Task> node = head;
        LinkedList<Task> tasks = new LinkedList<>();
        while (node != null) {
            tasks.addLast(node.getItem());
            node = node.getNext();
        }
        return tasks;
    }
}
