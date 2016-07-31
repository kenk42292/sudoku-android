package com.example.sudoku;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TableRow;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createEmptyGrid();
	}

	
	public void createEmptyGrid() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int width = metrics.widthPixels;
		int height = metrics.heightPixels;
		int dim = Math.min(width, height);
		BorderedTableLayout borderedtablelayout = (BorderedTableLayout) findViewById(R.id.bordered_table_layout);
		for (int row = 0; row < 9; row++) {
			TableRow tablerow = new TableRow(this);
			for (int col = 0; col < 9; col++) {
				EditText inp = new EditText(this);
				inp.setWidth(dim/10);
				inp.setHeight(dim/10);
				inp.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
				inp.setInputType(InputType.TYPE_CLASS_NUMBER);
				tablerow.addView(inp); // maybe add index argument
			}
			borderedtablelayout.addView(tablerow);
		}
	}
	
	public void solve(View view) {
		BorderedTableLayout tablelayout = (BorderedTableLayout) findViewById(R.id.bordered_table_layout);
		String puzzleArg = "";
		for (int r = 0; r < 9; r++) {
			TableRow row = (TableRow) tablelayout.getChildAt(r);
			for (int c = 0; c < 9; c++) {
				EditText text = (EditText) row.getChildAt(c);
				String rawCellText = text.getText().toString();
				String cellText = (rawCellText.trim().equals("")) ? "x" : rawCellText;
				puzzleArg += cellText;
			}
		}
		String[] argArr = {puzzleArg};
		Grid answer = SudokuBrain.main(argArr);
		for (int r = 0; r < 9; r++) {
			TableRow row = (TableRow) tablelayout.getChildAt(r);
			for (int c = 0; c < 9; c++) {
				EditText text = (EditText) row.getChildAt(c);
				text.setText(answer.__grid[r][c].toString().trim());
			}
		}
	}
	
	public void clear(View view) {
		BorderedTableLayout borderedtablelayout = (BorderedTableLayout) findViewById(R.id.bordered_table_layout);
		for (int r = 0; r < 9; r++) {
			TableRow row = (TableRow) borderedtablelayout.getChildAt(r);
			for (int c = 0; c < 9; c++) {
				EditText text = (EditText) row.getChildAt(c);
				text.setText("");
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
