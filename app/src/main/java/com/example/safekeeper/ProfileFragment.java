package com.example.safekeeper;


import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;

public class ProfileFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_profile,container,false);
        final FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        if(fragmentManager.beginTransaction().isEmpty()){
            fragmentManager.beginTransaction().replace(R.id.fragment_contaioner, new Fragment_First()).commit();

        }
        TabLayout tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition() ;
               switch ((pos))
               {
                   case 0 :
                       fragmentManager.beginTransaction().replace(R.id.fragment_contaioner, new Fragment_First()).commit();
                       break;
                   case 1 :
                       fragmentManager.beginTransaction().replace(R.id.fragment_contaioner, new Fragment_Second()).commit();
                       break;
                   case 2 :
                       fragmentManager.beginTransaction().replace(R.id.fragment_contaioner, new Fragment_Third()).commit();
                       break;
                   default : fragmentManager.beginTransaction().replace(R.id.fragment_contaioner,new Fragment_First()).commit();
                   break;
               }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return view;
    }

    private void changeView(int index) {


        switch (index) {
            case 0 :
                break ;
            case 1 :
                break ;
            case 2 :
                break ;

        }
    }


}
