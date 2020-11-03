package net.learn2develop.glumcirecycleview19.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.learn2develop.glumcirecycleview19.R;
import net.learn2develop.glumcirecycleview19.adapter.GlumciRecyclerAdapter;

public class GlumciFragment extends Fragment {

    RecyclerView rvList;
    //Prvi korak - kreiramo progress.xml u Drawable, a tek onda ovde dodjemo i odredimo naziv promenljive
    private ProgressBar progressBar;

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
        progressBar = view.findViewById(R.id.progressbar);
//Cetvrti korak - u okviru onViewCreated pozivamo dole spomenute metode, kako bi nam se progress bar ocitao i pokazao data
        startAsyncTaskLoad();
        setupList();
    }
//Treci korak -koliko sekundi ce nam se progress bar vrteti
    private void startAsyncTaskLoad() {
        LoadAsyncTask task = new LoadAsyncTask();
        task.execute(4);
    }

    private void setupList() {

        rvList.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(layoutManager);

        GlumciRecyclerAdapter adapter = new GlumciRecyclerAdapter((GlumciRecyclerAdapter.OnElementClickListener) getContext());
        rvList.setAdapter(adapter);

    }
    //Drugi korak - postavljamo metodote vidljivosti progress bara i na koliko sekundi
    public class LoadAsyncTask extends AsyncTask<Integer, Void, Void>{

        protected void onPreExecute(){
            rvList.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
        protected void onPostExecute(Void aVoid){
            rvList.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            super.onPostExecute(aVoid);
            setupList();

        }

        @Override
        protected Void doInBackground(Integer... integers) {
            int sekunde = integers[0];

            for (int i = 0; i < sekunde; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }


}
