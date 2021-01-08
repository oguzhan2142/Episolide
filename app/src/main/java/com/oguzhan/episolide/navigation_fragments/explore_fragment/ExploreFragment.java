package com.oguzhan.episolide.navigation_fragments.explore_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.oguzhan.episolide.R;

public class ExploreFragment extends Fragment
{



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {

        View root = inflater.inflate(R.layout.fragment_explore, container, false);


        return root;
    }
}