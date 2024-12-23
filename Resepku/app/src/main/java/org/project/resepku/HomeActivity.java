package org.project.resepku;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView recyclerView; // RecyclerView untuk daftar resep
    private RecipeAdapter recipeAdapter; // Adapter untuk menghubungkan data ke RecyclerView
    private DatabaseHelper databaseHelper; // DatabaseHelper untuk SQLite
    private EditText searchBar; // EditText untuk input pencarian
    private ImageButton searchButton; // Tombol untuk pencarian (ImageButton)
    private ImageButton addRecipeButton; // Tombol untuk menambah resep (ImageButton)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inisialisasi komponen UI
        recyclerView = findViewById(R.id.recycler_view);
        searchBar = findViewById(R.id.search_bar);
        searchButton = findViewById(R.id.search_button); // Sesuaikan dengan tipe ImageButton
        addRecipeButton = findViewById(R.id.add_recipe_button); // Sesuaikan dengan tipe ImageButton

        // Set LinearLayoutManager untuk RecyclerView (scroll vertikal)
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Inisialisasi DatabaseHelper
        databaseHelper = new DatabaseHelper(this);

        // Set listener untuk tombol pencarian
        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                searchRecipes(query); // Lakukan pencarian berdasarkan query
            } else {
                Toast.makeText(this, "Masukkan kata kunci untuk mencari resep.", Toast.LENGTH_SHORT).show();
                loadRecipes(); // Jika kosong, muat semua resep
            }
        });

        // Set listener untuk tombol tambah resep
        addRecipeButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, AddRecipeActivity.class);
            startActivity(intent);
        });

        // Muat semua resep saat pertama kali dibuka
        loadRecipes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadRecipes(); // Refresh data saat activity kembali ke fokus
    }

    // Fungsi untuk memuat semua resep dari database
    private void loadRecipes() {
        List<Recipe> recipes = databaseHelper.getAllRecipes(); // Ambil semua data resep
        if (recipes.isEmpty()) {
            Toast.makeText(this, "Belum ada resep tersimpan.", Toast.LENGTH_SHORT).show();
        }
        recipeAdapter = new RecipeAdapter(this, recipes); // Buat adapter dengan data
        recyclerView.setAdapter(recipeAdapter); // Hubungkan adapter ke RecyclerView
    }

    // Fungsi untuk mencari resep berdasarkan query
    private void searchRecipes(String query) {
        List<Recipe> recipes = databaseHelper.searchRecipes(query); // Cari resep di database
        if (recipes.isEmpty()) {
            Toast.makeText(this, "Resep tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
        recipeAdapter = new RecipeAdapter(this, recipes); // Perbarui adapter dengan hasil pencarian
        recyclerView.setAdapter(recipeAdapter); // Set adapter ke RecyclerView
    }
}
