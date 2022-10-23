package com.leul.notespro;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class noteDetail extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveNoteBtn;
    TextView pagetitleTextView;
    String title,content,docid;
    boolean iseditmode=false;

    ImageButton deleteNotetextviewBtn,bakkk;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

        titleEditText=findViewById(R.id.note_title_text);
        contentEditText=findViewById(R.id.note_content_text);
        saveNoteBtn=findViewById(R.id.save_btn);
        pagetitleTextView= findViewById(R.id.page_title);
        deleteNotetextviewBtn=findViewById(R.id.delete_note_text_view_btn);
        bakkk=findViewById(R.id.backe_note_text_view_btn);


        title=getIntent().getStringExtra("title");
        content=getIntent().getStringExtra("content");
        docid=getIntent().getStringExtra("docId");

        if (docid!=null && !docid.isEmpty()){
            iseditmode=true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        if(iseditmode){
            pagetitleTextView.setText("Edit your note");
            deleteNotetextviewBtn.setVisibility(View.VISIBLE);
            bakkk.setVisibility(View.VISIBLE);
        }

        saveNoteBtn.setOnClickListener((v)->savenote());
        deleteNotetextviewBtn.setOnClickListener((v)-> deletenotefromfirebase());
        bakkk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(noteDetail.this,MainActivity.class));
                finish();
            }
        });
    }
    void savenote(){
        String noteTitle=titleEditText.getText().toString();
        String noteContent=contentEditText.getText().toString();

        if (noteTitle==null || noteTitle.isEmpty()){
            titleEditText.setError("Title is required");
            return;
        }
        Note note=new Note();
        note.setTitle(noteTitle);
        note.setContent(noteContent);
        note.setTimestamp(Timestamp.now());

        saveNotetofirebase(note);
        finish();
    }

    void saveNotetofirebase(Note note){
        DocumentReference documentReference;
        if (iseditmode){
            documentReference=Utility.getcollectionReferencefornotes().document(docid);
        }else{
            documentReference=Utility.getcollectionReferencefornotes().document();
        }

        documentReference.set(note).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    Utility.showToast(noteDetail.this,"try again");
                }
            }
        });

    }
    void deletenotefromfirebase(){

        DocumentReference documentReference;
            documentReference=Utility.getcollectionReferencefornotes().document(docid);


        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    finish();
                }else {
                    Utility.showToast(noteDetail.this,"try again");
                }
            }
        });
        finish();
    }
}