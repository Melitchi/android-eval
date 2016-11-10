package com.android.melitchi.todolist.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.melitchi.todolist.NotesActivity;
import com.android.melitchi.todolist.PreferenceHelper;
import com.android.melitchi.todolist.R;
import com.android.melitchi.todolist.adapter.NoteAdapter;
import com.android.melitchi.todolist.model.HttpResult;
import com.android.melitchi.todolist.model.JsonParser;
import com.android.melitchi.todolist.model.NetworkHelper;
import com.android.melitchi.todolist.pojos.Note;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends Fragment {

    private ListView listView;
    private EditText noteTxt;
    private Button send;
    private NoteAdapter adapter;
    private SwipeRefreshLayout swipe;
    private View view;
    private GetNotesAsyncTask notesTask;
    Timer timer;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            refresh();
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       final View v = inflater.inflate(R.layout.fragment_notes, container, false);
        listView = (ListView)v.findViewById(R.id.listview);
        noteTxt=(EditText)v.findViewById(R.id.txtToSend);
        send=(Button)v.findViewById(R.id.sendBtn);
        swipe=(SwipeRefreshLayout)v.findViewById(R.id.swipeRefresh);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteTxt.getText().toString().isEmpty()) {
                    noteTxt.setError("vous ne pouvez pas envoyer un message vide");
                    return;
                }
                new SendNoteAsyncTask().execute(noteTxt.getText().toString());
                noteTxt.setText("");
            }
        });
        adapter = new NoteAdapter(inflater.getContext());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idClicked =adapter.getClickedNoteId(position);
                getItemStatus(position, idClicked);

            }
        });


        listView.setAdapter(adapter);
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        swipe.setColorSchemeColors(this.getResources().getColor(R.color.colorAccent), this.getResources().getColor(R.color.colorPrimary));
        return v;
    }
    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        timer.schedule(task, 500, 5000);
    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        notesTask.cancel(true);
    }
public void getItemStatus(int i,String id){
    Boolean done = adapter.getItemStatus(i);
    if(!done){
        alertDone(id);
    }
}
    public void alertDone(final String id){
        new AlertDialog.Builder(NotesFragment.this.getActivity())
                .setTitle("Clore une note")
                .setMessage("Etes vous sûr de vouloir terminer la note?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        new SetDoneAsyncTask().execute(id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public void refresh() {
        if (notesTask == null || notesTask.getStatus() != AsyncTask.Status.RUNNING) {
            notesTask = new GetNotesAsyncTask(NotesFragment.this.getActivity());
            notesTask.execute();
        }
    }
    protected class GetNotesAsyncTask extends AsyncTask<String, Void, List<Note>> {

        Context context;

        public GetNotesAsyncTask(final Context context) {
            this.context = context;
        }

        @Override
        protected List<Note> doInBackground(String... params) {
            if (!NetworkHelper.isInternetAvailable(context)) {
                return null;
            }
            try {
                HttpResult result = NetworkHelper.doGet("http://cesi.cleverapps.io/notes", null, PreferenceHelper.getToken(NotesFragment.this.getActivity()));
                // if ok
                if (result.code == 200) {
                    return JsonParser.getNotes(result.json);
                }
                return null;
            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(final List<Note> notes) {
            int nb = 0;
            if (notes != null) {
                nb = notes.size();
                adapter.setNotes(notes);
            }
            swipe.setRefreshing(false);
            //Toast.makeText(TchatActivity.this, "loaded nb messages: "+nb, Toast.LENGTH_LONG).show();

        }
    }

    protected class SendNoteAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;

            try {
                Map<String, String> p = new HashMap<>();
                p.put("note", params[0]);
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/notes", p, PreferenceHelper.getToken(NotesFragment.this.getActivity()));

                return result.code;

            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(Integer status) {
            if (status != 200) {
                Toast.makeText(NotesFragment.this.getActivity(), "Error sending note", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(NotesFragment.this.getActivity(), "Note sended", Toast.LENGTH_SHORT).show();
                // Snackbar.make(view.findViewById(R.id.activity_fragment_tchat),"Message envoyé",Snackbar.LENGTH_SHORT).show();
            }
        }
    }
    protected class SetDoneAsyncTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;

            try {
                Map<String, String> p = new HashMap<>();
                p.put("done", "true");
                HttpResult result = NetworkHelper.doPost("http://cesi.cleverapps.io/notes/"+params[0], p, PreferenceHelper.getToken(NotesFragment.this.getActivity()));

                return result.code;

            } catch (Exception e) {
                Log.e("NetworkHelper", e.getMessage());
                return null;
            }
        }

        @Override
        public void onPostExecute(Integer status) {
            if (status != 200) {
                Toast.makeText(NotesFragment.this.getActivity(), "Error ending note", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(NotesFragment.this.getActivity(), "Note ended", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
