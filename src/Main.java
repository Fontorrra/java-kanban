import manager.FileBackedTasksManager;
import manager.InMemoryTaskManager;
import manager.Managers;
import task.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Изачально массив заполнен двумя задачами," +
                " и двумя эпиками(в две подзадачи в другом одна)");
        File file = new File("info.txt");
        FileBackedTasksManager fileBackedTasksManager = (FileBackedTasksManager)
                Managers.getDefaultFile(file);
        fileBackedTasksManager.createNewTask(new Task(0, "first task", "description of first task"));
        fileBackedTasksManager.createNewTask(new Task(0, "second task", "description of second task"));
        fileBackedTasksManager.createNewEpic(new Epic(0, "first epic", "description of first epic"));
        fileBackedTasksManager.createNewEpic(new Epic(0, "second epic", "description of second epic"));
        ArrayList<Epic> epics = fileBackedTasksManager.getAllEpics();
        fileBackedTasksManager.createNewSubtask(new Subtask(0, "first subtask",
                        "subtask of first epic", epics.get(0).getID()));
        fileBackedTasksManager.createNewSubtask(new Subtask(0, "second subtask",
                        "another subtask of first epic", epics.get(0).getID()));
        fileBackedTasksManager.createNewSubtask(new Subtask(0, "third subtask",
                        "subtask of second epic", epics.get(1).getID()));
       /* System.out.println(fileBackedTasksManager.toString
                (new Task(0, "first task", "description of first task")));
        System.out.println(fileBackedTasksManager.toString
                (new Epic(0, "second epic", "description of second epic")));
        System.out.println(fileBackedTasksManager.toString
                (new Subtask(0, "first sub task",
                        "sub task of first epic", epics.get(0).getID())));
        System.out.println(fileBackedTasksManager.fromString(fileBackedTasksManager.toString
                (new Subtask(0, "first sub task",
                        "sub task of first epic", epics.get(0).getID()))))*/

        printMenu();
        while (true) {
            int choice = scanner.nextInt();
            switch (choice) {
                case 0:
                    return;
                case 1:
                    add(fileBackedTasksManager);
                    printMenu();
                    break;
                case 2:
                    show(fileBackedTasksManager);
                    printMenu();
                    break;
                case 3:
                    removeAll(fileBackedTasksManager);
                    printMenu();
                    break;
                case 4:
                    get(fileBackedTasksManager);
                    printMenu();
                    break;
                case 5:
                    removeOneTask(fileBackedTasksManager);
                    printMenu();
                    break;
                case 6:
                    update(fileBackedTasksManager);
                    printMenu();
                    break;
                case 7:
                    getSubtasksOfEpic(fileBackedTasksManager);
                    printMenu();
                    break;
                case 8:
                    getHistory(fileBackedTasksManager);
                    printMenu();
                    break;
                case 9:
                    fileBackedTasksManager = getManagerFromFile(file);
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
        System.out.println("9. Восстановить по файлу");
    }

    static void add(FileBackedTasksManager fileBackedTasksManager){
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
                fileBackedTasksManager.createNewTask(new Task(0, title, description));
                return;
            case 2:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                fileBackedTasksManager.createNewEpic(new Epic(0, title, description));
                return;
            case 3:
                scanner.nextLine();
                System.out.println("Введите название");
                title = scanner.nextLine();
                System.out.println("Введите описание");
                description = scanner.nextLine();
                System.out.println("Введите ID эпика. Все ID эпиков: " + fileBackedTasksManager.getAllEpicsID());
                int epicID = scanner.nextInt();
                fileBackedTasksManager.createNewSubtask(new Subtask(0, title, description, epicID));
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                add(fileBackedTasksManager);
        }
    }

    static void show(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Что получить");
        System.out.println("1. Задачи");
        System.out.println("2. Эпики");
        System.out.println("3. Подзадачи");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                for (Task task : fileBackedTasksManager.getAllTasks()) System.out.println(task);
                return;
            case 2:
                for (Epic epic : fileBackedTasksManager.getAllEpics())System.out.println(epic);
                return;
            case 3:
                for (Subtask subtask : fileBackedTasksManager.getAllSubTasks()) System.out.println(subtask);
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                show(fileBackedTasksManager);
        }
    }

    static void removeAll(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Что удалить");
        System.out.println("1. Все задачи");
        System.out.println("2. Все эпики");
        System.out.println("3. Все подзадачи");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                fileBackedTasksManager.removeAllTasks();
                return;
            case 2:
                fileBackedTasksManager.removeAllEpics();
                return;
            case 3:
                fileBackedTasksManager.removeAllSubtasks();
                return;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeAll(fileBackedTasksManager);
        }
    }

    static void get(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Что получить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + fileBackedTasksManager.getAllTasksID());
                int ID = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getTask(ID));
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + fileBackedTasksManager.getAllEpicsID());
                ID = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getEpic(ID));
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + fileBackedTasksManager.getAllSubtasksID());
                ID = scanner.nextInt();
                System.out.println(fileBackedTasksManager.getSubtask(ID));
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                get(fileBackedTasksManager);
        }
    }

    static void removeOneTask(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Что удалить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + fileBackedTasksManager.getAllTasksID());
                int ID = scanner.nextInt();
                fileBackedTasksManager.removeTask(ID);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + fileBackedTasksManager.getAllEpicsID());
                ID = scanner.nextInt();
                fileBackedTasksManager.removeEpic(ID);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + fileBackedTasksManager.getAllSubtasksID());
                ID = scanner.nextInt();
                fileBackedTasksManager.removeSubtask(ID);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                removeOneTask(fileBackedTasksManager);
        }
    }

    static void update(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Что обновить");
        System.out.println("1. Задачу");
        System.out.println("2. Эпик");
        System.out.println("3. Подзадачу");
        System.out.println("0. Выход");
        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                System.out.println("Введите ID задачи. Все ID задач: " + fileBackedTasksManager.getAllTasksID());
                int ID = scanner.nextInt();
                List<Task> tasks = fileBackedTasksManager.getAllTasks();
                if (tasks == null) return;
                Task thisTask = null;
                for (Task task : tasks) {
                    if (task.getID() == ID) {
                        thisTask = task;
                    }
                }
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                int ident = scanner.nextInt();
                if (ident == 1) thisTask.setStatus(Status.IN_PROGRESS);
                if (ident == 2) thisTask.setStatus(Status.DONE);
                fileBackedTasksManager.updateTask(thisTask);
                break;
            case 2:
                System.out.println("Введите ID эпика. Все ID эпиков: " + fileBackedTasksManager.getAllEpicsID());
                ID = scanner.nextInt();
                List<Epic> epics = fileBackedTasksManager.getAllEpics();
                if (epics == null) return;
                Epic thisEpic = null;
                for (Epic epic : epics) {
                    if (epic.getID() == ID) {
                        thisEpic = epic;
                    }
                }
                if (thisEpic == null) return;
                //можно менять title или description
                //но делать для этого отдельное меню долго и бесполезно, оно работает
                fileBackedTasksManager.updateEpic(thisEpic);
                break;
            case 3:
                System.out.println("Введите ID подзадачи. Все ID подзадач: " + fileBackedTasksManager.getAllSubtasksID());
                ID = scanner.nextInt();
                List<Subtask> subtasks = fileBackedTasksManager.getAllSubTasks();
                if (subtasks == null) return;
                Subtask thisSubtask = null;
                for (Subtask subtask : subtasks) {
                    if (subtask.getID() == ID) {
                        thisSubtask = subtask;
                    }
                }
                System.out.println("Введите новый статус: \n1. IN_PROGRESS\n2. DONE");
                ident = scanner.nextInt();
                if (ident == 1)  thisSubtask.setStatus(Status.IN_PROGRESS);
                if (ident == 2)  thisSubtask.setStatus(Status.DONE);
                fileBackedTasksManager.updateSubtask(thisSubtask);
                break;
            case 0:
                return;
            default:
                System.out.println("Такого выбора нет");
                update(fileBackedTasksManager);
        }
    }

    static void getSubtasksOfEpic(FileBackedTasksManager fileBackedTasksManager) {
        System.out.println("Введите ID эпика. Все ID эпиков: " + fileBackedTasksManager.getAllEpicsID());
        int ID = scanner.nextInt();
        ArrayList<Subtask> subtasksOfEpic = fileBackedTasksManager.getAllSubtasksOfEpic(ID);
        if (subtasksOfEpic == null) return;
        for (Subtask subtask : subtasksOfEpic) {
            System.out.println(subtask);
        }
    }

    static void getHistory(FileBackedTasksManager fileBackedTasksManager) {
        List<Task> history = fileBackedTasksManager.getHistory();
        for (Task task : history) {
            System.out.println(task);
        }
    }

    static FileBackedTasksManager getManagerFromFile(File file) {
        //данные считываются из testinfo.txt и полученном менеджере записываются в file который передается(info.txt)
        return FileBackedTasksManager.loadFromFile(new File("testinfo.txt"), file);
    }
}
