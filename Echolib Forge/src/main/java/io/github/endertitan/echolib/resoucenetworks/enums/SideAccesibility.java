package io.github.endertitan.echolib.resoucenetworks.enums;

public enum SideAccesibility {
    NONE(false, false),
    INPUT(true, false),
    OUTPUT(false, true),
    INPUT_OUTPUT(true, true);

    public boolean isInput;
    public boolean isOutput;

    SideAccesibility(boolean isInput, boolean isOutput) {
        this.isInput = isInput;
        this.isOutput = isOutput;
    }


}
