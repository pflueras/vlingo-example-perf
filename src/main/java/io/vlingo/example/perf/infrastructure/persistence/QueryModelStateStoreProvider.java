package io.vlingo.example.perf.infrastructure.persistence;

import io.vlingo.actors.Stage;
import io.vlingo.common.Tuple2;
import io.vlingo.example.perf.model.greeting.GreetingState;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry;
import io.vlingo.lattice.model.stateful.StatefulTypeRegistry.Info;
import io.vlingo.symbio.EntryAdapterProvider;
import io.vlingo.symbio.StateAdapterProvider;
import io.vlingo.symbio.store.common.jdbc.Configuration;
import io.vlingo.symbio.store.dispatch.Dispatchable;
import io.vlingo.symbio.store.dispatch.Dispatcher;
import io.vlingo.symbio.store.dispatch.DispatcherControl;
import io.vlingo.symbio.store.state.StateStore;
import io.vlingo.xoom.actors.Settings;
import io.vlingo.xoom.storage.DatabaseParameters;
import io.vlingo.xoom.storage.Model;


public class QueryModelStateStoreProvider {
  private static QueryModelStateStoreProvider instance;

  public final DispatcherControl dispatcherControl;
  public final StateStore store;
  public final Queries queries;

  public static QueryModelStateStoreProvider instance() {
    return instance;
  }

  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry) throws Exception {
    final Dispatcher noop = new Dispatcher() {
      public void controlWith(final DispatcherControl control) { }
      public void dispatch(Dispatchable d) { }
    };

    return using(stage, registry, noop);
  }

  @SuppressWarnings("rawtypes")
  public static QueryModelStateStoreProvider using(final Stage stage, final StatefulTypeRegistry registry, final Dispatcher dispatcher) throws Exception {
    if (instance != null) {
      return instance;
    }

    final StateAdapterProvider stateAdapterProvider = new StateAdapterProvider(stage.world());
    stateAdapterProvider.registerAdapter(GreetingState.class, new GreetingStateAdapter());

    new EntryAdapterProvider(stage.world()); // future use
    final Configuration configuration = new DatabaseParameters(Model.QUERY, Settings.properties(), true)
            .mapToConfiguration();
    final Tuple2<StateStore, DispatcherControl> storeWithControl = StorageProvider.storeWithControl(stage, configuration);

    registry.register(new Info(storeWithControl._1, GreetingState.class, GreetingState.class.getSimpleName()));
    Queries queries = stage.actorFor(Queries.class, QueriesActor.class, storeWithControl._1);
    instance = new QueryModelStateStoreProvider(storeWithControl._1, storeWithControl._2, queries);

    return instance;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private QueryModelStateStoreProvider(final StateStore store, final DispatcherControl dispatcherControl,final Queries queries) {
    this.store = store;
    this.dispatcherControl = dispatcherControl;
    this.queries = queries;
  }
}
