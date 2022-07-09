package com.zamron.world.entity.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.zamron.net.SessionState;
import com.zamron.world.entity.impl.player.Player;

/**
 * A collection that provides functionality for storing and managing characters.
 * This list does not support the storage of elements with a value of
 * {@code null}, and maintains an extremely strict ordering of the elements.
 * This list for storing characters will be blazingly faster than typical
 * implementations, mainly due to the fact that it uses a {@link Queue} to cache
 * the slots that characters are removed from in order to reduce the amount of
 * lookups needed to add a new character.
 *
 * @param <E>
 *            the type of character being managed with this collection.
 * @author lare96 <http://github.com/lare96>
 */
public final class CharacterList<E extends Character> implements Iterable<E> {

	/**
	 * The backing array of {@link CharacterNode}s within this collection.
	 */
	private E[] characters;

	/**
	 * The queue containing all of the slots that {@link CharacterNode}s were
	 * recently removed from. This is used to reduce slot lookup times for
	 * characters being added to this character list.
	 */
	private final Queue<Integer> slotQueue = new LinkedList<>();

	/**
	 * The finite capacity of this collection.
	 */
	private final int capacity;

	/**
	 * The size of this collection.
	 */
	private int size;

	/**
	 * Creates a new {@link CharacterList}.
	 *
	 * @param capacity
	 *            the finite capacity of this collection.
	 */
	@SuppressWarnings("unchecked")
	public CharacterList(int capacity) {
		this.capacity = ++capacity;
		this.characters = (E[]) new Character[capacity];
		this.size = 0;
	}

	/**
	 * Adds an element to this collection.
	 *
	 * @param e
	 *            the element to add to this collection.
	 * @return {@code true} if the element was successfully added, {@code false}
	 *         otherwise.
	 */
	public boolean add(E e) {
		Objects.requireNonNull(e);

		if (!e.isRegistered()) {
			int slot = slotSearch();
			if (slot < 0)
				return false;
			e.setRegistered(true);
			e.setIndex(slot);
			characters[slot] = e;
			size++;
			return true;
		}
		return false;
	}

	/**
	 * Removes an element from this collection.
	 *
	 * @param e
	 *            the element to remove from this collection.
	 * @return {@code true} if the element was successfully removed,
	 *         {@code false} otherwise.
	 */
	public boolean remove(E e) {
		Objects.requireNonNull(e);

		// A player cannot be removed from the character list unless it's from a
		// logout request. On-demand removes for players completely mess up the
		// login process, so we request a logout instead.
		if (e.isPlayer()) {
			Player player = (Player) e;
			if (player.isMiniMe && e.isRegistered()) {
				e.setRegistered(false);
				characters[e.getIndex()] = null;
				slotQueue.add(e.getIndex());
				size--;
				return true;
			}
			if (player.getSession().getState() != SessionState.LOGGING_OUT && !player.isMiniMe) {
				player.dispose();
				return false;
			}
		}

		if (e.isRegistered() && characters[e.getIndex()] != null) {
			e.setRegistered(false);
			characters[e.getIndex()] = null;
			slotQueue.add(e.getIndex());
			size--;
			return true;
		}
		return false;
	}

	/**
	 * Determines if this collection contains the specified element.
	 *
	 * @param e
	 *            the element to determine if this collection contains.
	 * @return {@code true} if this collection contains the element,
	 *         {@code false} otherwise.
	 */
	public boolean contains(E e) {
		Objects.requireNonNull(e);
		return characters[e.getIndex()] != null;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * This implementation will exclude all elements with a value of
	 * {@code null} to avoid {@link NullPointerException}s.
	 */
	@Override
	public void forEach(Consumer<? super E> action) {
		for (E e : characters) {
			if (e == null)
				continue;
			action.accept(e);
		}
	}

	@Override
	public Spliterator<E> spliterator() {
		return Spliterators.spliterator(characters, Spliterator.ORDERED);
	}

	/**
	 * Searches the backing array for the first element encountered that matches
	 * {@code filter}. This does not include elements with a value of
	 * {@code null}.
	 *
	 * @param filter
	 *            the predicate that the search will be based on.
	 * @return an optional holding the found element, or an empty optional if no
	 *         element was found.
	 */
	public Optional<E> search(Predicate<? super E> filter) {
		for (E e : characters) {
			if (e == null)
				continue;
			if (filter.test(e))
				return Optional.of(e);
		}
		return Optional.empty();
	}

	@Override
	public Iterator<E> iterator() {
		return new CharacterListIterator<>(this);
	}

	/**
	 * Retrieves the element on the given slot.
	 *
	 * @param slot
	 *            the slot to retrieve the element on.
	 * @return the element on the given slot or {@code null} if no element is on
	 *         the spot.
	 */
	public E get(int slot) {
		return characters[slot];
	}

	/**
	 * Searches through the backing array for the next available slot. This
	 * method will perform in constant time if the {@code removeSlots} list has
	 * a size greater than {@code 0}.
	 *
	 * @return the found slot, or -1 if no slot is available.
	 */
	private int slotSearch() {
		if (slotQueue.size() == 0) {
			for (int slot = 1; slot < capacity; slot++) {
				if (characters[slot] == null) {
					return slot;
				}
			}
			return -1;
		}
		return slotQueue.remove();
	}

	/**
	 * Determines the amount of elements stored in this collection.
	 *
	 * @return the amount of elements stored in this collection.
	 */
	public int size() {
		return size;
	}

	/**
	 * Gets the finite capacity of this collection.
	 *
	 * @return the finite capacity of this collection.
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * Gets the remaining amount of space in this collection.
	 *
	 * @return the remaining amount of space in this collection.
	 */
	public int spaceLeft() {
		return capacity - size;
	}

	/**
	 * Is the collection full?
	 * @return true if collection is full, otherwise false
	 */
	public boolean isFull() {
		return size >= capacity;
	}

	/**
	 * Returns a sequential stream with this collection as its source.
	 *
	 * @return a sequential stream over the elements in this collection.
	 */
	public Stream<E> stream() {
		return Arrays.stream(characters);
	}

	/**
	 * Removes all of the elements in this collection and resets the
	 * {@link CharacterList#characters} and {@link CharacterList#size}.
	 */
	@SuppressWarnings("unchecked")
	public void clear() {
		forEach(this::remove);
		characters = (E[]) new Character[capacity];
		size = 0;
	}

	/**
	 * An {@link Iterator} implementation that will iterate over the elements in
	 * a character list.
	 *
	 * @param <E>
	 *            the type of character being iterated over.
	 * @author lare96 <http://github.com/lare96>
	 */
	private static final class CharacterListIterator<E extends Character> implements Iterator<E> {

		/**
		 * The {@link CharacterList} that is storing the elements.
		 */
		private final CharacterList<E> list;

		/**
		 * The current index that the iterator is iterating over.
		 */
		private int index;

		/**
		 * The last index that the iterator iterated over.
		 */
		private int lastIndex = -1;

		/**
		 * Creates a new {@link CharacterListIterator}.
		 *
		 * @param list
		 *            the list that is storing the elements.
		 */
		public CharacterListIterator(CharacterList<E> list) {
			this.list = list;
		}

		@Override
		public boolean hasNext() {
			return !(index + 1 > list.capacity());
		}

		@Override
		public E next() {
			if (index >= list.capacity()) {
				throw new ArrayIndexOutOfBoundsException("There are no " + "elements left to iterate over!");
			}

			lastIndex = index;
			index++;
			return list.characters[lastIndex];
		}

		@Override
		public void remove() {
			if (lastIndex == -1) {
				throw new IllegalStateException("This method can only be " + "called once after \"next\".");
			}
			list.remove(list.characters[lastIndex]);
			index = lastIndex;
			lastIndex = -1;
		}
	}
}