package com.android.melitchi.todolist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.melitchi.todolist.R;
import com.android.melitchi.todolist.pojos.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by fonta on 08/11/2016.
 */

public class NoteAdapter extends BaseAdapter {
    private final Context context;

    public NoteAdapter(Context ctx){this.context=ctx;}
    List<Note> notes = new LinkedList<>();


    public void setNotes(List<Note>notes){
        this.notes = notes;
        this.notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        if(notes == null)
            return 0;
        return notes.size();
    }

    @Override
    public Note getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    public String getClickedNoteId(int i){
        return notes.get(i).getId();
    }
    public Boolean getItemStatus(int i){
        return notes.get(i).getDone();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater inflater =((Activity)context).getLayoutInflater();
        view = inflater.inflate(R.layout.item_notes,viewGroup,false);

        TextView id =(TextView) view.findViewById(R.id.note_id);
        TextView user =(TextView) view.findViewById(R.id.note_user);
        TextView message =(TextView) view.findViewById(R.id.note_message);
        TextView date =(TextView) view.findViewById(R.id.note_date);
        TextView done =(TextView) view.findViewById(R.id.note_done);

        id.setText(getItem(i).getId());
        user.setText(getItem(i).getUsername());
        message.setText(getItem(i).getNote());
        done.setText(String.valueOf(getItem(i).getDone()));
        DateFormat dateForm = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date netDate = (new Date(getItem(i).getDate()));
        date.setText(String.valueOf(dateForm.format(netDate)));

        return view;
    }


}
