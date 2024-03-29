package com.example.appAnime.activities.main.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appAnime.R;
import com.example.appAnime.activities.detail.CreateAnimeActivity;
import com.example.appAnime.activities.detail.DetailActivity;
import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.adapter.AnimeAdapter;
import com.example.appAnime.adapter.EventsInterface;
import com.example.appAnime.databinding.FragmentTopBinding;
import com.example.appAnime.model.Anime;
import com.example.appAnime.model.Usuario;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class TopFragment extends Fragment {

    final int CODE_CREATE_ANIME = 2;
    Context context;
    AnimeAdapter animeAdapter;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    Usuario usuario = new Usuario();
    ArrayList<Anime> animeList = new ArrayList<>(), listaFiltrados = new ArrayList<>();
    boolean listaEleccion, visualizarLista;
    private FragmentTopBinding binding;
    private EventsInterface function = (pos) -> {
        if (listaEleccion == false) {
            Intent launchInfo = new Intent(context, DetailActivity.class);
            launchInfo.putExtra("anime", animeList.get(pos));

            launchInfo.putExtra("pos", pos);
            startActivity(launchInfo);
        } else {
            Intent launchInfo = new Intent(context, DetailActivity.class);
            launchInfo.putExtra("anime", listaFiltrados.get(pos));

            startActivity(launchInfo);
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTopBinding.inflate(getLayoutInflater());
        usuario = ((MainActivity) getActivity()).usuario;
        setHasOptionsMenu(true);
        RecyclerView recyclerView = binding.rwr;
        recyclerView.setAdapter(animeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));

        //region FIRESTORE
        this.listaEleccion = ((MainActivity) getActivity()).listaEleccion;
        db.collection("usuarios").document(usuario.getUID()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                @Nullable FirebaseFirestoreException error) {
                usuario = documentSnapshot.toObject(Usuario.class);
                usuario.setUID(documentSnapshot.getId());
                db.collection("animes").orderBy("puntuacion", Query.Direction.DESCENDING).limit(10).addSnapshotListener((value, e) -> {
                    animeList.clear();
                    for (QueryDocumentSnapshot doc : value) {
                        Anime anime = doc.toObject(Anime.class);
                        anime.setUID(doc.getId());
                        animeList.add(anime);
                        if (usuario.getAnimes().containsKey(anime.getUID())) {
                            anime.setFavorite(true);
                        } else {
                            anime.setFavorite(false);
                        }
                    }
                    Log.e("Lista", animeList.toString());
                    animeAdapter = new AnimeAdapter(animeList, function);
                    recyclerView.setAdapter(animeAdapter);
                });
            }
        });
        //endregion

        //region Floating Button
        if (!usuario.isAdmin()) {
            binding.fabMenu.setVisibility(View.GONE);
        }
        binding.fabMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialAlertDialogBuilder dialogoCrear =
                        new MaterialAlertDialogBuilder(context);
                dialogoCrear.setIcon(R.drawable.mr_cast_thumb);
                dialogoCrear.setTitle("Operacion Crear Anime");
                dialogoCrear.setMessage("¿Estas seguro de que quieres crear un Anime?");
                dialogoCrear.setPositiveButton("Crear", (dialog, which) -> {
                    Intent launchCreate = new Intent(context,
                            CreateAnimeActivity.class);
                    startActivityForResult(launchCreate, CODE_CREATE_ANIME);
                });
                dialogoCrear.setNegativeButton("Cancelar", null);
                dialogoCrear.show();
            }
        });
        //endregion

        return binding.getRoot();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        this.context = context;
        super.onAttach(context);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            // Este método se llama cada vez que el texto de la barra de búsqueda cambia
            @Override
            public boolean onQueryTextChange(String newText) {
                listaFiltrados.clear();
                for (Anime a : animeList) {
                    if (a.getTitulo().toUpperCase().startsWith(newText.toUpperCase())) {
                        listaFiltrados.add(a);
                        listaEleccion = true;
                    }
                }
                animeAdapter.setAnimeList(listaFiltrados);
                animeAdapter.notifyDataSetChanged();
                return false;
            }

        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}