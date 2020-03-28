---
layout: default
title: flight modes
---

# TARDIS flight modes

From v2.9-beta-1, TARDIS now has three different flight modes. Flight modes are a player preference, and can be set using the command `/tardisprefs flight [mode]` command.

The three flight modes are:

- `normal` — the default, no special effort required
- `regulator` — use the Helmic Regulator to correct the TARDIS’ flight path, a GUI opens automatically — you need to use the direction blocks to keep the regulator in the centre
- `manual` — run round like crazy clicking the correct console repeater when told to do so

### Manual flight mode

In manual flight mode the repeaters are called:

- `world`: Helmic Regulator
- `x`: Astrosextant Rectifier
- `z`: Gravitic Anomaliser
- `y`: Absolute Tesseractulator
- A visual effect has been added to help show which one to click :)

In manual flight mode, the more repeaters you miss hitting, the further from your desired location you will land

You can set the delay between having to click the repeaters in the config: `/tardisadmin manual_flight_delay [ticks]` — default is 60 (3 seconds)

Video goes here

<!--<iframe src="https://player.vimeo.com/video/57807692" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>-->
### Regulator flight mode

Use the four direction buttons to keep the flight path stabilised in the centre of the vortex blocks (black stained-glass).

In regulator flight mode, the further away from the centre the regulator block is, the further from your desired location you will land

Video goes here

<iframe src="https://player.vimeo.com/video/90391961" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>
### Note:

Currently if the destination is inside a building, the adjusted location will most probably end up on the roof (even if there is room inside the building).

