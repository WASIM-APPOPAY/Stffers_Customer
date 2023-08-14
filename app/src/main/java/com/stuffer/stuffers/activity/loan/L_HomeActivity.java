package com.stuffer.stuffers.activity.loan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.loan.fragments.L_CalcFragment;
import com.stuffer.stuffers.activity.loan.fragments.L_HomeFragment;
import com.stuffer.stuffers.activity.loan.fragments.L_LoanFragment;
import com.stuffer.stuffers.activity.loan.fragments.L_MoreFragment;
import com.stuffer.stuffers.activity.wallet.HomeActivity;
import com.stuffer.stuffers.commonChat.chat.BottomChatFragment;
import com.stuffer.stuffers.fragments.bottom.BankFragment;
import com.stuffer.stuffers.fragments.bottom.ScanFragment;
import com.stuffer.stuffers.fragments.bottom.chat.ChatTabFragment;
import com.stuffer.stuffers.utils.AppoConstants;
import com.stuffer.stuffers.views.MyTextView;
import com.volcaniccoder.bottomify.BottomifyNavigationView;
import com.volcaniccoder.bottomify.OnNavigationItemChangeListener;

public class L_HomeActivity extends AppCompatActivity implements OnNavigationItemChangeListener {
    LinearLayout lHome, lLoan, lCalculator, lMore;
    BottomifyNavigationView bottomify_nav;
    private MyTextView tvLHomeTitle;
    private ImageView menu_icon_drawer;
    private DrawerLayout drawer_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhome);
        menu_icon_drawer = findViewById(R.id.menu_icon_drawer);
        drawer_layout = (DrawerLayout) findViewById(R.id.drawer_layout);

        tvLHomeTitle = findViewById(R.id.tvLHomeTitle);

        bottomify_nav = (BottomifyNavigationView) findViewById(R.id.bottomify_nav);
        bottomify_nav.setOnNavigationItemChangedListener(this);
        bottomify_nav.setActiveNavigationIndex(0);
        setToolbarTitle("Dashboard");
        menu_icon_drawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
                drawer_layout.requestLayout();
                drawer_layout.bringToFront();
            }
        });


    }

    private void setToolbarTitle(String param) {
        tvLHomeTitle.setText(param);
        tvLHomeTitle.setAllCaps(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onNavigationItemChanged(@NonNull BottomifyNavigationView.NavigationItem navigationItem) {
        switch (navigationItem.getPosition()) {

            case 0:

                setToolbarTitle("Dashboard");
                L_HomeFragment fragment1 = new L_HomeFragment();

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                fragmentTransaction1.addToBackStack(null);
                fragmentTransaction1.replace(R.id.landingContainer, fragment1);
                fragmentTransaction1.commit();
                break;


            case 1:
                setToolbarTitle("Loan");
                L_LoanFragment fragment2 = new L_LoanFragment();

                FragmentManager fragmentManager2 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction2 = fragmentManager2.beginTransaction();
                fragmentTransaction2.addToBackStack(null);
                fragmentTransaction2.replace(R.id.landingContainer, fragment2);
                fragmentTransaction2.commit();

                break;


            case 2:
                setToolbarTitle("Loan Calculator");
                L_CalcFragment fragment3 = new L_CalcFragment();

                FragmentManager fragmentManager3 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction3 = fragmentManager3.beginTransaction();
                fragmentTransaction3.addToBackStack(null);
                fragmentTransaction3.replace(R.id.landingContainer, fragment3);
                fragmentTransaction3.commit();


                break;

            case 3:
                setToolbarTitle("Settings");
                L_MoreFragment fragment4 = new L_MoreFragment();
                FragmentManager fragmentManager4 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction4 = fragmentManager4.beginTransaction();
                fragmentTransaction4.addToBackStack(null);
                fragmentTransaction4.replace(R.id.landingContainer, fragment4, "bank");
                fragmentTransaction4.commit();
                break;
        }
    }
}