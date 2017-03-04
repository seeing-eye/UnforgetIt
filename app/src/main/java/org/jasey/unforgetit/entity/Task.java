package org.jasey.unforgetit.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name = Task.TABLE_NAME)
public class Task {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    public static final String DELIMITER = " ";
    public static final String TIME_FORMAT = "HH:mm";
    public static final String TABLE_NAME = "tasks";
    public static final String ID = "id";
    public static final String TASK_TITLE_COLUMN = "title";
    public static final String TASK_DATE_COLUMN = "date";
    public static final String TASK_PRIORITY_COLUMN = "priority_level";
    public static final String TASK_DONE_COLUMN = "done";
    public static final String TASK_ALARM_ADVANCE_TIME_COLUMN = "alarm_advance_time";
    public static final Comparator<Task> TASK_COMPARATOR = new Comparator<Task>() {
        @Override
        public int compare(Task t1, Task t2) {
            int result = t1.getDate().compareTo(t2.getDate());
            if (result == 0) {
                result = t1.getPriorityLevel() - t2.getPriorityLevel();
            }
            if (result == 0) {
                result = t1.getTitle().compareTo(t2.getTitle());
            }
            return result;
        }
    };

    public static final int PRIORITY_LOW = 0;
    public static final int PRIORITY_NORMAL = 1;
    public static final int PRIORITY_HIGH = 2;

    public static final int ALARM_ADVANCE_10 = 10*60000;
    public static final int ALARM_ADVANCE_30 = 30*60000;

    public static Task buildTask(Long id, String title, Date date, int priorityLevel, boolean done, int alarmAdvanceTime) {
        return new Task(id, title, date, priorityLevel, done, alarmAdvanceTime);
    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = TASK_TITLE_COLUMN, nullable = false)
    private String title;

    @Column(name = TASK_DATE_COLUMN, nullable = false)
    @DatabaseField(columnName = TASK_DATE_COLUMN, dataType = DataType.DATE_LONG, canBeNull = false)
    private Date date;

    @Column(name = TASK_PRIORITY_COLUMN, nullable = false)
    private int priorityLevel;

    @Column(name = TASK_DONE_COLUMN, nullable = false)
    private boolean done;

    @Column(name = TASK_ALARM_ADVANCE_TIME_COLUMN, nullable = false)
    private int alarmAdvanceTime;

    public Task() {
    }

    private Task(Long id, String title, Date date, int priorityLevel, boolean done, int alarmAdvanceTime) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.priorityLevel = priorityLevel;
        this.done = done;
        this.alarmAdvanceTime = alarmAdvanceTime;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getAlarmAdvanceTime() {
        return alarmAdvanceTime;
    }

    public void setAlarmAdvanceTime(int alarmAdvanceTime) {
        this.alarmAdvanceTime = alarmAdvanceTime;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Task task = (Task) o;

        if (priorityLevel != task.priorityLevel) return false;
        if (done != task.done) return false;
        if (id != null ? !id.equals(task.id) : task.id != null) return false;
        if (!title.equals(task.title)) return false;
        if (alarmAdvanceTime != task.alarmAdvanceTime) return false;

        return date.equals(task.date);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + title.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + priorityLevel;
        result = 31 * result + (done ? 1 : 0);
        result = 31 * result + alarmAdvanceTime;
        return result;
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", priorityLevel=" + priorityLevel +
                ", done=" + done +
                ", alarmAdvanceTime=" + alarmAdvanceTime +
                '}';
    }
}