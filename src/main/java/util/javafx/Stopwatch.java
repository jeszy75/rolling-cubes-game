package util.javafx;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;

import org.apache.commons.lang3.time.DurationFormatUtils;

/**
 * A general-purpose stopwatch.
 */
public class Stopwatch {

    private LongProperty seconds = new SimpleLongProperty();
    private StringProperty hhmmss = new SimpleStringProperty();
    private Timeline timeline;

    /**
     * Creates a {@code Stopwatch} object.
     */
    public Stopwatch() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, e -> seconds.set(seconds.get() + 1)),
                new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        hhmmss.bind(Bindings.createStringBinding(() -> DurationFormatUtils.formatDuration(seconds.get() * 1000, "HH:mm:ss"), seconds));
    }

    /**
     * {@return a property to access the number of seconds elapsed}
     */
    public LongProperty secondsProperty() {
        return seconds;
    }

    /**
     * {@return a property to access the time elapsed in {@code hh:mm:ss}
     * format}
     */
    public StringProperty hhmmssProperty() {
        return hhmmss;
    }

    /**
     * Starts the stopwatch.
     */
    public void start() {
        timeline.play();
    }

    /**
     * Pauses the stopwatch.
     */
    public void stop() {
        timeline.pause();
    }

    /**
     * Resets the stopwatch. In order to reset the stopwatch it must be paused
     * first with the {@link #stop()} method.
     *
     * @throws IllegalStateException if the stopwatch is not paused
     */
    public void reset() {
        if (timeline.getStatus() != Animation.Status.PAUSED) {
            throw new IllegalStateException();
        }
        seconds.set(0);
    }

    /**
     * {@return the status of the stopwatch}
     */
    public Animation.Status getStatus() {
        return timeline.getStatus();
    }

}
