---
layout: default
title: Time Rotor
---

# Time Rotor

If you are using the [TARDIS-Resource-Pack](http://tardisjenkins.duckdns.org:8080/job/TARDIS-Resource-Pack/) you can add an animated time rotor to your console (or grow a ROTOR console from a TARDIS seed block).

There are 4 time rotor types.

Early era

![Early era time rotor](images/docs/early_time_rotor.gif)

Ninth / tenth era

![Time rotor](images/docs/time_rotor.gif)

Eleventh era

![Eleventh era time rotor](images/docs/copper_time_rotor.gif)

Twelfth era (not animated)

![Twelfth era time rotor](images/docs/round_time_rotor.jpg)

### Adding a Time Rotor

To add a time rotor to an existing console:

1. Craft a TARDIS Time Rotor &mdash; see recipes below
2. Place an item frame in the desired location on the TARDIS console
3. Place the crafted time rotor into the item frame
4. Run the command `/tardis update rotor`
5. Click the time rotor item frame

### To change a Time Rotor

Time rotors are locked (and the item frame made invisible) when you update them. In order to make changes to the time 
rotor you need to unlock it.

1. Run the command `/tardis update rotor unlock`
2. Make changes to the item frame and rotor
3. Re-update the rotor with the command `/tardis update rotor`

### Recipes

The crafting recipes for the four time rotors are:

![Time Rotor Recipes](images/docs/time_rotor_recipes.gif)
```
DYE       | REDSTONE | DYE
GLASSPANE | CLOCK    | GLASSPANE
GLASSPANE | REDSTONE | GLASSPANE
```
Change the dye colours for each rotor:

* Early &mdash; Gray ![#666](https://via.placeholder.com/15/666/000000?text=+)
* Tenth &mdash; Cyan ![#0099cc](https://via.placeholder.com/15/0099cc/000000?text=+)
* Eleventh &mdash; Brown ![#663300](https://via.placeholder.com/15/663300/000000?text=+)
* Twelfth &mdash; Orange ![#ff9900](https://via.placeholder.com/15/ff9900/000000?text=+)

Use the command:
```
/tardisrecipe time-rotor-[early|tenth|eleventh|twelfth]
```
to see the recipes in game.
