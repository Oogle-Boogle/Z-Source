package com.zamron.engine.task;

import com.zamron.GameSettings;
import com.zamron.world.World;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import org.jctools.queues.MessagePassingQueue;
import org.jctools.queues.MpscArrayQueue;
import org.jctools.queues.SpscArrayQueue;

import java.util.Set;

public enum TaskManager {
    ;

    private static final int CAPACITY = (World.MAX_PLAYERS + World.MAX_NPCS) * 250; //was 20

    private static final Object2ObjectMap<Object, Set<Task>> keyToTasks = new Object2ObjectOpenHashMap<>(CAPACITY);

    private static final MessagePassingQueue<Task> pendingTasks = new MpscArrayQueue<>(CAPACITY);
    private static final MessagePassingQueue<Task> tasks = new SpscArrayQueue<>(CAPACITY);

    private static final MessagePassingQueue.Consumer<Task> pendingTasksConsumer = task -> {
        if (tasks.offer(task)) {
            Object key = task.getKey();
            if (key != null) {
                Set<Task> keyTasks = keyToTasks.get(key);
                if (keyTasks == null) {
                    keyToTasks.put(key, keyTasks = new ObjectOpenHashSet<>());
                }
                keyTasks.add(task);
            }
        }
    };

    private static final MessagePassingQueue.Consumer<Task> tasksConsumer = task -> {
        try {
            Object key = task.getKey();
            if (!task.tick() || !submit(task)) {
                if (key != null) {
                    Set<Task> keyTasks = keyToTasks.get(key);
                    if (keyTasks != null) {
                        keyTasks.remove(task);
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
        }
    };

    public static void sequence() {
        pendingTasks.drain(pendingTasksConsumer, CAPACITY);
        tasks.drain(tasksConsumer, CAPACITY);
    }

    public static boolean submit(Task task) {
        if (!task.isRunning()) return false;
        if (task.isImmediate()) {
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return task.isRunning() && pendingTasks.offer(task);
    }

    public static boolean cancelTasks(Object key) {
        Set<Task> keyTasks = keyToTasks.get(key);
        if (keyTasks == null) return false;
        if (keyTasks.isEmpty()) {
            keyToTasks.remove(key);
            return false;
        }
        for (Task task : keyTasks) {
            task.stop();
        }
        keyToTasks.remove(key);
        return true;
    }

    public static int getTaskAmount() {
        return pendingTasks.size() + tasks.size();
    }

}
