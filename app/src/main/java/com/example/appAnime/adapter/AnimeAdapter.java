package com.example.appAnime.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {
    private ArrayList<Anime> animeList;
    private EventsInterface mOnClickListener;

    public AnimeAdapter(ArrayList<Anime> animeList, EventsInterface mOnClickListener) {
        this.animeList = animeList;
        this.mOnClickListener = mOnClickListener;
    }

    public ArrayList<Anime> getAnimeList() {
        return animeList;
    }

    public void setAnimeList(ArrayList<Anime> animeList) {
        this.animeList = animeList;
    }

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int ViewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_layout,
                parent, false);
        return new AnimeViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        Anime actualAnime = animeList.get(position);
        holder.getNameText().setText(actualAnime.getTitulo());
        holder.getSeasonsText().setText(String.valueOf(actualAnime.getTemporadas()));
        holder.getGenresText().setText(actualAnime.getGenero());
        holder.getStarsBar().setNumStars(actualAnime.getPuntuacion());

        //La url de la imagen
        String UrlImagen = String.valueOf(actualAnime.getFoto());
        Picasso.get().load(UrlImagen).into(holder.getCoverImg());
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public class AnimeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameText;
        TextView genresText;
        TextView seasonsText;
        RatingBar starsBar;
        ImageView coverImg;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameText = itemView.findViewById(R.id.name);
            this.genresText = itemView.findViewById(R.id.genres);
            this.seasonsText = itemView.findViewById(R.id.seasons);
            this.starsBar = itemView.findViewById(R.id.ratingBar);
            this.coverImg = itemView.findViewById(R.id.cover1);
            itemView.setOnClickListener(this);
        }

        public TextView getNameText() {
            return nameText;
        }

        public void setNameText(TextView nameText) {
            this.nameText = nameText;
        }

        public TextView getSeasonsText() {
            return seasonsText;
        }

        public TextView getGenresText() {
            return genresText;
        }

        public RatingBar getStarsBar() {
            return starsBar;
        }

        public ImageView getCoverImg() {
            return coverImg;
        }

        public void setCoverImg(ImageView coverImg) {
            this.coverImg = coverImg;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mOnClickListener.clickElement(position);
        }
    }
}