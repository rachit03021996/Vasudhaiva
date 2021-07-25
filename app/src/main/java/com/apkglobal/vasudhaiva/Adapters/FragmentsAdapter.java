package com.apkglobal.vasudhaiva.Adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.apkglobal.vasudhaiva.Fragments.CallsFragment;
import com.apkglobal.vasudhaiva.Fragments.ChatsFragment;
import com.apkglobal.vasudhaiva.Fragments.StatusFragment;

import org.jetbrains.annotations.NotNull;

public class FragmentsAdapter extends FragmentPagerAdapter {
    public FragmentsAdapter(@NonNull @NotNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @NotNull
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:return new ChatsFragment();

            case 1:return new StatusFragment();

            case 2:return new CallsFragment();

            default: return new ChatsFragment();

        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        String title=null;
        if(position==0){
            title="Chats";
        }
        if(position==1){
            title="Status";
        }
        if(position==2){
            title="Calls";
        }

        return title;
    }
}
