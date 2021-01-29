package io.vlingo.example.perf.model.greeting;

import io.vlingo.actors.Address;
import io.vlingo.actors.Stage;
import io.vlingo.common.Completes;
import io.vlingo.example.perf.infrastructure.data.GreetingData;
import io.vlingo.example.perf.infrastructure.data.UpdateGreetingData;

public interface Greeting {

    static Completes<GreetingState> defineGreeting(Stage stage, GreetingData data){
        Address address = stage.world().addressFactory().uniquePrefixedWith("g-");
        Greeting greeting = stage.actorFor(Greeting.class, GreetingEntity.class);
        return greeting.defineGreeting(data);
    }


    Completes<GreetingState> defineGreeting(GreetingData data);

    Completes<GreetingState> updateMessage(UpdateGreetingData data);

    Completes<GreetingState> updateDescription(UpdateGreetingData data);
}