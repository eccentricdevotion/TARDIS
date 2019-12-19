package me.eccentric_nz.TARDIS.mobfarming;

/**
 * According to the Fourth Doctor, bees are "insects with stings on their tails", and a non-sentient species found on
 * Earth. They are domesticated by humans for the production of honey, and even an average human like Donna Noble knows
 * that they experienced a profound global downturn in their population during the early 21st century.
 */
public class TARDISBee extends TARDISMob {

    private boolean nectar;
    private boolean stung;
    private int anger;

    /**
     * Data storage class for TARDIS Bee.
     */
    public TARDISBee() {
    }

    public boolean hasNectar() {
        return nectar;
    }

    public void setHasNectar(boolean nectar) {
        this.nectar = nectar;
    }

    public boolean hasStung() {
        return stung;
    }

    public void setHasStung(boolean stung) {
        this.stung = stung;
    }

    public int getAnger() {
        return anger;
    }

    public void setAnger(int anger) {
        this.anger = anger;
    }
}
