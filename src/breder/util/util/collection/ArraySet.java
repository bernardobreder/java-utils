package breder.util.util.collection;

import java.util.Arrays;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;

public class ArraySet<E> implements List<E>, Set<E> {

	/** Objetos */
	protected Object[] elements;
	/** Tamanho */
	protected int size;
	/** Mod Counter */
	protected transient int modCount = 0;

	/**
	 * Construtor
	 */
	public ArraySet() {
		this(10);
	}

	/**
	 * Construtor
	 * 
	 * @param initialCapacity
	 */
	public ArraySet(int initialCapacity) {
		this(0, new Object[initialCapacity]);
	}

	/**
	 * Construtor
	 * 
	 * @param initialCapacity
	 */
	private ArraySet(int size, Object[] elements) {
		this.elements = elements;
		this.size = size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int size() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEmpty() {
		return this.size == 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean contains(Object o) {
		for (Object object : this.elements) {
			if (object == o || (object.hashCode() == o.hashCode() && object.equals(o))) {
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iterator<E> iterator() {
		return new Itr();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return Arrays.copyOf(this.elements, this.size);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public <T> T[] toArray(T[] a) {
		if (a.length < size)
			return (T[]) Arrays.copyOf(elements, size, a.getClass());
		System.arraycopy(elements, 0, a, 0, size);
		if (a.length > size)
			a[size] = null;
		return a;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean add(E e) {
		this.ensureCapacity(this.size + 1);
		this.elements[this.size++] = e;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean remove(Object o) {
		for (int n = 0; n < this.size; n++) {
			Object object = this.elements[n];
			if (object == o || (object.hashCode() == o.hashCode() && object.equals(o))) {
				this.elements[n--] = this.elements[--this.size];
				modCount++;
				return true;
			}
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		Object[] a;
		if (c instanceof ArraySet) {
			a = ((ArraySet<?>) c).elements;
		} else {
			a = c.toArray();
		}
		for (Object other : a) {
			boolean flag = false;
			for (Object object : this.elements) {
				if (object == other || (object.hashCode() == other.hashCode() && object.equals(other))) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				return false;
			}
		}
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(Collection<? extends E> c) {
		Object[] a;
		if (c instanceof ArraySet) {
			a = ((ArraySet<?>) c).elements;
		} else {
			a = c.toArray();
		}
		int numNew = a.length;
		ensureCapacity(size + numNew);
		System.arraycopy(a, 0, elements, size, numNew);
		size += numNew;
		return numNew != 0;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		return this.addAll(c);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		Object[] a;
		if (c instanceof ArraySet) {
			a = ((ArraySet<?>) c).elements;
		} else {
			a = c.toArray();
		}
		boolean flag = false;
		for (int n = 0; n < this.size; n++) {
			Object object = this.elements[n];
			for (Object other : a) {
				if (object == other || (object.hashCode() == other.hashCode() && object.equals(other))) {
					this.elements[n--] = this.elements[--this.size];
					modCount++;
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		Object[] a;
		if (c instanceof ArraySet) {
			a = ((ArraySet<?>) c).elements;
		} else {
			a = c.toArray();
		}
		boolean flag = false;
		for (int n = 0; n < this.size; n++) {
			Object object = this.elements[n];
			for (Object other : a) {
				if (object == other || (object.hashCode() == other.hashCode() && object.equals(other))) {
					this.elements[n--] = this.elements[--this.size];
					modCount++;
					flag = true;
					break;
				}
			}
		}
		return flag;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void clear() {
		this.size = 0;
		modCount++;
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E get(int index) {
		if (index >= this.size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return (E) this.elements[index];
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E set(int index, E element) {
		if (index >= this.size) {
			throw new ArrayIndexOutOfBoundsException(index);
		}
		Object oldValue = this.elements[index];
		this.elements[index] = element;
		return (E) oldValue;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void add(int index, E element) {
		this.add(element);
	}

	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	@Override
	public E remove(int index) {
		Object value = this.elements[index];
		this.elements[index] = this.elements[--this.size];
		modCount++;
		return (E) value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int indexOf(Object o) {
		for (int n = 0; n < this.size; n++) {
			Object object = this.elements[n];
			if (object == o || (object.hashCode() == o.hashCode() && object.equals(o))) {
				return n;
			}
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int lastIndexOf(Object o) {
		for (int n = this.size - 1; n >= 0; n++) {
			Object object = this.elements[n];
			if (object == o || (object.hashCode() == o.hashCode() && object.equals(o))) {
				return n;
			}
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator() {
		return listIterator(0);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ListIterator<E> listIterator(int index) {
		return new ListItr(index);
	}

	@Override
	public Spliterator<E> spliterator() {
		return List.super.spliterator();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArraySet<E> subList(int fromIndex, int toIndex) {
		Object[] array = new Object[toIndex - fromIndex];
		int length = array.length;
		System.arraycopy(elements, fromIndex, array, 0, length);
		return new ArraySet<E>(length, array);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(elements);
		result = prime * result + size;
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArraySet<?> other = (ArraySet<?>) obj;
		if (size != other.size)
			return false;
		if (!Arrays.equals(elements, other.elements))
			return false;
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		Iterator<E> i = iterator();
		if (!i.hasNext())
			return "[]";

		StringBuilder sb = new StringBuilder();
		sb.append('[');
		for (;;) {
			E e = i.next();
			sb.append(e == this ? "(this Collection)" : e);
			if (!i.hasNext())
				return sb.append(']').toString();
			sb.append(", ");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArraySet<E> clone() {
		Object[] objects = Arrays.copyOf(this.elements, size);
		return new ArraySet<E>(size, objects);
	}

	/**
	 * Increases the capacity of this <tt>ArraySet</tt> instance, if necessary,
	 * to ensure that it can hold at least the number of elements specified by
	 * the minimum capacity argument.
	 * 
	 * @param minCapacity
	 *            the desired minimum capacity
	 */
	private void ensureCapacity(int minCapacity) {
		modCount++;
		int oldCapacity = elements.length;
		if (minCapacity > oldCapacity) {
			int newCapacity = (oldCapacity * 3) / 2 + 1;
			if (newCapacity < minCapacity)
				newCapacity = minCapacity;
			elements = Arrays.copyOf(elements, newCapacity);
		}
	}

	/**
	 * Classe de SubList
	 * 
	 * @author bernardobreder
	 * 
	 * @param <E>
	 *            tipo
	 */
	protected static class SubList<E> extends ArraySet<E> {

		private ArraySet<E> l;

		private int offset;

		private int size;

		private int expectedModCount;

		public SubList(ArraySet<E> list, int fromIndex, int toIndex) {
			if (fromIndex < 0)
				throw new IndexOutOfBoundsException("fromIndex = " + fromIndex);
			if (toIndex > list.size())
				throw new IndexOutOfBoundsException("toIndex = " + toIndex);
			if (fromIndex > toIndex)
				throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
			l = list;
			offset = fromIndex;
			size = toIndex - fromIndex;
			expectedModCount = l.modCount;
		}

		public E set(int index, E element) {
			rangeCheck(index);
			checkForComodification();
			return l.set(index + offset, element);
		}

		public E get(int index) {
			rangeCheck(index);
			checkForComodification();
			return l.get(index + offset);
		}

		public int size() {
			checkForComodification();
			return size;
		}

		public void add(int index, E element) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException();
			checkForComodification();
			l.add(index + offset, element);
			expectedModCount = l.modCount;
			size++;
			modCount++;
		}

		public E remove(int index) {
			rangeCheck(index);
			checkForComodification();
			E result = l.remove(index + offset);
			expectedModCount = l.modCount;
			size--;
			modCount++;
			return result;
		}

		public boolean addAll(Collection<? extends E> c) {
			return addAll(size, c);
		}

		public boolean addAll(int index, Collection<? extends E> c) {
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
			int cSize = c.size();
			if (cSize == 0)
				return false;

			checkForComodification();
			l.addAll(offset + index, c);
			expectedModCount = l.modCount;
			size += cSize;
			modCount++;
			return true;
		}

		public Iterator<E> iterator() {
			return listIterator();
		}

		public ListIterator<E> listIterator(final int index) {
			checkForComodification();
			if (index < 0 || index > size)
				throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);

			return new ListIterator<E>() {
				private ListIterator<E> i = l.listIterator(index + offset);

				public boolean hasNext() {
					return nextIndex() < size;
				}

				public E next() {
					if (hasNext())
						return i.next();
					else
						throw new NoSuchElementException();
				}

				public boolean hasPrevious() {
					return previousIndex() >= 0;
				}

				public E previous() {
					if (hasPrevious())
						return i.previous();
					else
						throw new NoSuchElementException();
				}

				public int nextIndex() {
					return i.nextIndex() - offset;
				}

				public int previousIndex() {
					return i.previousIndex() - offset;
				}

				public void remove() {
					i.remove();
					expectedModCount = l.modCount;
					size--;
					modCount++;
				}

				public void set(E e) {
					i.set(e);
				}

				public void add(E e) {
					i.add(e);
					expectedModCount = l.modCount;
					size++;
					modCount++;
				}
			};
		}

		public ArraySet<E> subList(int fromIndex, int toIndex) {
			return new SubList<E>(this, fromIndex, toIndex);
		}

		private void rangeCheck(int index) {
			if (index < 0 || index >= size)
				throw new IndexOutOfBoundsException("Index: " + index + ",Size: " + size);
		}

		private void checkForComodification() {
			if (l.modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	/**
	 * Iterador
	 * 
	 * @author bernardobreder
	 * 
	 */
	private class Itr implements Iterator<E> {
		/**
		 * Index of element to be returned by subsequent call to next.
		 */
		int cursor = 0;

		/**
		 * Index of element returned by most recent call to next or previous.
		 * Reset to -1 if this element is deleted by a call to remove.
		 */
		int lastRet = -1;

		/**
		 * The modCount value that the iterator believes that the backing List
		 * should have. If this expectation is violated, the iterator has
		 * detected concurrent modification.
		 */
		int expectedModCount = modCount;

		public boolean hasNext() {
			return cursor != size();
		}

		public E next() {
			checkForComodification();
			try {
				E next = get(cursor);
				lastRet = cursor++;
				return next;
			} catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			if (lastRet == -1)
				throw new IllegalStateException();
			checkForComodification();

			try {
				ArraySet.this.remove(lastRet);
				if (lastRet < cursor)
					cursor--;
				lastRet = -1;
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException e) {
				throw new ConcurrentModificationException();
			}
		}

		final void checkForComodification() {
			if (modCount != expectedModCount)
				throw new ConcurrentModificationException();
		}
	}

	private class ListItr extends Itr implements ListIterator<E> {
		ListItr(int index) {
			cursor = index;
		}

		public boolean hasPrevious() {
			return cursor != 0;
		}

		public E previous() {
			checkForComodification();
			try {
				int i = cursor - 1;
				E previous = get(i);
				lastRet = cursor = i;
				return previous;
			} catch (IndexOutOfBoundsException e) {
				checkForComodification();
				throw new NoSuchElementException();
			}
		}

		public int nextIndex() {
			return cursor;
		}

		public int previousIndex() {
			return cursor - 1;
		}

		public void set(E e) {
			if (lastRet == -1)
				throw new IllegalStateException();
			checkForComodification();

			try {
				ArraySet.this.set(lastRet, e);
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}

		public void add(E e) {
			checkForComodification();

			try {
				ArraySet.this.add(cursor++, e);
				lastRet = -1;
				expectedModCount = modCount;
			} catch (IndexOutOfBoundsException ex) {
				throw new ConcurrentModificationException();
			}
		}
	}

	/**
	 * Testador
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Set<Object> list = new ArraySet<Object>();
		int max = 20;
		for (Integer n = 0; n < max; n++) {
			list.add(n);
		}
		System.out.println(list);
		for (int n = 0; n < max; n++) {
			list.remove(n);
			System.out.println(list);
		}
		System.out.println(list);
	}

}
