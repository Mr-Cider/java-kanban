package manager;

import alltasks.Task;
import java.util.*;

public class InMemoryHistoryManager implements IHistoryManager {
    //    private final List<Task> history = new LinkedList<>();
//      private final Map<Integer, Node<Task>> map = new HashMap<>();
    customLinkedHashMap<Task> history = new customLinkedHashMap<>();


//    private static final int MAX_HISTORY_SIZE = 9;

    public class customLinkedHashMap<T> {
        private Node<T> head;
        private Node<T> tail;
        private int size = 0;
        private final Map<Integer, Node<T>> map = new HashMap<>();

        public void add(Integer key, T element) {
            final Node<T> newNode = new Node<>(tail, element, null);
            if (map.containsKey(key)) {
                Node<T> removingNode = map.get(key);
                removeNode(removingNode);
            }
            if (tail != null) tail.next = newNode;
            tail = newNode;
            if (head == null) head = newNode;
            map.put(key, newNode);
            size++;
        }

        public void removeNode(Node<T> node) {
            if(node == null) {
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
            size--;
            map.values().remove(node);
        }
    }

    @Override
    public void add(Task task) {
        if (task != null) history.add(task.getId(), task);
        }

    @Override
    public void remove(int id) {
        history.removeNode(history.map.get(id));
        history.map.remove(id);
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



