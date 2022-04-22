package me.eccentric_nz.TARDIS.enumeration;

public enum Autonomous {
    OFF(-1),
    HOME(12),
    AREAS(13),
    CONFIGURED_AREAS(14),
    CLOSEST(15);

    final int slot;

    Autonomous(int slot) {
        this.slot = slot;
    }

    public int getSlot() {
        return slot;
    }

    public enum Default {
        HOME(30),
        STAY(31);

        final int slot;

        Default(int slot) {
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }
    }
}