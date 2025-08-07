package me.eccentric_nz.TARDIS.info.dialog;

import io.papermc.paper.dialog.Dialog;
import io.papermc.paper.registry.data.dialog.ActionButton;
import io.papermc.paper.registry.data.dialog.DialogBase;
import io.papermc.paper.registry.data.dialog.action.DialogAction;
import io.papermc.paper.registry.data.dialog.body.DialogBody;
import io.papermc.paper.registry.data.dialog.type.DialogType;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import me.eccentric_nz.TARDIS.info.TISCategory;
import me.eccentric_nz.TARDIS.info.processors.SectionProcessor;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SectionDialog {

    public Dialog create(TISCategory category) {
        List<DialogBody> body = List.of(DialogBody.plainMessage(Component.text(category.getLore().replace("~", "\n")), 150));
        DialogBase dialogData = DialogBase.create(Component.text(category.getName()), null, true, true, DialogBase.DialogAfterAction.CLOSE, body, List.of());
        List<ActionButton> actions = new ArrayList<>();
        for (TARDISInfoMenu tardisInfoMenu : TARDISInfoMenu.values()) {
            if (category == TISCategory.ITEMS && tardisInfoMenu.isItem()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.CONSOLE_BLOCKS && tardisInfoMenu.isConsoleBlock()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.ACCESSORIES && tardisInfoMenu.isAccessory()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.COMPONENTS && tardisInfoMenu.isComponent()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.SONIC_COMPONENTS && tardisInfoMenu.isSonicComponent()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.SONIC_UPGRADES && tardisInfoMenu.isSonicUpgrade()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.CONSOLES && tardisInfoMenu.isConsole()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.DISKS && tardisInfoMenu.isDisk()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.ROOMS && tardisInfoMenu.isRoom()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.PLANETS && tardisInfoMenu.isPlanet()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.TIME_TRAVEL && tardisInfoMenu.isTimeTravel()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.FOOD && tardisInfoMenu.isFood()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.UPDATEABLE_BLOCKS && tardisInfoMenu.isUpdateable()) {
                actions.add(makeButton(tardisInfoMenu));
            } else if (category == TISCategory.MONSTERS && tardisInfoMenu.isMonster()) {
                actions.add(makeButton(tardisInfoMenu));
            }
        }
        DialogAction action = DialogAction.staticAction(ClickEvent.callback(
                audience -> audience.showDialog(new CategoryDialog().create())
        ));
        ActionButton exitButton = ActionButton.create(Component.text("Back"), null, 150, action);
        return Dialog.create(builder -> builder.empty()
                .base(dialogData)
                .type(DialogType.multiAction(actions, exitButton, 2))
        );
    }

    private ActionButton makeButton(TARDISInfoMenu tardisInfoMenu) {
        DialogAction action = DialogAction.customClick((response, audience) -> {
                    Player player = audience instanceof Player ? (Player) audience : null;
                    new SectionProcessor(TARDIS.plugin, player).showDialog(tardisInfoMenu.toString());
                },
                ClickCallback.Options.builder().build()
        );
        return ActionButton.create(Component.text(TARDISStringUtils.capitalise(tardisInfoMenu.toString())), null, 150, action);
    }
}
