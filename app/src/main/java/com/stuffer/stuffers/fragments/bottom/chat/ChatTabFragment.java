package com.stuffer.stuffers.fragments.bottom.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.stuffer.stuffers.R;
import com.stuffer.stuffers.activity.contact.InviteContactActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatTabFragment extends Fragment {

    private TabLayout chatting_tabs;
    private ViewPager chatting_viewpager;
    private FloatingActionButton action_button;
    private FirebaseUser fuser;



    public ChatTabFragment() {
        //Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_chat_tab, container, false);
        chatting_tabs = mView.findViewById(R.id.chatting_tabs);
        chatting_viewpager = mView.findViewById(R.id.chatting_viewpager);
        action_button = mView.findViewById(R.id.floating_action_button);
        setupViewPager(chatting_viewpager);
        chatting_tabs.setupWithViewPager(chatting_viewpager);
        setupTabIcons();
        action_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentContact = new Intent(getActivity(), InviteContactActivity.class);
                startActivity(intentContact);
            }
        });
        return mView;
    }

    private void setupTabIcons() {
       /*chatting_tabs.getTabAt(0).setText(getString(R.string.info_chat1));
        chatting_tabs.getTabAt(1).setText(getString(R.string.info_users));
        chatting_tabs.getTabAt(2).setText(getString(R.string.info_calls));*/
        chatting_tabs.getTabAt(0).setText(getString(R.string.info_chat1));
        chatting_tabs.getTabAt(1).setText(getString(R.string.info_calls));
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ChatFragment2(), getString(R.string.info_chat1));
        //adapter.addFragment(new UsersFragment(),getString(R.string.info_users));
        adapter.addFragment(new CallsFragment(), getString(R.string.info_calls));
        viewPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
