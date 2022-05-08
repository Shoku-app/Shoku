package com.example.appAnime.activities.main.ui;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.style.TtsSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.appAnime.activities.main.MainActivity;
import com.example.appAnime.databinding.FragmentProfileBinding;
import com.example.appAnime.model.Usuario;

public class ProfileFragment extends Fragment {
    private static final int IMG_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;
    FragmentProfileBinding binding;
    Uri uri;
    ImageView picker, imgProfile, imgChangeBtn;
    ImageButton imageButton;
    TextView nameTxt, nickTxt, mailTxt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(getLayoutInflater());
        picker = binding.imgPickerBtn;
        imgProfile = binding.imgProfile;
        imgChangeBtn = binding.imgChangeBtn;
        imageButton = binding.imageButton;
        nameTxt = binding.nametxt;
        mailTxt = binding.mailtxt;
        nickTxt = binding.nicktxt;
        Usuario usuario = ((MainActivity)getActivity()).usuario;
        nickTxt.setText(usuario.getUsuario());
        mailTxt.setText(usuario.getCorreo());
        nameTxt.setText(usuario.getNombre());


        imgChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //imgProfile.setImageResource(R.drawable.coffeeart);
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED)) {
                        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                        requestPermissions(permissions, PERMISSION_CODE);
                    } else {
                        pickImageFromGallery();
                    }
                } else {
                    pickImageFromGallery();
                }
            }
        });

        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select Picture"), IMG_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    pickImageFromGallery();
                } else {
                    Toast.makeText(getContext(), "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && resultCode == IMG_PICK_CODE) {
            uri = data.getData();
            imgProfile.setImageURI(uri);
        }
    }
}

