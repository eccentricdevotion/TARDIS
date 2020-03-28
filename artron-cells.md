---
layout: default
title: Artron Storage Cells
---

# Artron Storage Cells

You can store Artron Energy in a specially crafted bucket so that you can have energy in reserve, or transfer energy to another TARDIS.

Storing Artron Energy requires a player to have the `tardis.store` permission.

### Crafting the Artron Storage Cell

To see the crafting recipe for Artron Storage Cells use the command:

    /tardisrecipe cell

### Artron Storage command

The crafting recipe provides you with an _empty_ Artron Storage Cell. You can transfer energy either from your TARDIS, or using your own Time Lord energy. To do this you hold the Artron Storage Cell in your hand and use the `/tardisartron` command:

    /tardisartron [tardis|timelord] [amount]

The maximum amount you can transfer is the configured `full_charge` value as specified in artron.yml (default is 5000). To store more energy, you will need to craft more cells.

<!--<h3 id="video">Video</h3>
		<iframe src="https://player.vimeo.com/video/82537488" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>-->