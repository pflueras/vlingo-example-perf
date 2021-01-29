package io.vlingo.example.perf.infrastructure.persistence;


import io.vlingo.common.Completes;
import io.vlingo.example.perf.model.greeting.GreetingState;
import io.vlingo.lattice.query.StateStoreQueryActor;
import io.vlingo.symbio.store.state.StateStore;

import java.util.ArrayList;
import java.util.Collection;

public class QueriesActor extends StateStoreQueryActor implements Queries {

    public QueriesActor(final StateStore stateStore){
        super(stateStore);
    }

    @Override
    public Completes<GreetingState> greetingWithId(String id) {
        return queryStateFor(id,GreetingState.class, GreetingState.empty());
    }

    @Override
    public Completes<Collection<GreetingState>> greetings() {
        return streamAllOf(GreetingState.class,new ArrayList<>());
    }
}
