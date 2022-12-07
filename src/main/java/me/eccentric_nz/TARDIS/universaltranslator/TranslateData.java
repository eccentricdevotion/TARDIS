package me.eccentric_nz.TARDIS.universaltranslator;

public class TranslateData {

    String sender;
    Language from;
    Language to;

    public TranslateData(Language to, Language from, String sender) {
        this.to = to;
        this.from = from;
        this.sender = sender;
    }

    public Language getTo() {
        return to;
    }

    public Language getFrom() {
        return from;
    }

    public String getSender() {
        return sender;
    }
}
