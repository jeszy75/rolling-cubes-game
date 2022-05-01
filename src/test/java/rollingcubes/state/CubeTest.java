package rollingcubes.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CubeTest {

    @Test
    void testOf() {
        assertEquals(Cube.EMPTY, Cube.of(0));
        assertEquals(Cube.CUBE1, Cube.of(1));
        assertEquals(Cube.CUBE2, Cube.of(2));
        assertEquals(Cube.CUBE3, Cube.of(3));
        assertEquals(Cube.CUBE4, Cube.of(4));
        assertEquals(Cube.CUBE5, Cube.of(5));
        assertEquals(Cube.CUBE6, Cube.of(6));
    }

    @Test
    void testOf_shouldThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> Cube.of(-1));
        assertThrows(IllegalArgumentException.class, () -> Cube.of(7));
    }

    @Test
    void testRollTo() {
        assertEquals(Cube.CUBE1, Cube.CUBE1.rollTo(Direction.UP).rollTo(Direction.DOWN));
        assertEquals(Cube.CUBE1, Cube.CUBE1.rollTo(Direction.RIGHT).rollTo(Direction.LEFT));
        assertEquals(Cube.CUBE1, Cube.CUBE1.rollTo(Direction.DOWN).rollTo(Direction.UP));
        assertEquals(Cube.CUBE1, Cube.CUBE1.rollTo(Direction.LEFT).rollTo(Direction.RIGHT));
        assertEquals(Cube.CUBE1, Cube.CUBE1
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
                .rollTo(Direction.UP)
        );
    }

    @Test
    void testRollTo_shouldThrowUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> Cube.EMPTY.rollTo(Direction.UP));
    }

}