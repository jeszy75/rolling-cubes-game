package rollingcubes.state;

/**
 * Class representing the empty space and the possible orientations of a cube.
 */
public enum Cube {

    EMPTY,
    CUBE1,
    CUBE2,
    CUBE3,
    CUBE4,
    CUBE5,
    CUBE6;

    /**
     * The array defining the transitions between orientations when a cube is
     * rolled. Rows correspond to cube orientations, columns correspond to
     * directions.
     */
    private static final int[][] T = {
            {0, 0, 0, 0},
            {3, 4, 2, 5},
            {1, 2, 6, 2},
            {6, 3, 1, 3},
            {4, 6, 4, 1},
            {5, 1, 5, 6},
            {2, 5, 3, 4}
    };

    /**
     * Returns the instance represented by the value specified.
     *
     * @param value the value representing an instance
     * @return the instance represented by the value specified
     * @throws IllegalArgumentException if the value specified does not
     * represent an instance
     */
    public static Cube of(int value) {
        if (value < 0 || value >= values().length) {
            throw new IllegalArgumentException();
        }
        return values()[value];
    }

    /**
     * {@return the integer value that represents this instance}
     */
    public int getValue() {
        return ordinal();
    }

    /**
     * Rolls the cube to the direction specified.
     *
     * @param direction the direction to which the cube is rolled
     * @return the cube rolled to the direction specified
     * @throws UnsupportedOperationException if the method is invoked on the
     *         {@link #EMPTY} instance
     */
    public Cube rollTo(Direction direction) {
        if (this == EMPTY) {
            throw new UnsupportedOperationException();
        }
        return values()[T[ordinal()][direction.ordinal()]];
    }

    public String toString() {
        return Integer.toString(ordinal());
    }

}
