package com.i.vaccin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.MediaCas;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Vaccin_List extends AppCompatActivity {

    TextView Fullname;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    private RecyclerView VaccinList;
    String UserID;
    String TAG = "Vaccin_List";
    private FirestoreRecyclerAdapter adapter;
    //List<VaccinModel> vaccinModel = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccin__list);

        Fullname = findViewById(R.id.Hi);
        VaccinList = findViewById(R.id.VaccinList);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        UserID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(UserID);
        Query vaccinCollection =  fStore.collection("Vaccin");
        FirestoreRecyclerOptions<VaccinModel> options = new FirestoreRecyclerOptions.Builder<VaccinModel>().setQuery(vaccinCollection, VaccinModel.class).build();

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                try {
                    Fullname.setText("Hi, " + documentSnapshot.getString("Full Name"));
                } catch (Exception e){
                    System.out.println(e);
                }

            }

        });
/*
        vaccinCollection
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                VaccinModel item = new VaccinModel(String.valueOf(document.getString("Name")), Integer.parseInt(document.getString("Dose number")));
                                vaccinModel.add(item);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
*/
        adapter = new FirestoreRecyclerAdapter<VaccinModel, VaccinViewHolder>(options) {
            @NonNull
            @Override
            public VaccinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single, parent, false);
                return new VaccinViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VaccinViewHolder holder, int position, @NonNull VaccinModel model) {
                holder.VaccinName.setText(model.getName());
                holder.Doze.setText(model.getDoze()+"");

            }
        };

        VaccinList.setHasFixedSize(true);
        VaccinList.setLayoutManager(new LinearLayoutManager(this));
        VaccinList.setAdapter(adapter);

    }

    private class VaccinViewHolder extends RecyclerView.ViewHolder{

        private TextView VaccinName;
        private TextView Doze;

        public VaccinViewHolder(@NonNull View itemView) {
            super(itemView);
            VaccinName = itemView.findViewById(R.id.Name);
            Doze = itemView.findViewById(R.id.Doze);
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onStart(){
        super.onStart();
        adapter.startListening();
    }

    public void SignOut(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}