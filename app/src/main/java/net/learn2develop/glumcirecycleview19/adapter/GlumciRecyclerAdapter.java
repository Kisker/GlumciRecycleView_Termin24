package net.learn2develop.glumcirecycleview19.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.learn2develop.glumcirecycleview19.R;
import net.learn2develop.glumcirecycleview19.model.Glumac;
import net.learn2develop.glumcirecycleview19.providers.GlumacProvider;

import java.util.List;

public class GlumciRecyclerAdapter extends RecyclerView.Adapter<GlumciRecyclerAdapter.MyViewHolder> {

    private List<Glumac> glumac;
    private OnElementClickListener listener;

    public GlumciRecyclerAdapter(OnElementClickListener listener) {
        this.listener = listener;
        glumac = GlumacProvider.getAllGlumac();
    }
 // Kada odredimo prvi novo kreirani layout 'single_item.xml', onda tek krenemo sa radom na klasi GlumciAdapter!
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.single_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(listener, glumac.get(position));
    }

    @Override
    public int getItemCount() {
        return glumac.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvText;
        private View wholeView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvText = itemView.findViewById(R.id.tvText);
            wholeView = itemView;
        }

        public void bind(final OnElementClickListener listener, final Glumac item) {
            // bez "%s %s" formata ne bismo bili u mogucnosti da vidimo i prezimena, zato uvek ovo stavljati
            tvText.setText(String.format("%s %s",item.getIme(),item.getPrezime()));
            wholeView.setOnClickListener(new View.OnClickListener() {
                //anonimna klasa
                @Override
                public void onClick(View view) {
                    listener.onElementClicked(item);
                }
            });
        }
    }

    public interface OnElementClickListener {
        void onElementClicked(Glumac glumac);
    }
}
