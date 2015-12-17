package com.example.easycook;

import java.util.ArrayList;


import com.easycook.data.IngredientDao;
import com.easycook.data.EasyCookDBContext;
import com.easycook.data.RecipeDao;
import com.easycook.models.*;
import com.easycook.ui.*;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {	

	ArrayList<IngredientControl> ingredientControls;
	ArrayList<RecipeControl> recipeControls;
	private EasyCookDBContext db;
	private Button clearIngredients;
	boolean created = false;
	int sizeTile = 0;

	String clear_status = "normal"; 

	@SuppressLint("ResourceAsColor")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);

		_createTabControl();		
		try
		{
			db = new EasyCookDBContext(this);			
		}
		catch (Exception e)
		{			

		}

		GridLayout grid = (GridLayout)findViewById(R.id.likeGrid);		
		GridLayout parent = (GridLayout)grid.getParent();
		parent.removeView(grid);
		parent.addView(grid);

		if (size.x/4 >= 30) 
			sizeTile = size.x/4; 

		_populateIngredients(sizeTile);	

		_populateRecipes(sizeTile);

		created = true;

		ImageButton searchI = (ImageButton)findViewById(R.id.btnActionRI);
		searchI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_startSearchIngredient();
			}
		});

		ImageButton backI = (ImageButton)findViewById(R.id.btnActionLI);
		backI.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_leftButtonIngredientsAction();				
			}
		});

		ImageButton searchR = (ImageButton)findViewById(R.id.btnActionRR);
		searchR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_startSearchRecipe();
			}			
		});

		ImageButton backR = (ImageButton)findViewById(R.id.btnActionLR);
		backR.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				_leftButtonRecipesAction();				
			}
		});

		clearIngredients = (Button)findViewById(R.id.buttonClear);
		clearIngredients.setText("CLEAR");
		clearIngredients.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				if (clear_status == "normal")
				{
					for(IngredientControl ingredientC : ingredientControls)
						ingredientC.SelectIngredient(false);				
					SearchRecipe();
				}
				else if (clear_status == "category")
				{
					clearIngredients.setVisibility(View.GONE);
					
					for(IngredientControl ingredientC : ingredientControls)
					{
						if (ingredientC.isSelected())
						{
							clearIngredients.setVisibility(View.VISIBLE);
						}
						ingredientC.setVisibility(View.VISIBLE);
						_populateIngredientTab(sizeTile);
					}
					
					clear_status = "normal";
				}
			}
		});

		((GridLayout)findViewById(R.id.likeGrid)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((GridLayout)findViewById(R.id.likeGrid)).setVisibility(View.GONE);
				((LinearLayout)findViewById(R.id.relativeLayout1)).setVisibility(View.VISIBLE);	
				for (IngredientControl ingredient : ingredientControls)
				{				
					if (ingredient.isOnLikeOption())
					{
						ingredient.setOnLikeOption(false);
					}				
				}
			}
		});

		((Button)findViewById(R.id.buttonFavorite)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				for (IngredientControl ingredient : ingredientControls)
				{			
					if (ingredient.isOnLikeOption())
					{					
						ingredient.SetLikeIngredient(1);
						ingredient.setOnLikeOption(false);
						db.UpdateLikeIngredient(ingredient.getId(), ingredient.getLike());
					}				
				}
				((GridLayout)findViewById(R.id.likeGrid)).setVisibility(View.GONE);
				((LinearLayout)findViewById(R.id.relativeLayout1)).setVisibility(View.VISIBLE);	
			}
		});

		((Button)findViewById(R.id.buttonExclude)).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {					
				for (IngredientControl ingredient : ingredientControls)
				{				
					if (ingredient.isOnLikeOption())
					{	
						ingredient.SetLikeIngredient(-1);
						ingredient.setOnLikeOption(false);
						db.UpdateLikeIngredient(ingredient.getId(), ingredient.getLike());
					}				
				}
				((GridLayout)findViewById(R.id.likeGrid)).setVisibility(View.GONE);
				((LinearLayout)findViewById(R.id.relativeLayout1)).setVisibility(View.VISIBLE);
			}
		});


		Button clearRecipes = (Button)findViewById(R.id.buttonBackRecipes);
		clearRecipes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LinearLayout mainTab = (LinearLayout)findViewById(R.id.tab2);
				mainTab.setVisibility(View.VISIBLE);
				LinearLayout mainTabR = (LinearLayout)findViewById(R.id.tabR2);
				mainTabR.setVisibility(View.GONE);

				((Button)findViewById(R.id.buttonBackRecipes)).setVisibility(View.GONE);

				for (IngredientControl ingredient : ingredientControls)
				{				
					if (ingredient.isSelected())
						clearIngredients.setVisibility(View.VISIBLE);
				}

			}
		});

		for (IngredientCategory cat : IngredientDao.GetIngredientCategory(db.GetIngredientCategory()))
		{			
			Button category = new Button(this);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 5, 5, 5);
			category.setLayoutParams(lp);
			category.setText(cat.getName());
			category.setTag(cat.get_id());

			category.setBackgroundColor(Color.parseColor("#99FA1616"));
			category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Button b = (Button)v;					

					for (IngredientControl ingredient : ingredientControls)
					{		
						if (b.getTag().toString() == String.valueOf(ingredient.getCategory()))
						{
							ingredient.setVisibility(View.VISIBLE);								
						}
						else							
						{
							ingredient.setVisibility(View.GONE);
						}
					}

					((LinearLayout)findViewById(R.id.tabICategory)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.tab1)).setVisibility(View.VISIBLE);
					clearIngredients.setVisibility(View.VISIBLE);
					clear_status = "category";
					_populateIngredientTab(sizeTile);
				}
			});

			((LinearLayout)findViewById(R.id.tabICategory)).addView(category);
		}
		
		for (RecipeCategory cat : RecipeDao.GetRecipeCategory(db.GetRecipeCategory()))
		{			
			Button category = new Button(this);

			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			lp.setMargins(5, 5, 5, 5);
			category.setLayoutParams(lp);
			category.setText(cat.getName());
			category.setTag(cat.get_id());

			category.setBackgroundColor(Color.parseColor("#99FA1616"));
			category.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					Button b = (Button)v;					

					for (RecipeControl recipe : recipeControls)
					{		
						if (b.getTag().toString() == String.valueOf(recipe.getCategory()) && recipe.isFilter())
						{
							recipe.setVisibility(View.VISIBLE);								
						}
						else							
						{
							recipe.setVisibility(View.GONE);
						}
					}

					((LinearLayout)findViewById(R.id.tabRCategory)).setVisibility(View.GONE);
					((LinearLayout)findViewById(R.id.tab2)).setVisibility(View.VISIBLE);
					clearIngredients.setVisibility(View.VISIBLE);
					clear_status = "category_recipe";					
				}
			});

			((LinearLayout)findViewById(R.id.tabRCategory)).addView(category);
		}
	}

	private void _leftButtonIngredientsAction()
	{
		EditText editText = (EditText)findViewById(R.id.txtSearchI);

		if (editText.getVisibility() == View.VISIBLE)
		{
			editText.setVisibility(View.INVISIBLE);
			editText.setText("");
			for (IngredientControl ingredient : ingredientControls)
			{
				ingredient.setVisibility(View.VISIBLE);
				if (ingredient.isSelected())
					clearIngredients.setVisibility(View.VISIBLE);
			}
			_populateIngredientTab(sizeTile);
		}
		else if (((LinearLayout)findViewById(R.id.tabICategory)).getVisibility() == View.GONE)
		{
			((LinearLayout)findViewById(R.id.tab1)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.tabICategory)).setVisibility(View.VISIBLE);			
		}
		else
		{
			((LinearLayout)findViewById(R.id.tabICategory)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.tab1)).setVisibility(View.VISIBLE);
		}
	}

	private void _startSearchIngredient()
	{
		EditText editText = (EditText)findViewById(R.id.txtSearchI);

		if (clearIngredients.getVisibility() == View.VISIBLE)
			clearIngredients.setVisibility(View.GONE);		

		if (editText.getVisibility() == View.VISIBLE)
		{
			_cleanIngredients();
			_searchIngredient(editText.getText().toString());			
			_populateIngredientTab(sizeTile);
		}
		else
		{
			_cleanIngredients();
			editText.setVisibility(View.VISIBLE);
		}		
	}

	private void _leftButtonRecipesAction()
	{
		EditText editText = (EditText)findViewById(R.id.txtSearchR);

		if (editText.getVisibility() == View.VISIBLE)
		{
			editText.setVisibility(View.INVISIBLE);
			editText.setText("");
			for (RecipeControl recipe : recipeControls)
			{
				if (recipe.isFilter())
					recipe.setVisibility(View.VISIBLE);				
			}	

			for (IngredientControl ingredient : ingredientControls)
			{				
				if (ingredient.isSelected())
					clearIngredients.setVisibility(View.VISIBLE);
			}
		}
		/*
		else if (((LinearLayout)findViewById(R.id.tabRCategory)).getVisibility() == View.GONE 
				&& ((LinearLayout)findViewById(R.id.tabR2)).getVisibility() != View.VISIBLE)
		{
			((LinearLayout)findViewById(R.id.tab2)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.tabRCategory)).setVisibility(View.VISIBLE);			
		}
		else if (((LinearLayout)findViewById(R.id.tabR2)).getVisibility() != View.VISIBLE)
		{
			((LinearLayout)findViewById(R.id.tabRCategory)).setVisibility(View.GONE);
			((LinearLayout)findViewById(R.id.tab2)).setVisibility(View.VISIBLE);
		}
		*/
	}

	private void _startSearchRecipe()
	{
		EditText editText = (EditText)findViewById(R.id.txtSearchR);

		if (clearIngredients.getVisibility() == View.VISIBLE)
			clearIngredients.setVisibility(View.GONE);		

		if (editText.getVisibility() == View.VISIBLE)
		{
			_cleanRecipes();
			_searchRecipe(editText.getText().toString());
		}
		else
		{
			_cleanRecipes();
			editText.setVisibility(View.VISIBLE);
		}		
	}

	@SuppressLint("DefaultLocale")
	private void _searchIngredient(String searchValue)
	{
		if (searchValue.isEmpty())
			return;

		for (IngredientControl ingredient : ingredientControls)
		{
			if (ingredient.getTitle().toLowerCase().contains(searchValue.toLowerCase()))			
				ingredient.setVisibility(View.VISIBLE);
		}
	}

	private void _searchRecipe(String searchValue)
	{
		if (searchValue.isEmpty())
			return;

		for (RecipeControl recipe : recipeControls)
		{
			if (recipe.getTitle().toLowerCase().contains(searchValue.toLowerCase()) && recipe.isFilter())			
				recipe.setVisibility(View.VISIBLE);
		}
	}

	private void _cleanIngredients()
	{
		for (IngredientControl ingredient : ingredientControls)
		{
			ingredient.setVisibility(View.GONE);
		}
	}

	private void _cleanRecipes()
	{
		for (RecipeControl recipe : recipeControls)
		{
			recipe.setVisibility(View.GONE);
		}
	}

	private void _populateIngredientTab(int size)
	{
		LinearLayout mainTab = (LinearLayout)findViewById(R.id.tab1);
		LinearLayout linearLayout = new LinearLayout(this);	
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
		mainTab.addView(linearLayout);
		int i = 0;

		for (IngredientControl ingredient : ingredientControls)
		{
			LinearLayout parent = (LinearLayout)ingredient.getParent();

			if (parent != null)
			{
				parent.removeView(ingredient);

				if (parent.getChildCount() == 0)
					((LinearLayout)parent.getParent()).removeView(parent);
			}

			if (ingredient.getVisibility() == View.VISIBLE)
			{
				linearLayout.addView(ingredient);
				ingredient.setSize(size);
				i++;
			}

			if (i == 4)
			{
				linearLayout = new LinearLayout(this);	
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				mainTab.addView(linearLayout);
				i = 0;
			}
		}
	}

	private void _populateRecipes(int size) {

		if (recipeControls != null && !recipeControls.isEmpty())
			return;

		LinearLayout mainTab = (LinearLayout)findViewById(R.id.tab2);

		ArrayList<Recipe> recipes = new ArrayList<Recipe>();

		try {			
			recipes = RecipeDao.GetIngredients(db.getRecipes());
		}catch(Exception e){

		}

		recipeControls = new ArrayList<RecipeControl>();

		try
		{
			for (Recipe recipe : recipes)
			{			
				RecipeControl recipeControl = new RecipeControl(this);	
				recipeControl.mainactivity = this;	
				recipeControl.setRecipeModel(recipe);
				recipeControl.setImage(recipe.getRecipe_picture_name());
				recipeControl.setId(recipe.get_id());
				recipeControl.setTitle(recipe.getRecipe_name());
				mainTab.addView(recipeControl);
				recipeControl.setSize(size);				
				recipeControl.setCategory(recipe.getCategory());
				recipeControls.add(recipeControl);
			}
		}
		catch(Exception e)
		{			

		}
	}

	private void _populateIngredients(int size)
	{
		if (ingredientControls != null && !ingredientControls.isEmpty())
			return;

		ingredientControls = new ArrayList<IngredientControl>();

		ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();

		try {			
			ingredients = IngredientDao.GetIngredients(db.getIngredients());
		}catch(Exception e){

		}
		for (Ingredient ingredient : ingredients)
		{			
			IngredientControl ingredientControl = new IngredientControl(this);	
			ingredientControl.mainactivity = this;

			ingredientControl.setImage(ingredient.getIngredient_picture_name());
			ingredientControl.setTitle(ingredient.getIngredient_name());
			ingredientControl.setId(ingredient.get_id());		
			ingredientControl.setSize(size);
			ingredientControl.setCategory(ingredient.getCategory());
			ingredientControl.setLike(ingredient.getLike());
			ingredientControls.add(ingredientControl);
		}

		_populateIngredientTab(size);
	}

	private void _createTabControl()
	{
		if (created)
			return;

		TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
		tabHost.setup();

		final TabWidget tabWidget = tabHost.getTabWidget();
		final FrameLayout tabContent = tabHost.getTabContentView();

		// Get the original tab textviews and remove them from the viewgroup.
		TextView[] originalTextViews = new TextView[tabWidget.getTabCount()];
		for (int index = 0; index < tabWidget.getTabCount(); index++) {
			originalTextViews[index] = (TextView) tabWidget.getChildTabViewAt(index);
		}
		tabWidget.removeAllViews();

		// Ensure that all tab content childs are not visible at startup.
		for (int index = 0; index < tabContent.getChildCount(); index++) {
			tabContent.getChildAt(index).setVisibility(View.GONE);
		}

		// Create the tabspec based on the textview childs in the xml file.
		// Or create simple tabspec instances in any other way...
		for (int index = 0; index < originalTextViews.length; index++) {

			final TextView tabWidgetTextView = originalTextViews[index];

			final View tabContentView = tabContent.getChildAt(index);

			TabSpec tabSpec = tabHost.newTabSpec((String) tabWidgetTextView.getTag());

			tabSpec.setContent(new TabContentFactory() {
				public View createTabContent(String tag) {
					return tabContentView;
				}
			});

			if (tabWidgetTextView.getBackground() == null) 
			{
				tabSpec.setIndicator(tabWidgetTextView.getText());
			} 
			else 
			{
				tabSpec.setIndicator(tabWidgetTextView.getText(), tabWidgetTextView.getBackground());				
			}
			tabHost.addTab(tabSpec);
		}
	}

	public void SearchRecipe()
	{
		ArrayList<BridgeTable> bridgeTables = RecipeDao.GetBridgeTable(db.GetBridgeTable());
		ArrayList<IngredientControl> selectedI = new ArrayList<IngredientControl>();
		int counter = 0;		

		for(IngredientControl ingredientC : ingredientControls)
			if (ingredientC.isSelected())
				selectedI.add(ingredientC);		

		if (selectedI.size() == 0)
		{
			for(RecipeControl recipeControl : recipeControls)	
			{
				recipeControl.setVisibility(0x00000000);
				recipeControl.setFilter(true);
			}
			_setCounter(-1);
			clearIngredients.setVisibility(0x00000008);
			return;
		}

		if (((EditText)findViewById(R.id.txtSearchI)).getVisibility() != View.VISIBLE)
			clearIngredients.setVisibility(0x00000000);

		for(RecipeControl recipeControl : recipeControls)
		{
			ArrayList<BridgeTable> bridgerecipe = new ArrayList<BridgeTable>();

			for(BridgeTable bridgeTable : bridgeTables)			
				if (bridgeTable.getRecipe_id() == recipeControl.getId())
					bridgerecipe.add(bridgeTable);

			int i = 0;

			for(BridgeTable bridge : bridgerecipe)
				for(IngredientControl selected : selectedI)
					if (bridge.getIngredient_id() == selected.getId())
						i++;

			if (i == bridgerecipe.size())
			{
				recipeControl.setVisibility(0x00000000);
				recipeControl.setFilter(true);
				counter++;
			}
			else
			{
				recipeControl.setVisibility(0x00000008);
				recipeControl.setFilter(false);
			}
		}			
		_setCounter(counter);
	}

	private void _setCounter(int counter) {			
		try
		{
			TabHost tabHost = (TabHost) findViewById(android.R.id.tabhost);
			TextView title = (TextView)((LinearLayout)tabHost.getTabWidget().getChildAt(1)).getChildAt(1);		

			if (counter <= 20 && counter >= 0 )				
			{
				title.setText("RECIPES (" + Integer.toString(counter) + ")");
			}
			else if (counter == -1)
				title.setText("RECIPES");
			else if (counter > 20)
				title.setText("RECIPES (20+)");
		}
		catch (Exception e)
		{
			Log.d("ERROR", e.getMessage());
		}
	}

	public void SelectRecipe(Recipe recipe)
	{
		((TextView)findViewById(R.id.txtTittleRec)).setText(recipe.getRecipe_name());

		clearIngredients.setVisibility(View.GONE);

		ImageView image = ((ImageView)findViewById(R.id.imgRecipeL));
		Context context = image.getContext();
		int id = context.getResources().getIdentifier(recipe.getRecipe_picture_name(), "drawable", context.getPackageName());		
		image.setImageResource(id);

		String listI = "Ingredient List:\n";

		for (String list : recipe.getIngredient_list().split("//")){
			listI += " - " + list.trim() + "\n";
		}

		((TextView)findViewById(R.id.txtIngList)).setText(listI);	

		listI = "Recipe Description: \n";

		for (String list : recipe.getDescription().split("//")){
			if (list.trim() != "" && list.trim() != "\n")
				listI += " - " + list.trim() + "\n";
		}

		((TextView)findViewById(R.id.txtRecDesc)).setText(listI);

		LinearLayout mainTab = (LinearLayout)findViewById(R.id.tab2);

		mainTab.setVisibility(View.GONE);

		LinearLayout mainTabR = (LinearLayout)findViewById(R.id.tabR2);

		mainTabR.setVisibility(View.VISIBLE);

		((Button)findViewById(R.id.buttonBackRecipes)).setVisibility(View.VISIBLE);
	}

	public void ShowLikePanel() {

		((GridLayout)findViewById(R.id.likeGrid)).setVisibility(View.VISIBLE);
		((LinearLayout)findViewById(R.id.relativeLayout1)).setVisibility(View.GONE);

		for (IngredientControl ingredient : ingredientControls)
		{				
			if (ingredient.isOnLikeOption())
			{
				if (ingredient.getLike() == 0)
				{
					((Button)findViewById(R.id.buttonFavorite)).setText("Favorite");
					((Button)findViewById(R.id.buttonExclude)).setText("Exclude");
				}

				if (ingredient.getLike() == -1)
				{
					((Button)findViewById(R.id.buttonFavorite)).setText("Favorite");
					((Button)findViewById(R.id.buttonExclude)).setText("Include");
				}

				if (ingredient.getLike() == 1)
				{
					((Button)findViewById(R.id.buttonFavorite)).setText("No Favorite");
					((Button)findViewById(R.id.buttonExclude)).setText("Exclude");
				}

				ImageView imageC =  ((ImageView)findViewById(R.id.imageIPreview));

				GridLayout grid = (GridLayout)imageC.getParent();
				grid.getLayoutParams().width = (int)(sizeTile*1.2); 
				grid.getLayoutParams().height = (int)(sizeTile*1.2);
				grid.requestLayout();

				Context context = imageC.getContext();
				int id = context.getResources().getIdentifier(ingredient.getPhoto_name(), "drawable", context.getPackageName());		
				imageC.setImageResource(id);
				imageC.getLayoutParams().width = (int) (sizeTile - (sizeTile*0.35));
				imageC.getLayoutParams().height = (int) (sizeTile - (sizeTile*0.35));
				imageC.requestLayout();		
				((TextView)findViewById(R.id.textIPreview)).setText(ingredient.getTitle());		
			}				
		}

	}	

	@Override
	protected void onDestroy() {
		super.onDestroy();		
		db.close();
	}

}
