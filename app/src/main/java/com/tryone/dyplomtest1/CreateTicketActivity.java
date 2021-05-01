package com.tryone.dyplomtest1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.tryone.dyplomtest1.constants.Constants;
import com.tryone.dyplomtest1.utils.ImageListForCreateTicketAdapter;
import com.tryone.dyplomtest1.views.Ticket;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CreateTicketActivity extends AppCompatActivity {
    private DatabaseReference databaseReference, adminAreasReference, localityReference;
    private FirebaseAuth mAuth;
    private EditText etTicketTitle, etTicketDescription;
    private TextView tvTicketAddress, tvTicketCoordinates;
    private FirebaseUser fUser;

    private ListView lvCreateTicketImages;
    private ArrayAdapter<String> adapter;
    private ImageListForCreateTicketAdapter coolAdapter;
    private List<String> imageUris;
    private List<Uri> uriList;

    private StorageReference storageRef;

    private Ticket ticket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ticket_new);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101 && data != null && data.getData() != null) {
            if (resultCode == RESULT_OK) {
                Log.d("MyLog", "Image URI: " + data.getData());
                //ivUploadedImg.setImageURI(data.getData());
                imageUris.add(data.getData().toString());
                uriList.add(data.getData());
                adapter.notifyDataSetChanged();
                coolAdapter.notifyDataSetChanged();
            }
        }
    }

    private void init() {
        etTicketTitle = findViewById(R.id.etTicketTitleNew);
        etTicketDescription = findViewById(R.id.etTicketDescriptionNew);
        Intent i=getIntent();
        tvTicketAddress=findViewById(R.id.tvTicketAddress);
        tvTicketCoordinates=findViewById(R.id.tvTicketCoordinates);
        tvTicketAddress.setText(i.getStringExtra(Constants.MAP_ADDRESS_EXTRA));
        String coords="["+i.getDoubleExtra(Constants.MAP_COORDS_EXTRA_LATITUDE,0d)+" : "+i.getDoubleExtra(Constants.MAP_COORDS_EXTRA_LONGITUDE,0d)+"]";
        tvTicketCoordinates.setText(coords);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constants.TICKETS_KEY);
        adminAreasReference=FirebaseDatabase.getInstance().getReference(Constants.ADMIN_AREAS_KEY);
        localityReference=FirebaseDatabase.getInstance().getReference(Constants.LOCALITIES_KEY);

        mAuth = FirebaseAuth.getInstance();
        fUser = mAuth.getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference("imagesDB");

        //initiate list
        lvCreateTicketImages = findViewById(R.id.lvCreateTicketImages);
        imageUris = new LinkedList<>();
        uriList=new LinkedList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, imageUris);
        coolAdapter=new ImageListForCreateTicketAdapter(this,R.layout.list_create_ticket_images_item,uriList,getLayoutInflater());
        lvCreateTicketImages.setAdapter(coolAdapter);
    }


    public void onClickTicketCreate(View view) {
        String ticketId = databaseReference.push().getKey();
        Intent oldI=getIntent();
        ticket = new Ticket(etTicketTitle.getText().toString(), etTicketDescription.getText().toString(), ticketId, mAuth.getUid(),oldI.getStringExtra(Constants.MAP_ADDRESS_EXTRA),oldI.getDoubleExtra(Constants.MAP_COORDS_EXTRA_LATITUDE,0d),oldI.getDoubleExtra(Constants.MAP_COORDS_EXTRA_LONGITUDE,0d));

        String adminArea=oldI.getStringExtra(Constants.MAP_ADMIN_AREA_EXTRA);
        String locality=oldI.getStringExtra(Constants.MAP_LOCALITY_EXTRA);
        Log.d("MapData","Admin area: "+adminArea);
        Log.d("MapData","Locality: "+locality);
        if (adminArea!=null){
            ticket.adminArea=adminArea.hashCode();
            Log.d("MapData","Admin area hashCode: "+adminArea.hashCode());
            adminAreasReference.child(String.valueOf(adminArea.hashCode())).setValue(adminArea);
            if (locality!=null){
                Log.d("MapData","Locality hashCode: "+locality.hashCode());
                ticket.locality=locality.hashCode();
                localityReference.child(String.valueOf(adminArea.hashCode())).child(String.valueOf(locality.hashCode())).setValue(locality);
            }
        }
        //DatabaseReference toPush=databaseReference.child(fUser.getUid()).push();//.setValue(ticket);
        DatabaseReference toPush=databaseReference.push();
        ticket.id=toPush.getKey();
        toPush.setValue(ticket);
        for (Uri imageUri: uriList){
            Log.d("Upload File","File Uri: "+imageUri);
            uploadImageFromUriByStream(imageUri);
            Log.d("Upload File","File Uri: "+imageUri + "Completed");
        }


        Intent i = new Intent(CreateTicketActivity.this, MainActivity.class);
        startActivity(i);
    }

    public void onClickChooseImage(View view) {
        Intent ic = new Intent();
        ic.setAction(Intent.ACTION_GET_CONTENT);
        ic.setType("image/*");
        startActivityForResult(ic, 101);
    }

    private void uploadImageFromUriByStream(Uri imageUri) {
        try {
            //Uri uri = Uri.fromFile(new File(imageUri));
            InputStream stream = getContentResolver().openInputStream(imageUri);//new FileInputStream(new File(imageUri));
            final StorageReference toSave = storageRef.child(fUser.getUid()).child(ticket.id).child(ticket.id + System.currentTimeMillis());
            UploadTask ut = toSave.putStream(stream);
            Task<Uri> task = ut.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    return toSave.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    Log.d("URLD: ",task.getResult().toString());
                    ticket.imageUrls.add(task.getResult().toString());
                    databaseReference.child(mAuth.getUid()).child(ticket.id).child("imageUrls").setValue(ticket.imageUrls);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}