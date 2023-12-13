package com.stuffer.stuffers.fragments.bottom.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.api.ApiUtils;
import com.stuffer.stuffers.api.MainAPIInterface;
import com.stuffer.stuffers.fragments.bottom.chatadapter.MessageAdapter;
import com.stuffer.stuffers.fragments.bottom.chatmodel.Chat;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Data;
import com.stuffer.stuffers.fragments.bottom.chatnotification.MyResponse;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Sender;
import com.stuffer.stuffers.fragments.bottom.chatnotification.Token;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageActivity extends AppCompatActivity {
    private static final String TAG = "MessageActivity";
    private EditText messageEditText;
    private ImageView addMessageImageView;
    private ImageView sendButton;
    private Intent intent;
    private String receiverId;
    private MyTextView tvUserName;
    private List<Chat> mChatList;
    private DatabaseReference reference;
    private MessageAdapter messageAdapter;
    private RecyclerView messageRecyclerView;
    private ValueEventListener seenListener;
    private FirebaseUser fuser;
    public static final int REQUEST_IMAGE = 2;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressBar progressBar;
    //private String Regex = "\\d+";
    private String Regex = "(?<=^| )\\d+(\\.\\d+)?(?=$| )|(?<=^| )\\.\\d+(?=$| )";
    private String mMobileNumber;
    private MainAPIInterface mainAPIInterface;
    private boolean notify = false;
    private String mPhone_number,mArea_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        progressBar = findViewById(R.id.progressBar);
        messageEditText = (EditText) findViewById(R.id.messageEditText);
        addMessageImageView = (ImageView) findViewById(R.id.addMessageImageView);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageRecyclerView = (RecyclerView) findViewById(R.id.messageRecyclerView);
        messageRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView.setLayoutManager(linearLayoutManager);
        intent = getIntent();
        mainAPIInterface = ApiUtils.getApiServiceForNotification("https://fcm.googleapis.com/");
        //mMobileNumber = getIntent().getStringExtra(AppoConstants.MOBILENUMBER);
        receiverId = intent.getStringExtra(AppoConstants.USERID);
        //Log.e(TAG, "onCreate: ID :: " + receiverId);

        setupActionBar();

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        reference = FirebaseDatabase.getInstance().getReference("Users").child(receiverId);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUser user = dataSnapshot.getValue(ChatUser.class);
                //username.setText(user.getUsername());
                tvUserName.setText(Objects.requireNonNull(user).getUsername());
                 mPhone_number = user.getPhone_number();
                String username = user.getUsername();
                mArea_code = user.getArea_code();
                /*Log.e(TAG, "onDataChange: user name : " + username);
                Log.e(TAG, "onDataChange: phone Number : " + phone_number);
                Log.e(TAG, "onDataChange: area_code : "+ area_code);*/

                //readMessages(fuser.getUid(),receiverId,user.getImageURL());
                readMessages(fuser.getUid(), receiverId, "default");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //readMessages(fuser.getUid(), receiverId, "default");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = messageEditText.getText().toString();
                if (msg.matches(Regex)) {
                    notify = false;
                    messageEditText.setText("");
                    Intent intent = new Intent(MessageActivity.this, TransferChatActivityOld.class);
                    intent.putExtra(AppoConstants.AMOUNT, msg);
                    intent.putExtra(AppoConstants.AREACODE,mArea_code);
                    intent.putExtra(AppoConstants.PHWITHCODE,mPhone_number);
                    startActivity(intent);
                    /*Intent intent = new Intent(MessageActivity.this, QuickPaymentActivity.class);
                    intent.putExtra(AppoConstants.AMOUNT, msg);
                    startActivity(intent);*/
                } else if (!msg.equals("")) {

                    sendMessage(fuser.getUid(), receiverId, msg);
                } else {
                    Toast.makeText(MessageActivity.this, "You cannot send an empty message", Toast.LENGTH_SHORT).show();
                }
                messageEditText.setText("");

            }
        });

        addMessageImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Select image for image message on click.
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_IMAGE);

            }
        });
    }


    private void setupActionBar() {
        ImageView ivBack = findViewById(R.id.ivBack);
        tvUserName = findViewById(R.id.tvUserName);
        //tvUserName.setText(intent.getStringExtra(AppoConstants.USERNAME) + "\n" + mMobileNumber);
        //tvUserName.setText(intent.getStringExtra(AppoConstants.USERNAME));
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    public void readMessages(final String myid, final String userid, final String imageurl) {
        mChatList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        mChatList.add(chat);
                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, mChatList, imageurl);
                    messageRecyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        seenMessage(userid);
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void sendMessage(String sender, final String receiver, String message) {
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("isseen", false);

        reference1.child("chats").push().setValue(hashMap);

        final String userid = intent.getStringExtra("userid");
        //extra changes(add user to chat fragment)
        final DatabaseReference chatref = FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(fuser.getUid())
                .child(userid);

        chatref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                chatref.child("id").setValue(userid);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //for notification
        final String msg = message;
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ChatUser user = dataSnapshot.getValue(ChatUser.class);
                if (notify) {

                    sendNotification(receiver, user.getUsername(), msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Log.e(TAG, "onCancelled: error ::" + databaseError.getDetails().toString());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (data != null) {
                Uri uri = data.getData();
                if (uri != null) {
                    uploadImage(uri);
                }
            }
        }


    }

    public void uploadImage(Uri uri) {
        // Defining the child of storageReference
        progressBar.setVisibility(View.VISIBLE);
        // Code for showing progressDialog while uploading
        /*final ProgressDialog progressDialog
                = new ProgressDialog(this);
        progressDialog.setTitle("Please wait uploading...");
        progressDialog.show();*/

        StorageReference ref
                = storageReference
                .child("images/" + UUID.randomUUID().toString());

        ref.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    task.getResult().getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);
                                //progressDialog.dismiss();
                                sendMessage(fuser.getUid(), receiverId, task.getResult().toString());
                            }
                        }
                    });
                } else {
                    progressBar.setVisibility(View.GONE);
                    // progressDialog.dismiss();
                    //Log.e(TAG, "Image upload task was not successful.",
                    //task.getException());
                    Toast.makeText(MessageActivity.this, "image uploading failed", Toast.LENGTH_SHORT).show();
                }
            }
        })/*.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress
                        = (100.0
                        * taskSnapshot.getBytesTransferred()
                        / taskSnapshot.getTotalByteCount());

                progressDialog.setMessage(
                        "Uploaded "
                                + (int) progress + "%");
            }
        }*//*)*/;
    }


    private void sendNotification(String receiver, final String username, final String message) {
        final String userid = intent.getStringExtra("userid");
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Token token = snapshot.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), 0, username + ": " + message, "New Message",
                            userid);
                    //start coding from here
                    Sender sender = new Sender(data, token.getToken());
// ==>  /0i3tUzTXYkW6WCn7HROT6pqgwSe2/-MGyU2EZRFtngMSRSVhr
                    mainAPIInterface.sendNotification(sender).enqueue(new Callback<MyResponse>() {
                        @Override
                        public void onResponse(@NotNull Call<MyResponse> call, @NotNull Response<MyResponse> response) {
                            if (response.code() == 200) {
                                if (response.body().success == 1) {
                                    // Toast.makeText(MessageActivity.this, "Failes", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<MyResponse> call, Throwable t) {
                            Log.e(TAG, "onFailure: " + t.getMessage().toString());
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "onCancelled: " + databaseError.getDetails().toString());

            }
        });
    }
}
