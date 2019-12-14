package me.devnatan.hsorg.plugin.mc;

import io.netty.handler.codec.http.HttpHeaderNames;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import reactor.netty.http.client.HttpClient;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HSCore {

    private final Logger logger;
    private final String apiEndpoint;
    private final Map<String, String> credentials;

    @Nullable
    private String accessToken;

    private boolean isStarted;
    private boolean isConnected;
    private long initializedAt;
    private HttpClient httpClient;

    public HSCore(
            @NotNull Logger logger,
            @NotNull String name,
            @NotNull String apiEndpoint,
            @NotNull Map<String, String> credentials)
    {
        this.logger = new HSLogger(name, logger);
        this.apiEndpoint = apiEndpoint;
        this.credentials = credentials;
        this.isStarted = false;
        this.isConnected = false;
    }

    /**
     * The address of the official HappyShop API.
     * @return String
     */
    public String getApiEndpoint() {
        return apiEndpoint;
    }

    /**
     * If the initialization and WebSocket connection process has started.
     * @return boolean
     */
    public boolean isStarted() {
        return isStarted;
    }

    /**
     * If the client is connected and synchronized with the server.
     * @return boolean
     */
    public boolean isConnected() {
        return isConnected;
    }

    public CompletableFuture<Void> start() {
        if (this.isStarted)
            throw new IllegalStateException("Already started.");

        long startedAt = System.nanoTime();
        CompletableFuture<Void> future = new CompletableFuture<>();

        try {
            checkCredentials();
            future.whenComplete(($, $$) -> {
                long now = System.nanoTime();
                this.logger.info(String.format("Initialized, took %dms", TimeUnit.NANOSECONDS.toMillis(now - startedAt)));
                this.initializedAt = now;
                this.isStarted = true;
                connect();
            });

            this.httpClient = HttpClient.create()
                    .wiretap(true)
                    .baseUrl(this.apiEndpoint)
                    .headers(h -> h.set(HttpHeaderNames.CONTENT_TYPE, "application/json")
                            .set("X-ApiKeys", "accessKey=" + this.credentials.get("access-key") + "; secret=" + this.credentials.get("secret")))
                    .doOnRequest((request, connection) -> {
                        /*
                         * At this point the customer should have already logged into WebSocket.
                         * The server will provide an access token that will be used for requests in the API.
                         * If the access token is not set then we are unable to connect to the server.
                         */
                        request.addHeader(HttpHeaderNames.AUTHORIZATION,  "Bearer " + this.accessToken);
                    });
            future.complete(null);
        } catch (Throwable e) {
            logger.log(Level.SEVERE, "An error occurred at startup", e);
            future.completeExceptionally(e);
        }
        return future;
    }

    public void stop() {
        if (!this.isStarted) {
            /*
             * It does not throw any exceptions because the plugin initialization
             * process can terminate before the core is really started,
             * calling this method when it terminates without calling #start().
             */
            return;
        }

        disconnect();
        this.isStarted = false;
    }

    /**
     * Connects the client to the WebSocket server.
     * The value of {@link #isConnected} is finally changed only if the client
     * is fully connected and authorized by the server.
     *
     * When you connect, you will send a package called `HELLO` with the client information.
     * The server will verify the information and may reject or accept the client thus connecting
     * it and sending back the `HELLO` with a token so the client can identify himself.
     *
     * The client will send an `IDENTITY` message to the server using the token
     * that the server sent and authorization information as the access token,
     * the token that the previous server sent has an expiration period of 1 minute,
     * so send it after this period will cause rejection, the client will be disconnected
     * from the server and the token invalidated.
     *
     * The server will verify the token sent with a copy of it stored on the server and will compare them and the client as well.
     * If verification is successful, the token will be usable and the client will
     * finally be synchronized with the server. The server will send an {@link #accessToken}
     * for use in HTTP requests and WebSocket. The value of {@link #isConnected} will be changed to` true`.
     *
     * @throws IllegalStateException if the client is already connected.
     */
    public void connect() {
        if (this.isConnected)
            throw new IllegalStateException("Already connected.");

        // TODO...: connect
        this.logger.info("Connecting...");
    }

    /**
     * Disconnects the client from the WebSocket server.
     * @throws IllegalStateException if the client is not connected.
     */
    public void disconnect() {
        if (!this.isConnected)
            throw new IllegalStateException("Not connected.");

        this.isConnected = false;
        this.logger.info("Disconnected.");
    }

    private void checkCredentials() {
        for (String key : new String[] { "store", "access-key", "secret" }) {
            if (!this.credentials.containsKey(key))
                throw new NoSuchElementException("Missing \"" + key + "\" credential.");
        }
    }

}
