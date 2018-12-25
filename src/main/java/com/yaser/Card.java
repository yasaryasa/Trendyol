package com.yaser;

/**
 * 
 * @author yaser
 *
 */
public class Card {

	/**
	 * Damage cost based on Mana
	 */
	private int manaCost;

	/**
	 * Creates {@link Card} with the given integer value
	 * @param cost
	 */
	public Card(int cost) {
		this.manaCost = cost;
	}

	/**
	 * Default constructor
	 */
	public Card() {
	}

	public int getManaCost() {
		return manaCost;
	}

	public void setManaCost(int manaCost) {
		this.manaCost = manaCost;
	}

}
