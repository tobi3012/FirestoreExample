package com.example.hieptq.firestoreexample;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    private EditText etTitle, etDescription, etPrority;
    private TextView tvLoad;

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = firebaseFirestore.collection("Notebook");
    private DocumentReference reference = firebaseFirestore.document("Notebook/My First Note");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etTitle = findViewById(R.id.edit_text_title);
        etDescription = findViewById(R.id.edit_text_description);
        etPrority = findViewById(R.id.edit_text_priority);
        tvLoad = findViewById(R.id.tv_load);

    }

    @Override
    protected void onStart() {
        super.onStart();
        /*reference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(MainActivity.class.getSimpleName(), e.toString());
                    return;
                }
                if (documentSnapshot.exists()) {
                    Note note = documentSnapshot.toObject(Note.class);
                    String title = note.getTitle();
                    String description = note.getDescription();
                    tvLoad.setText("Title: " + title + "\nDescription: " + description);
                }
            }
        });*/
        collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null)
                    return;
                String data = "";
                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                    Note note = queryDocumentSnapshot.toObject(Note.class);
                    note.setDocumentId(queryDocumentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();
                    int priority = note.getPriority();
                    data += "ID: " + documentId
                            + "\nTitle: " + title + "\nDescription: " + description + "\nPriority: " + priority + "\n\n";
                }
                tvLoad.setText(data);
            }
        });
    }

    /*public void saveNote(View v) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();

//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);

        Note note = new Note(title, description);

        reference.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(MainActivity.class.getSimpleName(), e.toString());
                    }
                });
    }*/

    public void loadNote(View view) {
        Task task1 = collectionReference.whereGreaterThan("priority", 2).orderBy("priority").get();
        Task task2 = collectionReference.whereLessThan("priority", 2).orderBy("priority").get();
        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(task1, task2);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                String data = "";
                for (QuerySnapshot querySnapshot : querySnapshots) {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : querySnapshot) {
                        Note note = queryDocumentSnapshot.toObject(Note.class);
                        note.setDocumentId(queryDocumentSnapshot.getId());

                        String documentId = note.getDocumentId();
                        String title = note.getTitle();
                        String description = note.getDescription();
                        int priority = note.getPriority();
                        data += "ID: " + documentId
                                + "\nTitle: " + title + "\nDescription: " + description + "\nPriority: " + priority + "\n\n";
                    }
                    tvLoad.setText(data);
                }
            }
        });
        /*reference.get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot != null) {
                            Note note = documentSnapshot.toObject(Note.class);
                            String title = note.getTitle();
                            String description = note.getDescription();
                            tvLoad.setText("Title: " + title + "\nDescription: " + description);
                        } else {
                            Toast.makeText(MainActivity.this, "No Data to load!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(MainActivity.class.getSimpleName(), e.toString());
                    }
                });*/
        /*collectionReference.whereGreaterThanOrEqualTo("priority", 27)
//                .orderBy("priority", Query.Direction.DESCENDING)
//                .limit(3)
                .orderBy("priority")
                .orderBy("title")
                .get().
                addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            Note note = queryDocumentSnapshot.toObject(Note.class);
                            note.setDocumentId(queryDocumentSnapshot.getId());

                            String documentId = note.getDocumentId();
                            String title = note.getTitle();
                            String description = note.getDescription();
                            int priority = note.getPriority();
                            data += "ID: " + documentId
                                    + "\nTitle: " + title + "\nDescription: " + description + "\nPriority: " + priority + "\n\n";
                        }
                        tvLoad.setText(data);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(MainActivity.class.getSimpleName(), "onFailure: " + e.toString());
                    }
                });*/
    }

    public void updateDescription(View view) {
        String description = etDescription.getText().toString();
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION, description);
//        reference.set(note, SetOptions.merge());
        reference.update(KEY_DESCRIPTION, description);
    }

    public void deleteDescription(View view) {
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_DESCRIPTION, FieldValue.delete());
//        reference.update(note);
        reference.update(KEY_DESCRIPTION, FieldValue.delete());
    }

    public void deleteNote(View view) {
        if (reference != null)
            reference.delete();
    }

    public void addNote(View view) {
        String title = etTitle.getText().toString();
        String description = etDescription.getText().toString();
        int priority = Integer.parseInt(etPrority.getText().toString());

        Note note = new Note(title, description, priority);
        collectionReference.add(note);
    }
}
