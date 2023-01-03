package clases;

import java.io.Serializable;

import jakarta.xml.bind.annotation.XmlType;

@XmlType(propOrder = {"number", "name", "classification", "height", "weight", "hit_points", "attack", "defense", "special", "speed"})
public class Pokemon implements Serializable {

	private static final long serialVersionUID = 1L;
	private String number;
	private String name;
	private String classification;
	private String height;
	private String weight;
	private int hit_points, attack, defense, special, speed;

	public Pokemon() {

	}

	public Pokemon(String number, String name, String classification, String height, String weight, int hp, int att, int df,
			int sp, int speed) {

		this.number = number;
		this.name = name;
		this.classification = classification;
		this.height = height;
		this.weight = weight;
		this.hit_points = hp;
		this.attack = att;
		this.defense = df;
		this.special = sp;
		this.speed = speed;

	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String num) {
		this.number = num;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public int getHit_points() {
		return hit_points;
	}

	public void setHit_points(int hit_points) {
		this.hit_points = hit_points;
	}

	public int getAttack() {
		return attack;
	}

	public void setAttack(int attack) {
		this.attack = attack;
	}

	public int getDefense() {
		return defense;
	}

	public void setDefense(int defense) {
		this.defense = defense;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	@Override
	public String toString() {
		return number +" | " + name + " | "+ classification + " | "+ height + " | "+ weight + " | "+ hit_points + " | "+ attack + " | "+ defense + " | "+ special + " | "+ speed + " | ";
	}
}