/*
 * Copyright (C) 2026 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.handles.wiki;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.dialog.DialogResponseView;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlesWikiDialogProcessor {

    private final TARDIS plugin;

    public HandlesWikiDialogProcessor(TARDIS plugin) {
        this.plugin = plugin;
    }

    private static Set<WikiLink> getWikiLinks(DialogResponseView response) {
        String query = response.getText("search");
        Set<WikiLink> results = new HashSet<>();
        if (query != null) {
            Pattern pattern = Pattern.compile(query, Pattern.CASE_INSENSITIVE);
            // <a title="Dev commands" href="/commands/dev">Dev commands</a>
            try {
                Document doc = Jsoup.connect("https://tardis.pages.dev/site-map").get();
                Elements links = doc.select("a");
                for (Element e : links) {
                    String linkHref = e.attr("href"); // "/commands/dev"
                    String linkText = e.text(); // "Dev commands"
                    Matcher mat = pattern.matcher(linkText);
                    if (mat.find()) {
                        results.add(new WikiLink(linkText, linkHref));
                    }
                }
            } catch (IOException ignored) {
            }
        }
        return results;
    }

    public void getLinks(DialogResponseView response, Player player) {
        Set<WikiLink> results = getWikiLinks(response);
        // build a custom results dialog
        List<ActionButton> actions = new ArrayList<>();
        if (!results.isEmpty()) {
            for (WikiLink w : results) {
                try {
                    URI uri = URI.create(w.getURL());
                    DialogAction action = DialogAction.staticAction(ClickEvent.openUrl(uri.toURL()));
                    ActionButton button = ActionButton.create(Component.text(w.getTitle()), null, 150, action);
                    actions.add(button);
                } catch (MalformedURLException ignored) {
                }
            }
        } else {
            DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                    audience -> audience.showDialog(new SearchDialog().create())
            ));
            ActionButton yesButton = ActionButton.create(Component.text("No results"), null, 150, action);
            actions.add(yesButton);
        }
        List<DialogBody> body = List.of(DialogBody.plainMessage(Component.text("Search results:"), 200));
        DialogBase dialogData = DialogBase.create(Component.text("TARDIS Wiki"), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new SearchDialog().create())
        ));
        ActionButton yesButton = ActionButton.create(Component.text("Back"), null, 150, action);
        Dialog dialog = Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.multiAction(actions, yesButton, 1))
        );
        Audience receiver = Audience.audience(player);
        receiver.showDialog(dialog);
    }
}
