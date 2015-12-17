package com.easycook.models;

/*
 * This class is a model that should be identical
 * to the Ingredient table on the database so 
 * the name of the attributes should be the same as on the DB
 */
public class Ingredient  {
	
	private int _id;
	private String ingredient_name;
	private String ingredient_picture_name; // wee need to add this to the table
	private int ingredient_category_id;
	private int like;
	private int category;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}
	
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int get_id() {
		return _id;
	}
	public void set_id(int _id) {
		this._id = _id;
	}
	public String getIngredient_name() {
		return ingredient_name;
	}
	public void setIngredient_name(String ingredient_name) {
		this.ingredient_name = ingredient_name;
	}
	public String getIngredient_picture_name() {
		return ingredient_picture_name;
	}
	public void setIngredient_picture_name(String ingredient_picture_name) {
		this.ingredient_picture_name = ingredient_picture_name;
	}
	public int getIngredient_category_id() {
		return ingredient_category_id;
	}
	public void setIngredient_category_id(int ingredient_category_id) {
		this.ingredient_category_id = ingredient_category_id;
	}
}
