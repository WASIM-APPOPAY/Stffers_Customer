package com.stuffer.stuffers.commonChat.chat;

import static android.os.Build.VERSION.SDK_INT;
import static android.view.View.VISIBLE;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.kbeanie.multipicker.api.AudioPicker;
import com.kbeanie.multipicker.api.CameraImagePicker;
import com.kbeanie.multipicker.api.FilePicker;
import com.kbeanie.multipicker.api.ImagePicker;
import com.kbeanie.multipicker.api.Picker;
import com.kbeanie.multipicker.api.VideoPicker;
import com.kbeanie.multipicker.api.callbacks.AudioPickerCallback;
import com.kbeanie.multipicker.api.callbacks.FilePickerCallback;
import com.kbeanie.multipicker.api.callbacks.ImagePickerCallback;
import com.kbeanie.multipicker.api.callbacks.VideoPickerCallback;
import com.kbeanie.multipicker.api.entity.ChosenAudio;
import com.kbeanie.multipicker.api.entity.ChosenFile;
import com.kbeanie.multipicker.api.entity.ChosenImage;
import com.kbeanie.multipicker.api.entity.ChosenVideo;
import com.onesignal.OneSignal;
import com.skyfishjy.library.RippleBackground;
import com.stuffer.stuffers.AppoPayApplication;
import com.stuffer.stuffers.BuildConfig;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.wallet.HomeActivity2;
import com.stuffer.stuffers.activity.wallet.SignInActivity;
import com.stuffer.stuffers.commonChat.chatAdapters.ContactsAdapter;
import com.stuffer.stuffers.commonChat.chatAdapters.MessageAdapter;
import com.stuffer.stuffers.commonChat.chatModel.Attachment;
import com.stuffer.stuffers.commonChat.chatModel.AttachmentTypes;
import com.stuffer.stuffers.commonChat.chatModel.Chat;
import com.stuffer.stuffers.commonChat.chatModel.ChatMore;
import com.stuffer.stuffers.commonChat.chatModel.DownloadFileEvent;
import com.stuffer.stuffers.commonChat.chatModel.Group;
import com.stuffer.stuffers.commonChat.chatModel.Message;
import com.stuffer.stuffers.commonChat.chatModel.User;
import com.stuffer.stuffers.commonChat.chatUtils.ChatHelper;
import com.stuffer.stuffers.commonChat.chatUtils.ConfirmationDialogFragment;
import com.stuffer.stuffers.commonChat.chatUtils.DownloadUtil;
import com.stuffer.stuffers.commonChat.chatUtils.FileUtils;
import com.stuffer.stuffers.commonChat.chatUtils.KeyboardUtil;
import com.stuffer.stuffers.commonChat.chatUtils.MyFileProvider;
import com.stuffer.stuffers.commonChat.interfaces.MoreListener;
import com.stuffer.stuffers.commonChat.interfaces.OnMessageItemClick;
import com.stuffer.stuffers.commonChat.interfaces.ProceedRequest;
import com.stuffer.stuffers.commonChat.viewHolders.BaseMessageViewHolder;
import com.stuffer.stuffers.commonChat.viewHolders.MessageAttachmentRecordingViewHolder;
import com.stuffer.stuffers.myService.UploadAndSendService;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.utils.DataVaultManager;

import com.vanniktech.emoji.EmojiEditText;
import com.vanniktech.emoji.EmojiPopup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import ezvcard.Ezvcard;
import ezvcard.VCard;
import ezvcard.io.chain.ChainingTextStringParser;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;


public class ChatActivity extends BaseActivity implements OnMessageItemClick, MessageAttachmentRecordingViewHolder.RecordingViewInteractor, View.OnClickListener, ImagePickerCallback, FilePickerCallback, AudioPickerCallback, VideoPickerCallback, MoreListener, ProceedRequest {
    private static final int REQUEST_CODE_CONTACT = 1;
    private static final int REQUEST_PLACE_PICKER = 2;
    private static final int REQUEST_CODE_PLAY_SERVICES = 3;
    private static final int REQUEST_CODE_UPDATE_USER = 753;
    private static final int REQUEST_CODE_UPDATE_GROUP = 357;
    private static final int REQUEST_PERMISSION_RECORD = 159;
    private static final int REQUEST_PERMISSION_CALL = 951;
    private static final int FIREBASE_MESSAGE_QUERY_LIMIT = 50;
    private static String EXTRA_DATA_CHAT = "extradatachat";
    private static String MOBILE_NUMBER = "mobilenumber";
    private static String EXTRA_DATA_LIST = "extradatalist";
    private static String DELETE_TAG = "deletetag";
    private MessageAdapter messageAdapter;
    private ArrayList<Message> dataList = new ArrayList<>();
    private int countSelected = 0;

    private Handler recordWaitHandler, recordTimerHandler;
    private Runnable recordRunnable, recordTimerRunnable;
    private MediaRecorder mRecorder = null;
    private String recordFilePath;
    private float displayWidth;
    private boolean callIsVideo;
    private ArrayList<String> userPlayerIds = new ArrayList<>();
    private ArrayList<String> deletedMessages = new ArrayList<>();
    private ArrayList<String> adapterMediaMessagePositions = new ArrayList<>();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private String currentlyPlaying = "";

    private Toolbar toolbar;
    private RelativeLayout toolbarContent;
    private TextView selectedCount, status, userName;
    private TableLayout addAttachmentLayout;
    private RecyclerView recyclerView;
    private EmojiEditText newMessage;
    private ImageView usersImage, addAttachment, sendMessage, attachment_emoji;
    private LinearLayout rootView, sendContainer;
    private ImageView callAudio, callVideo, chatMore;
    private RippleBackground rippleBackground;

    private String cameraPhotoPath;
    private EmojiPopup emojIcon;

    private String pickerPath;
    private ImagePicker imagePicker;
    private CameraImagePicker cameraPicker;
    private FilePicker filePicker;
    private AudioPicker audioPicker;
    private VideoPicker videoPicker;

    private Chat chat;
    private Group group;
    private User user;
    private boolean listeningChats = false, ignoreSendClick = false;

    private Dialog myDialog1;
    private ImageView contactImage;
    private TextView contactName, addToContactText;
    private RecyclerView contactPhones, contactEmails;
    private boolean resumed;
    private RecyclerView rvBottomChat;

    //Download complete listener
    private BroadcastReceiver downloadCompleteReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (intent != null && intent.getAction() != null)
                switch (intent.getAction()) {
                    case DownloadManager.ACTION_DOWNLOAD_COMPLETE:
                        Message toOpen = null;
                        if (adapterMediaMessagePositions.size() > 0 && messageAdapter != null)
                            for (String msgId : adapterMediaMessagePositions) {
                                int pos = -1;
                                for (int i = dataList.size() - 1; i >= 0; i--) {
                                    if (dataList.get(i).getId().equals(msgId)) {
                                        pos = i;
                                        break;
                                    }
                                }
                                if (pos != -1) {
                                    messageAdapter.notifyItemChanged(pos);
                                    if (dataList.get(pos).getAttachmentType() == AttachmentTypes.IMAGE)
                                        toOpen = dataList.get(pos);
                                }
                            }
                        adapterMediaMessagePositions.clear();
                        if (toOpen != null) openOrQueDownloadFile(toOpen);
                        break;
                }
        }
    };

    private ValueEventListener singleValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String playerId = dataSnapshot.getValue(String.class);
            if (playerId != null) {
                ////Log.e("chat", "onDataChange: called");
                userPlayerIds.add(playerId);
                permitChat();
                forwardIfAny();
            } else {
//                Log.e("chat", "onDataChange: null");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
//            Log.e("chat", "onCancelled: called" + databaseError.getMessage());
        }
    };

    private ValueEventListener groupValueChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (mContext != null) {
                Group group = dataSnapshot.getValue(Group.class);
                if (group != null && group.getUserIds() != null) {
                    ChatActivity.this.group = group;
                    if (isChatAllowed()) {
                        permitChat();
                        userPlayerIds.clear();
                        for (String userId : group.getUserIds())
                            if (!userId.equals(userMe.getId())) {
                                usersRef.child(userId).child("userPlayerId").addListenerForSingleValueEvent(singleValueEventListener);
                            }
                        forwardIfAny();
                        chat.setChatName(group.getName());
                        chat.setChatImage(group.getImage());
                        //chat.setChatStatus(group.getStatus());
                        setUserInfo();
                    } else {
                        forbidChat();
                    }
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ValueEventListener userValueChangeListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (mContext != null) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    ChatActivity.this.user = user;
                    showTyping(user.isTyping());
                    user.setName(chat.getChatName());
                    chat.setChatImage(user.getImage());
                    //chat.setChatStatus(user.getStatus());
                    setUserInfo();
                }
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private ChildEventListener messagesChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null && listeningChats) {
                Message newMessage = dataSnapshot.getValue(Message.class);
                if (newMessage != null && newMessage.getId() != null && newMessage.getChatId() != null) {
                    if (newMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(newMessage);
                    }
                    if (newMessage.getChatId().startsWith(ChatHelper.GROUP_PREFIX))
                        newMessage.setSenderName(isMine(newMessage) ? getString(R.string.you) : getNameById(newMessage.getSenderId()));
                    if (addMessage(newMessage)) {
                        markDelivered(newMessage);
                        if (resumed) dismissNotifications();
                    }
                }
            }
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            if (mContext != null && listeningChats) {
                Message updateMessage = dataSnapshot.getValue(Message.class);
                if (updateMessage != null && updateMessage.getId() != null && updateMessage.getChatId() != null) {
                    if (updateMessage.getAttachmentType() == AttachmentTypes.NONE_NOTIFICATION) {
                        setNotificationMessageNames(updateMessage);
                    }
                    if (updateMessage.getChatId().startsWith(ChatHelper.GROUP_PREFIX))
                        updateMessage.setSenderName(isMine(updateMessage) ? getString(R.string.you) : getNameById(updateMessage.getSenderId()));
                    addMessage(updateMessage);
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

    private ArrayList<ChatMore> moreItems;

    private int mRequestPosition = 0;

    private String Regex = "(?<=^| )\\d+(\\.\\d+)?(?=$| )|(?<=^| )\\.\\d+(?=$| )";
    private String mMobileNumber;
    private PhoneNumberUtil phoneUtil;
    private String mCCode, mMNumber;
    private String msg;


    public void passPhoneNumber(String mMobileNumber) {

        try {
            // phone must begin with '+'
            if (phoneUtil == null) {
                phoneUtil = PhoneNumberUtil.createInstance(ChatActivity.this);
            }
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse("+" + mMobileNumber, "");
            mCCode = String.valueOf(numberProto.getCountryCode());
            ////Log.e("TAG", "onActivityResult: " + mCCode);
            mMNumber = String.valueOf(numberProto.getNationalNumber());
            ////Log.e("TAG", "onActivityResult: " + mMNumber);

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);
        rvBottomChat = (RecyclerView) findViewById(R.id.rvBottomChat);
        rvBottomChat.setLayoutManager(new GridLayoutManager(this, 4));
        Intent intent = getIntent();
        if (intent.hasExtra(MOBILE_NUMBER)) {
            mMobileNumber = intent.getStringExtra(MOBILE_NUMBER);
            passPhoneNumber(mMobileNumber);
        }

        if (intent.hasExtra(EXTRA_DATA_CHAT)) {
            chat = intent.getParcelableExtra(EXTRA_DATA_CHAT);
            ChatHelper.CURRENT_CHAT_ID = chat.getUserId();
//            if (chatId.startsWith(Helper.GROUP_PREFIX)) {
//                group = intent.getParcelableExtra(EXTRA_DATA_CHAT_GROUP);
//                if (group == null) {
//                    group = rChatDb.copyFromRealm(rChatDb.where(Group.class).equalTo("id", chatId).findFirst());
//                    Helper.CURRENT_CHAT_ID = group.getId();
//                }
//            } else {
//                user = rChatDb.copyFromRealm(rChatDb.where(User.class).equalTo("id", chatId).findFirst());
//                String chatName = intent.getStringExtra(EXTRA_DATA_CHAT_NAME);
//                if (!TextUtils.isEmpty(chatName) && TextUtils.isEmpty(user.getNameInPhone())) {
//                    user.setNameInPhone(chatName);
//                }
//                Helper.CURRENT_CHAT_ID = user.getId();
//            }
        } else {
            finish();//temporary fix
        }

        initUi();

        //set basic user info
        setUserInfo();

        callAudio.setClickable(false);
        callVideo.setClickable(false);

        animateToolbarViews();

        //setup chat child reference of logged in user and selected user
//        chatChild = user != null ? Helper.getChatChild(user.getId(), userMe.getId()) : group.getId();
//        userOrGroupId = user != null ? user.getId() : group.getId();

        //setup recycler view
        messageAdapter = new MessageAdapter(this, dataList, userMe.getId(), newMessage);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        emojIcon = EmojiPopup.Builder.fromRootView(rootView).setOnEmojiPopupShownListener(() -> {
            closeAttachmentPicker();
        }).build(newMessage);

        displayWidth = ChatHelper.getDisplayWidth(this);

        deletedMessages = chatHelper.getMessagesDeleted(chat.getChatChild());
        ArrayList<Message> savedList = chatHelper.getMessages(chat.getChatChild());
        if (savedList != null && !savedList.isEmpty()) {
            for (Message savedMessage : savedList)
                if (!deletedMessages.contains(savedMessage.getId())) dataList.add(savedMessage);
            messageAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        }

        registerMyTypingUpdates();
        usersRef.child(chat.getUserId()).addValueEventListener(userValueChangeListener);
        usersRef.child(chat.getUserId()).child("userPlayerId").addListenerForSingleValueEvent(singleValueEventListener);

        /*if (chat.isGroup()) {
            groupRef.child(chat.getChatChild()).addValueEventListener(groupValueChangeListener);
        } else {
            usersRef.child(chat.getUserId()).addValueEventListener(userValueChangeListener);
            usersRef.child(chat.getUserId()).child("userPlayerId").addListenerForSingleValueEvent(singleValueEventListener);
            permitChat();
            forwardIfAny();
        }*/


    }

    private void dismissNotifications() {
        try {
            if (mContext != null && chat != null && chat.getChatChild() != null) {
                // OneSignal.cancelGroupedNotifications(chat.getChatChild()); //need
            }
        } catch (Exception ex) {
        }
    }

    private void registerChatUpdates() {
        //chatRef.child(chat.getChatChild()).limitToLast(FIREBASE_MESSAGE_QUERY_LIMIT).addChildEventListener(messagesChildEventListener);
        chatRef.child(chat.getChatChild()).addChildEventListener(messagesChildEventListener);

    }

    private void markDelivered(Message msg) {
        if (!msg.getSenderId().equals(userMe.getId())) {
            msg.setDelivered(true);
            chatRef.child(chat.getChatChild()).child(msg.getId()).child("delivered").setValue(true);
        }
    }

    private void notifyMessage(Message message) {
        if (!userPlayerIds.isEmpty()) {
            String myPlayerId = chatHelper.getMyPlayerId();
            if (myPlayerId != null) {
                userPlayerIds.remove(myPlayerId);
            }
            try {
                ////Log.e("TAG", "notifyMessage: called");
                //String headings = userMe.getId() + " " + getString(R.string.new_message_sent);
                String headings = (chat.isGroup() && group != null) ? group.getName() : userMe.getId();
                ////Log.e("TAG", "notifyMessage: "+headings );


                OneSignal.postNotification(new JSONObject("{'headings': {'en':'" + headings + "'}, 'contents': {'en':'" + message.getBody() + "'}, 'include_player_ids': " + userPlayerIds.toString() + ",'data': " + new Gson().toJson(message) + ",'android_group':" + message.getChatId() + " }"),
                        new OneSignal.PostNotificationResponseHandler() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                Log.i("OneSignalExample", "postNotification Success: " + response.toString());
                            }

                            @Override
                            public void onFailure(JSONObject response) {
//                                Log.e("OneSignalExample", "postNotification Failure: " + response.toString());
                            }
                        });
            } catch (Exception e) {
                ////Log.e("TAG", "notifyMessage: exception called");
                e.printStackTrace();
            }
        } else {
            ////Log.e("TAG", "notifyMessage: empty ");
        }
    }

    private boolean addMessage(Message msg) {
        if (deletedMessages.contains(msg.getId()))
            return false;
        int existingPos = -1;
        for (int i = dataList.size() - 1; i >= 0; i--) {
            if (dataList.get(i).getId() != null && dataList.get(i).getChatId() != null && dataList.get(i).getId().equals(msg.getId())) {
                existingPos = i;
                break;
            } else if (dataList.size() > FIREBASE_MESSAGE_QUERY_LIMIT && (dataList.size() - (FIREBASE_MESSAGE_QUERY_LIMIT + 1)) == i) {
                break;
            }
        }
        if (existingPos == -1) {
            if (!isMine(msg) && msg.getAttachment() != null && msg.getAttachment().getUrl().equals("loading"))
                return false;
            showTyping(false);
            dataList.add(msg);
            messageAdapter.notifyItemInserted(dataList.size() - 1);
            recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
        } else {
            dataList.set(existingPos, msg);
            messageAdapter.notifyItemChanged(existingPos);
        }
        if (ChatHelper.CHAT_CAB) undoSelectionPrepared();
        return existingPos == -1;
    }

    private void setUserInfo() {
        userName.setText(chat.getChatName());
        //status.setText(chat.getChatStatus());
        userName.setSelected(true);
        status.setSelected(true);
        Glide.with(this).load(chat.getChatImage()).apply(new RequestOptions().placeholder(R.drawable.user1)).into(usersImage);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initUi() {
        toolbar = findViewById(R.id.chatToolbar);
        toolbarContent = findViewById(R.id.chatToolbarContent);
        selectedCount = findViewById(R.id.selectedCount);
        addAttachmentLayout = findViewById(R.id.add_attachment_layout);
        usersImage = findViewById(R.id.users_image);
        status = findViewById(R.id.emotion);
        userName = findViewById(R.id.user_name);
        recyclerView = findViewById(R.id.recycler_view);
        newMessage = findViewById(R.id.new_message);
        addAttachment = findViewById(R.id.add_attachment);
        sendMessage = findViewById(R.id.send);
        sendContainer = findViewById(R.id.sendContainer);
        rootView = findViewById(R.id.rootView);
        attachment_emoji = findViewById(R.id.attachment_emoji);
        callAudio = findViewById(R.id.callAudio);
        callVideo = findViewById(R.id.callVideo);
        chatMore = findViewById(R.id.chatMore);

        rippleBackground = findViewById(R.id.rippleBackground);

        ImageView chatBackground = findViewById(R.id.chatBackground);
        Glide.with(this).load(R.drawable.background_chat).into(chatBackground);

        callAudio.setVisibility(chat.isGroup() ? View.GONE : VISIBLE);
        callVideo.setVisibility(chat.isGroup() ? View.GONE : VISIBLE);

        setSupportActionBar(toolbar);
        addAttachment.setOnClickListener(this);
        toolbarContent.setOnClickListener(this);
        attachment_emoji.setOnClickListener(this);
        sendMessage.setOnClickListener(this);
        callAudio.setOnClickListener(this);
        callVideo.setOnClickListener(this);
        usersImage.setOnClickListener(this);
        findViewById(R.id.back_button).setOnClickListener(this);
        findViewById(R.id.attachment_video).setOnClickListener(this);
        findViewById(R.id.attachment_contact).setOnClickListener(this);
        findViewById(R.id.attachment_gallery).setOnClickListener(this);
        findViewById(R.id.attachment_camera).setOnClickListener(this);
        findViewById(R.id.attachment_audio).setOnClickListener(this);
        //findViewById(R.id.attachment_location).setOnClickListener(this);
        findViewById(R.id.attachment_document).setOnClickListener(this);
//        newMessage.setOnTouchListener((v, event) -> {
//            if (addAttachmentLayout.getVisibility() == View.VISIBLE) {
//                addAttachmentLayout.setVisibility(View.GONE);
//                addAttachment.animate().setDuration(400).rotationBy(-45).start();
//            }
//            return false;
//        });
        newMessage.setOnClickListener(v -> {
            closeAttachmentPicker();
        });

        sendMessage.setOnTouchListener(voiceMessageListener);
    }

    private View.OnTouchListener voiceMessageListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (chat.isGroup() && group == null) {
                Toast.makeText(mContext, R.string.just_moment, Toast.LENGTH_SHORT).show();
                return false;
            }
            closeAttachmentPicker();
            int x = (int) event.getX();
            int y = (int) event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //Log.i("TAG", "touched down");
                    if (newMessage.getText().toString().trim().isEmpty()) {
                        if (recordWaitHandler == null)
                            recordWaitHandler = new Handler();
                        recordRunnable = () -> recordingStart();
                        recordWaitHandler.postDelayed(recordRunnable, 500);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    //Log.i("TAG", "moving: (" + displayWidth + ", " + x + ")");
                    if (mRecorder != null && newMessage.getText().toString().trim().isEmpty()) {
                        if (Math.abs(event.getX()) / displayWidth > 0.35f) {
                            recordingStop(false);
                            ChatHelper.presentToast(mContext, getString(R.string.recording_cancelled), false);
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //Log.i("TAG", "touched up");
                    if (recordWaitHandler != null && newMessage.getText().toString().trim().isEmpty())
                        recordWaitHandler.removeCallbacks(recordRunnable);
                    if (mRecorder != null && newMessage.getText().toString().trim().isEmpty()) {
                        recordingStop(true);
                        ignoreSendClick = true;
                        new Handler().postDelayed(() -> ignoreSendClick = false, 100);
                    }
                    break;
            }

            return false;
        }
    };

    private void recordingStop(boolean send) {
        try {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        } catch (IllegalStateException ex) {
            mRecorder = null;
        } catch (RuntimeException ex) {
            mRecorder = null;
        }
        recordTimerStop();
        if (send) {
            newFileUploadTask(recordFilePath, AttachmentTypes.RECORDING, null);
        } else {
            new File(recordFilePath).delete();
        }
    }

    private void recordingStart() {
        if (recordPermissionsAvailable()) {
            File recordFile = new File(ChatHelper.getFileBase(this) + "/" + AttachmentTypes.getTypeName(AttachmentTypes.RECORDING) + "/.sent/");
            boolean dirExists = recordFile.exists();
            if (!dirExists)
                dirExists = recordFile.mkdirs();
            if (dirExists) {
                try {
                    recordFile = new File(ChatHelper.getFileBase(this) + "/" + AttachmentTypes.getTypeName(AttachmentTypes.RECORDING) + "/.sent/", System.currentTimeMillis() + ".mp3");
                    if (!recordFile.exists())
                        recordFile.createNewFile();
                    recordFilePath = recordFile.getAbsolutePath();
                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mRecorder.setOutputFile(recordFilePath);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                    mRecorder.prepare();
                    mRecorder.start();
                    recordTimerStart(System.currentTimeMillis());
                } catch (IOException e) {
                    e.printStackTrace();
                    mRecorder = null;
                } catch (IllegalStateException ex) {
                    ex.printStackTrace();
                    mRecorder = null;
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, permissionsRecord, REQUEST_PERMISSION_RECORD);
        }
    }

    private void recordTimerStart(final long currentTimeMillis) {
        //Toast.makeText(this, R.string.recodring, Toast.LENGTH_SHORT).show();
        recordTimerRunnable = new Runnable() {
            public void run() {
                Long elapsedTime = System.currentTimeMillis() - currentTimeMillis;
                newMessage.setHint(ChatHelper.timeFormater(elapsedTime) + " " + getString(R.string.slide_cancel));
                recordTimerHandler.postDelayed(this, 1000);
            }
        };
        if (recordTimerHandler == null)
            recordTimerHandler = new Handler();
        recordTimerHandler.post(recordTimerRunnable);
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) v.vibrate(100);

        rippleBackground.startRippleAnimation();
    }

    private void recordTimerStop() {
        recordTimerHandler.removeCallbacks(recordTimerRunnable);
        newMessage.setHint(getString(R.string.type_your_message));
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        if (v != null) v.vibrate(100);

        rippleBackground.stopRippleAnimation();
    }

    private boolean recordPermissionsAvailable() {
        boolean available = true;
        for (String permission : permissionsRecord) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                available = false;
                break;
            }
        }
        return available;
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumed = true;
        registerReceiver(downloadCompleteReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        dismissNotifications();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resumed = false;
        if (chat != null && dataList != null && !dataList.isEmpty()) {
            chatHelper.setLastRead(chat.getChatChild(), dataList.get(dataList.size() - 1).getId());
        }
        unregisterReceiver(downloadCompleteReceiver);
    }

    @Override
    protected void onStop() {
        super.onStop();
        chatHelper.setMessages(chat.getChatChild(), dataList);
        chatHelper.setMessagesDeleted(chat.getChatChild(), deletedMessages);
        ChatHelper.CURRENT_CHAT_ID = null;
        mediaPlayer.release();
    }

    @Override
    protected void onDestroy() {
        if (ChatHelper.CHAT_CAB)
            undoSelectionPrepared();
        if (chatRef != null && messagesChildEventListener != null)
            chatRef.removeEventListener(messagesChildEventListener);
        if (usersRef != null && userValueChangeListener != null)
            usersRef.removeEventListener(userValueChangeListener);
        if (groupRef != null && groupValueChangeListener != null)
            groupRef.removeEventListener(groupValueChangeListener);
        super.onDestroy();
    }

    private void unregisterChatUpdates() {
        if (chatRef != null && messagesChildEventListener != null)
            chatRef.removeEventListener(messagesChildEventListener);
    }

    @Override
    public void onBackPressed() {
        if (ChatHelper.CHAT_CAB) {
            undoSelectionPrepared();
        } else if (addAttachmentLayout.getVisibility() == VISIBLE) {
            closeAttachmentPicker();
        } else {
            KeyboardUtil.getInstance(this).closeKeyboard();
            if (SDK_INT > 21) {
                finishAfterTransition();
            } else {
                finish();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_copy:
                ArrayList<Message> toCopy = new ArrayList<>();
                for (Message message : dataList) {//Get all selected messages in a String
                    if (message.isSelected() && !TextUtils.isEmpty(message.getBody())) {
                        toCopy.add(message);
                        message.setSelected(false);
                    }
                }
                if (!toCopy.isEmpty()) {
                    StringBuilder stringBuilder = new StringBuilder("");
                    if (toCopy.size() > 1) {
                        for (Message message : toCopy) {
                            stringBuilder.append(ChatHelper.getTime(Long.valueOf(message.getDateTimeStamp())));
                            stringBuilder.append(" ");
                            stringBuilder.append(isMine(message) ? message.getRecipientName() : message.getSenderName());
                            stringBuilder.append(" : ");
                            stringBuilder.append(message.getBody());
                            stringBuilder.append("\n");
                        }
                    } else {
                        stringBuilder.append(toCopy.get(0).getBody());
                    }
                    //Add String in clipboard
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("simple text", stringBuilder.toString());
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(this, R.string.message_copied, Toast.LENGTH_SHORT).show();
                }
                undoSelectionPrepared();
                break;
            case R.id.action_delete:
                FragmentManager manager = getSupportFragmentManager();
                Fragment frag = manager.findFragmentByTag(DELETE_TAG);
                if (frag != null) {
                    manager.beginTransaction().remove(frag).commit();
                }

                ConfirmationDialogFragment confirmationDialogFragment = ConfirmationDialogFragment.newConfirmInstance(getString(R.string.delete_msg_title),
                        getString(R.string.delete_msg_message), null, null,
                        view -> {
                            ArrayList<Message> messagesToRemove = new ArrayList<>();
                            for (int i = 0; i < dataList.size(); i++) {
                                if (dataList.get(i).isSelected()) {
                                    messagesToRemove.add(dataList.get(i));
                                    dataList.get(i).setSelected(false);
                                }
                            }
                            for (Message msg : messagesToRemove) {
                                deletedMessages.add(msg.getId());
                                chatRef.child(chat.getChatChild()).child(msg.getId()).setValue(null);
                                dataList.remove(msg);
                            }
                            messageAdapter.notifyDataSetChanged();
                            resetLastMessage();
                            toolbar.getMenu().clear();
                            selectedCount.setVisibility(View.GONE);
                            toolbarContent.setVisibility(VISIBLE);
                            ChatHelper.CHAT_CAB = false;
                        },
                        view -> undoSelectionPrepared());
                confirmationDialogFragment.show(manager, DELETE_TAG);
                break;
            case R.id.action_forward:
                ArrayList<Message> forwardList = new ArrayList<>();
                for (Message msg : dataList)
                    if (msg.isSelected()) {
                        forwardList.add(msg);
                        msg.setSelected(false);
                    }
                Intent resultIntent = new Intent();
                resultIntent.putParcelableArrayListExtra("FORWARD_LIST", forwardList);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
                //undoSelectionPrepared();
                break;
        }
        return true;
    }

    private void registerMyTypingUpdates() {
        //get chat user
        //Publish logged in user's typing status
        newMessage.addTextChangedListener(new TextWatcher() {
            CountDownTimer timer = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //sendMessage.setImageDrawable(ContextCompat.getDrawable(mContext, s.length() == 0 ? R.drawable.ic_keyboard_voice_24dp : R.drawable.ic_send));
                sendMessage.setImageDrawable(ContextCompat.getDrawable(mContext, s.length() == 0 ? R.drawable.ic_keyboard_voice_24dp : R.drawable.icon_send_btn));
                //sendMessage.setMaxWidth(45);
                //sendMessage.setMaxHeight(45);
                if (!chat.isGroup()) {
                    if (timer != null) {
                        timer.cancel();
                        usersRef.child(userMe.getId()).child("typing").setValue(true);
                    }
                    timer = new CountDownTimer(1500, 1000) {
                        public void onTick(long millisUntilFinished) {
                        }

                        public void onFinish() {
                            usersRef.child(userMe.getId()).child("typing").setValue(false);
                        }
                    }.start();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void permitChat() {
        listeningChats = true;
        registerChatUpdates();
        sendContainer.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));
        newMessage.setText("");
        newMessage.setHint(R.string.type_your_message);
        newMessage.setGravity(Gravity.START);
        newMessage.requestFocus();
        newMessage.setEnabled(true);
        addAttachment.setClickable(true);
        sendMessage.setClickable(true);
        //attachment_emoji.setVisibility(View.VISIBLE);
        attachment_emoji.setVisibility(View.GONE);//change here

        addAttachment.setVisibility(VISIBLE);
        sendMessage.setVisibility(VISIBLE);
    }

    private void forbidChat() {
        listeningChats = false;
        unregisterChatUpdates();
        sendContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.bg_gray));
        newMessage.setText("");
        newMessage.setHint(R.string.removed_from_group);
        newMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        newMessage.requestFocus();
        newMessage.setEnabled(false);
        addAttachment.setClickable(false);
        sendMessage.setClickable(false);
        attachment_emoji.setVisibility(View.GONE);
        addAttachment.setVisibility(View.GONE);
        addAttachmentLayout.setVisibility(View.GONE);
        sendMessage.setVisibility(View.GONE);

        ChatHelper.closeKeyboard(this, newMessage);
    }

    private boolean isChatAllowed() {
        if (!chat.isGroup())
            return true;
        return group.getUserIds().contains(userMe.getId());
    }

    private void forwardIfAny() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_DATA_LIST)) {
            ArrayList<Message> toForward = intent.getParcelableArrayListExtra(EXTRA_DATA_LIST);
            if (!toForward.isEmpty()) {
                intent.removeExtra(EXTRA_DATA_LIST);
                for (Message msg : toForward)
                    sendMessage(msg.getBody(), msg.getAttachmentType(), msg.getAttachment());
            } else {
                if (intent.hasExtra("share")) {
                    String share = intent.getStringExtra("share");
                    uploadImage(share);
                }
            }

        } else {
            /*if (intent.hasExtra("share")) {
                String share = intent.getStringExtra("share");
                uploadImage(share);
            }*/
        }
    }

    private void showTyping(boolean isTyping) {
        if (isTyping) {
            if (dataList.isEmpty() || dataList.get(dataList.size() - 1).getAttachmentType() != AttachmentTypes.NONE_TYPING) {
                dataList.add(new Message(AttachmentTypes.NONE_TYPING));
                messageAdapter.notifyItemInserted(dataList.size() - 1);
                recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
            }
        } else {
            if (!dataList.isEmpty() && dataList.get(dataList.size() - 1).getAttachmentType() == AttachmentTypes.NONE_TYPING) {
                dataList.remove(dataList.size() - 1);
                messageAdapter.notifyItemRemoved(dataList.size());
            }
        }
    }

    private void animateToolbarViews() {
        Animation emotionAnimation = AnimationUtils.makeInChildBottomAnimation(this);
        emotionAnimation.setDuration(400);
        status.startAnimation(emotionAnimation);
        Animation nameAnimation = AnimationUtils.makeInChildBottomAnimation(this);
        nameAnimation.setDuration(420);
        userName.startAnimation(nameAnimation);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.back_button:
            case R.id.users_image:
                ChatHelper.closeKeyboard(this, view);
                onBackPressed();
                break;
            case R.id.add_attachment:
                ChatHelper.closeKeyboard(this, view);

                /*if (addAttachmentLayout.getVisibility() == View.VISIBLE) {
                    addAttachmentLayout.setVisibility(View.GONE);
                    addAttachment.animate().setDuration(400).rotationBy(-45).start();
                } else {
                    addAttachmentLayout.setVisibility(View.VISIBLE);
                    addAttachment.animate().setDuration(400).rotationBy(45).start();
                    emojIcon.dismiss();
                }*/
                plusMinus();

                break;
            case R.id.send:
                if (ignoreSendClick) return;
                if (chat.isGroup() && group == null) {
                    Toast.makeText(mContext, R.string.just_moment, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(newMessage.getText().toString())) {
                    ChatHelper.presentToast(this, getString(R.string.hold_record), false);
                } else {
                    msg = newMessage.getText().toString().trim();
                    /*newMessage.setText(newMessage.getText().toString().trim());
                    if (!TextUtils.isEmpty(newMessage.getText().toString())) {
                        sendMessage(newMessage.getText().toString(), AttachmentTypes.NONE_TEXT, null);
                        newMessage.setText("");
                    }*/

                    if (msg.matches(Regex)) {
                        newMessage.setText("");
                        String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
                        if (TextUtils.isEmpty(userData)) {
                            goToLoginScreen(5);
                        } else {
                            Intent intent = new Intent(ChatActivity.this, TransferChatActivity.class);
                            intent.putExtra(AppoConstants.AMOUNT, msg);
                            intent.putExtra(AppoConstants.WHERE, 5);
                            intent.putExtra(AppoConstants.AREACODE, mCCode);
                            intent.putExtra(EXTRA_DATA_CHAT, chat);
                            intent.putExtra(AppoConstants.PHWITHCODE, mCCode + mMNumber);
                            startActivity(intent);
                        }


                    } else {
                        newMessage.setText(newMessage.getText().toString().trim());
                        if (!TextUtils.isEmpty(newMessage.getText().toString())) {
                            sendMessage(newMessage.getText().toString(), AttachmentTypes.NONE_TEXT, null);
                            newMessage.setText("");
                        }
                    }

                }
                break;
            case R.id.chatToolbarContent:
                if (toolbarContent.getVisibility() == VISIBLE) {
                    if (isChatAllowed()) {
                        ////Log.e("tag", "onClick: ");
                        //       startActivity(ChatDetailActivity.newIntent(this, chat, dataList, user, group));
                    } else {
                        ChatHelper.presentToast(mContext, getString(R.string.removed_from_group), false);
                    }
                }
                break;
            case R.id.attachment_contact:
                openContactPicker();
                closeAttachmentPicker();
                break;
            case R.id.attachment_emoji:
                emojIcon.toggle();
                rvBottomChat.setVisibility(View.GONE);
                break;
            case R.id.attachment_gallery:
                openImagePick();
                closeAttachmentPicker();
                break;
            case R.id.attachment_camera:
                openImageClick();
                closeAttachmentPicker();
                break;
            case R.id.attachment_audio:
                openAudioPicker();
                closeAttachmentPicker();
                break;
//            case R.id.attachment_location:
//                openPlacePicker();
//            closeAttachmentPicker();
//                break;
            case R.id.attachment_video:
                openVideoPicker();
                closeAttachmentPicker();
                break;
            case R.id.attachment_document:
                openDocumentPicker();
                closeAttachmentPicker();
                break;
            case R.id.callVideo:
                if (user != null) {
                    callIsVideo = true;
                    //placeCall();//need
                } else {
                    Toast.makeText(mContext, R.string.just_moment, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.callAudio:
                if (user != null) {
                    callIsVideo = false;
                    //placeCall();//need
                } else {
                    Toast.makeText(mContext, R.string.just_moment, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void goToLoginScreen(int i) {
        Intent intent = new Intent(ChatActivity.this, SignInActivity.class);
        intent.putExtra(AppoConstants.WHERE, i);
        startActivity(intent);
    }

    private void plusMinus() {
        if (rvBottomChat.getVisibility() == VISIBLE) {

            rvBottomChat.setVisibility(View.GONE);
            addAttachment.animate().setDuration(400).rotationBy(-45).start();
        } else {

            rvBottomChat.setVisibility(VISIBLE);
            addAttachment.animate().setDuration(400).rotationBy(45).start();
            emojIcon.dismiss();
        }
    }


    private void sendMessage(String messageBody, @AttachmentTypes.AttachmentType int attachmentType, Attachment attachment) {
        //Create message object
        Message message = new Message();
        message.setAttachmentType(attachmentType);
        if (attachmentType != AttachmentTypes.NONE_TEXT)
            message.setAttachment(attachment);
        else
            BaseMessageViewHolder.animate = true;
        message.setChatId(chat.getChatChild());
        message.setBody(messageBody);
        message.setDateTimeStamp(String.valueOf(System.currentTimeMillis()));
        message.setSenderId(userMe.getId());
        message.setSenderName(userMe.getName());
        //message.setSenderStatus(userMe.getStatus());
        message.setSenderImage(userMe.getImage());
        message.setSent(true);
        message.setDelivered(false);
        message.setRecipientId(user != null ? user.getId() : chat.getUserId());
        //message.setRecipientGroupIds(group != null ? new ArrayList<MyString>(group.getUserIds()) : null);
        message.setRecipientName(user != null ? user.getName() : chat.getChatName());
        message.setRecipientImage(user != null ? user.getImage() : chat.getChatImage());
        // message.setRecipientStatus(user != null ? user.getStatus() : chat.getChatStatus());
        message.setId(chatRef.child(message.getChatId()).push().getKey());

        //Add message in chat child
        chatRef.child(chat.getChatChild()).child(message.getId()).setValue(message);
        //Add message for inbox updates
        if (chat.isGroup()) {
            if (group != null && group.getUserIds() != null) {
                for (String memberId : group.getUserIds()) {
                    inboxRef.child(memberId).child(group.getId()).setValue(message);
                }
            }
        } else {
            inboxRef.child(message.getSenderId()).child(message.getRecipientId()).setValue(message);
            inboxRef.child(message.getRecipientId()).child(message.getSenderId()).setValue(message);
        }

        notifyMessage(message);
    }

    private void resetLastMessage() {
        inboxRef.child(userMe.getId()).child(chat.getUserId()).setValue((dataList != null && !dataList.isEmpty()) ? dataList.get(dataList.size() - 1) : null);
    }

    private void checkAndCopy(String directory, File source) {
        //Create and copy file content
        File file = new File(directory);
        boolean dirExists = file.exists();
        if (!dirExists)
            dirExists = file.mkdirs();
        if (dirExists) {
            try {
                file = new File(directory, Uri.fromFile(source).getLastPathSegment());
                boolean fileExists = file.exists();
                if (!fileExists)
                    fileExists = file.createNewFile();
                if (fileExists && file.length() == 0) {
                    FileUtils.copyFile(source, file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    void openContactPicker() {
        if (permissionsAvailable(permissionsContact)) {
            Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT);
        } else {
            ActivityCompat.requestPermissions(this, permissionsContact, 14);
        }
    }

    void openAudioPicker() {
        if (permissionsAvailable(permissionsStorage)) {
            audioPicker = new AudioPicker(this);
            audioPicker.setAudioPickerCallback(this);
            audioPicker.pickAudio();
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 25);
        }
    }

    public void openImagePick() {
        if (SDK_INT >= 30) {
            if (!Environment.isExternalStorageManager()) {
                Snackbar.make(findViewById(android.R.id.content), "Permission needed!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Settings", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                try {
                                    Uri uri = Uri.parse("package:" + BuildConfig.APPLICATION_ID);
                                    Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION, uri);
                                    startActivity(intent);
                                } catch (Exception ex) {
                                    Intent intent = new Intent();
                                    intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                                    startActivity(intent);
                                }
                            }
                        })
                        .show();
            } else {
                imagePicker = new ImagePicker(this);
                imagePicker.shouldGenerateMetadata(true);
                imagePicker.shouldGenerateThumbnails(true);
                imagePicker.setImagePickerCallback(this);
                imagePicker.pickImage();
            }

        } else if (permissionsAvailable(permissionsStorage)) {
            imagePicker = new ImagePicker(this);
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(this);
            imagePicker.pickImage();
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 36);
        }
    }

    void openImageClick() {
        if (permissionsAvailable(permissionsCamera)) {
            cameraPicker = new CameraImagePicker(this);
            cameraPicker.shouldGenerateMetadata(true);
            cameraPicker.shouldGenerateThumbnails(true);
            cameraPicker.setImagePickerCallback(this);
            pickerPath = cameraPicker.pickImage();
        } else {
            ActivityCompat.requestPermissions(this, permissionsCamera, 47);
        }
    }

    public void openDocumentPicker() {
        if (permissionsAvailable(permissionsStorage)) {
            filePicker = new FilePicker(this);
            filePicker.setFilePickerCallback(this);
            filePicker.setMimeType("application/*");
            filePicker.pickFile();
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 58);
        }
    }

    private void openVideoPicker() {
        if (permissionsAvailable(permissionsStorage)) {
            videoPicker = new VideoPicker(this);
            videoPicker.shouldGenerateMetadata(true);
            videoPicker.shouldGeneratePreviewImages(true);
            videoPicker.setVideoPickerCallback(this);
            videoPicker.pickVideo();
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 41);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 14:
                if (permissionsAvailable(permissions))
                    openContactPicker();
                break;
            case 25:
                if (permissionsAvailable(permissions))
                    openAudioPicker();
                break;
            case 36:
                if (permissionsAvailable(permissions))
                    openImagePick();
                break;
            case 47:
                if (permissionsAvailable(permissions))
                    openImageClick();
                break;
            case 58:
                if (permissionsAvailable(permissions))
                    openDocumentPicker();
                break;
            case 69:
                if (permissionsAvailable(permissions))
                    //placeCall();
                    break;
            case 41:
                if (permissionsAvailable(permissions))
                    openVideoPicker();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case Picker.PICK_IMAGE_DEVICE:
                    if (imagePicker == null) {
                        imagePicker = new ImagePicker(this);
                        imagePicker.setImagePickerCallback(this);
                    }
                    imagePicker.submit(data);
                    break;
                case Picker.PICK_IMAGE_CAMERA:
                    if (cameraPicker == null) {
                        cameraPicker = new CameraImagePicker(this);
                        cameraPicker.setImagePickerCallback(this);
                        cameraPicker.reinitialize(pickerPath);
                    }
                    cameraPicker.submit(data);
                    break;
                case Picker.PICK_VIDEO_DEVICE:
                    if (videoPicker == null) {
                        videoPicker = new VideoPicker(this);
                        videoPicker.setVideoPickerCallback(this);
                    }
                    videoPicker.submit(data);
                    break;
                case Picker.PICK_FILE:
                    filePicker.submit(data);
                    break;
                case Picker.PICK_AUDIO:
                    audioPicker.submit(data);
                    break;
            }
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
//                case REQUEST_CODE_UPDATE_USER:
//                    user = data.getParcelableExtra(EXTRA_DATA_USER);
//                    userUpdated(user);
//                    break;
//                case REQUEST_CODE_UPDATE_GROUP:
//                    group = data.getParcelableExtra(EXTRA_DATA_GROUP);
//                    groupUpdated(group);
//                    break;
                case REQUEST_CODE_CONTACT:
                    getSendVCard(data.getData());
                    break;
//                case REQUEST_PLACE_PICKER:
//                    Place place = PlacePicker.getPlace(this, data);
//                    JSONObject jsonObject = new JSONObject();
//                    try {
//                        jsonObject.put("address", place.getAddress().toString());
//                        jsonObject.put("latitude", place.getLatLng().latitude);
//                        jsonObject.put("longitude", place.getLatLng().longitude);
//                        Attachment attachment = new Attachment();
//                        attachment.setData(jsonObject.toString());
//                        sendMessage(null, AttachmentTypes.LOCATION, attachment);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    break;
//                case REQUEST_CODE_PLAY_SERVICES:
//                    openPlacePicker();
//                    break;
            }
        }
    }

    private void getSendVCard(Uri contactsData) {
        @SuppressLint("StaticFieldLeak") AsyncTask<Cursor, Void, File> task = new AsyncTask<Cursor, Void, File>() {
            String vCardData;

            @Override
            protected File doInBackground(Cursor... params) {
                Cursor cursor = params[0];
                File toSend = new File(ChatHelper.getFileBase(mContext) + "/Contact/.sent/");
                if (cursor != null && !cursor.isClosed()) {
                    cursor.getCount();
                    if (cursor.moveToFirst()) {
                        @SuppressLint("Range") String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_VCARD_URI, lookupKey);
                        try {
                            AssetFileDescriptor assetFileDescriptor = getContentResolver().openAssetFileDescriptor(uri, "r");
                            if (assetFileDescriptor != null) {
                                FileInputStream inputStream = assetFileDescriptor.createInputStream();
                                boolean dirExists = toSend.exists();
                                if (!dirExists)
                                    dirExists = toSend.mkdirs();
                                if (dirExists) {
                                    try {
                                        toSend = new File(ChatHelper.getFileBase(mContext) + "/Contact/.sent/", name + ".vcf");
                                        boolean fileExists = toSend.exists();
                                        if (!fileExists)
                                            fileExists = toSend.createNewFile();
                                        if (fileExists) {
                                            OutputStream stream = new BufferedOutputStream(new FileOutputStream(toSend, false));
                                            byte[] buffer = readAsByteArray(inputStream);
                                            vCardData = new String(buffer);
                                            stream.write(buffer);
                                            stream.close();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        } catch (FileNotFoundException e) {
                            ////Log.e(ChatActivity.class.getSimpleName(), "Vcard for the contact " + lookupKey + " not found", e);
                        } catch (IOException e) {
                            ////Log.e(ChatActivity.class.getSimpleName(), "Problem creating stream from the assetFileDescriptor.", e);
                        } finally {
                            cursor.close();
                        }
                    }
                }
                return toSend;
            }

            @Override
            protected void onPostExecute(File f) {
                super.onPostExecute(f);
                if (f != null && !TextUtils.isEmpty(vCardData)) {
                    Attachment attachment = new Attachment();
                    attachment.setData(vCardData);
                    newFileUploadTask(f.getAbsolutePath(), AttachmentTypes.CONTACT, attachment);
                }
            }
        };
        task.execute(getContentResolver().query(contactsData, null, null, null, null));
    }

    public byte[] readAsByteArray(InputStream ios) throws IOException {
        ByteArrayOutputStream ous = null;
        try {
            byte[] buffer = new byte[4096];
            ous = new ByteArrayOutputStream();
            int read = 0;
            while ((read = ios.read(buffer)) != -1) {
                ous.write(buffer, 0, read);
            }
        } finally {
            try {
                if (ous != null)
                    ous.close();
            } catch (IOException e) {
            }

            try {
                if (ios != null)
                    ios.close();
            } catch (IOException e) {
            }
        }
        return ous.toByteArray();
    }

    private void uploadImage(String filePath) {
        newFileUploadTask(filePath, AttachmentTypes.IMAGE, null);
    }

    /*public static  void uploadChatImage(String filePath){
        newFileUploadTask(filePath, AttachmentTypes.IMAGE, null);
    }*/

    private void uploadThumbnail(final String filePath) {
        Toast.makeText(this, R.string.just_moment, Toast.LENGTH_LONG).show();
        File file = new File(filePath);
        final StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                .child(getString(R.string.app_name)).child("video").child("thumbnail").child(file.getName() + ".jpg");
        storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
            //If thumbnail exists
            Attachment attachment = new Attachment();
            attachment.setData(uri.toString());
            newFileUploadTask(filePath, AttachmentTypes.VIDEO, attachment);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                @SuppressLint("StaticFieldLeak") AsyncTask<String, Void, byte[]> thumbnailTask = new AsyncTask<String, Void, byte[]>() {
                    @Override
                    protected byte[] doInBackground(String... params) {
                        //Create thumbnail
                        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(params[0], MediaStore.Video.Thumbnails.MINI_KIND);
                        if (bitmap != null) {
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            return baos.toByteArray();
                        } else {
                            return null;
                        }
                    }

                    @Override
                    protected void onPostExecute(byte[] data) {
                        super.onPostExecute(data);
                        if (data != null) {
                            UploadTask uploadTask = storageReference.putBytes(data);
                            uploadTask.continueWithTask(task -> {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                // Continue with the task to get the download URL
                                return storageReference.getDownloadUrl();
                            }).addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    Attachment attachment = new Attachment();
                                    attachment.setData(downloadUri.toString());
                                    newFileUploadTask(filePath, AttachmentTypes.VIDEO, attachment);
                                } else {
                                    newFileUploadTask(filePath, AttachmentTypes.VIDEO, null);
                                }
                            }).addOnFailureListener(e1 -> newFileUploadTask(filePath, AttachmentTypes.VIDEO, null));
                        } else
                            newFileUploadTask(filePath, AttachmentTypes.VIDEO, null);
                    }
                };
                thumbnailTask.execute(filePath);
            }
        });
    }

    private void newFileUploadTask(String filePath,
                                   @AttachmentTypes.AttachmentType final int attachmentType, final Attachment attachment) {
        closeAttachmentPicker();
//        Log.e("TAG", "newFileUploadTask: called");


        final File fileToUpload = new File(filePath);
//        Attachment preSendAttachment = attachment;//Create/Update attachment
//        if (preSendAttachment == null) preSendAttachment = new Attachment();
//        preSendAttachment.setName(fileName);
//        preSendAttachment.setBytesCount(fileToUpload.length());
//        preSendAttachment.setUrl("loading");
//        prepareMessage(null, attachmentType, preSendAttachment);

        checkAndCopy(ChatHelper.getFileBase(this) + "/" + AttachmentTypes.getTypeName(attachmentType) + "/.sent/", fileToUpload);//Make a copy


        Message message = new Message();
        message.setChatId(chat.getChatChild());
        message.setSenderId(userMe.getId());
        message.setSenderName(userMe.getName());
        //message.setSenderStatus(userMe.getStatus());
        message.setSenderImage(userMe.getImage());
        message.setSent(false);
        message.setDelivered(false);
        message.setRecipientId(user != null ? user.getId() : chat.getUserId());
        message.setRecipientName(user != null ? user.getName() : chat.getChatName());
        message.setRecipientImage(user != null ? user.getImage() : chat.getChatImage());
        //message.setRecipientStatus(user != null ? user.getStatus() : chat.getChatStatus());
        //message.setId(chatRef.child(chat.getChatChild()).push().getKey());

        Intent intent = new Intent(this, UploadAndSendService.class);
        intent.putExtra("attachment", attachment);
        intent.putExtra("attachment_type", attachmentType);
        intent.putExtra("attachment_file_path", filePath);
        intent.putExtra("attachment_message", message);
        intent.putExtra("attachment_player_ids", userPlayerIds);
//        Log.e("TAG", "newFileUploadTask: "+userPlayerIds );
        if (chat.isGroup() && group != null)
            intent.putExtra("attachment_group_ids", group.getUserIds());
        ContextCompat.startForegroundService(this, intent);

    }

    private void closeAttachmentPicker() {
        if (addAttachmentLayout.getVisibility() == VISIBLE) {
            addAttachmentLayout.setVisibility(View.GONE);
            addAttachment.animate().setDuration(400).rotationBy(-45).start();
        }
    }

    public void downloadFile(DownloadFileEvent downloadFileEvent) {
        if (permissionsAvailable(permissionsStorage)) {
            new DownloadUtil().checkAndLoad(this, downloadFileEvent);
            adapterMediaMessagePositions.add(downloadFileEvent.getMessageId());
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 52);
        }
    }

    @Override
    public void OnMessageClick(Message message, int position) {
        if (ChatHelper.CHAT_CAB) {
            message.setSelected(!message.isSelected());//Toggle message selection
            messageAdapter.notifyItemChanged(position);//Notify changes

            if (message.isSelected())
                countSelected++;
            else
                countSelected--;

            selectedCount.setText(String.valueOf(countSelected));//Update count
            if (countSelected == 0)
                undoSelectionPrepared();//If count is zero then reset selection
        } else {
            if (true) {//need to change
                if (message != null && message.getAttachmentType() == AttachmentTypes.IMAGE) {
                    openOrQueDownloadFile(message);
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.CONTACT) {
                    VCard vcard = null;
                    if (!TextUtils.isEmpty(message.getAttachment().getData())) {
                        try {
                            ChainingTextStringParser ctsp = Ezvcard.parse(message.getAttachment().getData());
                            vcard = ctsp.first();
                        } catch (RuntimeException ex) {
                        }
                    }

                    if (vcard == null)
                        return;
                    if (myDialog1 == null) {
                        myDialog1 = new Dialog(this);
                        myDialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        myDialog1.setCancelable(true);
                        myDialog1.setContentView(R.layout.dialog_v_card_detail);

                        contactImage = (ImageView) myDialog1.findViewById(R.id.contactImage);
                        contactName = (TextView) myDialog1.findViewById(R.id.contactName);
                        addToContactText = (TextView) myDialog1.findViewById(R.id.addToContactText);
                        contactPhones = (RecyclerView) myDialog1.findViewById(R.id.recyclerPhone);
                        contactEmails = (RecyclerView) myDialog1.findViewById(R.id.recyclerEmail);

                        contactPhones.setLayoutManager(new LinearLayoutManager(this));
                        contactEmails.setLayoutManager(new LinearLayoutManager(this));

                        myDialog1.findViewById(R.id.contactAdd).setOnClickListener(v -> {
                            if (message != null) {
                                File file = ChatHelper.getFile(this, message, userMe.getId());
                                if (file.exists()) {
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        Uri uri = MyFileProvider.getUriForFile(mContext,
                                                "com.stuffer.stuffers.fileprovider",
                                                file);
                                        intent.setDataAndType(uri, ChatHelper.getMimeType(mContext, uri)); //storage path is path of your vcf file and vFile is name of that file.
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(intent);
                                    } catch (ActivityNotFoundException ex) {
                                        Toast.makeText(mContext, R.string.error_detail, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (!isMine(message))
                                    downloadFile(new DownloadFileEvent(message.getAttachmentType(), message.getAttachment(), message.getId()));
                                else
                                    Toast.makeText(mContext, R.string.file_na, Toast.LENGTH_SHORT).show();

//                        Intent intent = new Intent(Intent.ACTION_VIEW);
//                        intent.setDataAndType(Uri.fromFile(file), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
//                        context.startActivity(intent);
                            }
                        });

                        myDialog1.findViewById(R.id.close).setOnClickListener(v -> myDialog1.dismiss());
                    }

                    if (vcard.getPhotos().size() > 0)
                        Glide.with(mContext).load(vcard.getPhotos().get(0).getData()).apply(new RequestOptions().dontAnimate()).into(contactImage);

                    contactName.setText(vcard.getFormattedName().getValue());

                    contactPhones.setAdapter(new ContactsAdapter(mContext, vcard.getTelephoneNumbers(), null));
                    contactEmails.setAdapter(new ContactsAdapter(mContext, null, vcard.getEmails()));

                    myDialog1.show();
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.DOCUMENT) {
                    openOrQueDownloadFile(message);
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.LOCATION) {

                    try {
                        JSONObject placeData = new JSONObject(message.getAttachment().getData());
                        String latitude = placeData.getString("latitude");
                        String longitude = placeData.getString("longitude");

                        if (!TextUtils.isEmpty(latitude) && !TextUtils.isEmpty(longitude)) {
                            Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            if (mapIntent.resolveActivity(getPackageManager()) != null) {
                                startActivity(mapIntent);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.VIDEO) {
                    openOrQueDownloadFile(message);
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.RECORDING) {
                    File file = ChatHelper.getFile(this, message, userMe.getId());
                    if (file.exists()) {
                        playRecording(file, message.getAttachment().getName(), position);
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            Uri uri = MyFileProvider.getUriForFile(context,
//                    context.getString("com.stuffer.stuffers.fileprovider),
//                    file);
//            intent.setDataAndType(uri, Helper.getMimeType(context, uri)); //storage path is path of your vcf file and vFile is name of that file.
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            context.startActivity(intent);
                    } else if (!isMine(message) && !message.getAttachment().getUrl().equals("loading")) {
                        downloadFile(new DownloadFileEvent(message.getAttachmentType(), message.getAttachment(), message.getId()));
                    } else {
                        Toast.makeText(mContext, getString(R.string.file_na), Toast.LENGTH_SHORT).show();
                    }
                } else if (message != null && message.getAttachmentType() == AttachmentTypes.AUDIO) {
                    openOrQueDownloadFile(message);
                }
            } else {
                ActivityCompat.requestPermissions(this, permissionsStorage, 52);
            }
        }

    }

    private void openOrQueDownloadFile(Message message) {
        File file = ChatHelper.getFile(this, message, userMe.getId());
        Log.e("TAG", "openOrQueDownloadFile: called" );
        if (file.exists()) {
            if (message.getAttachmentType() == AttachmentTypes.IMAGE) {
                startActivity(ImageViewerActivity.newMessageInstance(this, message));
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    Uri uri = MyFileProvider.getUriForFile(mContext,
                            "com.stuffer.stuffers.fileprovider",
                            file);
                    intent.setDataAndType(uri, ChatHelper.getMimeType(mContext, uri)); //storage path is path of your vcf file and vFile is name of that file.
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Toast.makeText(mContext, R.string.error_detail, Toast.LENGTH_SHORT).show();
                }
            }
        } else if (!isMine(message))
            downloadFile(new DownloadFileEvent(message.getAttachmentType(), message.getAttachment(), message.getId()));
        else
            Toast.makeText(mContext, getString(R.string.file_na), Toast.LENGTH_SHORT).show();
    }

    private boolean isMine(Message message) {
        return message.getSenderId().equals(userMe.getId());
    }

    @Override
    public void OnMessageLongClick(Message message, int position) {
        if (!ChatHelper.CHAT_CAB) {//Prepare selection if not in selection mode
            prepareToSelect();
            String s = new Gson().toJson(message);
            ////Log.e("TAG", "OnMessageLongClick: " + s);
            message.setSelected(true);
            messageAdapter.notifyItemChanged(position);
            countSelected++;
            selectedCount.setText(String.valueOf(countSelected));
        }
    }

    private void prepareToSelect() {
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_chat_cab);
        getSupportActionBar().setTitle("");
        selectedCount.setText("1");
        selectedCount.setVisibility(VISIBLE);
        toolbarContent.setVisibility(View.GONE);
        ChatHelper.CHAT_CAB = true;
    }

    private void undoSelectionPrepared() {
        for (Message msg : dataList) {
            msg.setSelected(false);
        }
        countSelected = 0;
        messageAdapter.notifyDataSetChanged();
        toolbar.getMenu().clear();
        selectedCount.setVisibility(View.GONE);
        toolbarContent.setVisibility(VISIBLE);
        ChatHelper.CHAT_CAB = false;
    }

    public static Intent newIntent(Context context, ArrayList<Message> forwardMessages, Chat chat, String share) {
        //intent contains user to chat with and message forward list if any.
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(MOBILE_NUMBER, chat.getUserId());

        if (share.isEmpty()) {
            ////// Log.e("TAG", "newIntent: nothing to share" );
        } else {
            intent.putExtra("share", share);
            //intent.putExtra("")
        }
        intent.putExtra(EXTRA_DATA_CHAT, chat);
        if (forwardMessages == null)
            forwardMessages = new ArrayList<>();
        intent.putParcelableArrayListExtra(EXTRA_DATA_LIST, forwardMessages);
        return intent;
    }

    @Override
    public boolean isRecordingPlaying(String fileName) {
        return isMediaPlayerPlaying() && currentlyPlaying.equals(fileName);
    }

    private boolean isMediaPlayerPlaying() {
        try {
            return mediaPlayer.isPlaying();
        } catch (IllegalStateException ex) {
            return false;
        }
    }

    public void playRecording(File file, String fileName, int position) {
        if (recordPermissionsAvailable()) {
            if (isMediaPlayerPlaying()) {
                mediaPlayer.stop();
                notifyRecordingPlaybackCompletion();
                if (!fileName.equals(currentlyPlaying)) {
                    if (startPlayback(file)) {
                        currentlyPlaying = fileName;
                        messageAdapter.notifyItemChanged(position);
                    }
                }
            } else {
                if (startPlayback(file)) {
                    currentlyPlaying = fileName;
                    messageAdapter.notifyItemChanged(position);
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, permissionsRecord, REQUEST_PERMISSION_RECORD);
        }
    }

    private boolean startPlayback(File file) {
        boolean started = true;
        resetMediaPlayer();
        try {
            FileInputStream is = new FileInputStream(file);
            FileDescriptor fd = is.getFD();
            mediaPlayer.setDataSource(fd);
            is.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mediaPlayer -> notifyRecordingPlaybackCompletion());
        } catch (IOException e) {
            e.printStackTrace();
            started = false;
        }
        return started;
    }

    private void resetMediaPlayer() {
        try {
            mediaPlayer.reset();
        } catch (IllegalStateException ex) {
            mediaPlayer = new MediaPlayer();
        }
    }

    private void notifyRecordingPlaybackCompletion() {
        if (recyclerView != null && messageAdapter != null) {
            int total = dataList.size();
            for (int i = total - 1; i >= 0; i--) {
                if (dataList.get(i).getAttachment() != null
                        &&
                        dataList.get(i).getAttachment().getName().equals(currentlyPlaying)) {
                    messageAdapter.notifyItemChanged(i);
                    break;
                }
            }
        }
    }

    @Override
    public void onVideosChosen(List<ChosenVideo> list) {
        if (list != null && !list.isEmpty())
            uploadThumbnail(Uri.parse(list.get(0).getOriginalPath()).getPath());
    }

    @Override
    public void onAudiosChosen(List<ChosenAudio> list) {
        if (list != null && !list.isEmpty())
            newFileUploadTask(Uri.parse(list.get(0).getOriginalPath()).getPath(), AttachmentTypes.AUDIO, null);
    }

    @Override
    public void onFilesChosen(List<ChosenFile> list) {
        if (list != null && !list.isEmpty())
            newFileUploadTask(Uri.parse(list.get(0).getOriginalPath()).getPath(), AttachmentTypes.DOCUMENT, null);
    }

    /*public void openImagePick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (permissionsAvailable(permissionsStorage30)){
                imagePicker = new ImagePicker(this);
                imagePicker.shouldGenerateMetadata(true);
                imagePicker.shouldGenerateThumbnails(true);
                imagePicker.setImagePickerCallback(this);
                imagePicker.pickImage();
            }else {
                ActivityCompat.requestPermissions(this, permissionsStorage30, 36);
            }

        } else if (permissionsAvailable(permissionsStorage)) {
            imagePicker = new ImagePicker(this);
            imagePicker.shouldGenerateMetadata(true);
            imagePicker.shouldGenerateThumbnails(true);
            imagePicker.setImagePickerCallback(this);
            imagePicker.pickImage();
        } else {
            ActivityCompat.requestPermissions(this, permissionsStorage, 36);
        }
    }*/
    @Override
    public void onImagesChosen(List<ChosenImage> list) {
        if (list != null && !list.isEmpty()) {
            Uri originalFileUri = Uri.parse(list.get(0).getOriginalPath());
            //File tempFile = new File(getCacheDir(), originalFileUri.getLastPathSegment());
            try {
                //val compressedImageFile = Compressor.compress(context, actualImageFile)
                //new Compressor(mContext).compressToFile(new File(list.get(0).getOriginalPath())).getAbsolutePath();
                //String mPath = mContext.getCacheDir().getPath() + File.separator + "images";
                //uploadImage(SiliCompressor.with(this).compress(originalFileUri.toString(), new File(mPath)));
                uploadImage(originalFileUri.getPath());
            } catch (Exception ex) {
                uploadImage(originalFileUri.getPath());
            }
        }
    }

    @Override
    public void onError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // You have to save path in case your activity is killed.
        // In such a scenario, you will need to re-initialize the CameraImagePicker
        outState.putString("picker_path", pickerPath);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // After Activity recreate, you need to re-intialize these
        // two values to be able to re-intialize CameraImagePicker
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("picker_path")) {
                pickerPath = savedInstanceState.getString("picker_path");
            }
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onMoreItemClick(int pos) {
        mRequestPosition = pos;
        String userData = DataVaultManager.getInstance(AppoPayApplication.getInstance()).getVaultValue(DataVaultManager.KEY_USER_DETIALS);
        if (mRequestPosition == 5) {
            /*if (TextUtils.isEmpty(userData)) {
                mBottomNotAccount = new BottomNotAccount();
                Bundle mBundle = new Bundle();
                mBundle.putInt(AppoConstants.WHERE, 1);
                mBottomNotAccount.setArguments(mBundle);
                mBottomNotAccount.show(getSupportFragmentManager(), mBottomNotAccount.getTag());
                mBottomNotAccount.setCancelable(false);
            } else {
                plusMinus();
                Intent mIntent = new Intent(ChatActivity.this, AddMoneyToWallet.class);
                mIntent.putExtra(AppoConstants.WHERE, 1);
                startActivity(mIntent);

            }*/
            /*plusMinus();
            Intent mIntent = new Intent(ChatActivity.this, FundCountry.class);
            mIntent.putExtra(AppoConstants.WHERE, 1);
            startActivity(mIntent);*/

        }
    }

    @Override
    public void onProceedRequest(int mType) {
        /*mBottomNotAccount.dismiss();
        if (mType == 5) {
            Intent intent = new Intent(ChatActivity.this, SignInActivity.class);
            intent.putExtra(AppoConstants.AMOUNT, msg);
            intent.putExtra(AppoConstants.WHERE, 5);
            intent.putExtra(AppoConstants.AREACODE, mCCode);
            intent.putExtra(AppoConstants.PHWITHCODE, mCCode + mMNumber);
            intent.putExtra(EXTRA_DATA_CHAT, chat);
            startActivity(intent);
        } else {
            Intent mIntent = new Intent(ChatActivity.this, SignInActivity.class);
            mIntent.putExtra(AppoConstants.WHERE, mType);
            startActivity(mIntent);
        }*/

    }
}
