---
layout: default
title: Big list of permissions
---

<style>
#child>tbody>tr>td,
#child>tbody>tr>th {
  border-top: none!important;
  border: 0px;
}
#child {
  margin-top: -53px;
  border-bottom: 1px solid #aaa;
  margin-bottom: 0px;
}
#child>tbody>tr>th {
  margin-top: 100px;
}
</style>

# Permission List

Permission Node | Description | Default
----- | ---- | ----
`tardis.admin` | Allow players access to the TARDIS admin commands. | op
`tardis.delete` | Allow players to delete any TARDIS. | false
`tardis.abandon` | Allow players to abandon their TARDIS. | op
`tardis.archive` | Allow players to archive their TARDIS console. | op
`tardis.repair` | Allow players to repair or clean their TARDIS console. | op
`tardis.skeletonkey` | Allow players to enter any TARDIS. | false
`tardis.remote` | Allow players to remote control a TARDIS. | op
`tardis.use` | Allow players to create & delete a TARDIS, and teleport to destinations. | op

{:#child}
|  |
| ---------- | -------------------- | ------- |
|            | `tardis.budget`      | true    |
|            | `tardis.create`      | true    |
|            | `tardis.exterminate`      | true    |
|            | `tardis.timetravel`      | true    |
|            | `tardis.list`      | true    |
|            | `tardis.save`      | true    |
|            | `tardis.enter`      | true    |
|            | `tardis.find`      | true    |
|            | `tardis.add`      | true    |
| Children of `tardis.use` | `tardis.help`      | true    |
|            | `tardis.update`      | true    |
|            | `tardis.rebuild`      | true    |
|            | `tardis.book`      | true    |
|            | `tardis.gravity`      | true    |
|            | `tardis.texture`      | true    |
|            | `tardis.temporal`      | true    |
|            | `tardis.advanced`      | true    |
|            | `tardis.storage`      | true    |
|            | `tardis.vault`      | true    |
|            | `tardis.preset.*`      | true    |

`tardis.preset.*` | Allow players to use all exterior Chameleon Presets. | op

{:#child}
|  |
| ---------- | -------------------- | ------- |
|            | `tardis.preset.andesite`      | true    |
|            | `tardis.preset.angel`      | true    |
|            | `tardis.preset.apperture`      | true    |
|            | `tardis.preset.cake`      | true    |
|            | `tardis.preset.candy`      | true    |
|            | `tardis.preset.chalice`      | true    |
|            | `tardis.preset.chorus`      | true    |
|            | `tardis.preset.creepy`      | true    |
|            | `tardis.preset.desert`      | true    |
|            | `tardis.preset.diorite`      | true    |
|            | `tardis.preset.duck`      | true    |
|            | `tardis.preset.fence`      | true    |
|            | `tardis.preset.flower`      | true    |
|            | `tardis.preset.gazebo`      | true    |
|            | `tardis.preset.granite`      | true    |
|            | `tardis.preset.gravestone`      | true    |
|            | `tardis.preset.helix`      | true    |
|            | `tardis.preset.jail`      | true    |
|            | `tardis.preset.jungle`      | true    |
|            | `tardis.preset.lamp`      | true    |
|            | `tardis.preset.library`      | true    |
|            | `tardis.preset.lighthouse`      | true    |
|            | `tardis.preset.mineshaft`      | true    |
|            | `tardis.preset.nether`      | true    |
|            | `tardis.preset.new`      | true    |
|            | `tardis.preset.old`      | true    |
|            | `tardis.preset.pandorica`      | true    |
|            | `tardis.preset.party`      | true    |
|            | `tardis.preset.peanut`      | true    |
|            | `tardis.preset.pine`      | true    |
|            | `tardis.preset.portal`      | true    |
| Children of `tardis.preset.*` | `tardis.preset.prismarine`      | true    |
|            | `tardis.preset.punked`      | true    |
|            | `tardis.preset.robot`      | true    |
|            | `tardis.preset.shroom`      | true    |
|            | `tardis.preset.snowman`      | true    |
|            | `tardis.preset.stone`      | true    |
|            | `tardis.preset.submerged`      | true    |
|            | `tardis.preset.swamp`      | true    |
|            | `tardis.preset.telephone`      | true    |
|            | `tardis.preset.toilet`      | true    |
|            | `tardis.preset.topsyturvey`      | true    |
|            | `tardis.preset.torch`      | true    |
|            | `tardis.preset.village`      | true    |
|            | `tardis.preset.well`      | true    |
|            | `tardis.preset.windmill`      | true    |
|            | `tardis.preset.yellow`      | true    |
|            | `tardis.preset.police_box_blue`      | true    |
|            | `tardis.preset.police_box_white`      | true    |
|            | `tardis.preset.police_box_orange`      | true    |
|            | `tardis.preset.police_box_magenta`      | true    |
|            | `tardis.preset.police_box_light_blue`      | true    |
|            | `tardis.preset.police_box_yellow`      | true    |
|            | `tardis.preset.police_box_lime`      | true    |
|            | `tardis.preset.police_box_pink`      | true    |
|            | `tardis.preset.police_box_gray`      | true    |
|            | `tardis.preset.police_box_light_gray`      | true    |
|            | `tardis.preset.police_box_cyan`      | true    |
|            | `tardis.preset.police_box_purple`      | true    |
|            | `tardis.preset.police_box_brown`      | true    |
|            | `tardis.preset.police_box_green`      | true    |
|            | `tardis.preset.police_box_red`      | true    |
|            | `tardis.preset.police_box_black`      | true    |

`tardis.exile` | Disallow players from travelling anywhere but to areas they have a permission node for. | false
`tardis.create` | Allow players to create a TARDIS. | op
`tardis.create_world` | Allow players to create a TARDIS in it's own world. This is only used in conjuction with the create_worlds_with_perms config option. | op
`tardis.exterminate` | Allow players to delete their own TARDIS. | op
`tardis.timetravel` | Allow players to travel to random locations and saved destinations. | op
`tardis.timetravel.player` | Allow players to travel to player locations. | op
`tardis.timetravel.location` | Allow players to travel to coordinates. | op
`tardis.timetravel.biome` | Allow players to travel to specific biomes. | op
`tardis.timetravel.cave` | Allow players to travel to caves. | op
`tardis.timetravel.village` | Allow players to travel to villages. | op
`tardis.list` | Allow players to list saved time travel destinations. | op
`tardis.save` | Allow players to save time travel destinations. | op
`tardis.home` | Allow players to save a time travel destination as their home. | op
`tardis.enter` | Allow players to enter a TARDIS. | op
`tardis.find` | Allow players to find their TARDIS. | op
`tardis.add` | Allow players to add/remove companions to their TARDIS. | op
`tardis.help` | Allow players to view TARDIS help pages. | true
`tardis.update` | Allow players to modify the TARDIS interior. | true
`tardis.rebuild` | Allow players to rebuild the TARDIS. | true
`tardis.bigger` | Allow players to build the 'bigger' TARDIS. | op
`tardis.deluxe` | Allow players to build the 'deluxe' TARDIS. | op
`tardis.eleventh` | Allow players to build the 'eleventh' TARDIS. | op
`tardis.twelfth` | Allow players to build the 'twelfth' TARDIS. | op
`tardis.redstone` | Allow players to build the 'redstone' TARDIS. | op
`tardis.steampunk` | Allow players to build the 'steampunk' TARDIS. | op
`tardis.ars` | Allow players to build the 'Architectural Reconfiguration System' TARDIS. | op
`tardis.war` | Allow players to build the 'War Doctor's' TARDIS. | op
`tardis.pyramid` | Allow players to build the Sandstone Pyramid TARDIS. | op
`tardis.master` | Allow players to build the Master's TARDIS. | op
`tardis.ender` | Allow players to build the Ender TARDIS. | op
`tardis.coral` | Allow players to build the Tenth Doctor's coral TARDIS. | op
`tardis.legacy_budget` | Allow players to build the legacy 'budget' TARDIS. | op
`tardis.legacy_bigger` | Allow players to build the legacy 'bigger' TARDIS. | op
`tardis.legacy_deluxe` | Allow players to build the legacy 'deluxe' TARDIS. | op
`tardis.legacy_eleventh` | Allow players to build the legacy 'eleventh' TARDIS. | op
`tardis.legacy_redstone` | Allow players to build the legacy 'redstone' TARDIS. | op
`tardis.storage` | Allow players to utilise the TARDIS Disk Storage. | op
`tardis.advanced` | Allow players to utilise the TARDIS Advanced Console. | op
`tardis.architectural` | Allow players to utilise the TARDIS Architectural Reconfiguration System. | op
`tardis.kit.join` | Allow players to automatically get a TARDIS Item Kit when they join the server. | op
`tardis.kit.create` | Allow players to automatically get a TARDIS Item Kit when they create a TARDIS. | op
`tardis.plank` | Allow players to build the 'wood' TARDIS. | op
`tardis.tom` | Allow players to build the '4th Doctor's' TARDIS. | op
`tardis.custom` | Allow players to build the server's 'custom' TARDIS. | op
`tardis.end` | Allow players to time travel to the END. | op
`tardis.nether` | Allow players to time travel to the NETHER. | op
`tardis.room` | Allow players to grow extra rooms. | op

{:#child}
|  |
| ---------- | -------------------- | ------- |
|            | `tardis.room.antigravity`      | true    |
|            | `tardis.room.apiary`      | true    |
|            | `tardis.room.aquarium`      | true    |
|            | `tardis.room.arboretum`      | true    |
|            | `tardis.room.baker`      | true    |
|            | `tardis.room.bamboo`      | true    |
|            | `tardis.room.bedroom`      | true    |
|            | `tardis.room.birdcage`      | true    |
|            | `tardis.room.chemistry`      | true    |
|            | `tardis.room.empty`      | true    |
|            | `tardis.room.farm`      | true    |
|            | `tardis.room.gravity`      | true    |
|            | `tardis.room.greenhouse`      | true    |
|            | `tardis.room.harmony`      | true    |
|            | `tardis.room.hutch`      | true    |
|            | `tardis.room.igloo`      | true    |
| Children of `tardis.room` | `tardis.room.kitchen`      | true    |
|            | `tardis.room.lazarus`      | true    |
|            | `tardis.room.library`      | true    |
|            | `tardis.room.mushroom`      | true    |
|            | `tardis.room.passage`      | true    |
|            | `tardis.room.pool`      | true    |
|            | `tardis.room.rail`      | true    |
|            | `tardis.room.renderer`      | true    |
|            | `tardis.room.shell`      | true    |
|            | `tardis.room.smelter`      | true    |
|            | `tardis.room.stable`      | true    |
|            | `tardis.room.stall`      | true    |
|            | `tardis.room.trenzalore`      | true    |
|            | `tardis.room.vault`      | true    |
|            | `tardis.room.village`      | true    |
|            | `tardis.room.wood`      | true    |
|            | `tardis.room.workshop`      | true    |
|            | `tardis.room.zero`      | true    |

`tardis.jettison` | Allow players to use the /tardis jettison command. | op
`tardis.gravity` | Allow players to use the /tardisgravity command. | op
`tardis.farm` | Allow players to farm mobs. | op
`tardis.eject` | Allow players eject farmed mobs, villagers and companions. | op
`tardis.autonomous` | Allow players to use the TARDIS autonomous homing function. | op
`tardis.book` | Allow players to use TARDIS books and gain achievements. | op
`tardis.backdoor` | Allow players to add a TARDIS back door. | op
`tardis.texture` | Allow players to switch texture packs. | op
`tardis.temporal` | Allow players to set their player time (Temporal Location). | op
`tardis.prune.bypass` | Ignore /tardisadmin prune [days] command to remove unused TARDISes. | op
`tardis.sonic.standard` | Allows a player to use a basic Sonic Screwdriver. | op
`tardis.sonic.admin` | Allows the sonic screwdriver to be upgraded with Admin abilities. | op
`tardis.sonic.bio` | Allows the sonic screwdriver to be upgraded with Bio-scanner abilities. | op
`tardis.sonic.freeze` | Allows a player to use the freeze function of the sonic screwdriver with Bio-scanner. | op
`tardis.sonic.redstone` | Allows the sonic screwdriver to be upgraded with Redstone Activation abilities. | op
`tardis.sonic.diamond` | Allows the sonic screwdriver to be upgraded with Diamond Disruptor abilities. | op
`tardis.sonic.silktouch` | Allows the sonic screwdriver with Diamond Disruptor to drop 'silk touch' blocks. | op
`tardis.sonic.emerald` | Allows the sonic screwdriver to be upgraded with Emerald Environment abilities. | op
`tardis.sonic.plant` | Allows the sonic screwdriver with Emerald Environment abilities to re-sow plants if the player has the appropriate seed items in their inventory. | op
`tardis.sonic.paint` | Allows the sonic screwdriver to be upgraded and used with Painter abilities.. | op
`tardis.sonic.sort` | Allows a player to use the chest sorting function of the sonic screwdriver. | op
`tardis.sonic.ignite` | Allows a player to use the ignite function of the sonic screwdriver. | op
`tardis.sonic.arrow` | Allows a player to use the pickup arrows function of the sonic screwdriver. | op
`tardis.store` | Allows the Artron Energy to be stored in an Artron Storage Cell. | op
`tardis.filter` | Allows a player to use the TARDIS Perception Filter. | op
`tardis.translate` | Allows a player to use the TARDIS Universal Translator. | op
`tardis.lazarus` | Allows a player to use the TARDIS Genetic Manipulator. | op
`tardis.themaster` | Allows a player to reverse the polarity of the TARDIS Genetic Manipulator turning it into the Immortality Gate. | op
`tardis.chameleonarch` | Allows a player to use the Chameleon Arch. | op
`tardis.difficulty` | Allows a player to use the /tardisprefs difficulty command. | op
`tardis.vault` | Allows a player to have an automatic vault room chest sorter. | op
`tardis.upgrade` | Allows a player to upgrade their TARDIS. | op
`tardis.furnace` | Allows a player to have a TARDIS Artron Furnace. | op
`tardis.acid.bypass` | Allows a player to not dissolve in the Skaro planet's acid water. | false
`tardis.rift` | Allows a player to create personal time rift rechargers. | op
`tardis.atmospheric` | Allows a player to initiate atmospheric exitation around their TARDIS. | op
`tardis.transmat` | Allows a player to add internal transmat locations to their TARDIS. | op
`tardis.handles` | Allow players to use the Handles companion. | op

{:#child}
|  |
| ---------- | -------------------- | ------- |
|            | `tardis.handles.use`      | true    |
| Children of `tardis.handles` | `tardis.handles.communicator`      | true    |
|            | `tardis.handles.program`      | true    |

`tardis.paper_bag` | Allows a player to store and dispense Jelly Babies from a paper bag. | op
`tardis.gamemode.bypass` | Allows a player to bypass gamemode switching when changing worlds. | false
`tardis.chemistry.command` | Allow a player to open Chemistry GUIs by command. | op
`tardis.chemistry.creative` | Allow a player to craft and open the Chemistry creative GUIs. | op
`tardis.construct.build` | Allow a player to craft and open the 'Element constructor' GUI. | op
`tardis.compound.create` | Allow a player to craft and open the 'Chemical compounds' GUI. | op
`tardis.reducer.use` | Allow a player to craft and open the 'Material reducer' GUI. | op
`tardis.products.craft` | Allow a player to craft and open the 'Product crafting' GUI. | op
`tardis.lab.combine` | Allow a player to craft and open the 'Lab table' GUI. | op
`tardis.formula.show` | Allow a player to view a compound or product formula. | op
`tardis.chemistry.brew` | Allow a player to brew potions and cures using a cauldron. | op
`tardis.chemistry.cure` | Allow a player to cure poison, weakness, blindness and nausea effects. | op
`tardis.chemistry.glue` | Allow a player to turn pistons into sticky pistons with glue. | op
`tardis.tag` | Allow a player to broadcast Tag The Ood messages. | true
`tardis.forcefield` | Allow a player to turn on the TARDIS force field. | op
