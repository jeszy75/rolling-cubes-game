package util.javafx;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Objects;

/**
 * Provides helper methods to work with JavaFX properties.
 */
public class Properties {

    /**
     * Returns whether the two properties specified wrap the same value.
     *
     *  @return {@code true} if the argument properties wrap the same value,
     *  {@code false} otherwise
     */
    public static <T> boolean equals(Property<T> p1, Property<T> p2) {
        return Objects.equals(p1.getValue(), p2.getValue());
    }

    /**
     * Returns whether the two specified one-dimensional arrays of properties
     * are equal to one another. The two arrays are considered equal if both
     * contain the same number of elements, and all corresponding pairs of
     * elements in the two arrays wrap the same value.
     *
     * @param a1 a property array
     * @param a2 another property array
     * @param <T>
     * @return {@code true} if the arrays are equal, {@code false} otherwise
     */
    public static <T> boolean equals(Property<T>[] a1, Property<T>[] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        if (a1.length != a2.length) {
            return false;
        }
        for (var i = 0; i < a1.length; i++) {
            if (!equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean equals(Property<T>[][] a1, Property<T>[][] a2) {
        if (a1 == a2) {
            return true;
        }
        if (a1 == null || a2 == null) {
            return false;
        }
        if (a1.length != a2.length) {
            return false;
        }
        for (var i = 0; i < a1.length; i++) {
            if (!equals(a1[i], a2[i])) {
                return false;
            }
        }
        return true;
    }

    public static <T> int hashCode(Property p) {
        return p != null ? Objects.hashCode(p.getValue()) : 0;
    }

    public static <T> int hashCode(Property[] a) {
        if (a == null) {
            return 0;
        }
        int result = 1;
        for (var i = 0; i < a.length; i++) {
            result = 31 * result + hashCode(a[i]);
        }
        return result;
    }

    public static <T> int hashCode(Property[][] a) {
        if (a == null) {
            return 0;
        }
        int result = 1;
        for (var i = 0; i < a.length; i++) {
            result = 31 * result + hashCode(a[i]);
        }
        return result;
    }

    public static void main(String[] args) {
        IntegerProperty p1 = new SimpleIntegerProperty(42);
        IntegerProperty p2 = new SimpleIntegerProperty(42);
        IntegerProperty p3 = new SimpleIntegerProperty(1);
        System.out.println(p1.equals(p2));
        System.out.println(equals(p1, p2));
        System.out.println(equals(p1, p3));
        System.out.println(equals(new IntegerProperty[] {p1, p2, p3}, new IntegerProperty[] {p1, p2, p3}));
        System.out.println(equals(new IntegerProperty[] {p1, p2, p3}, new IntegerProperty[] {p1, p2, p2}));
        var a1 = new IntegerProperty[][] {{p1, p2}, {p2, p3}};
        var a2 = new IntegerProperty[][] {{p1, p2}, {p2, p3}};
        var a3 = new IntegerProperty[][] {{p1, p2}, {p3, p1}};
        System.out.println(equals(a1, a2));
        System.out.println(equals(a1, a3));
        System.out.println(hashCode(new IntegerProperty[] {p1, p2, p3}));
        System.out.println(hashCode(a1));
    }
}
