package org.project.resepku;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvRecipeName, tvIngredients, tvSteps;
    private Button btnEdit, btnBackHome, btnDeleteRecipe;
    private int recipeId; // Menyimpan ID resep

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        // Inisialisasi View
        tvRecipeName = findViewById(R.id.tv_recipe_name);
        tvIngredients = findViewById(R.id.tv_ingredients);
        tvSteps = findViewById(R.id.tv_steps);
        btnBackHome = findViewById(R.id.btn_backHome);
        btnEdit = findViewById(R.id.btn_edit_recipe);

        // Ambil data dari Intent
        Intent intent = getIntent();
        recipeId = intent.getIntExtra("id", -1); // Ambil ID untuk keperluan edit atau hapus
        String name = intent.getStringExtra("name");
        String ingredients = intent.getStringExtra("ingredients");
        String steps = intent.getStringExtra("steps");

        // Tampilkan data pada TextView
        tvRecipeName.setText(name);
        tvIngredients.setText(ingredients);
        tvSteps.setText(steps);

        // Tombol Edit - mengarahkan ke halaman EditRecipeActivity untuk mengedit resep
        btnEdit.setOnClickListener(v -> {
            Intent editIntent = new Intent(RecipeDetailActivity.this, EditRecipeActivity.class);
            editIntent.putExtra("id", recipeId); // Mengirim ID resep
            editIntent.putExtra("name", name); // Mengirim nama resep
            editIntent.putExtra("ingredients", ingredients); // Mengirim bahan-bahan resep
            editIntent.putExtra("steps", steps); // Mengirim langkah-langkah resep
            startActivity(editIntent); // Memulai EditRecipeActivity
        });

        // Tombol Back Home
        btnBackHome.setOnClickListener(v -> {
            Intent intentBack = new Intent(RecipeDetailActivity.this, HomeActivity.class);
            startActivity(intentBack); // Pindah ke HomeActivity
        });




    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ketika activity kembali tampil, reload data resep
        loadRecipeDetails();
    }

    // Fungsi untuk memuat ulang detail resep setelah edit
    private void loadRecipeDetails() {
        // Ambil data terbaru berdasarkan recipeId
        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        Recipe updatedRecipe = databaseHelper.getRecipeById(recipeId);

        // Perbarui tampilan dengan data terbaru
        if (updatedRecipe != null) {
            tvRecipeName.setText(updatedRecipe.getName());
            tvIngredients.setText(updatedRecipe.getIngredients());
            tvSteps.setText(updatedRecipe.getSteps());
        } else {
            // Jika tidak ditemukan resep, beri tahu pengguna (misalnya setelah dihapus)
            Toast.makeText(this, "Resep tidak ditemukan.", Toast.LENGTH_SHORT).show();
        }
    }


}
