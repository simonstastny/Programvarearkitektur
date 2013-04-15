package no.ntnu.swa.a13.util;

import java.util.Iterator;
import java.util.List;

public class RingIterator<E> implements Iterator<E> {

	List<E> list;
	int position;

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public E next() {
		position = (position + 1) % list.size();
		return list.get(position);
	}

	@Override
	public void remove() {
		list.remove(position);
	}

	public RingIterator(List<E> list) {
		super();
		this.list = list;
		this.position = 0;
	}
}
