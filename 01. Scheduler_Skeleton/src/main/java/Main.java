import core.ProcessScheduler;
import model.ScheduledTask;
import model.Task;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ProcessScheduler processScheduler =new ProcessScheduler();
        Task task = new ScheduledTask(111,"1");
        Task task1 = new ScheduledTask(1,"1");
        Task task2 = new ScheduledTask(2,"1");
        Task task3 = new ScheduledTask(3,"1");
        Task task4 = new ScheduledTask(4,"1");
        Task task5 = new ScheduledTask(5,"1");
        Task task6 = new ScheduledTask(6,"1");
        processScheduler.add(task1);
        processScheduler.add(task2);
        processScheduler.add(task3);
        processScheduler.add(task4);
        processScheduler.add(task5);
        processScheduler.add(task6);

        processScheduler.insertAfter(task6.getId(),task);
    }
}