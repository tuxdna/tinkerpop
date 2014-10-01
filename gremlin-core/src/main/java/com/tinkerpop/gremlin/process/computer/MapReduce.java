package com.tinkerpop.gremlin.process.computer;

import com.tinkerpop.gremlin.process.Traversal;
import com.tinkerpop.gremlin.process.util.DefaultSideEffects;
import com.tinkerpop.gremlin.structure.Vertex;
import org.apache.commons.configuration.Configuration;
import org.javatuples.Pair;

import java.io.Serializable;
import java.util.Iterator;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public interface MapReduce<MK, MV, RK, RV, R> {

    public static enum Stage {MAP, COMBINE, REDUCE}


    public default void storeState(final Configuration configuration) {

    }

    public default void loadState(final Configuration configuration) {

    }

    public boolean doStage(final Stage stage);

    public default void map(final Vertex vertex, final MapEmitter<MK, MV> emitter) {
    }

    public default void combine(final MK key, final Iterator<MV> values, final ReduceEmitter<RK, RV> emitter) {
    }

    public default void reduce(final MK key, final Iterator<MV> values, final ReduceEmitter<RK, RV> emitter) {
    }

    public R generateSideEffect(final Iterator<Pair<RK, RV>> keyValues);

    public String getSideEffectKey();

    public default void addSideEffectToMemory(final Memory memory, final Iterator<Pair<RK, RV>> keyValues) {
        memory.set(this.getSideEffectKey(), this.generateSideEffect(keyValues));
    }

    public static Traversal.SideEffects getLocalSideEffects(final Vertex localVertex) {
        return new DefaultSideEffects(localVertex);
    }

    //////////////////

    public interface MapEmitter<K, V> {
        public void emit(final K key, final V value);
    }

    public interface ReduceEmitter<OK, OV> {
        public void emit(final OK key, OV value);
    }

    //////////////////

    public static class NullObject implements Comparable<NullObject>, Serializable {
        private static final NullObject INSTANCE = new NullObject();
        private static final String NULL_OBJECT = "MapReduce$NullObject";

        public static NullObject instance() {
            return INSTANCE;
        }

        public int hashCode() {
            return 666666666;
        }

        public boolean equals(final Object object) {
            return object instanceof NullObject;
        }

        @Override
        public int compareTo(final NullObject nullObject) {
            return 0;
        }

        public String toString() {
            return NULL_OBJECT;
        }
    }
}
