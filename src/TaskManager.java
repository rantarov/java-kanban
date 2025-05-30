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
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            subtasks.put(subtask.getId(), subtask);
            epic.getSubtasksId().add(subtask.getId());
            updateEpicStatus(epic);
        }
    }

    public Task updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return null;
        }
        tasks.put(task.getId(), task);
        return task;
    }


    public Epic updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return null;
        }

        Epic oldEpic = epics.get(epic.getId());
        oldEpic.setName(epic.getName());
        oldEpic.setDescription(epic.getDescription());
        return oldEpic;
    }

    public Subtask updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return null;
        }

        Epic epic = epics.get(subtask.getEpicId());
        if (epic == null || !epic.getSubtasksId().contains(subtask.getId())) {
            return null;
        }

        subtasks.put(subtask.getId(), subtask);
        updateEpicStatus(epic);
        return subtask;
    }

    public Task removeTask(int id) {
        return tasks.remove(id);
    }

    public Epic removeEpic(int id) {
        Epic removed = epics.remove(id);
        if (removed != null) {
            for (Integer subtaskId : removed.getSubtasksId()) {
                subtasks.remove(subtaskId);
            }
        }
        return removed;
    }

    public Subtask removeSubtask(int id) {
        Subtask removed = subtasks.remove(id);
        if (removed != null) {
            Epic epic = epics.get(removed.getEpicId());
            if (epic != null) {
                epic.getSubtasksId().remove((Integer) id);
                updateEpicStatus(epic);
            }
        }
        return removed;
    }

    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksId().clear();
            epic.setTaskStatus(TaskStatus.NEW);
        }
    }

    public void clearAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void updateEpicStatus(Epic epic) {
        int countDone = 0;
        int countInProgress = 0;
        ArrayList<Integer> subtasksFromEpic = epic.getSubtasksId();
        int total = subtasksFromEpic.size();

        for (Integer subtaskId : subtasksFromEpic) {
            Subtask subtask = subtasks.get(subtaskId);
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
