package com.easycook.ui;

import com.example.easycook.MainActivity;
import com.example.easycook.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

public class IngredientControl extends GridLayout {

	private ImageView image;
	private ImageView selected_image;
	private ImageView imageLike;
	private ImageView imageNoLike;
	private TextView name;
	private boolean selected;
	private boolean onLikeOption = false;
	private String photo_name;
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

	public String getPhoto_name() {
		return photo_name;
	}

	public void setPhoto_name(String photo_name) {
		this.photo_name = photo_name;
	}

	public boolean isOnLikeOption() {
		return onLikeOption;
	}

	public void setOnLikeOption(boolean onLikeOption) {
		this.onLikeOption = onLikeOption;
	}

	public MainActivity mainactivity;
	public GestureDetector gestureDetector;

	long down;

	private int id;	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public IngredientControl(Context context) {
		super(context);
		// TODO Auto-generated constructor stub		

		String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater layoutInflater;		
		layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
		layoutInflater.inflate(R.layout.ingredient, this,true);

		image = (ImageView)findViewById(R.id.imageView1);
		selected_image = (ImageView)findViewById(R.id.imageView2);
		name = (TextView)findViewById(R.id.textView1);
		imageLike = (ImageView)findViewById(R.id.imageLike);
		imageNoLike = (ImageView)findViewById(R.id.imageNoLike);

		selected = false;

		image.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Log.d("HOLD", "HOLD");
				onLikeOption = true;
				mainactivity.ShowLikePanel();				
				return true;
			}
		});


		image.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {	

				if (selected_image.getVisibility() == VISIBLE)
				{
					selected_image.setVisibility(INVISIBLE);
					selected = false;					
				}
				else
				{
					selected_image.setVisibility(VISIBLE);
					selected = true;
				}
				Log.d("HOLD", "SHORT");
				mainactivity.SearchRecipe();
			}
		});

	}

	public void setImage(String title_text)
	{
		photo_name = title_text;
		Context context = image.getContext();
		int id = context.getResources().getIdentifier(title_text, "drawable", context.getPackageName());		
		image.setImageResource(id);
	}

	public void setTitle(String title_text)
	{
		name.setText(title_text);
	}

	public String getTitle()
	{
		return name.getText().toString();
	}

	public void setSize(int size)
	{
		try
		{
			if (like == 1 )
			{	
				imageLike.setVisibility(View.VISIBLE);
				imageNoLike.setVisibility(View.INVISIBLE);
			}
			else if (like == -1)
			{	
				imageLike.setVisibility(View.INVISIBLE);
				imageNoLike.setVisibility(View.VISIBLE);
			}
			
			
			this.getLayoutParams().height = (int)(size*1.15);
			this.getLayoutParams().width = size;
			image.getLayoutParams().width = (int) (size - (size*0.35));
			image.getLayoutParams().height = (int) (size - (size*0.35));
			selected_image.getLayoutParams().width = (int) (size - (size*0.8));
			selected_image.getLayoutParams().height = (int) (size - (size*0.8));
			imageLike.getLayoutParams().width = (int) (size - (size*0.8));
			imageLike.getLayoutParams().height = (int) (size - (size*0.8));
			imageNoLike.getLayoutParams().width = (int) (size - (size*0.8));
			imageNoLike.getLayoutParams().height = (int) (size - (size*0.8));
			this.requestLayout();
			image.requestLayout();
			imageLike.requestLayout();
			imageNoLike.requestLayout();
			selected_image.requestLayout();
		}
		catch(Exception e)
		{

		}
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void SelectIngredient(boolean select) {		
		if (select)
		{
			selected_image.setVisibility(VISIBLE);
			selected = select;					
		}
		else
		{
			selected_image.setVisibility(INVISIBLE);
			selected = select;
		}
	}

	public void SetLikeIngredient(int i)
	{
		if (like == 1 && i == 1)
		{
			like =0;
			imageLike.setVisibility(View.INVISIBLE);
			imageNoLike.setVisibility(View.INVISIBLE);
		}
		else if (like == -1 && i == -1)
		{
			like =0;
			imageLike.setVisibility(View.INVISIBLE);
			imageNoLike.setVisibility(View.INVISIBLE);
		}
		else if (like == 1 && i == -1)
		{
			like = -1;
			imageLike.setVisibility(View.INVISIBLE);
			imageNoLike.setVisibility(View.VISIBLE);
		}
		else if (like == -1 && i == 1)
		{
			like = 1;
			imageLike.setVisibility(View.VISIBLE);
			imageNoLike.setVisibility(View.INVISIBLE);
		}
		else if (like == 0 && i == 1)
		{
			like = 1;
			imageLike.setVisibility(View.VISIBLE);
			imageNoLike.setVisibility(View.INVISIBLE);
		}
		else if (like == 0 && i == -1)
		{
			like = -1;
			imageLike.setVisibility(View.INVISIBLE);
			imageNoLike.setVisibility(View.VISIBLE);
		}
	}
}
