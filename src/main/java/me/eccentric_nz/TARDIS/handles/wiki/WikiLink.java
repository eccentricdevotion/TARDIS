package me.eccentric_nz.TARDIS.handles.wiki;

public class WikiLink {
    private final String title;
    private final String URL;

    public WikiLink(String title, String URL) {
        this.title = title;
        this.URL = "https://tardis.pages.dev" + URL;
    }

    public WikiLink(String title, String URL, boolean skin) {
        this.title = title;
        this.URL = URL;
    }

    public String getTitle() {
        return title;
    }

    public String getURL() {
        return URL;
    }
}
