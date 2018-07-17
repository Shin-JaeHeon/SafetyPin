package kr.saintdev.safetypin.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;

import kr.saintdev.safetypin.R;
import kr.saintdev.safetypin.models.datas.Chat;
import kr.saintdev.safetypin.models.datas.ChatListItem;

public class ChatListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Chat> chatList;

    public ChatListAdapter(Context context) {
        super();
        this.context = context;
        this.chatList = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return chatList.size();
    }

    @Override
    public Object getItem(int position) {
        return chatList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null) return convertView;
        ChatListItem item;
        Chat chat = chatList.get(position);
        item = new ChatListItem();
        convertView = inflater.inflate(chat.isTeacher() ? R.layout.item_message_received : R.layout.item_message_sent, null);
        convertView.setClickable(false);
        convertView.setFocusable(false);
        item.setMessage((TextView) convertView.findViewById(chat.isTeacher() ? R.id.received_message : R.id.sent_message));
        item.setDate((TextView) convertView.findViewById(chat.isTeacher() ? R.id.received_date : R.id.sent_date));
        if (chat.isTeacher())
            item.setName((TextView) convertView.findViewById(R.id.received_name));
        convertView.setTag(item);
        item.getMessage().setText(chat.getMessage());
        item.getDate().setText(chat.getDate());
        if (chat.isTeacher()) item.getName().setText(chat.getName());
        return convertView;
    }

    public void addItem(String msg, String date, String name, int teacher) {
        this.chatList.add(new Chat(msg, date, name, teacher == 1));
    }

    public void clear() {
        this.chatList = new ArrayList<>();
    }
}
