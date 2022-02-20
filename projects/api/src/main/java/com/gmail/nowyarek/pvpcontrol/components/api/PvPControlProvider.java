package com.gmail.nowyarek.pvpcontrol.components.api;

/**
 * Provides static access to the {@link PvPControl} API.
 *
 * <p>Ideally, the ServiceManager for the platform should be used to obtain an
 * instance, however, this provider can be used if this is not viable.</p>
 */
public final class PvPControlProvider {
    private static PvPControl instance = null;

    private PvPControlProvider() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }

    /**
     * Gets an instance of the {@link PvPControl} API,
     * throwing {@link IllegalStateException} if the API is not loaded yet.
     *
     * @return an instance of the PvPControl API
     * @throws IllegalStateException if the API is not loaded yet
     */
    public static PvPControl get() {
        PvPControl instance = PvPControlProvider.instance;

        if (instance == null) throw new NotLoadedException();

        return instance;
    }

    static void register(PvPControl instance) {
        PvPControlProvider.instance = instance;
    }

    static void unregister() {
        PvPControlProvider.instance = null;
    }

    /**
     * Exception thrown when the API is requested before it has been loaded.
     */
    private static final class NotLoadedException extends IllegalStateException {
        private static final String MESSAGE = "The PvPControl API isn't loaded yet!\n" +
            "This could be because:\n" +
            "  a) the PvPControl is not installed or it failed to enable\n" +
            "  b) the plugin does not declare a dependency on PvPControl\n" +
            "  c) the plugin is retrieving the API before the plugin is enabled (i.e. in onLoad() instead of onEnable)\n" +
            "Learn more how to resolve this problem here: https://link/to/non-existing-docs-yet.";

        NotLoadedException() {
            super(MESSAGE);
        }
    }

}
