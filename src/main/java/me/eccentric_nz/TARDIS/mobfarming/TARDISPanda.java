package me.eccentric_nz.TARDIS.mobfarming;

import org.bukkit.entity.Panda;

public class TARDISPanda extends TARDISMob {

    private Panda.Gene mainGene;
    private Panda.Gene hiddenGene;

    public Panda.Gene getMainGene() {
        return mainGene;
    }

    public void setMainGene(Panda.Gene mainGene) {
        this.mainGene = mainGene;
    }

    public Panda.Gene getHiddenGene() {
        return hiddenGene;
    }

    public void setHiddenGene(Panda.Gene hiddenGene) {
        this.hiddenGene = hiddenGene;
    }
}
