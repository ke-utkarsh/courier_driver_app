This is initial code for Couriemate android app which follows MVVM architecture. It uses live data, view model and rxjava at its core.
Repository is the single source of truth for view models and view models are responsible for any information transition over the activity/fragments.
The architecture uses retrofit2 library for API calls and dagger2 DI library for objects creation.

The app also contains handful of unit tests, mockito tests and espresso(UI) test. Swagger has no role in this app so far. Once sorted, swagger will be added.