package ar.edu.itba.util;

public class Pair<T, K>{

    private T left;
    private K right;

    public Pair(T left, K right) {
        this.left = left;
        this.right = right;
    }

    public T getLeft() {
        return this.left;
    }

    public K getRight() {
        return this.right;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("[")
                .append(left)
                .append("-")
                .append(right)
                .append("]")
                .toString();
    }
}
