import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generatorId = 1;
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int getNextId() {
        return generatorId++;
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void addTask(Task task) {
        task.setId(getNextId());
        tasks.put(task.getId(), task);
    }

    public void addEpic(Epic epic) {
        epic.setId(getNextId());
        epics.put(epic.getId(), epic);
    }

    public void addSubtask(Subtask subtask) {
        subtask.setId(getNextId());
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.getSubtasks().add(subtask);
            updateEpicStatus(epic);
        }
    }

    public Task updateTask(Task task) {
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic updateEpic(Epic epic) {
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            updateEpicStatus(epic);
        }
        return subtask;
    }

    public Task removeTask(int id) {
        return tasks.remove(id);
    }

    public Epic removeEpic(int id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (Subtask subtask : removed.getSubtasks()) {
                subtasks.remove(subtask.getId());
            }
        }
        return removed;
    }

    public Subtask removeSubtask(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.getSubtasks().remove(removed);
                updateEpicStatus(epic);
            }
        }
        return removed;
    }

    public void updateEpicStatus(Epic epic) {
        int countDone = 0;
        int countInProgress = 0;
        ArrayList<Subtask> subtasksFromEpic = epic.getSubtasks();
        int total = subtasksFromEpic.size();

        for (Subtask subtask : subtasksFromEpic) {
            if (subtask.getTaskStatus() == TaskStatus.DONE) {
                countDone++;
            } else if (subtask.getTaskStatus() == TaskStatus.IN_PROGRESS) {
                countInProgress++;
            }
        }

        if (total == 0) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else if (countDone == total) {
            epic.setTaskStatus(TaskStatus.DONE);
        } else if (countInProgress > 0 || countDone > 0) {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
        } else {
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }
}
