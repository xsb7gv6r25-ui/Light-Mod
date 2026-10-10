package com.lightclient.math;

public final class RingBuffer<T> {
    private final Object[] data;
    private int head = 0;
    private int size = 0;

    public RingBuffer(int capacity) {
        if (capacity <= 0) throw new IllegalArgumentException("capacity must be > 0");
        this.data = new Object[capacity];
    }

    public void add(T value) {
        data[head] = value;
        head = (head + 1) % data.length;
        if (size < data.length) size++;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= size) throw new IndexOutOfBoundsException("index=" + index);
        int idx = (head - 1 - index + data.length * 2) % data.length;
        return (T) data[idx];
    }
}
