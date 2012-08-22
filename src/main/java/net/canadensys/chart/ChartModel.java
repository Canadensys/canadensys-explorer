package net.canadensys.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * Model representing a chart.
 * @author canadensys
 *
 */
public class ChartModel {
	
	private List<Object[]> rows;
	
	public ChartModel(){
		rows = new ArrayList<Object[]>();
	}
	
	public void addRow(Object value[]){
		rows.add(value);
	}
	
	public List<Object[]> getRows() {
		return rows;
	}
	
	public int getRowCount(){
		return rows.size();
	}
}
