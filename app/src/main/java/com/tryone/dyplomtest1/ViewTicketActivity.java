package com.tryone.dyplomtest1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tryone.dyplomtest1.constants.Constants;
import com.tryone.dyplomtest1.views.Ticket;

public class ViewTicketActivity extends AppCompatActivity {

    private TextView tvViewTicketTitle, tvViewTicketDescription;
    private Query query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ticket);
        tvViewTicketTitle=findViewById(R.id.tvViewTicketTitle);
        tvViewTicketDescription=findViewById(R.id.tvViewTicketDescription);


    }

    @Override
    protected void onStart() {
        super.onStart();
        ValueEventListener vListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds: snapshot.getChildren()){
                    Ticket ticket=ds.getValue(Ticket.class);
                    assert ticket!=null;
                    tvViewTicketTitle.setText(ticket.name);
                    tvViewTicketDescription.setText(ticket.description);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        Intent i=getIntent();
        if (i!=null){
            query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(i.getStringExtra("userId")).orderByChild("id").equalTo(i.getStringExtra("ticketId"));
        }
        query.addListenerForSingleValueEvent(vListener);
    }
}