package org.project.resepku;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Nama Database dan Tabel
    private static final String DATABASE_NAME = "resepku.db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_RECIPES = "recipes";

    // Kolom pada tabel
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_INGREDIENTS = "ingredients";
    private static final String COLUMN_STEPS = "steps";

    // Query untuk membuat tabel
    private static final String CREATE_TABLE_RECIPES =
            "CREATE TABLE " + TABLE_RECIPES + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NAME + " TEXT NOT NULL, " +
                    COLUMN_INGREDIENTS + " TEXT, " +
                    COLUMN_STEPS + " TEXT)";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECIPES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        onCreate(db);
    }

    // Fungsi untuk menambahkan resep
    public long addRecipe(String name, String ingredients, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_INGREDIENTS, ingredients);
        values.put(COLUMN_STEPS, steps);

        long result = db.insert(TABLE_RECIPES, null, values);
        db.close();
        return result; // Kembalikan ID dari resep yang ditambahkan
    }

    // Fungsi untuk mendapatkan semua resep
    public List<Recipe> getAllRecipes() {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, null, null, null, null, COLUMN_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
                recipeList.add(new Recipe(id, name, ingredients, steps));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipeList;
    }

    // Fungsi untuk mengedit resep
    public int updateRecipe(int id, String name, String ingredients, String steps) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_INGREDIENTS, ingredients);
        values.put(COLUMN_STEPS, steps);

        int rowsAffected = db.update(TABLE_RECIPES, values, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected; // Kembalikan jumlah baris yang diperbarui
    }

    // Fungsi untuk menghapus resep
    public int deleteRecipe(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_RECIPES, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return rowsDeleted; // Kembalikan jumlah baris yang dihapus
    }

    // Fungsi untuk mencari resep berdasarkan nama
    public List<Recipe> searchRecipes(String query) {
        List<Recipe> recipeList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Query untuk mencari resep berdasarkan nama
        Cursor cursor = db.query(TABLE_RECIPES, null,
                COLUMN_NAME + " LIKE ?", new String[]{"%" + query + "%"},
                null, null, COLUMN_ID + " ASC");

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
                String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
                recipeList.add(new Recipe(id, name, ingredients, steps));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return recipeList; // Kembalikan daftar resep hasil pencarian
    }

    // Fungsi untuk mengambil resep berdasarkan ID
    public Recipe getRecipeById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_RECIPES, null, COLUMN_ID + " = ?", new String[]{String.valueOf(id)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            String ingredients = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INGREDIENTS));
            String steps = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STEPS));
            cursor.close();
            return new Recipe(id, name, ingredients, steps);
        }
        cursor.close();
        return null;
    }




}
