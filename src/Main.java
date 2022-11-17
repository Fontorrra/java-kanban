import manager.Manager;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Изачально массив заполнен двумя задачами," +
                " и двумя эпиками(в две подзадачи в другом одна)");
        Manager manager = new Manager();
        manager.createNewTask(new Task(0, "first task", "description of first task"));
        manager.createNewTask(new Task(0, "second task", "description of second task"));
        manager.createNewEpic(new Epic(0, "first epic", "description of first epic"));
        manager.createNewEpic(new Epic(0, "second epic", "description of second epic"));
        ArrayList<Epic> epics = manager.getAllEpics();
        manager.createNewSubtask(new Subtask(0, "first sub task",
                        "sub task of first epic", epics.get(0).getID()));
        manager.createNewSubtask(new Subtask(0, "second sub task",
                        "another sub task of first epic", epics.get(0).getID()));
        manager.createNewSubtask(new Subtask(0, "third sub task",
                        "sub task of second epic", epics.get(1).getID()));
        //manager.removeTaskOfAnyType(task);
        //manager.removeTaskOfAnyType(epics.get(0));
        //manager.printAllTasks();
        //manager.printAllEpics();
        printMenu();
        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    add(manager);
                    printMenu();
                    break;
                case 2:
                    show(manager);
                    printMenu();
                    break;
                case 3:
                    removeAll(manager);
                    printMenu();
                    break;
                case 4:
                    get(manager);
                    printMenu();
                    break;
                case 5:
                    removeOneTask(manager);
                    printMenu();
                    break;
                case 6:
                    update(manager);
                    printMenu();
                    break;
                case 7:
                    getSubtasksOfEpic(manager);
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
    }

    static void add(Manager manager){
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
                manager.createNewTask(new Task(0, title, description));
                return;
            case 2:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                manager.createNewEpic(new Epic(0, title, description));
                return;
            case 3:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                System.out.println("Введите ID эпика. Все ID эпиков: " + manager.getAllEpicsID());
                int epicID = scanner.nextInt();
                manager.createNewSubtask(new Subtask(0, title, description, epicID));
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                add(manager);
        }
    }

    static void show(Manager manager) {
        System.out.println("Что получить");
        System.out.println("1. Задачи");
        System.out.println("2. Эпики");
        System.out.println("3. Подзадачи");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                for (Task task : manager.getAllTasks()) System.out.println(task);
                return;
            case 2:
                for (Epic epic : manager.getAllEpics())System.out.println(epic);
                return;
            case 3:
                for (Subtask subtask : manager.getAllSubTasks()) System.out.println(subtask);
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                show(manager);
        }
    }

    static void removeAll(Manager manager) {
        System.out.println("Что удалить");
        System.out.println("1. Все задачи");
        System.out.println("2. Все эпики");
        System.out.println("3. Все подзадачи");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                manager.removeAllTasks();
                return;
            case 2:
                manager.removeAllEpics();
                return;
            case 3:
                manager.removeAllSubtasks();
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeAll(manager);
        }
    }

    static void get(Manager manager) {
        System.out.println("Что получить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + manager.getAllTasksID());
                int ID = scanner.nextInt();
                System.out.println(manager.getTask(ID));
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + manager.getAllEpicsID());
                ID = scanner.nextInt();
                System.out.println(manager.getEpic(ID));
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + manager.getAllSubtasksID());
                ID = scanner.nextInt();
                System.out.println(manager.getSubtask(ID));
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                get(manager);
        }
    }

    static void removeOneTask(Manager manager) {
        System.out.println("Что удалить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + manager.getAllTasksID());
                int ID = scanner.nextInt();
                manager.removeTask(ID);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + manager.getAllEpicsID());
                ID = scanner.nextInt();
                manager.removeEpic(ID);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + manager.getAllSubtasksID());
                ID = scanner.nextInt();
                manager.removeSubtask(ID);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeOneTask(manager);
        }
    }

    static void update(Manager manager) {
        System.out.println("Что обновить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + manager.getAllTasksID());
                int ID = scanner.nextInt();
                Task task = manager.getTask(ID);
                if (task == null) return;
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                int ident = scanner.nextInt();
                if (ident == 1) task.setStatus(Status.IN_PROGRESS);
                if (ident == 2) task.setStatus(Status.DONE);
                manager.updateTask(task);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + manager.getAllEpicsID());
                ID = scanner.nextInt();
                Epic epic = manager.getEpic(ID);
                if (epic == null) return;
                //можно менять title или description
                //но делать для этого отдельное меню долго и бесполезно, оно работает
                manager.updateEpic(epic);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + manager.getAllSubtasksID());
                ID = scanner.nextInt();
                Subtask subtask = manager.getSubtask(ID);
                if (subtask == null) return;
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                ident = scanner.nextInt();
                if (ident == 1)  subtask.setStatus(Status.IN_PROGRESS);
                if (ident == 2)  subtask.setStatus(Status.DONE);
                manager.updateSubtask(subtask);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                update(manager);
        }
    }

    static void getSubtasksOfEpic(Manager manager) {
        System.out.println("Введите ID эпика. Все ID эпиков: " + manager.getAllEpicsID());
        int ID = scanner.nextInt();
        ArrayList<Subtask> subtasksOfEpic = manager.getAllSubtasksOfEpic(ID);
        if (subtasksOfEpic == null) return;
        for (Subtask subtask : subtasksOfEpic) {
            System.out.println(subtask);
        }
    }
}
