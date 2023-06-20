---
layout: default
title: Bind command
---

# The Bind command

To make travelling to saved destinations, TARDIS areas, biomes, players and
internal transmat locations easier you can &lsquo;bind&rsquo; them to buttons,
levers and signs.

You can also bind the following commands:

```
/tardis hide
/tardis rebuild
/tardistravel home
```

### Binding

To bind the destination/command to a block run the command:

```
/tardisbind add save [save name]
/tardisbind add cmd [hide|rebuild|home|cave]
/tardisbind add player [player name]
/tardisbind add area [area name]
/tardisbind add biome [biome name]
/tardisbind add transmat [transmat name]
```

Follow the onscreen instructions, and click the block you want to bind.

### Unbinding

To unbind a destination/command from a block run the command:

```
/tardisbind remove [save|command|player|area|transmat]
```

You will receive a confirmation message that the block is now safe to be removed.
