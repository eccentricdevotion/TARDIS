---
layout: default
title: The Control Menu
---

# The TARDIS Control Menu

From TARDIS version 3.6 and higher, the TARDIS Control Menu is the main way to access the TARDIS’s functions. Previously 
the TARDIS had multiple signs scattered throughout the console to access its various GUIs. The TARDIS Control Menu brings 
them all together in one place.

The TARDIS Control Menu sign displays information about the TARDIS’s location and Artron Energy levels.

![TARDIS Control Menu sign](images/docs/control_menu_sign.jpg)

If you have an existing TARDIS without the Control Menu, you can add it by placing a sign where you want the Control 
Menu to be, and running the command:

    /tardis update control

Click on the sign and its position will be added to the TARDIS database.

## Control Menu use

To use the TARDIS Control Menu, right-click on the Control Menu sign.

![TARDIS Control Menu](images/docs/control_menu.jpg)

The Control Menu opens, giving you a range of buttons grouped by type / colour. Hover over each button to see what it 
does. The button functions are explained below:

| Button                               | Icon                                                                                                                                          | Action                                                                                                   |
|--------------------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------|
| TARDIS Travel functions              |
| ---                                  |
| Random Location                      | ![rand](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/random_button.png)       | Sets a random destination based on the position of the console _world, x, z_ and _multiplier_ repeaters. |
| Saved Locations                      | ![saves](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/saves_button.png)       | Opens the TARDIS Saves GUI.                                                                              |
| Fast Return                          | ![back](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/back_button.png)         | Sets the destination to the last location the TARDIS travelled to.                                       |
| TARDIS Areas                         | ![areas](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/areas_button.png)       | Opens the TARDIS Areas GUI.                                                                              |
| Destination Terminal                 | ![dest](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/dest_terminal.png)       | Opens the TARDIS Areas GUI.                                                                              |
| TARDIS Interior functions            |
| ---                                  |
| Architectural Reconfiguration System | ![ars](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/ars_button.png)           | Opens the Architectural Reconfiguration GUI.                                                             |
| Desktop Theme                        | ![theme](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/theme_button.png)       | Opens the Desktop Theme GUI.                                                                             |
| Power                                | ![power](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/power_on.png)           | Toggles the TARDIS power on and off.                                                                     |
| Light Switch                         | ![lights](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/on_switch.png)         | Toggles the TARDIS lamps on and off.                                                                     |
| Toggle blocks behind door            | ![toggle](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/toggle_open.png)       | Removes or places the wool behind the TARDIS door.                                                       |
| TARDIS Map                           | ![map](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/map_button.png)           | Opens the TARDIS Map GUI.                                                                                |
| TARDIS Exterior functions            |
| ---                                  |
| Chameleon Circuit                    | ![cham](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/chameleon_button.png)    | Opens the Chameleon Circuit GUI.                                                                         |
| Siege Mode                           | ![siege](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/siege_on.png)           | Toggles Siege Mode on and off.                                                                           |
| Hide                                 | ![hide](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/hide_button.png)         | Hides the TARDIS exterior.                                                                               |
| Rebuild                              | ![rebuild](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/rebuild_button.png)   | Rebuilds the TARDIS exterior.                                                                            |
| Direction                            | ![nwse](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/direction_button.png)    | Changes the direction the TARDIS exterior faces.                                                         |
| Temporal Locator                     | ![temporal](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/temporal_button.png) | Opens the Temporal Locator GUI.                                                                          |
| TARDIS Information functions         |
| ---                                  |
| Artron Energy Levels                 | ![artron](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/artron_button.png)     | Displays the current TARDIS Artron Energy levels.                                                        |
| Scanner                              | ![scan](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/scan_button.png)         | Performs a scan of the TARDIS current (or next if set) destination.                                      |
| TARDIS Information System            | ![hide](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/info_button.png)         | Opens the TARDIS Information System chat interface.                                                      |
| Transmat                             | ![hide](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/transmat_button.png)     | Opens the TARDIS Transmat locations GUI.                                                                 |
| Miscellaneous functions              |
| ---                                  |
| Zero Room transmat                   | ![artron](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/zero_button.png)       | Transmats the player into the Zero Room.                                                                 |
| Player Preferences                   | ![scan](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/prefs_button.png)        | Opens the Player Prefs Menu GUI.                                                                         |
| Companions                           | ![scan](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/control/companions_button.png)   | Opens the Companions GUI.                                                                                |
| Close                                | ![close](https://github.com/eccentricdevotion/TARDIS-Resource-Pack/raw/master/assets/tardis/textures/item/gui/close.png)                      | Close the TARDIS Control Menu GUI.                                                                       |

