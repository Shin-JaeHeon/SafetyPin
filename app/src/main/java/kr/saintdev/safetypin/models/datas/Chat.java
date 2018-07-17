package kr.saintdev.safetypin.models.datas;

public class Chat {
    private String message;
    private String date;
    private String name;
    private boolean teacher;

    public Chat(String message, String date, String name, boolean teacher) {
        this.message = message;
        this.date = date;
        this.name = name;
        this.teacher = teacher;
    }


    public boolean isTeacher() {
        return teacher;
    }

    public void setTeacher(boolean teacher) {
        this.teacher = teacher;
    }

    public String getDate() {
        return date;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }
}
