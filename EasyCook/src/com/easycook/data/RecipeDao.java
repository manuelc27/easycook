package com.easycook.data;

import java.util.ArrayList;

import com.easycook.models.BridgeTable;
import com.easycook.models.IngredientCategory;
import com.easycook.models.Recipe;
import com.easycook.models.RecipeCategory;

import android.database.Cursor;
import android.util.Log;

public class RecipeDao {
	
	public static ArrayList<Recipe> GetIngredients(Cursor cursor)
	{
		ArrayList<Recipe> recipes = new ArrayList<Recipe>();

		if (cursor != null)
		{
			if ( cursor.moveToFirst())
			{
				do
				{
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));
					int category_id = cursor.getInt(cursor.getColumnIndex("recipe_category_id"));					
					String name = cursor.getString(cursor.getColumnIndex("recipe_name"));					
					String image_name = cursor.getString(cursor.getColumnIndex("photo_name"));			
					Recipe recipe = new Recipe();
					recipe.set_id(_id);			    	    	
					recipe.setRecipe_name(name);
					recipe.setRecipe_picture_name(image_name);
					recipe.setDescription(cursor.getString(cursor.getColumnIndex("description")));
					recipe.setIngredient_list(cursor.getString(cursor.getColumnIndex("ingredient_list")));
					recipe.setCategory(category_id);
					recipes.add(recipe);

				}while(cursor.moveToNext());
			}
		}		
		cursor.close();
		return recipes;
	}	
	
	public static ArrayList<RecipeCategory> GetRecipeCategory(Cursor cursor)
	{
		ArrayList<RecipeCategory> categories = new ArrayList<RecipeCategory>();

		if (cursor != null)
		{
			if ( cursor.moveToFirst())
			{
				do
				{
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));									
					String name = cursor.getString(cursor.getColumnIndex("recipe_category_name"));					
					RecipeCategory cat = new RecipeCategory();
					cat.setName(name);
					cat.set_id(_id);			    	    	
					categories.add(cat);
				}while(cursor.moveToNext());
			}
		}			
		cursor.close();
		return categories;
	}	

	public static ArrayList<BridgeTable> GetBridgeTable(Cursor cursor)
	{
		ArrayList<BridgeTable> bridgeTables = new ArrayList<BridgeTable>();

		int i = 0;
		
		if (cursor != null)
		{
			if ( cursor.moveToFirst())
			{
				do
				{
					int _id = cursor.getInt(cursor.getColumnIndex("_id"));
					int ingredient_id = cursor.getInt(cursor.getColumnIndex("ingredient_id"));					
					int recipe_id = cursor.getInt(cursor.getColumnIndex("recipe_id"));										
					
					BridgeTable bridgeTable = new BridgeTable();
					bridgeTable.set_id(_id);			    	    	
					bridgeTable.setIngredient_id(ingredient_id);
					bridgeTable.setRecipe_id(recipe_id);
					bridgeTables.add(bridgeTable);
					i++;				

				}while(cursor.moveToNext());
			}
		}		
		cursor.close();
		return bridgeTables;
	}	
}
