/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package org.apache.tinkerpop.gremlin.akka.process.actor;

import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.tinkerpop.gremlin.process.actor.Actors;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.traverser.util.TraverserSet;
import org.apache.tinkerpop.gremlin.structure.Partitioner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
public final class AkkaActors<S, E> implements Actors<S, E> {

    public final ActorSystem system;
    private TraverserSet<E> results = new TraverserSet<>();

    public AkkaActors(final Traversal.Admin<S, E> traversal, final Partitioner partitioner) {
        this.system = ActorSystem.create("traversal-" + traversal.hashCode());
        this.system.actorOf(Props.create(MasterTraversalActor.class, traversal.clone(), partitioner, this.results), "master");
    }

    @Override
    public Future<TraverserSet<E>> submit() {
        return CompletableFuture.supplyAsync(() -> {
            while (!this.system.isTerminated()) {

            }
            return this.results;
        });
    }
}
