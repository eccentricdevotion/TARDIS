package me.eccentric_nz.TARDIS.skins;

import java.io.Serializable;

public record Skin(String name, String value, String url, String signature, boolean slim) implements Serializable {

    public Skin(String name, String value, String url, String signature) {
        this(name, value, url, signature,false);
    }
}
