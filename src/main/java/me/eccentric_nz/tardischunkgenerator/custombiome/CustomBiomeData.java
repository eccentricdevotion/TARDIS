package me.eccentric_nz.tardischunkgenerator.custombiome;

public class CustomBiomeData {

    private final String minecraftName;
    private final String customName;
    private final float temperature;
    private final float downfall;
    private final int fogColour;
    private final int waterColour;
    private final int waterFogColour;
    private final int skyColour;
    private final int foliageColour;
    private final int grassColour;
    private final boolean frozen;

    public CustomBiomeData(String minecraftName, String customName, float temperature, float downfall, int fogColour, int waterColour, int waterFogColour, int skyColour, int foliageColour, int grassColour, boolean frozen) {
        this.minecraftName = minecraftName;
        this.customName = customName;
        this.temperature = temperature;
        this.downfall = downfall;
        this.fogColour = fogColour;
        this.waterColour = waterColour;
        this.waterFogColour = waterFogColour;
        this.skyColour = skyColour;
        this.foliageColour = foliageColour;
        this.grassColour = grassColour;
        this.frozen = frozen;
    }

    public static int fromHex(String hex) {
        return Integer.parseInt(hex, 16);
    }

    public String getMinecraftName() {
        return minecraftName;
    }

    public String getCustomName() {
        return customName;
    }

    public float getTemperature() {
        return temperature;
    }

    public float getDownfall() {
        return downfall;
    }

    public int getFogColour() {
        return fogColour;
    }

    public int getWaterColour() {
        return waterColour;
    }

    public int getWaterFogColour() {
        return waterFogColour;
    }

    public int getSkyColour() {
        return skyColour;
    }

    public int getFoliageColour() {
        return foliageColour;
    }

    public int getGrassColour() {
        return grassColour;
    }

    public boolean isFrozen() {
        return frozen;
    }
}
