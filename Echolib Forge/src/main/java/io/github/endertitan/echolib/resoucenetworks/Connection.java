package io.github.endertitan.echolib.resoucenetworks;

import io.github.endertitan.echolib.resoucenetworks.enums.ConnectionMode;

public class Connection {
    public ConnectionMode mode;
    public Network<?> other;

    public Connection(Network<?> other, ConnectionMode mode) {
        this.other = other;
        this.mode = mode;
    }
}
