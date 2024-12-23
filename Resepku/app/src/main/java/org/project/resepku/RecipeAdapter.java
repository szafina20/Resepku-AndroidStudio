package org.project.resepku;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Collections;
import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private final List<Recipe> recipes;
    private final Context context;

    // Constructor
    public RecipeAdapter(Context context, List<Recipe> recipes) {
        this.context = context;
        this.recipes = recipes != null ? recipes : Collections.emptyList(); // Menghindari NullPointerException jika daftar resep null
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Memasukkan layout item_recipe.xml ke dalam RecyclerView untuk setiap item
        View view = LayoutInflater.from(context).inflate(R.layout.item_recipe, parent, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder holder, int position) {
        // Mendapatkan item resep pada posisi tertentu
        Recipe recipe = recipes.get(position);

        // Mengikat data ke ViewHolder
        holder.bind(recipe);



        // Tombol View - mengarahkan ke halaman RecipeDetailActivity untuk melihat detail resep
        holder.btnView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("id", recipe.getId()); // Mengirim ID resep
            intent.putExtra("name", recipe.getName()); // Mengirim nama resep
            intent.putExtra("ingredients", recipe.getIngredients()); // Mengirim bahan-bahan resep
            intent.putExtra("steps", recipe.getSteps()); // Mengirim langkah-langkah resep
            context.startActivity(intent); // Memulai RecipeDetailActivity
        });
    }

    @Override
    public int getItemCount() {
        // Mengembalikan jumlah item dalam daftar resep
        return recipes.size();  // Harus diimplementasikan untuk menentukan berapa banyak item yang ditampilkan
    }

    static class RecipeViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvRecipeName;

        private final Button btnView;

        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRecipeName = itemView.findViewById(R.id.tv_recipe_name);  // Mendapatkan referensi ke TextView untuk nama resep
            btnView = itemView.findViewById(R.id.btn_view_recipe);  // Mendapatkan referensi ke Button View
        }

        // Mengikat data resep ke tampilan di ViewHolder
        public void bind(Recipe recipe) {
            if (recipe != null && recipe.getName() != null) {
                tvRecipeName.setText(recipe.getName());  // Menampilkan nama resep pada TextView
            } else {
                tvRecipeName.setText("Resep Tidak Diketahui");  // Jika nama resep null, tampilkan teks default
            }
        }
    }
}
