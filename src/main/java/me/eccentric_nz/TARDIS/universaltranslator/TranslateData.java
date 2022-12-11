package me.eccentric_nz.TARDIS.universaltranslator;

public class TranslateData {

    String sender;
    Language from;
    Language to;

    public TranslateData(Language from, Language to, String sender) {
        this.from = from;
        this.to = to;
        this.sender = sender;
    }

    public Language getFrom() {
        return from;
    }

    public Language getTo() {
        return to;
    }

    public String getSender() {
        return sender;
    }
}
