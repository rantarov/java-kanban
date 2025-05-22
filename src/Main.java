public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();
        Task task1 = new Task("Задача1", "Описание1", TaskStatus.NEW);
        taskManager.addTask(task1);
        Task task2 = new Task("Задача2", "Описание2", TaskStatus.IN_PROGRESS);
        taskManager.addTask(task2);

        Epic epicFirst = new Epic("Эпик1", "ОписаникЭпик1");
        taskManager.addEpic(epicFirst);
        Subtask subtaskFirst1 = new Subtask("SubtaskFirst1", "Сабтаска1", TaskStatus.NEW, epicFirst.getId());
        taskManager.addSubtask(subtaskFirst1);
        Subtask subtaskFirst2 = new Subtask("SubtaskFirst2", "Сабтаска2", TaskStatus.NEW, epicFirst.getId());
        taskManager.addSubtask(subtaskFirst2);

        Epic epicTwice = new Epic("Эпик2", "ОписаникЭпик2");
        taskManager.addEpic(epicTwice);
        Subtask subtaskTwice = new Subtask("SubtaskTwice", "СабтаскаТвайс", TaskStatus.NEW, epicTwice.getId());
        taskManager.addSubtask(subtaskTwice);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        task1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateTask(task1);

        task2.setTaskStatus(TaskStatus.NEW);
        taskManager.updateTask(task2);

        subtaskFirst1.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskFirst1);

        subtaskFirst2.setTaskStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskFirst2);

        subtaskTwice.setTaskStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtaskTwice);

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());

        taskManager.removeTask(task1.getId());
        taskManager.removeEpic(epicFirst.getId());

        System.out.println();
        System.out.println("-".repeat(20));
        System.out.println();

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}