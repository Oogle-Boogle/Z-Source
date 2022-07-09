package com.zamron.world.content.aoesystem;

import com.zamron.model.CombatIcon;

public class AOEWeaponData {
	
	int id, minDamage, maxDamage, radius;
	CombatIcon icon;
	
	public AOEWeaponData(int id, int minDamage, int maxDamage, int radius, CombatIcon icon) {
		this.id = id;
		this.minDamage = minDamage;
		this.maxDamage = maxDamage;
		this.radius = radius;
		this.icon = icon;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinDamage() {
		return minDamage;
	}

	public void setMinDamage(int minDamage) {
		this.minDamage = minDamage;
	}

	public int getMaxDamage() {
		return maxDamage;
	}

	public void setMaxDamage(int maxDamage) {
		this.maxDamage = maxDamage;
	}

	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public CombatIcon getIcon() {
		return icon;
	}

	public void setIcon(CombatIcon icon) {
		this.icon = icon;
	}
	
	
	
}
