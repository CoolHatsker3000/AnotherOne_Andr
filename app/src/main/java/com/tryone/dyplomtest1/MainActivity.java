package com.tryone.dyplomtest1;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.tryone.dyplomtest1.constants.Constants;
import com.tryone.dyplomtest1.utils.TicketsArrayAdapter;
import com.tryone.dyplomtest1.views.Ticket;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private ArrayAdapter<String> adapter;
    private TicketsArrayAdapter ticketsAdapter;
    private ListView lvTickets;
    private int category=R.id.nav_all,minStatus=0,maxStatus=7;
    private List<Ticket> tickets;
    private ValueEventListener vListener;
    private Query query;

/*
0 - открыт
1 - принят
2 - рассматривается
3 - выполняется
4 - выполнен
5 - закрыт
6 - аир
 */



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        init();


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,titles);

        lvTickets.setAdapter(ticketsAdapter);

        ticketsAdapter.notifyDataSetChanged();
    }
    private void init(){
        mAuth = FirebaseAuth.getInstance();
        tickets=new LinkedList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        lvTickets=findViewById(R.id.lvTickets);
        ticketsAdapter=new TicketsArrayAdapter(this,R.layout.list_ticket_view_1,tickets,getLayoutInflater());
        lvTickets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(MainActivity.this,ViewTicketActivity.class);
                i.putExtra("ticketId",tickets.get(position).id);
                i.putExtra("userId",mAuth.getCurrentUser().getUid());
                startActivity(i);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        //query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(mAuth.getCurrentUser().getUid());
        query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).orderByChild("uid").equalTo(mAuth.getCurrentUser().getUid());

        vListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (tickets.size()>0) tickets.clear();
                for (DataSnapshot ds : snapshot.getChildren()){
                    Ticket ticket=ds.getValue(Ticket.class);
                    assert ticket!=null;
                    if (ticket.status>=minStatus && ticket.status<maxStatus){
                        tickets.add(ticket);
                    }
                }
                ticketsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        //query.addListenerForSingleValueEvent(vListener);
        query.addListenerForSingleValueEvent(vListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        category=menuItem.getItemId();
        if (category==R.id.nav_all){
            //fillList(id);
            /*query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(mAuth.getCurrentUser().getUid());
            query.addListenerForSingleValueEvent(vListener);*/
            minStatus=0;
            maxStatus=7;
            query.removeEventListener(vListener);
            query.addListenerForSingleValueEvent(vListener);
        } else if (category==R.id.nav_opened){
            /*query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(mAuth.getCurrentUser().getUid()).orderByChild("status").startAt(0).endAt(4);
            query.addListenerForSingleValueEvent(vListener);*/
            minStatus=0;
            maxStatus=5;
            query.removeEventListener(vListener);
            query.addListenerForSingleValueEvent(vListener);
        } else if (category==R.id.nav_air){
            /*query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(mAuth.getCurrentUser().getUid()).orderByChild("status").equalTo(6);
            query.addListenerForSingleValueEvent(vListener);*/
            minStatus=6;
            maxStatus=7;
            query.removeEventListener(vListener);
            query.addListenerForSingleValueEvent(vListener);
        } else if (category==R.id.nav_resolved){
            /*query=FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY).child(mAuth.getCurrentUser().getUid()).orderByChild("status").equalTo(5);
            query.addListenerForSingleValueEvent(vListener);*/
            minStatus=5;
            maxStatus=6;
            query.removeEventListener(vListener);
            query.addListenerForSingleValueEvent(vListener);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onClickAddCreateTicket(View view) {
        Intent i=new Intent(MainActivity.this,CreateTicketActivity.class);
        Intent mapI=new Intent(MainActivity.this,MapActivityTest.class);
        startActivity(mapI);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.action_logout){
            FirebaseAuth.getInstance().signOut();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
