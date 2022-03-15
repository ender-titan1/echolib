package io.github.endertitan.echolib.util;

import net.minecraft.util.math.BlockPos;

public enum EchoDirection {
    TOP(false),
    BOTTOM(false),
    NORTH(false),
    EAST(false),
    SOUTH(false),
    WEST(false);

    private boolean isRelative;
    private EchoDirection relativeBack;

    EchoDirection(boolean isRelative) {
        this.isRelative = isRelative;
    }

    static {
        TOP.relativeBack    = BOTTOM;
        BOTTOM.relativeBack = TOP;
        NORTH.relativeBack  = SOUTH;
        SOUTH.relativeBack  = NORTH;
        EAST.relativeBack   = WEST;
        WEST.relativeBack   = EAST;
    }

    public EchoDirection getRelativeBack() {
        return this.relativeBack;
    }

    public BlockPos toPos(BlockPos original) {

        int x = original.getX();
        int y = original.getY();
        int z = original.getZ();

        switch(this) {
            case BOTTOM:
                return new BlockPos(x, y - 1, z);
            case TOP:
                return new BlockPos(x, y + 1, z);
            case NORTH:
                return new BlockPos(x, y, z - 1);
            case SOUTH:
                return new BlockPos(x, y, z + 1);
            case EAST:
                return new BlockPos(x + 1, y, z);
            case WEST:
                return new BlockPos(x - 1, y, z);
        }

        return original;
    }

    public static EchoDirection[] all() {
        return new EchoDirection[] { TOP, BOTTOM, NORTH, SOUTH, EAST, WEST };
    }
}
