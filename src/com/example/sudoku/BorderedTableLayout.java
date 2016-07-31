package com.example.sudoku;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BorderedTableLayout extends TableLayout{


	@SuppressLint("NewApi")
	public BorderedTableLayout(Context context) {
		super(context);
		setAlpha((float) 0.4);
		this.setWillNotDraw(false);
	}
	
	@SuppressLint("NewApi")
	public BorderedTableLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setAlpha((float) 0.4);
		this.setWillNotDraw(false);
	}
	
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		addBorders(3);
	}
	
	//Add visible borders to all cells of width n
	@SuppressLint("NewApi")
	public void addBorders(int n) {
		GradientDrawable cellGrad = new GradientDrawable();
		cellGrad.setShape(GradientDrawable.RECTANGLE);
		cellGrad.setColor(Color.WHITE);
		cellGrad.setStroke(n, Color.BLACK);
		GradientDrawable lineGrad = new GradientDrawable();
		lineGrad.setShape(GradientDrawable.LINE);
		lineGrad.setColor(Color.WHITE);
		lineGrad.setStroke(4,  Color.BLACK);
		for (int r = 0; r < this.getChildCount(); r++) {
			for (int c = 0; c < ((TableRow) this.getChildAt(r)).getChildCount(); c++) {
				((TableRow)this.getChildAt(r)).getChildAt(c).setBackground(cellGrad);
			}
		}
		
		this.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
	}
}
