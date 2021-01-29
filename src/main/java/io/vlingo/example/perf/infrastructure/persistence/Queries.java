package io.vlingo.example.perf.infrastructure.persistence;

import io.vlingo.common.Completes;
import io.vlingo.example.perf.model.greeting.GreetingState;

import java.util.Collection;

public interface Queries {
    Completes<GreetingState> greetingWithId(final String id);
    Completes<Collection<GreetingState>> greetings();
}
