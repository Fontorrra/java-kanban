package manager;

import node.Node;
import task.Task;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    HashMap <Integer, Node<Task>> mapOfNodes;

    Node<Task> head = null;
    Node <Task> tail = null;

    InMemoryHistoryManager() {
        mapOfNodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        Node<Task> node = mapOfNodes.getOrDefault(task.getID(), null);
        if (node == null) {
            linkLast(task);
            return;
        }
        removeNode(node);
        linkLast(task);
    }

    @Override
    public void remove(int id) {
        Node<Task> node = mapOfNodes.getOrDefault(id, null);
        if (node == null) return;
        removeNode(node);
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
            mapOfNodes.put(task.getID(), head);
            return;
        }

        if (head == tail) {
            tail = new Node<>(task);
            head.next = tail;
            tail.prev = head;
            mapOfNodes.put(task.getID(), tail);
            return;
        }
        Node<Task> newNode = new Node<>(task);
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        mapOfNodes.put(newNode.item.getID(), newNode);
    }

    void removeNode(Node<Task> node) {
        mapOfNodes.remove(node.item.getID());

        if (head == tail) {
            head = null;
            tail = null;
            return;
        }

        if (node == head) {
            if (node.next == tail) {
                tail.prev = null;
                head = tail;
                return;
            }
            head = node.next;
            return;
        }

        if (node == tail) {
            if (node.prev == head) {
                head.next = null;
                tail = head;
                return;
            }
            tail = node.prev;
            return;
        }

        node.prev.next = node.next;
        node.next.prev = node.prev;

    }

    List<Task> getTasks() {
        Node<Task> node = head;
        LinkedList<Task> tasks = new LinkedList<>();
        while (node != null) {
            tasks.addLast(node.item);
            node = node.next;
        }
        return tasks;
    }
}
