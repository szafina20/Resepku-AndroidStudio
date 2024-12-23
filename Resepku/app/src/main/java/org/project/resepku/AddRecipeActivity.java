package org.project.resepku;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddRecipeActivity extends AppCompatActivity {

    private EditText etRecipeName, etIngredients, etSteps;
    private Button btnSaveRecipe, btnBackHome;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        // Inisialisasi View dan Database
        etRecipeName = findViewById(R.id.et_recipe_name);
        etIngredients = findViewById(R.id.et_ingredients);
        etSteps = findViewById(R.id.et_steps);
        btnSaveRecipe = findViewById(R.id.btn_save_recipe);
        btnBackHome = findViewById(R.id.btn_backHome);
        databaseHelper = new DatabaseHelper(this);

        // Tombol Simpan
        btnSaveRecipe.setOnClickListener(v -> saveRecipe());

        // Tombol Back Home
        btnBackHome.setOnClickListener(v -> {
            Intent intentBack = new Intent(AddRecipeActivity.this, HomeActivity.class);
            startActivity(intentBack); // Pindah ke HomeActivity
            // Tidak menggunakan finish() agar activity tidak ditutup
        });
    }

    private void saveRecipe() {
        String recipeName = etRecipeName.getText().toString().trim();
        String ingredients = etIngredients.getText().toString().trim();
        String steps = etSteps.getText().toString().trim();

        if (recipeName.isEmpty() || ingredients.isEmpty() || steps.isEmpty()) {
            Toast.makeText(this, "Semua field harus diisi!", Toast.LENGTH_SHORT).show();
        } else {
            long id = databaseHelper.addRecipe(recipeName, ingredients, steps);
            if (id > 0) {
                Toast.makeText(this, "Resep berhasil disimpan!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Gagal menyimpan resep.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
