package com.novoda.sqliteprovider.demo.domain;

public class Firework {

	private final String name;
	private final String color;
	private final String type;
	private final String noise;
	
	public Firework(String name, String color, String type, String noise) {
		this.name = name;
		this.color = color;
		this.type = type;
		this.noise = noise;
	}
	
	@Override
	public String toString() {
		return name;
	}
}