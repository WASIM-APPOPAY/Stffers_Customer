package com.stuffer.stuffers.commonChat.interfaces;


import com.stuffer.stuffers.commonChat.chatModel.User;


import java.util.ArrayList;

/**
 * Created by a_man on 31-12-2017.
 */

public interface UserGroupSelectionDismissListener {
    void onUserGroupSelectDialogDismiss(ArrayList<User> selectedUsers);

    void selectionDismissed();
}
