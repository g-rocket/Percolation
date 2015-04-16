package net.clonecomputers.percolation;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.*;

public class Percolation implements Runnable {
	private Set<Configuration> state = new HashSet<>();
	private Set<Configuration> nextState = new HashSet<>();
	private int pathLength = 0;

	public static void main(String[] args) {
		new Percolation().run();
	}
	
	public Percolation() {
		state.add(new Configuration());
	}

	@Override
	public void run() {
		while(true) {
			step();
			//TODO: process and save the state, and print out some usefull information
		}
	}
	
	public void step() {
		for(Configuration config: state){
			recursivelyTryAll(config);
		}
		state = nextState;
		nextState = new HashSet<>();
		// compactify equivalent configurations?
	}
	
	public void recursivelyTryAll(Configuration config) {
		BiConsumer<List<Point>, List<Integer>> tryConfiguration = (objects, state) -> {
			//TODO: process the possible configuration and add it to nextState
		};
		Function<Point, ? extends Iterable<Integer>> getPossibleValues = (object) -> {return Arrays.asList(0,1);};
		recurse(tryConfiguration, getPossibleValues, config.zeroesNeighboringOnes());
	}
	
	public static <O, S> void recurse(BiConsumer<List<O>,List<S>> method, Function<O, ? extends Iterable<S>> validStatesForObject, List<O> objects) {
		recurse(method, validStatesForObject, objects.listIterator(), objects, new ArrayList<S>(objects.size()));
	}
	
	public static <O, S> void recurse(BiConsumer<List<O>,List<S>> method, Function<O, ? extends Iterable<S>> validStatesForObject, ListIterator<O> objectsIterator, List<O> objects, List<S> states) {
		if(objectsIterator.hasNext()) {
			for(S item: validStatesForObject.apply(objectsIterator.next())) {
				states.set(objectsIterator.nextIndex(), item);
				recurse(method, validStatesForObject, objectsIterator, objects, states);
			}
			objectsIterator.previous();
		} else {
			method.accept(objects,states);
		}
	}

}
