package com.example.tp_inmobiliariaceli.ui.inmuebles;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import com.example.tp_inmobiliariaceli.databinding.FragmentInmuebleCrearBinding;
import com.example.tp_inmobiliariaceli.modelo.Inmueble;
import java.io.File;

public class InmuebleCrearFragment extends Fragment {
    private FragmentInmuebleCrearBinding binding;
    private InmuebleCrearViewModel viewModel;

    // Variables para la imagen
    private Uri imageUri;
    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<String> pickImageLauncher;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentInmuebleCrearBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(InmuebleCrearViewModel.class);

        // --- INICIALIZAR LA LÓGICA DE CÁMARA Y GALERÍA ---
        takePictureLauncher = registerForActivityResult(new ActivityResultContracts.TakePicture(), result -> {
            if (result) binding.ivImagenInmueble.setImageURI(imageUri);
        });

        pickImageLauncher = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
            if (uri != null) {
                imageUri = uri;
                binding.ivImagenInmueble.setImageURI(uri);
            }
        });

        binding.btnTomarFoto.setOnClickListener(v -> {
            File photoFile = new File(requireContext().getExternalFilesDir(null), "temp_img.jpg");
            imageUri = FileProvider.getUriForFile(requireContext(), requireContext().getPackageName() + ".fileprovider", photoFile);
            takePictureLauncher.launch(imageUri);
        });

        binding.btnElegirGaleria.setOnClickListener(v -> pickImageLauncher.launch("image/*"));

        // --- BOTÓN GUARDAR ---
        binding.btnGuardarInmueble.setOnClickListener(v -> {
            try {
                Inmueble nuevo = new Inmueble();
                nuevo.setDireccion(binding.etCrearDireccion.getText().toString());
                nuevo.setPrecio(Double.parseDouble(binding.etCrearPrecio.getText().toString()));
                nuevo.setAmbientes(Integer.parseInt(binding.etCrearAmbientes.getText().toString()));
                nuevo.setUso(binding.spinnerCrearUso.getSelectedItem().toString());
                nuevo.setTipo(binding.spinnerCrearTipo.getSelectedItem().toString());
                nuevo.setDisponible(true);

                nuevo.setSuperficie(100);
                nuevo.setLatitud(-33.2950);
                nuevo.setLongitud(-66.3356);

                // ENVIAMOS EL OBJETO Y LA IMAGEN REAL (O NULL SI NO SACÓ FOTO)
                viewModel.crearInmueble(nuevo, imageUri);

            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Complete todos los campos numéricos correctamente", Toast.LENGTH_SHORT).show();
            }
        });

        viewModel.getMensaje().observe(getViewLifecycleOwner(), msj ->
                Toast.makeText(getContext(), msj, Toast.LENGTH_SHORT).show());

        viewModel.getCreado().observe(getViewLifecycleOwner(), creado -> {
            if (creado) Navigation.findNavController(requireView()).navigateUp();
        });

        return binding.getRoot();
    }
}