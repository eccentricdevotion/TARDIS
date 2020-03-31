---
layout: default
title: PlaceholderAPI Placeholders
---

# PlaceholderAPI Placeholders

TARDIS does in fact hook into PlaceholderAPI, here are the placeholders it supports:

| Placeholder                       | What it does                           |
| --------------------------------- | -------------------------------------- |
| `%tardis_artron_amount%`          | Gets your current artron amount        |
| `%tardis_artron_percent%`         | Gets your current artron percentage    |
| `%tardis_timelord_artron_amount%` | Gets your Time-Lord Artron amount      |
| `%tardis_console%`                | Gets the TARDIS schematic/console name |
| `%tardis_preset%`                 | Gets your TARDIS Preset                |

These are located in the [TARDISPlaceholderExpansion.java](https://github.com/eccentricdevotion/TARDIS/blob/v4.0/src/main/java/me/eccentric_nz/TARDIS/placeholders/TARDISPlaceholderExpansion.java#L57-L105) file.

Got suggestions for more? Feel free to [make an issue about it on github](https://github.com/eccentricdevotion/TARDIS/issues/new?assignees=&labels=&template=feature_request.md&title=Add more placeholders!)
