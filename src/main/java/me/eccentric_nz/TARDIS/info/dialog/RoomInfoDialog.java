package me.eccentric_nz.TARDIS.info.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RoomInfoDialog {

    public Dialog create(TARDIS plugin, TARDISInfoMenu tardisInfoMenu) {
        TARDISDescription description = TARDISDescription.valueOf(tardisInfoMenu.toString());
        String r = tardisInfoMenu.toString();
        List<DialogBody> body = new ArrayList<>();
        if (ItemLookup.ITEMS.containsKey(tardisInfoMenu)) {
            InfoIcon infoIcon = ItemLookup.ITEMS.get(tardisInfoMenu);
            ItemStack icon = ItemStack.of(infoIcon.item());
            // set custom name
            ItemMeta im = icon.getItemMeta();
            im.displayName(Component.text(infoIcon.name()));
            icon.setItemMeta(im);
            body.add(DialogBody.item(icon, null, false, false, 16, 16));
        }
        Component desc = Component.text(description.getDesc());
        Component seed = Component.text("Seed Block: ", NamedTextColor.GOLD).append(Component.text(plugin.getRoomsConfig().getString("rooms." + r + ".seed", "Unknown"), NamedTextColor.WHITE));
        Component offset = Component.text("Offset: ", NamedTextColor.GOLD).append(Component.text(plugin.getRoomsConfig().getString("rooms." + r + ".offset", "Unknown"), NamedTextColor.WHITE));
        Component cost = Component.text("Cost: ", NamedTextColor.GOLD).append(Component.text(plugin.getRoomsConfig().getString("rooms." + r + ".cost", "Unknown"), NamedTextColor.WHITE));
        Component enabled = Component.text("Enabled: ", NamedTextColor.GOLD).append(Component.text(plugin.getRoomsConfig().getString("rooms." + r + ".enabled", "Unknown"), NamedTextColor.WHITE));
        body.add(DialogBody.plainMessage(desc, 200));
        body.add(DialogBody.plainMessage(seed, 200));
        body.add(DialogBody.plainMessage(offset, 200));
        body.add(DialogBody.plainMessage(cost, 200));
        body.add(DialogBody.plainMessage(enabled, 200));
        String title = TARDISStringUtils.capitalise(r.replace("_", " "));
        DialogBase dialogData = DialogBase.create(Component.text(title), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new SectionDialog().create(TISCategory.ROOMS))
        ));
        ActionButton yesButton = ActionButton.create(Component.text("Back"), null, 150, action);
        ActionButton noButton = ActionButton.create(Component.text("Done"), null, 150, null);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.confirmation(yesButton, noButton))
        );
    }
}
