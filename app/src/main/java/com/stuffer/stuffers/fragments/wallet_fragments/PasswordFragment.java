package com.stuffer.stuffers.fragments.wallet_fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.stuffer.stuffers.MainActivity;
import com.stuffer.stuffers.R;
import com.stuffer.stuffers.commonChat.chat.PinDemoActivity;
import com.stuffer.stuffers.utils.DataVaultManager;
import com.stuffer.stuffers.utils.Helper;
import com.stuffer.stuffers.utils.PasswordUtil;
import com.stuffer.stuffers.views.MyEditText;
import com.stuffer.stuffers.views.MyTextView;


public class PasswordFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private MyEditText txtUserPassword;
    private MyEditText txtUserPasswordConfirm;
    private MyTextView btnSignUp;
    private ScrollView scroll_layout;

    public PasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_password, container, false);
        txtUserPassword = (MyEditText) this.mView.findViewById(R.id.txtUserPassword);
        scroll_layout = (ScrollView) this.mView.findViewById(R.id.scroll_layout);
        txtUserPasswordConfirm = (MyEditText) this.mView.findViewById(R.id.txtUserPasswordConfirm);
        btnSignUp = (MyTextView) mView.findViewById(R.id.btnSignUp);
        btnSignUp.setOnClickListener(this);
        txtUserPasswordConfirm.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // IME_ACTION_DONE was confirmed
                    //Toast.makeText(getActivity(), "IME_ACTION_DONE triggered", Toast.LENGTH_SHORT).show();
                    hideKeyboard(requireActivity());
                    scroll_layout.post(new Runnable() {
                        @Override
                        public void run() {
                            scroll_layout.fullScroll(View.FOCUS_DOWN);
                        }
                    });

                    return true;
                }
                return false;
            }
        });


        return mView;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btnSignUp) {
            if (!PasswordUtil.PASSWORD_PATTERN.matcher(txtUserPassword.getText().toString().trim()).matches()) {
                txtUserPassword.setFocusable(true);
                txtUserPassword.setError("please follow the pattern");
                txtUserPassword.requestFocus();
                return;
            }

            if (!PasswordUtil.PASSWORD_PATTERN.matcher(txtUserPasswordConfirm.getText().toString().trim()).matches()) {
                txtUserPasswordConfirm.setFocusable(true);
                txtUserPasswordConfirm.setError("please follow the pattern");
                txtUserPasswordConfirm.requestFocus();
                return;
            }

            String trim = txtUserPasswordConfirm.getText().toString().trim();
            String trim2 = txtUserPassword.getText().toString().trim();
            if (!trim.equals(trim2)) {
                Helper.showLongMessage(getActivity(), "password mismatch");
                return;
            }

            Helper.showLoading(getString(R.string.info_please_wait), getActivity());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Helper.hideLoading();
                    Toast.makeText(getActivity(), "Successfully Created!!!!", Toast.LENGTH_SHORT).show();
                    DataVaultManager.getInstance(getActivity()).savIsCreated("yes");
                    //Intent intent = new Intent(getActivity(), HomeActivity3.class);
                    Intent intent = new Intent(getActivity(), PinDemoActivity.class);
                    intent.putExtra("open", "yes");
                    startActivity(intent);
                    getActivity().finish();
                }
            }, 500);
        }
    }


}