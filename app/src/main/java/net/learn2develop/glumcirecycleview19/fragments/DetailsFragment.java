package net.learn2develop.glumcirecycleview19.fragments;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.learn2develop.glumcirecycleview19.R;
import net.learn2develop.glumcirecycleview19.model.Glumac;

import java.io.InputStream;

public class DetailsFragment extends Fragment {

    private TextView tvIme, tvPrezime, tvBiografija, tvFilmovi, tvOcena, tvDatumRodjenja, tvDatumSmrti;
    private ImageView ivSlika;
    private static Glumac glumac;

    public DetailsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvIme = view.findViewById(R.id.textView_Ime);
        tvPrezime = view.findViewById(R.id.textView_Prezime);
        tvBiografija = view.findViewById(R.id.textView_Biografija);
        tvFilmovi = view.findViewById(R.id.textView_Filmovi);
        tvOcena = view.findViewById(R.id.textView_Ocena);
        tvDatumRodjenja = view.findViewById(R.id.textView_DatumRodjenja);
        tvDatumSmrti = view.findViewById(R.id.textView_DatumSmrti);

        ivSlika = view.findViewById(R.id.imageView_Slika);

        setupViews();

    }

    @SuppressLint("SetTextI18n")
    private void setupViews() {
        tvIme.setText(glumac.getIme());
        tvPrezime.setText(glumac.getPrezime());
        tvBiografija.setText(glumac.getBiografija());
        tvFilmovi.setText(glumac.getFilmovi());
        tvOcena.setText(Float.toString((float) glumac.getOcena()));
        tvDatumRodjenja.setText(glumac.getDatumRodjenja());
        tvDatumSmrti.setText(glumac.getDatumSmrti());

        try {
            InputStream is = getContext().getAssets().open(glumac.getFotografijaUrl());
            Drawable drawable = Drawable.createFromStream(is, null);

            ivSlika.setImageDrawable(drawable);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void setGlumac(Glumac glumac) {
        this.glumac = glumac;
    }
}

