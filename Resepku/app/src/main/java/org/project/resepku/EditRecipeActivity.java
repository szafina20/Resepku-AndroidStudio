package org.project.resepku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class EditRecipeActivity extends AppCompatActivity {

    private EditText etRecipeName, etIngredients, etSteps;
    private Button btnUpdateRecipe, btnBackHome, btnDeleteRecipe; // Tambahkan btnDeleteRecipe

    private DatabaseHelper databaseHelper;

    private int recipeId;
    private String recipeName, ingredients, steps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_recipe);

        // Inisialisasi View dan Database
        etRecipeName = findViewById(R.id.et_recipe_name);
        etIngredients = findViewById(R.id.et_ingredients);
        etSteps = findViewById(R.id.et_steps);
        btnUpdateRecipe = findViewById(R.id.btn_update_recipe);
        btnBackHome = findViewById(R.id.btn_backHome); // Inisialisasi btnBackHome
        btnDeleteRecipe = findViewById(R.id.btn_delete_recipe); // Inisialisasi btnDeleteRecipe
        databaseHelper = new DatabaseHelper(this);

        // Ambil data dari Intent
        Intent intent = getIntent();
        recipeId = intent.getIntExtra("id", -1);
        recipeName = intent.getStringExtra("name");
        ingredients = intent.getStringExtra("ingredients");
        steps = intent.getStringExtra("steps");

        // Isi field dengan data awal
        etRecipeName.setText(recipeName);
        etIngredients.setText(ingredients);
        etSteps.setText(steps);

        // Tombol Update
        btnUpdateRecipe.setOnClickListener(v -> updateRecipe());

        // Tombol Back Home
        btnBackHome.setOnClickListener(v -> {
            Intent intentBack = new Intent(EditRecipeActivity.this, HomeActivity.class);
            startActivity(intentBack); // Pindah ke HomeActivity
            // Tidak menggunakan finish() agar activity tidak ditutup
        });

        // Tombol Delete
        btnDeleteRecipe.setOnClickListener(v -> deleteRecipe());
    }

    private void updateRecipe() {
        String updatedName = etRecipeName.getText().toString().trim();
        String updatedIngredients = etIngredients.getText().toString().trim();
        String updatedSteps = etSteps.getText().toString().trim();

        if (updatedName.isEmpty() || updatedIngredients.isEmpty() || updatedSteps.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
        } else {
            int result = databaseHelper.updateRecipe(recipeId, updatedName, updatedIngredients, updatedSteps);
            if (result > 0) {
                Toast.makeText(this, "Resep berhasil diperbarui!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui resep.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Fungsi untuk menghapus resep
    private void deleteRecipe() {
        int result = databaseHelper.deleteRecipe(recipeId);
        if (result > 0) {
            Toast.makeText(this, "Resep berhasil dihapus!", Toast.LENGTH_SHORT).show();

            // Setelah berhasil menghapus, kembali ke HomeActivity
            Intent intentBack = new Intent(EditRecipeActivity.this, HomeActivity.class);
            startActivity(intentBack); // Pindah ke HomeActivity
            finish(); // Tutup activity saat ini setelah kembali ke HomeActivity
        } else {
            Toast.makeText(this, "Gagal menghapus resep.", Toast.LENGTH_SHORT).show();
        }
    }
}
