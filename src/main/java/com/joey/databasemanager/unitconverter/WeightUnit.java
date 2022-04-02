package com.joey.databasemanager.unitconverter;

public enum WeightUnit implements Unit {
	KILOGRAM(1),
	GRAM(1000),
	POUND(2.20462262),
	TONNE(0.001),
	OUNCE(35.2739619);
	
	private double scale;
	private WeightUnit(double scale) {
		this.scale = scale;
	}
	public double convert(WeightUnit src, WeightUnit tgt, double value) {
		return (value/src.getScale())*tgt.getScale();
		
	}
	
	public double getScale() {
		return this.scale;
	}
}
