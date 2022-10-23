package com.leul.notespro;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class noteAdapter extends FirestoreRecyclerAdapter<Note,noteAdapter.NoteviewHolder>
     {
         Context context;

         public noteAdapter(@NonNull FirestoreRecyclerOptions<Note> options, Context context) {
             super(options);
             this.context=context;
         }

         @Override
         protected void onBindViewHolder(@NonNull NoteviewHolder holder, int position, @NonNull Note note) {
             holder.titletextview.setText(note.title);
             holder.contenttextview.setText(note.content);
             holder.timestamptextview.setText(Utility.timestamptostring(note.timestamp));

             holder.itemView.setOnClickListener((v)->{
                 Intent intent=new Intent(context,noteDetail.class);
                 intent.putExtra("title",note.title);
                 intent.putExtra("content",note.content);
                 String docId=this.getSnapshots().getSnapshot(position).getId();
                 intent.putExtra("docId",docId);
                 context.startActivity(intent);
             });
         }

         @NonNull
         @Override
         public NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
             View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_note_itme,parent,false);
            return new NoteviewHolder(view);
         }

         class NoteviewHolder extends RecyclerView.ViewHolder{
    TextView titletextview,contenttextview,timestamptextview;
        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);
            titletextview=itemView.findViewById(R.id.note_title_text_view);
            contenttextview=itemView.findViewById(R.id.note_content_text_view);
            timestamptextview=itemView.findViewById(R.id.note_timestamp_text_view);
        }
    }
}