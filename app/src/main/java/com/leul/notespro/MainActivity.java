 package com.leul.notespro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

 public class MainActivity extends AppCompatActivity {
    FloatingActionButton addNoteBtn;
    RecyclerView recyclerView;
    ImageButton menubtn;
    noteAdapter noteAdapte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addNoteBtn=findViewById(R.id.add_note);
        recyclerView=findViewById(R.id.recycler_view);
        menubtn=findViewById(R.id.menu_btn);

        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(MainActivity.this,noteDetail.class)));

        menubtn.setOnClickListener((v)-> showmenu());
        setupRecyclerView();
    }

    void showmenu(){
        PopupMenu popupMenu=new PopupMenu(MainActivity.this,menubtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this,login.class));
                    finish();
                    return true;
                }
                return false;
            }
        });
    }

    void setupRecyclerView(){
        Query query=Utility.getcollectionReferencefornotes().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options=new FirestoreRecyclerOptions.Builder<Note>().setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteAdapte=new noteAdapter(options,this);
        recyclerView.setAdapter(noteAdapte);
    }

     @Override
     protected void onStart() {
         super.onStart();
         noteAdapte.startListening();
     }

     @Override
     protected void onStop() {
         super.onStop();
         noteAdapte.stopListening();
     }

     @Override
     protected void onResume() {
         super.onResume();
         noteAdapte.notifyDataSetChanged();
     }
 }