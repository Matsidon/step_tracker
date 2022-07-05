package manager;

import task.*;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {

    private CustomLinkedList<Task> customLinkedList = new CustomLinkedList();
    private Map<Integer, Node<Task>> tableOfUniqueHistory = new HashMap<>();

    //************ Добавление задачи в просмотренные ************
    @Override
    public void add(Task task) {
        if (tableOfUniqueHistory.containsKey(task.getId())) {
            remove(task.getId());
        }
        customLinkedList.linkLast(task);
    }

    //************ История просмотров задач ************
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }

    //************ Удаление задачи из просмотренных ************
    @Override
    public void remove(int id) {
        if (tableOfUniqueHistory.containsKey(id)) {
            customLinkedList.removeNode(tableOfUniqueHistory.get(id));
            tableOfUniqueHistory.remove(id);
        }
    }

    //************ Класс  CustomLinkedList ************
    public class CustomLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;

        //************ Добавляет задачу последней в двухсвязный список ************
        public void linkLast(Task task) {
            final Node<T> oldTail = tail;
            final Node<T> newNode = new Node<>(oldTail, task, null);
            tail = newNode;
            if (oldTail == null)
                head = newNode;
            else
                oldTail.next = newNode;
            tableOfUniqueHistory.put(task.getId(), (Node<Task>) newNode);
        }

        public List<Task> getTasks() {
            List<Task> listOfHistory = new ArrayList<>();
            Node<T> curHead = head;
            while (curHead != null) {
                listOfHistory.add(curHead.data);
                curHead = curHead.next;
            }
            return listOfHistory;
        }

        //************ Удаление узла ************
        public void removeNode(Node node) {
            if (tableOfUniqueHistory.containsKey(node.data.getId())) {
                if (node.prev == null && node.next == null) { //null
                    head = null;
                    tail = null;
                } else if (node.prev == null) { //head
                    node.next.prev = null;
                    head = node.next;
                } else if (node.next == null) { //tail
                    node.prev.next = null;
                    tail = node.prev;
                } else {
                    node.prev.next = node.next;
                    node.next.prev = node.prev;
                }
            }
        }
    }

    //************ Класс Node ************
    public class Node<T> {

        public Task data;
        public Node<T> next;
        public Node<T> prev;

        public Node(Node<T> prev, Task data, Node<T> next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }
    }
}




