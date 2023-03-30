---
layout: default
title: Sonic Blaster
---

# Sonic Blaster

This TARDIS module is a work in progress, and as such the documentation is far from complete!

A sonic blaster, or squareness gun, is a type of weapon available in the 51st century. They were produced at the
Villengard factory, until it was destroyed by the Doctor, and replaced by banana groves.

> Bananas are good

## Configuration

```yaml
# the maximum effective distance of the blaster
max_blocks: 10
# the amount of time in seconds between blaster uses
cooldown: 5
tachyon_use:
  # maximum charge the blaster can hold
  max: 1000
  # amoutn the blaster recharges when close to a TARDIS recharger
  recharge: 25
  # the time in ticks between adding recharge amount - 1200 = 60 seconds
  recharge_interval: 1200
  # the amount of tachyon energy to use per block removed
  remove_per_block: 10
  # the amount of tachyon energy to use to restore a removed block
  restore_per_block: 20
recipes:
  # recipe for the sonic blaster
  Sonic Blaster:
    shape: DTD,TST,EBE
    ingredients:
      D: DISPENSER
      T: TNT
      S: GLOWSTONE_DUST=Sonic Oscillator
      E: BUCKET=Blaster Battery
      B: OAK_BUTTON
    result: GOLDEN_HOE
    amount: 1
    lore: 'The Squareness Gun'
  # recipe for the blaster battery
  Blaster Battery:
    shape: -S-,-R-,-B-
    ingredients:
      S: ORANGE_STAINED_GLASS
      R: REDSTONE
      B: BUCKET
    result: BUCKET
    amount: 1
    lore: ''
  # recipe for the landing pad
  Landing Pad:
    shape: -C-,-S-,-R-
    ingredients:
      C: WHITE_CARPET
      S: SLIME_BLOCK
      R: REPEATER
    result: SLIME_BLOCK
    amount: 1
    lore: ''
```

## Use

> The blaster uses digital technology to create a sonic wave, projected into the form of pulsing squares of blue light,
> which could can through thick walls. It also has a reverse function which can replace the removed chunk of material
> afterwards. This is deemed a "special feature" of the blaster, and is said to use up a lot of the batteries on which
> it runs.

## Landing pad

Can't remember what this does...