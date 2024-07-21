package me.eccentric_nz.TARDIS.skins;

public record Skin(String name, String value, String url, String signature, boolean slim) {

    public Skin(String name, String value, String url, String signature) {
        this(name, value, url, signature,false);
    }
}
