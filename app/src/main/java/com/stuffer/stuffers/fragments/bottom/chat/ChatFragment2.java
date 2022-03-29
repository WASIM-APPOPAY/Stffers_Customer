package com.stuffer.stuffers.fragments.bottom.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.fragments.bottom.chatadapter.UserAdapter;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatList;
import com.stuffer.stuffers.fragments.bottom.chatmodel.ChatUser;
import com.stuffer.stuffers.views.MyTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment2 extends Fragment {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseUser fuser;
    private List<ChatList> userList;
    private DatabaseReference reference;
    private List<ChatUser> mUsers;
    private UserAdapter userAdapter;
    private MyTextView tvInfo;

    public ChatFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_chat2, container, false);
        recyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view_chat);
        recyclerView.setHasFixedSize(true);
        progressBar = (ProgressBar) mView.findViewById(R.id.chats_progressbar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tvInfo = mView.findViewById(R.id.tvInfo);
        fuser = FirebaseAuth.getInstance().getCurrentUser();

        userList = new ArrayList<>();


        if (fuser != null) {
            progressBar.setVisibility(View.VISIBLE);
            reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    userList.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        ChatList chatList = snapshot.getValue(ChatList.class);
                        userList.add(chatList);
                    }
                    chatList();
                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        return mView;
    }

    private void chatList() {
        progressBar.setVisibility(View.VISIBLE);
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ChatUser user = snapshot.getValue(ChatUser.class);
                    for (ChatList chatList : userList) {
                        if (user.getId().equals(chatList.getId())) {
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UserAdapter(getContext(), mUsers, true);
                recyclerView.setAdapter(userAdapter);
                progressBar.setVisibility(View.GONE);
                if (mUsers.size() > 0) {
                    tvInfo.setVisibility(View.GONE);
                } else {
                    tvInfo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
