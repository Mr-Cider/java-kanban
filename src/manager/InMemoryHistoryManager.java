package manager;

import alltasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements IHistoryManager {

    private final CustomLinkedHashMap<Task> history = new CustomLinkedHashMap<>();

    private static class CustomLinkedHashMap<T> {
        private Node<T> head;
        private Node<T> tail;
        private final Map<Integer, Node<T>> map = new HashMap<>();

        private void add(Integer key, T element) {
            final Node<T> newNode = new Node<>(tail, element, null, key);
            if (map.containsKey(key)) {
                Node<T> removingNode = map.get(key);
                removeNode(removingNode);
            }
            if (tail != null) tail.next = newNode;
            tail = newNode;
            if (head == null) head = newNode;
            map.put(key, newNode);
        }

        public void removeNode(Node<T> node) {
            if (node == null) {
                return;
            }
            if (node == head) {
                head = node.next;
            } else {
                node.prev.next = node.next;
            }
            if (node == tail) {
                tail = node.prev;
            } else {
                node.next.prev = node.prev;
            }
            map.remove(node.key);
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) history.add(task.getId(), task);
    }

    @Override
    public void remove(int id) {
        history.removeNode(history.map.get(id));
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> historyList = new ArrayList<>();
        Node<Task> node = history.head;
        while (node != null) {
            historyList.add(node.data);
            node = node.next;
        }
        return historyList;
    }
}



