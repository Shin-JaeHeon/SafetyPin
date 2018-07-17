package kr.saintdev.safetypin.models.datas;

import android.widget.TextView;

public class ChatListItem {
    private TextView message;
    private TextView date;
    private TextView name;
    private boolean teacher;

    public TextView getDate() {
        return date;
    }

    public TextView getMessage() {
        return message;
    }

    public TextView getName() {
        return name;
    }

    public void setDate(TextView date) {
        this.date = date;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public void setName(TextView name) {
        this.name = name;
    }
}