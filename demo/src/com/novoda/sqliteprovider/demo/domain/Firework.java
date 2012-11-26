package com.novoda.sqliteprovider.demo.domain;

import java.io.Serializable;

public class Firework implements Serializable {

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
		return getName();
	}

	public String getName() {
		return name;
	}
	
	public String getColor() {
		return color;
	}

	public String getType() {
		return type;
	}

	public String getNoise() {
		return noise;
	}

	public static Firework getNullSafeFirework() {
		return new Firework("", "", "", "");
	}
}