package com.easycook.data;

import java.util.ArrayList;
import java.util.Random;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.easycook.models.Ingredient;
import com.easycook.models.IngredientCategory;

public class IngredientDao {

	// List of ingredients for testing 
	static String[] ingredientBase = { "Chicken" , "Tomato" , "Beef" , "Onion" , "Eggs" };	

	public static ArrayList<Ingredient> GetIngredients(Cursor cursor)
	{
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

		if (cursor != null)
		{
			if ( cursor.moveToFirst())
			{
				do
				{
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));
					int category_id = cursor.getInt(cursor.getColumnIndex("category_id"));					
					String name = cursor.getString(cursor.getColumnIndex("ingredient_name"));
					String image_name = cursor.getString(cursor.getColumnIndex("image_name"));
					int like = cursor.getInt(cursor.getColumnIndex("Like"));			

					Ingredient ingredient = new Ingredient();
					ingredient.set_id(_id);		
					ingredient.setCategory(category_id);
					ingredient.setIngredient_name(name);
					ingredient.setIngredient_picture_name(image_name);
					ingredient.setLike(like);
					ingredients.add(ingredient);

				}while(cursor.moveToNext());
			}
		}			
		cursor.close();
		return ingredients;
	}	
	
	public static ArrayList<IngredientCategory> GetIngredientCategory(Cursor cursor)
	{
		ArrayList<IngredientCategory> categories = new ArrayList<IngredientCategory>();

		if (cursor != null)
		{
			if ( cursor.moveToFirst())
			{
				do
				{
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));									
					String name = cursor.getString(cursor.getColumnIndex("ingredient_category_name"));					
					IngredientCategory cat = new IngredientCategory();
					cat.setName(name);
					cat.set_id(_id);			    	    	
					categories.add(cat);
				}while(cursor.moveToNext());
			}
		}			
		cursor.close();
		return categories;
	}	

	/*
	 * This method should return all the ingredients from 
	 * the table Ingredients	  
	 */
	@SuppressLint("DefaultLocale")
	public static ArrayList<Ingredient> GetIngredientsTest()
	{
		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();	

		Random randomGenerator = new Random();
		for (int i = 0; i < 50 ; i++)
		{
			Ingredient ingredient = new Ingredient();
			ingredient.set_id(i);
			int number = randomGenerator.nextInt(ingredientBase.length);	    	
			ingredient.setIngredient_name(ingredientBase[number]);
			ingredient.setIngredient_picture_name(ingredientBase[number].toLowerCase());
			ingredients.add(ingredient);
		}		
		return ingredients;	
	}
}
