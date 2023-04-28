---
layout: default
title: TARDIS Item Display custom blocks
---

# TARDIS Item Display custom blocks

From TARDIS v5.0.0 the plugin now uses Minecraft's Item Display entities to fake custom TARDIS blocks and lights 
(instead of unused mushroom blocks). The benefit of this is that if the TARDIS Resource Pack is not installed, the 
custom block's appearance will default to a regular Minecraft block instead of a random mushroom block.

![Custom block resource pack comparison](images/docs/resource_pack_comparison.jpg)

As these custom blocks are actually entities, interacting and breaking them is different to regular blocks.

## Blocks

To break a custom TARDIS block:

- In SURVIVAL gamemode you need use a pickaxe.
- You need to __LEFT-click__ the block multiple times in SURVIVAL in order to break the block.
- If you have the TARDIS Resource Pack installed you will see the break animation - if not, the block will 
visually change to a gravel block before breaking and dropping the relevant item. 
- In CREATIVE, the block will just break and not drop an item.

## Lights

To break a custom TARDIS light:

- In SURVIVAL gamemode you need use a pickaxe.
- You need to __RIGHT-click__ the block multiple times in SURVIVAL in order to break the block.
- If you have the TARDIS Resource Pack installed you will see the break animation - if not, the block will
  visually change to a gravel block before breaking and dropping the relevant item.
- In CREATIVE, the block will just break and not drop an item.