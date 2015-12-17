package com.easycook.ui;

import com.easycook.models.Recipe;
import com.example.easycook.MainActivity;
import com.example.easycook.R;

import android.R.bool;
import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class RecipeControl extends GridLayout {

	private ImageView image;	
	private TextView name;	
	private TextView description;	
	private boolean selected;	
	public MainActivity mainactivity;
	
	private int category;

	public int getCategory() {
		return category;
	}

	public void setCategory(int category) {
		this.category = category;
	}

	
	private boolean filter = true;
	
	public boolean isFilter() {
		return filter;
	}

	public void setFilter(boolean filter) {
		this.filter = filter;
	}

	private Button imgCanvas;
	
	private Recipe recipeModel;
	
	private int id;	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public RecipeControl(Context context) {
		super(context);

		String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater;		
		layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
		layoutInflater.inflate(R.layout.recipe, this,true);

		image = (ImageView)findViewById(R.id.imgMiniPic);
		description = (TextView)findViewById(R.id.txtRecDescription);
		name = (TextView)findViewById(R.id.txtRecTittle);
		imgCanvas = (Button)findViewById(R.id.imgCanvas);
		selected = false;
		
		imgCanvas.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainactivity.SelectRecipe(recipeModel);				
			}
		});
	}

	public void setImage(String title_text)
	{
		Context context = image.getContext();
		int id = context.getResources().getIdentifier(title_text, "drawable", context.getPackageName());		
		image.setImageResource(id);
	}

	public void setTitle(String title_text)
	{
		name.setText(title_text);
	}

	public void setSize(int size)
	{
		try
		{
			this.getLayoutParams().height = size;	
			//image.getLayoutParams().height = size;
			this.requestLayout();
			image.requestLayout();			
		}
		catch(Exception e)
		{
			
		}
	}

	public Recipe getRecipeModel() {
		return recipeModel;
	}

	public void setRecipeModel(Recipe recipeModel) {
		this.recipeModel = recipeModel;
	}
	
	public String getTitle()
	{
		return name.getText().toString();
	}
}