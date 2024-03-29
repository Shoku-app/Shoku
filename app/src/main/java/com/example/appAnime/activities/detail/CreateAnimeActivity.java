package com.example.appAnime.activities.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RatingBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.appAnime.R;
import com.example.appAnime.model.Anime;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateAnimeActivity extends AppCompatActivity {
    EditText inTitle;
    AutoCompleteTextView genre;
    NumberPicker nEpisodios;
    NumberPicker nTemporadas;
    String genero;
    ImageView imgMuestra;
    RatingBar rating;
    Date lanzamiento;
    String titulo;
    int episodios;
    int temporadas;
    String descripcion;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String estudio;
    int puntuacion;

    String foto = getResources().getString(R.string.imagen);
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");


    AdapterView.OnItemClickListener funcionSpinner = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            genero = (String) parent.getItemAtPosition(position);
            if (genero.equals("Action")) {
                Picasso.get().load(R.drawable.animegun).into(imgMuestra);
            } else if (genero.equals("Drama")) {
                Picasso.get().load(R.drawable.sadchibi).into(imgMuestra);
            } else if (genero.equals("Fantasy")) {
                Picasso.get().load(R.drawable.neko1).into(imgMuestra);
            } else if (genero.equals("Sci-fi")) {
                Picasso.get().load(R.drawable.armoredchibi).into(imgMuestra);
            } else if (genero.equals("Suspense")) {
                Picasso.get().load(R.drawable.suspense).into(imgMuestra);
            } else if (genero.equals("Sports")) {
                Picasso.get().load(R.drawable.sports).into(imgMuestra);
            } else if (genero.equals("Romance")) {
                Picasso.get().load(R.drawable.lovechibi).into(imgMuestra);
            } else if (genero.equals("Slice of Life")) {
                Picasso.get().load(R.drawable.lifechibi).into(imgMuestra);
            }  else if (genero.equals("Comedy")) {
                Picasso.get().load(R.drawable.happychibi).into(imgMuestra);
            } else if (genero.equals("Adventure")) {
                Picasso.get().load(R.drawable.adventure).into(imgMuestra);
            } else {
                Picasso.get().load(R.drawable.animelogo).into(imgMuestra);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_create);
        inTitle = findViewById(R.id.inTitle);
        nEpisodios = findViewById(R.id.picker);
        nTemporadas = findViewById(R.id.picker2);
        genre = findViewById(R.id.act);
        imgMuestra = findViewById(R.id.imageView);
        rating = findViewById(R.id.ratingBar2);
        descripcion = null;
        estudio = null;
        lanzamiento = null;

        String[] generos = getResources().getStringArray(R.array.genres);
        ArrayAdapter arrayList2 = new ArrayAdapter(this, R.layout.dropdown_item, generos);
        genre.setAdapter(arrayList2);
        genre.setOnItemClickListener(funcionSpinner);

        nEpisodios.setMaxValue(99);
        nEpisodios.setMinValue(1);
        nTemporadas.setMaxValue(10);
        nTemporadas.setMinValue(1);
    }

    @Override
    public void onBackPressed() {
        MaterialAlertDialogBuilder dialog =
                new MaterialAlertDialogBuilder(CreateAnimeActivity.this);
        dialog.setIcon(R.drawable.ic_dialog_close_dark);
        dialog.setTitle("Are you sure?");
        dialog.setMessage("Are you sure that you want to insert this order?");
        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Snackbar.make(,"See you later", Snackbar.LENGTH_SHORT).show();
                if (inTitle.getText() != null) {
                    titulo = String.valueOf(inTitle.getText());
                } else if (inTitle.getText().equals("Name") || inTitle.getText() == null) {
                    titulo = "Anónimo";
                }

                episodios = nEpisodios.getValue();
                temporadas = nTemporadas.getValue();
                puntuacion = (int) rating.getRating();

                if (descripcion == null) {
                    descripcion = "No se conoce nada sobre este anime.";
                } else {
                    //pillar valor del formulario
                }
                if (estudio == null) {
                    estudio = "Desconocido.";
                } else {
                    //pillar valor del formulario
                }
                if (lanzamiento == null) {
                    lanzamiento = new Date();
                }

                Anime newAnime = new Anime(titulo, descripcion, episodios, estudio, foto,
                        genero, sdf.format(lanzamiento), puntuacion, temporadas, false);
                Intent intent = new Intent();
                intent.putExtra("anime", newAnime);
                db.collection("animes").add(newAnime.setFirestore());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNeutralButton("STAY", null);
        dialog.show();


    }
}