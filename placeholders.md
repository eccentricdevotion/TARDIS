---
layout: default
title: PlaceholderAPI Placeholders
---

# PlaceholderAPI Placeholders

TARDIS hooks into PlaceholderAPI, here are the placeholders it supports:

_Current Location placeholders have been added in build #2049_

| Placeholder                           | What it does                                                                |
| ------------------------------------- | --------------------------------------------------------------------------- |
| `%tardis_artron_amount%`              | Gets your TARDIS's current Artron amount                                    |
| `%tardis_artron_percent%`             | Gets your TARDIS's current Artron filled percentage                         |
| `%tardis_timelord_artron_amount%`     | Gets your current Time-Lord Artron amount                                   |
| `%tardis_console%`                    | Gets the TARDIS schematic/console name                                      |
| `%tardis_preset%`                     | Gets your TARDIS Preset                                                     |
| `%tardis_ars_status%`                 | Gets the status of the in-progress ARS task                                 |
| `%tardis_in_any%`                     | Gets whether you're in any TARDIS or not                                    |
| `%tardis_in_own%`                     | Gets whether you're in your TARDIS or not                                   |
| `%tardis_in_whose%`                   | Gets who's TARDIS you're in.                                                |
| `%tardis_in_user_<username>%`         | Gets whether you're in <username>'s TARDIS or not                           |
| `%tardis_current_location%`           | Gets your TARDIS's Current Location as a String (similar to `/tardis find`) |
| `%tardis_current_location_x%`         | Gets your TARDIS's Current "x" Location                                     |
| `%tardis_current_location_y%`         | Gets your TARDIS's Current "y" Location                                     |
| `%tardis_current_location_z%`         | Gets your TARDIS's Current "z" Location                                     |
| `%tardis_current_location_biome%`     | Gets your TARDIS's Current Location's Biome                                 |
| `%tardis_current_location_world%`     | Gets your TARDIS's Current Location's World Name                            |
| `%tardis_current_location_direction%` | Gets your TARDIS's Current Direction                                        |

These are located in the [TARDISPlaceholderExpansion.java](https://github.com/eccentricdevotion/TARDIS/blob/v4.0/src/main/java/me/eccentric_nz/TARDIS/placeholders/TARDISPlaceholderExpansion.java#L60-L180) file.

Got suggestions for more? Feel free to [create an issue about it on GitHub](<https://github.com/eccentricdevotion/TARDIS/issues/new?assignees=&labels=&template=feature_request.md&title=Add more placeholders>)!
