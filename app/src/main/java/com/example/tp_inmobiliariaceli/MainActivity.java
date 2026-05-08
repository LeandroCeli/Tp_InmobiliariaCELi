package com.example.tp_inmobiliariaceli;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.tp_inmobiliariaceli.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        setSupportActionBar(binding.toolbar);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_perfil, R.id.nav_inmuebles, R.id.nav_inquilinos, R.id.nav_contratos, R.id.nav_logout)
                .setOpenableLayout(binding.drawerLayout)
                .build();

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        MainViewModel vm = new androidx.lifecycle.ViewModelProvider(this).get(MainViewModel.class);
        vm.getPropietario().observe(this, new androidx.lifecycle.Observer<com.example.tp_inmobiliariaceli.modelo.Propietario>() {
            @Override
            public void onChanged(com.example.tp_inmobiliariaceli.modelo.Propietario propietario) {
                android.view.View headerView = binding.navView.getHeaderView(0);
                android.widget.TextView tvNombre = headerView.findViewById(R.id.tvUserName);
                android.widget.TextView tvEmail = headerView.findViewById(R.id.tvUserEmail);
                
                tvNombre.setText(propietario.getNombre() + " " + propietario.getApellido());
                tvEmail.setText(propietario.getEmail());
            }
        });
        vm.cargarPerfil();
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main);
        NavController navController = navHostFragment.getNavController();
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
