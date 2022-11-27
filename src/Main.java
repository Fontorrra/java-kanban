import manager.InMemoryTaskManager;
import manager.Managers;
import task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Изачально массив заполнен двумя задачами," +
                " и двумя эпиками(в две подзадачи в другом одна)");
        InMemoryTaskManager inMemoryTaskManager = (InMemoryTaskManager) Managers.getDefault();
        inMemoryTaskManager.createNewTask(new Task(0, "first task", "description of first task"));
        inMemoryTaskManager.createNewTask(new Task(0, "second task", "description of second task"));
        inMemoryTaskManager.createNewEpic(new Epic(0, "first epic", "description of first epic"));
        inMemoryTaskManager.createNewEpic(new Epic(0, "second epic", "description of second epic"));
        ArrayList<Epic> epics = inMemoryTaskManager.getAllEpics();
        inMemoryTaskManager.createNewSubtask(new Subtask(0, "first sub task",
                        "sub task of first epic", epics.get(0).getID()));
        inMemoryTaskManager.createNewSubtask(new Subtask(0, "second sub task",
                        "another sub task of first epic", epics.get(0).getID()));
        inMemoryTaskManager.createNewSubtask(new Subtask(0, "third sub task",
                        "sub task of second epic", epics.get(1).getID()));
        printMenu();
        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    add(inMemoryTaskManager);
                    printMenu();
                    break;
                case 2:
                    show(inMemoryTaskManager);
                    printMenu();
                    break;
                case 3:
                    removeAll(inMemoryTaskManager);
                    printMenu();
                    break;
                case 4:
                    get(inMemoryTaskManager);
                    printMenu();
                    break;
                case 5:
                    removeOneTask(inMemoryTaskManager);
                    printMenu();
                    break;
                case 6:
                    update(inMemoryTaskManager);
                    printMenu();
                    break;
                case 7:
                    getSubtasksOfEpic(inMemoryTaskManager);
                    printMenu();
                    break;
                case 8:
                    getHistory(inMemoryTaskManager);
                    printMenu();
                    break;
                default:
                    System.out.println("Такого выбора нет");
                    printMenu();
            }
        }
    }

    static void printMenu() {
        System.out.println("0. Выход");
        System.out.println("1. Добавить новую задачу/эпик/подзадачу");
        System.out.println("2. Получить список всех задач/эпиков/подзадач");
        System.out.println("3. Удалить все задачи/эпики/подзадачи");
        System.out.println("4. Получить задачу/эпик/подзадачу по ID");
        System.out.println("5. Удалить задачу/эпик/подзадачу по ID");
        System.out.println("6. Обновить задачу/эпик/подзадачу по ID");
        System.out.println("7. Вывести все подзадачи эпика");
        System.out.println("8. Вывести историю просмотров");
    }

    static void add(InMemoryTaskManager inMemoryTaskManager){
        System.out.println("Что добавить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                scanner.nextLine(); // по какой то причине каждый раз первый nextLine
                // в каждом блоке программы у меня пропускался
                System.out.println("Введите название");
                String title = scanner.nextLine();
                System.out.println("Введите описание");
                String description = scanner.nextLine();
                inMemoryTaskManager.createNewTask(new Task(0, title, description));
                return;
            case 2:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                inMemoryTaskManager.createNewEpic(new Epic(0, title, description));
                return;
            case 3:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                System.out.println("Введите ID эпика. Все ID эпиков: " + inMemoryTaskManager.getAllEpicsID());
                int epicID = scanner.nextInt();
                inMemoryTaskManager.createNewSubtask(new Subtask(0, title, description, epicID));
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                add(inMemoryTaskManager);
        }
    }

    static void show(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Что получить");
        System.out.println("1. Задачи");
        System.out.println("2. Эпики");
        System.out.println("3. Подзадачи");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                for (Task task : inMemoryTaskManager.getAllTasks()) System.out.println(task);
                return;
            case 2:
                for (Epic epic : inMemoryTaskManager.getAllEpics())System.out.println(epic);
                return;
            case 3:
                for (Subtask subtask : inMemoryTaskManager.getAllSubTasks()) System.out.println(subtask);
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                show(inMemoryTaskManager);
        }
    }

    static void removeAll(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Что удалить");
        System.out.println("1. Все задачи");
        System.out.println("2. Все эпики");
        System.out.println("3. Все подзадачи");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                inMemoryTaskManager.removeAllTasks();
                return;
            case 2:
                inMemoryTaskManager.removeAllEpics();
                return;
            case 3:
                inMemoryTaskManager.removeAllSubtasks();
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeAll(inMemoryTaskManager);
        }
    }

    static void get(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Что получить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + inMemoryTaskManager.getAllTasksID());
                int ID = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getTask(ID));
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + inMemoryTaskManager.getAllEpicsID());
                ID = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getEpic(ID));
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + inMemoryTaskManager.getAllSubtasksID());
                ID = scanner.nextInt();
                System.out.println(inMemoryTaskManager.getSubtask(ID));
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                get(inMemoryTaskManager);
        }
    }

    static void removeOneTask(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Что удалить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + inMemoryTaskManager.getAllTasksID());
                int ID = scanner.nextInt();
                inMemoryTaskManager.removeTask(ID);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + inMemoryTaskManager.getAllEpicsID());
                ID = scanner.nextInt();
                inMemoryTaskManager.removeEpic(ID);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + inMemoryTaskManager.getAllSubtasksID());
                ID = scanner.nextInt();
                inMemoryTaskManager.removeSubtask(ID);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeOneTask(inMemoryTaskManager);
        }
    }

    static void update(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Что обновить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + inMemoryTaskManager.getAllTasksID());
                int ID = scanner.nextInt();
                Task task = inMemoryTaskManager.getTask(ID);
                if (task == null) return;
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                int ident = scanner.nextInt();
                if (ident == 1) task.setStatus(Status.IN_PROGRESS);
                if (ident == 2) task.setStatus(Status.DONE);
                inMemoryTaskManager.updateTask(task);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + inMemoryTaskManager.getAllEpicsID());
                ID = scanner.nextInt();
                Epic epic = inMemoryTaskManager.getEpic(ID);
                if (epic == null) return;
                //можно менять title или description
                //но делать для этого отдельное меню долго и бесполезно, оно работает
                inMemoryTaskManager.updateEpic(epic);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + inMemoryTaskManager.getAllSubtasksID());
                ID = scanner.nextInt();
                Subtask subtask = inMemoryTaskManager.getSubtask(ID);
                if (subtask == null) return;
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                ident = scanner.nextInt();
                if (ident == 1)  subtask.setStatus(Status.IN_PROGRESS);
                if (ident == 2)  subtask.setStatus(Status.DONE);
                inMemoryTaskManager.updateSubtask(subtask);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                update(inMemoryTaskManager);
        }
    }

    static void getSubtasksOfEpic(InMemoryTaskManager inMemoryTaskManager) {
        System.out.println("Введите ID эпика. Все ID эпиков: " + inMemoryTaskManager.getAllEpicsID());
        int ID = scanner.nextInt();
        ArrayList<Subtask> subtasksOfEpic = inMemoryTaskManager.getAllSubtasksOfEpic(ID);
        if (subtasksOfEpic == null) return;
        for (Subtask subtask : subtasksOfEpic) {
            System.out.println(subtask);
        }
    }

    static void getHistory(InMemoryTaskManager inMemoryTaskManager) {
        List<Task> history = inMemoryTaskManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
    }
}
