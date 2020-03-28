---
layout: default
title: Artron Energy configuration option
---

# Artron Energy configuration option

These are the Artron Energy configuration options. These config options are found in the file: `artron.yml`

For Artron Energy room growing costs see the [Room configuration](configuration-rooms.html) page.

## Full charge

Set the amount of energy a fully charged TARDIS Artron Energy Capacitor holds.

    full_charge: [amount]

The default is `5000`.

Set the item that fully charges the Energy Capacitor when you right-click the Artron button with it.

    full_charge_item: [material]

The default is `NETHER_STAR`.

## Time Lord energy

Set the amount of energy a player absorbs from the time vortex each time they time travel.

    player: [amount]

The default is `25`.

## Time travel energy costs

Set the amount of energy the different forms of time travel / vortex manipulation / room use consume. The options are: random locations, any `/tardistravel`, `/tardis comehere`, `/tardis hide` commands, autonomous homing and using the renderer or zero rooms

    player: [amount] - default: 25
    random: [amount] - default: 75
    travel: [amount] - default: 100
    comehere: [amount] - default: 400
    hide: [amount] - default: 500
    autonomous: [amount] - default: 100
    renderer: [amount] - default: 250
    zero: [amount] - default: 250

## Inter-dimensional travel

Set the minimum amount of energy the TARDIS Artron Energy Capacitor must have before players can time travel to the Nether or The End.

    nether_min: [amount] - default: 4250
    the_end_min: [amount] - default: 5500

## Beacon recharge distance

Set the maximum distance in blocks, a Police Box can be from a recharge beacon in order to start recharging.

    recharge_distance: [blocks]

The default is `20`.

## Natural lightning recharge

Set the amount of energy TARDISes receive if they are within the `recharge_distance` of a natural lightning strike.

    lightning_recharge: [amount]

The default is `300`.

## Charged creeper bonus

Set the amount of energy players receive if they kill a charged creeper.

    creeper_recharge: [amount]

The default is `150`.

## Room jettisons

Set the percentage of energy the TARDIS gets back when a room is jettisoned.

    jettison: [percentage]

The default is `75`.

## Standby power mode

You can set how much energy and how often energy is used when the TARDIS is in standby power mode.

    standby: [amount]

The default is `5`.

    standby_time: [ticks]

The default is `6000` or every 5 minutes.

If `standby_time` is set to `0`, then no energy is removed in standby mode.

## Siege mode

Set siege mode related values.

    siege_transfer: [percent]
    siege_deplete: [amount]
    siege_creeper: [amount]
    siege_ticks: [ticks]

`siege_transfer` — the minimum amount of Time Lord energy needed to transfer to the Siege block. This is a percentage of `full_charge`. Default is 10% (500).

`siege_deplete` — the amount of energy needed Siege mode uses every cycle. Default is `100`.

`siege_creeper` — the amount of energy gained when the Artron Creeper is sacrificed. The default is `150`. Only relevant if the main config option `siege.creeper` is set to true.

`siege_ticks` — the amount of time in server ticks that the energy depletion / siege mode healing cycle takes. The default is `1500` (or every 1.25 minutes).

## TARDIS Upgrades

Set the energy cost to upgrade / crossgrade to a different console when using the Desktop Theme changer. If a player is just changing the walls and floor, then `just_wall_floor` sets the percentage the cost is reduced.

    upgrades:
        ars: [amount] - default: 5000
        budget: [amount] - default: 5000
        deluxe: [amount] - default: 10000
        eleventh: [amount] - default: 10000
        redstone: [amount] - default: 7500
        bigger: [amount] - default: 7500
        plank: [amount] - default: 5000
        steampunk: [amount] - default: 5000
        tom: [amount] - default: 5000
        twelfth: [amount] - default: 7500
        war: [amount] - default: 5000
        master: [amount] - default: 10000
        pyramid: [amount] - default: 5000
        ender: [amount] - default: 5000
        coral: [amount] - default: 8000
        custom: [amount] - default: 10000
        archive:
          tall: [amount] - default: 10000
          small: [amount] - default: 5000
          medium: [amount] - default: 7500
        template:
          medium: [amount] - default: 2500
          small: [amount] - default: 1666
          tall: [amount] - default: 3333
        legacy_deluxe: [amount] - default: 10000
        legacy_budget: [amount] - default: 5000
        legacy_redstone: [amount] - default: 8000
        legacy_bigger: [amount] - default: 7500
        legacy_eleventh: [amount] - default: 10000
    just_wall_floor: [percentage] - default: 50

## Artron Furnaces

Set the options for Artron Furnaces. See further explanation on the [Artron Furnace](artron-furnace.html) page.

    artron_furnace:
      set_biome: true
      cook_time: 0.5
      burn_time: 0.5
      burn_limit: 100000
      particles: true

## Sonic Generator

Set the energy costs for the [sonic generator](sonic-generator.html) as a percentage of the `full_charge` option.

    sonic_generator:
      painter: 10
      emerald: 10
      standard: 10
      ignite: 10
      redstone: 10
      bio: 10
      diamond: 10

# Rechargers configuration section

**NB:** The rechargers configuration section is found in the file: `config.yml`

The rechargers section holds the locations of the TARDIS recharge beacons.

The format is:

    rechargers:
       [name]:
          world: [world]
          x: [co-ordinate]
          y: [co-ordinate]
          z: [co-ordinate]

Entries are added automatically by the `/tardisadmin recharger [name]` command, you should only need to edit this section if you want to remove a recharge point.

[Back to main configuration page](configuration.html)

