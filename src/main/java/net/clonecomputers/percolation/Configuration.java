package net.clonecomputers.percolation;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Configuration {
	private Configuration parent;
	private Map<Point, Byte> points = new HashMap<>();
	private Set<Point> ones = new HashSet<>();
	private double probability = 1;
	
	public Configuration() {
		this(null);
		setPoint(0,0, 1, 1);
	}
	
	public Configuration(Configuration parent) {
		this.parent = parent;
		this.probability = parent==null? 1: parent.getProbability();
	}
	
	public double getProbability() {
		return probability;
	}

	public byte getPoint(Point p) {
		if(parent == null) {
			return points.getOrDefault(p, (byte)0);
		} else {
			Byte value = points.get(p);
			if(value == null) {
				return parent.getPoint(p);
			} else {
				return value;
			}
		}
	}
	
	public byte getPoint(int x, int y) {
		return getPoint(new Point(x,y));
	}
	
	public boolean isOne(Point p) {
		return getPoint(p) == 1;
	}
	
	public boolean isOne(int x, int y) {
		return isOne(new Point(x,y));
	}
	
	public boolean isZero(Point p) {
		return getPoint(p) == 0;
	}
	
	public boolean isZero(int x, int y) {
		return isZero(new Point(x,y));
	}
	
	public boolean isOn(Point p) {
		return getPoint(p) > 0;
	}
	
	public boolean isOn(int x, int y) {
		return isOn(new Point(x,y));
	}
	
	public void setPoint(Point p, byte value, double probability) {
		Byte oldValue = points.put(p, value);
		if(oldValue != null && oldValue == 1) ones.remove(p);
		if(value == 1) ones.add(p);
		this.probability *= probability;
	}
	
	public void setPoint(int x, int y, byte value, double probability) {
		setPoint(new Point(x,y), value, probability);
	}
	
	public void setPoint(Point p, byte value) {
		setPoint(p, value, 1);
	}
	
	public void setPoint(int x, int y, byte value) {
		setPoint(new Point(x,y), value);
	}
	
	public void setPoint(Point p, int value, double probability) {
		setPoint(p, (byte)value, probability);
	}
	
	public void setPoint(int x, int y, int value, double probability) {
		setPoint(x, y, (byte)value, probability);
	}
	
	public void setPoint(Point p, int value) {
		setPoint(p, (byte)value);
	}
	
	public void setPoint(int x, int y, int value) {
		setPoint(x, y, (byte)value);
	}
	
	@Override
	public int hashCode() {
		return points.hashCode() ^ Double.hashCode(probability);
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Configuration &&
				((Configuration)obj).probability == probability &&
				((Configuration)obj).points.equals(points);
	}
	
	public List<Point> zeroesNeighboringOnes() {
		Set<Point> zeroesNeighboringOnes = new HashSet<>();
		for(Point p: ones) {
			for(Point p2: neighbors(p)) {
				if(getPoint(p2) == 0) zeroesNeighboringOnes.add(p2);
			}
		}
		return new ArrayList<>(zeroesNeighboringOnes);
	}

	public static List<Point> neighbors(Point p) {
		return Arrays.asList(new Point(p.x, p.y+1),new Point(p.x+1, p.y),new Point(p.x, p.y-1),new Point(p.x-1, p.y));
	}
}
