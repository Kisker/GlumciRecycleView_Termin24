package net.learn2develop.glumcirecycleview19.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.learn2develop.glumcirecycleview19.R;
import net.learn2develop.glumcirecycleview19.adapter.GlumciRecyclerAdapter;

public class GlumciFragment extends Fragment {

    RecyclerView rvList;

    public GlumciFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_glumci, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvList = view.findViewById(R.id.rvList);

        setupList();


    }

    private void setupList() {

        rvList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);

        GlumciRecyclerAdapter adapter = new GlumciRecyclerAdapter((GlumciRecyclerAdapter.OnElementClickListener) getContext());
        rvList.setAdapter(adapter);

    }
}
