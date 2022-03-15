package io.github.endertitan.echolib.resoucenetworks.enums;

public enum ResourceNetworkMode {
    EQUAL(true),
    DISPERSIVE(false),
    PRIORITY(true),
    ISOLATED(false);

    private final boolean isParticipant;

    ResourceNetworkMode(boolean isParticpant) {
        this.isParticipant = isParticpant;
    }

    public boolean isParticipant() { return isParticipant; }

}
