package net.clonecomputers.percolation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;

public class Percolation implements Runnable {
	private Set<Configuration> state = new HashSet<>();
	private Set<Configuration> nextState = new HashSet<>();
	private int pathLength = 0;
	private final double probabilityOfConnection;

	public static void main(String[] args) {
		new Percolation(.7).run();
	}
	
	public Percolation(double p) {
		state.add(new Configuration());
		probabilityOfConnection = p;
	}

	@Override
	public void run() {
		while(true) {
			System.out.println(pathLength+".csv");
			
			for(int y = 0; y <= pathLength; y++) {
				for(int x = 0; x <= pathLength; x++) {
					double p = 0;
					if(x+y <= pathLength) {
						for(Configuration c: state) {
							if(c.isOn(x, y)) p += c.getProbability();
						}
					}
					System.out.printf("%.16f, ", p);
				}
				System.out.println();
			}

			step();
		}
	}
	
	public void step() {
		for(Configuration config: state){
			recursivelyTryAll(config);
		}
		state = nextState;
		nextState = new HashSet<>();
		pathLength++;
		//TODO: compactify equivalent configurations?
	}
	
	public void recursivelyTryAll(Configuration config) {
		Consumer<Map<Point, Integer>> tryConfiguration = (state) -> {
			Configuration child = new Configuration(config);
			for(Map.Entry<Point, Integer> setting: state.entrySet()) {
				double probability = 1;
				for(Point p: Configuration.neighbors(setting.getKey())) {
					if(config.isOne(p)) probability *= (1-probabilityOfConnection);
				}
				if(setting.getValue() == 1) probability = 1 - probability;
				child.setPoint(setting.getKey(), setting.getValue(), probability);
			}
			nextState.add(child);
		};
		Function<Point, ? extends Iterable<Integer>> getPossibleValues = (object) -> {return Arrays.asList(0,1);};
		recurse(tryConfiguration, getPossibleValues, config.zeroesNeighboringOnes());
	}
	
	public static <O, S> void recurse(Consumer<Map<O, S>> method, Function<O, ? extends Iterable<S>> validStatesForObject, List<O> objects) {
		recurse(method, validStatesForObject, objects.listIterator(), new HashMap<>(objects.size()));
	}
	
	//TODO: paralellize recursion
	public static <O, S> void recurse(Consumer<Map<O, S>> method, Function<O, ? extends Iterable<S>> validStatesForObject, ListIterator<O> objectsIterator, Map<O, S> state) {
		if(objectsIterator.hasNext()) {
			O object = objectsIterator.next();
			for(S item: validStatesForObject.apply(object)) {
				state.put(object, item);
				recurse(method, validStatesForObject, objectsIterator, state);
			}
			objectsIterator.previous();
		} else {
			method.accept(state);
		}
	}

}
