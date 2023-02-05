---
layout: default
title: The Chameleon Circuit
---

# The Chameleon Circuit

The Chameleon Circuit has changed in TARDIS version 3.7 and higher. The old Chameleon Circuit documentation can be
found [here](chameleon-circuit-legacy.html).

To make your TARDIS Police Box blend in with its surroundings you can use the Chameleon Circuit.
The Chameleon Circuit will be created automatically when you make a new TARDIS, and can be accessed from the
[TARDIS Control Menu](control-menu.html) sign.

![TARDIS Control Menu](images/chameleon/control_menu.jpg)

## Using the circuit

To use the Chameleon Circuit, right-click on the Control Menu sign, then click the Chameleon Circuit button.

![TARDIS Control Menu](images/chameleon/new_circuit.jpg)

The Chameleon GUI opens, giving you a range of options. Hover over each button to see what it does. The button functions
are explained below:

| Button            | Icon                                                                                                                                          | Action                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                          |
|-------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Apply             | ![apply](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/apply_button.png)       | Use this to rebuild the TARDIS exterior with the current Chameleon settings.                                                                                                                                                                                                                                                                                                                                                                                                                                                                    |
| Chameleon Circuit | ![cham](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png)    | Toggle the Chameleon Circuit on or off. When OFF, the TARDIS exterior will revert to the FACTORY preset.                                                                                                                                                                                                                                                                                                                                                                                                                                        |
| Adaptive          | ![adapt](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/adapt_button.png)     | Use this to make the Chameleon Circuit blend in with its environment. There are two modes you can choose from: BIOME and BLOCK. If set to BIOME the TARDIS will choose a Chameleon preset suited to the biome that the TARDIS lands in (for example the swamp hut preset for the SWAMP biome). If set to BLOCK, the TARDIS will scan the block at the next landing location and then change the wall block of the TARDIS to the same kind of block. BLOCK mode will only run if the previous Chameleon preset was set to NEW, OLD or SUBMERGED. |
| Lock              | ![lock](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/lock_button.png)       | Lock in the current adaptive BIOME preset.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      |
| Invisible         | ![invsbl](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/invisible_button.png)  | Make the TARDIS [invisible](invisibility.html).                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 |
| Shorted out       | ![short](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/shorted_button.png)   | Make the Chameleon Circuit malfunction and always choose the same appearance, for example: a Police Box. Clicking this button will open the Chameleon [Presets](presets.html) GUI. Selecting one of the presets will change the appearance of the TARDIS exterior next time you time travel. To change the appearance immediately, use the Apply button.                                                                                                                                                                                        |
| Construct         | ![build](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/chameleon/construct_button.png) | Create your own custom preset using the [Chameleon construction GUI](chameleon-construction.html).                                                                                                                                                                                                                                                                                                                                                                                                                                              |
| Close             | ![close](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/close.png)                      | Close the Chameleon Circuit GUI.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |

Underneath the buttons are indicators for the current Chameleon settings, hover the mouse over the items to view details.
A lime green wool block indicates the currently selected setting.

### Locking the Chameleon Circuit

If you have been using the Chameleon Circuit in _Adaptive BIOME_ mode, you can lock in the currently used preset by pressing the **Lock** button in the Chameleon GUI. The button will only appear if you are in the correct mode.

Clicking the button will switch the settings to _Shorted out_ and apply the biome preset on a more permanent basis (it won't change when you go yto a differnet biome).

![Chameleon lock](images/docs/chameleon_lock.jpg)
