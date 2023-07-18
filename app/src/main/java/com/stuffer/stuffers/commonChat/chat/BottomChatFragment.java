package com.stuffer.stuffers.commonChat.chat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.gson.Gson;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chatAdapters.ChatAdapter;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.views.MyRecyclerView;



import java.util.ArrayList;
import java.util.Collections;


public class BottomChatFragment extends BaseFragment implements View.OnClickListener {


    private FloatingActionButton addConversation;
    private ChatHelper helper;
    User loggedInUser;
    private static final String TAG = "BottomChatFragment";
    private Context mContext;
    private boolean groupChats = false;
    private ArrayList<Chat> chatDataList = new ArrayList<>();
    private MyRecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private DatabaseReference myInboxRef;

    public BottomChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        helper = new ChatHelper(mContext);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        helper.saveChats(("chats_" + groupChats), chatDataList);
        mContext = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerChatUpdates();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        helper = new ChatHelper(getActivity());
        View mView = inflater.inflate(R.layout.fragment_bottom_chat, container, false);
        addConversation = mView.findViewById(R.id.addConversation);

        loggedInUser = helper.getLoggedInUser();


        addConversation.setOnClickListener(this);

        recyclerView = mView.findViewById(R.id.recycler_view);
        recyclerView.setEmptyView(mView.findViewById(R.id.emptyView));
        recyclerView.setEmptyImageView(((ImageView) mView.findViewById(R.id.emptyImage)));
        TextView emptyTextView = mView.findViewById(R.id.emptyText);
        emptyTextView.setText(getString(groupChats ? R.string.empty_group_chat_list : R.string.empty_text_chat_list));
        recyclerView.setEmptyTextView(emptyTextView);

        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<Chat> savedChats = helper.getChats("chats_" + groupChats);
        String s = new Gson().toJson(savedChats);
        Log.e(TAG, "onViewCreated: "+s );
        //Collections.reverse(savedChats);
        chatDataList.addAll(savedChats);
        chatAdapter = new ChatAdapter(getActivity(), chatDataList);
        recyclerView.setAdapter(chatAdapter);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.addConversation) {

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
            if (firebaseUser != null) {
                firebaseUser.getIdToken(false).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String idToken = task.getResult().getToken();
                        InviteActivity.startInvite(getActivity(), loggedInUser.getId(), idToken);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure: called");
                        e.printStackTrace();
                    }
                });
            }
        }
    }
    public void addMessage(Chat chat) {
        Log.e(TAG, "addMessage: called" );
        if (mContext != null && chatAdapter != null) {
            int pos = chatDataList.indexOf(chat);
            if (pos == -1) {
                chatDataList.add(chat);
            } else {
                chatDataList.set(pos, chat);
            }
            Collections.sort(chatDataList, (one, two) -> {
                Long oneTime = Long.valueOf(one.getDateTimeStamp());
                Long twoTime = Long.valueOf(two.getDateTimeStamp());
                return twoTime.compareTo(oneTime);
            });
            refreshUnreadIndicatorFor(chat.getChatChild(), false);
        }
    }

    public void refreshUnreadIndicatorFor(String chatChild, boolean force) {
        if (mContext != null && chatAdapter != null && chatAdapter.getItemCount() > 0) {
            chatAdapter.loadLastReadIds(chatChild, force);
            chatAdapter.notifyDataSetChanged();
        }
    }

    private ChildEventListener chatChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                if (newMessage != null && newMessage.getId() != null && newMessage.getChatId() != null) {

                    if (newMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(newMessage);
                    }

                    Chat newChat = new Chat(newMessage, newMessage.getSenderId().equals(userMe.getId()));


                    //Log.e(TAG, "onChildAdded: "+new Gson().toJson(newChat) );
                    if (!newChat.isGroup()) {
                        newChat.setChatName(getNameById(newChat.getUserId()));
//                            for (User user : myUsers) {
//                                if (user.getId().equals(newChat.getUserId())) {
//                                    newChat.setChatName(user.getNameToDisplay());
//                                    break;
//                                }
//                            }
                    }
                    //if (adapter != null) {
                    //Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.landingContainer);
                    //if (mFragment instanceof BottomChatFragment) {
                        addMessage(newChat);
                    //}

                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null) {
                Message updatedMessage = dataSnapshot.getValue(Message.class);
                if (updatedMessage != null && updatedMessage.getId() != null && updatedMessage.getChatId() != null) {
                    if (updatedMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(updatedMessage);
                    }

                    Chat newChat = new Chat(updatedMessage, updatedMessage.getSenderId().equals(userMe.getId()));
                    if (!newChat.isGroup()) {
                        newChat.setChatName(getNameById(newChat.getUserId()));
                        //Fragment mFragment = getSupportFragmentManager().findFragmentById(R.id.landingContainer);
                        //if (mFragment instanceof BottomChatFragment) {
                            //((BottomChatFragment) mFragment).addMessage(newChat);
                        //}
                        addMessage(newChat);
                    }
                }
            }
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    private void registerChatUpdates() {
        if (myInboxRef == null) {
            myInboxRef = inboxRef.child(userMe.getId());
            myInboxRef.addChildEventListener(chatChildEventListener);
        }
    }
}