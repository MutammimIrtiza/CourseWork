import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Task Priority Enum
enum Priority {
    HIGH, MEDIUM, LOW
}

// Task Model
class Task {
    private String id;
    private int startTime;
    private int endTime;
    private Priority priority;

    public Task(String id, int startTime, int endTime, Priority priority) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
    }

    public String getId() { return id; }
    public int getStartTime() { return startTime; }
    public int getEndTime() { return endTime; }
    public Priority getPriority() { return priority; }

    public int getExecutionTime() {
        return endTime - startTime;
    }

    @Override
    public String toString() {
        return String.format("%s [Start: %d, End: %d, ExecTime: %d, Priority: %s]",
                id, startTime, endTime, getExecutionTime(), priority);
    }
}

// Strategy Interface for Scheduling Policies
interface SchedulingStrategy {
    String getName();
    Task selectNextTask(List<Task> tasks);
}

// 1. First-Come First-Served Strategy
class FCFSStrategy implements SchedulingStrategy {
    @Override
    public String getName() {
        return "FCFS";
    }

    @Override
    public Task selectNextTask(List<Task> tasks) {
        return tasks.stream()
                .min(Comparator.comparingInt(Task::getStartTime))
                .orElse(null);
    }
}

// 2. Priority Scheduling Strategy
class PriorityStrategy implements SchedulingStrategy {
    @Override
    public String getName() {
        return "Priority Scheduling";
    }

    @Override
    public Task selectNextTask(List<Task> tasks) {
        return tasks.stream()
                .min(Comparator.comparing(Task::getPriority)
                        .thenComparingInt(Task::getStartTime))
                .orElse(null);
    }
}

// 3. Shortest Job First Strategy
class SJFStrategy implements SchedulingStrategy {
    @Override
    public String getName() {
        return "SJF";
    }

    @Override
    public Task selectNextTask(List<Task> tasks) {
        return tasks.stream()
                .min(Comparator.comparingInt(Task::getExecutionTime)
                        .thenComparingInt(Task::getStartTime))
                .orElse(null);
    }
}

// Adaptive Task Scheduler Context
class TaskScheduler {
    private List<Task> waitingQueue = new ArrayList<>();
    private SchedulingStrategy preferredPolicy;

    // Pre-instantiated strategies for adaptive evaluation
    private final SchedulingStrategy priorityStrategy = new PriorityStrategy();
    private final SchedulingStrategy sjfStrategy = new SJFStrategy();

    public TaskScheduler(SchedulingStrategy preferredPolicy) {
        this.preferredPolicy = preferredPolicy;
    }

    public void setPreferredPolicy(SchedulingStrategy preferredPolicy) {
        this.preferredPolicy = preferredPolicy;
    }

    public void addTask(Task task) {
        waitingQueue.add(task);
    }

    // Adaptive Scheduling Logic
    private SchedulingStrategy determineActivePolicy() {
        // Rule 1: Urgent Workload (at least 1 HIGH priority task waiting)
        boolean hasHighPriority = waitingQueue.stream()
                .anyMatch(t -> t.getPriority() == Priority.HIGH);
        if (hasHighPriority) {
            return priorityStrategy;
        }

        // Rule 2: Short-Task Workload (at least 3 tasks with Execution Time <= 3)
        long shortTaskCount = waitingQueue.stream()
                .filter(t -> t.getExecutionTime() <= 3)
                .count();
        if (shortTaskCount >= 3) {
            return sjfStrategy;
        }

        // Rule 3: Normal Workload (fallback to preferred policy)
        return preferredPolicy;
    }

    public Task executeNextTask() {
        if (waitingQueue.isEmpty()) {
            System.out.println("No remaining tasks in the queue.");
            return null;
        }

        SchedulingStrategy activePolicy = determineActivePolicy();
        Task selectedTask = activePolicy.selectNextTask(waitingQueue);

        if (selectedTask != null) {
            waitingQueue.remove(selectedTask);
            System.out.printf("Executed Task: %-4s | Policy Used: %-19s | %s%n",
                    selectedTask.getId(), activePolicy.getName(), selectedTask);
        }

        return selectedTask;
    }

    public void executeAll() {
        while (!waitingQueue.isEmpty()) {
            executeNextTask();
        }
    }
}

// Demo Application matching Example Scenario
public class TaskSchedulerMain {
    public static void main(String[] args) {
        // Initialize scheduler with preferred policy: FCFS
        TaskScheduler scheduler = new TaskScheduler(new FCFSStrategy());

        // Add Example Scenario Tasks
        scheduler.addTask(new Task("T1", 0, 8, Priority.MEDIUM));
        scheduler.addTask(new Task("T2", 1, 4, Priority.LOW));
        scheduler.addTask(new Task("T3", 2, 4, Priority.MEDIUM));
        scheduler.addTask(new Task("T4", 3, 4, Priority.LOW));
        scheduler.addTask(new Task("T5", 4, 9, Priority.HIGH));

        System.out.println("--- Starting Adaptive Task Execution ---");
        scheduler.executeAll();
    }
}