package com.easycook.data;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class EasyCookDBContext extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "easycookdb.sqlite";
	private static final int DATABASE_VERSION = 1;

	public EasyCookDBContext(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public Cursor getIngredients() {

		SQLiteDatabase db = getReadableDatabase();			
		Cursor c = db.rawQuery("SELECT * FROM ingredients WHERE image_name IS NOT NULL ORDER BY Like DESC",null);
		c.moveToFirst();
		return c;
	}

	public Cursor GetBridgeTable() {

		SQLiteDatabase db = getReadableDatabase();				
		Cursor c = db.rawQuery("SELECT * FROM bridge_table",null);
		c.moveToFirst();
		return c;
	}

	public Cursor getRecipes() {

		SQLiteDatabase db = getReadableDatabase();		
		Cursor c = db.rawQuery("SELECT * FROM recipe",null);
		c.moveToFirst();
		return c;
	}

	public void UpdateLikeIngredient(int id, int like) {

		SQLiteDatabase db = getReadableDatabase();		
		ContentValues cv = new ContentValues();
		cv.put("Like",like);
		db.update("ingredients", cv, "_id="+id, null);		
	}

	public Cursor GetRecipeCategory() {

		SQLiteDatabase db = getReadableDatabase();				
		Cursor c = db.rawQuery("SELECT * FROM recipes_category",null);
		c.moveToFirst();
		return c;
	}

	public Cursor GetIngredientCategory() {

		SQLiteDatabase db = getReadableDatabase();				
		Cursor c = db.rawQuery("SELECT * FROM ingredients_category",null);
		c.moveToFirst();
		return c;
	}

}
