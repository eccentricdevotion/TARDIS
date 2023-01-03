---
layout: default
title: Big list of TARDIS sub commands
---

<style>
    table { table-layout: fixed; font-size: 14px; }
    td { vertical-align: top; word-wrap: break-word; }
    td.usage code { font-size: 11px; }
    td a code { color: #3d7dbc; border-bottom: 1px dashed #3d7dbc; }
</style>

# TARDIS sub commands

A list of all the `/tardis` sub commands.

<table>
    <tr><th>Sub command</th><th>Description</th><th>Usage</th><th>Permission</th></tr>
    <tr><td id="abandon"><code>abandon</code></td><td>Abandon your current TARDIS.</td><td class="usage"><code>/tardis abandon</code></td><td>tardis.abandon</td></tr>
    <tr><td id="abort"><code>abort</code></td><td>Stop growing a TARDIS room.</td><td class="usage"><code>/tardis abort [task ID number]</code></td><td>tardis.room</td></tr>
    <tr><td id="add"><code>add</code></td><td>Add a companion that can travel with you in your TARDIS.</td><td class="usage"><code>/tardis add [player name]</code></td><td>tardis.add</td></tr>
    <tr><td id="arch_time"><code>arch_time</code></td><td>Display the time that a player must remain Chameleon arched.</td><td class="usage"><code>/tardis arch_time</code></td><td>tardis.chameleonarch</td></tr>
    <tr><td id="archive"><code>archive</code></td><td>Archive your current TARDIS console.</td><td class="usage"><code>/tardis archive [scan|add|remove|update|description|y] [name] &gt;y_value&lt;</code></td><td>tardis.archive</td></tr>
    <tr><td id="arsremove"><code>arsremove</code></td><td>Remove the TARDIS' ARS records so that manual room growing can be used.</td><td class="usage"><code>/tardis arsremove</code></td><td>tardis.use</td></tr>
    <tr><td id="bell"><code>bell</code></td><td>Toggle the TARDIS Cloister Bell on and off.</td><td class="usage"><code>/tardis bell [on|off]</code></td><td>tardis.use</td></tr>
    <tr><td id="check_loc"><code>check_loc</code></td><td>Checks the location the player is targeting is suitable for landing the TARDIS.</td><td class="usage"><code>/tardis check_loc</code></td><td>tardis.use</td></tr>
    <tr><td id="colourise"><code>colourise</code></td><td>Allows a player to change the colour of their TARDIS beacon glass with a Sonic Screwdriver.</td><td class="usage"><code>/tardis colourise</code></td><td>tardis.upgrade</td></tr>
    <tr><td id="colorize"><code>colorize</code></td><td colspan="3">Alias to the <a href="#colourise"><code>colourise</code></a> sub command.</td></tr>
    <tr><td id="comehere"><code>comehere</code></td><td>Make the TARDIS come to the block you are looking at.</td><td class="usage"><code>/tardis comehere</code></td><td>tardis.timetravel</td></tr>
    <tr><td id="construct"><code>construct</code></td><td>Set the sign text of the TARDIS CONSTRUCT Chameleon preset.</td><td class="usage"><code>/tardis construct [line number] [text with optional colour code]</code></td><td>tardis.use</td></tr>
    <tr><td id="cube"><code>cube</code></td><td>Show a list of players who are carrying a Siege Cube.</td><td class="usage"><code>/tardis cube</code></td><td>tardis.find</td></tr>
    <tr><td id="desktop"><code>desktop</code></td><td>Open the Desktop Theme GUI.</td><td class="usage"><code>/tardis desktop</code></td><td>tardis.upgrade</td></tr>
    <tr><td id="direction"><code>direction</code></td><td>Change the direction the Police Box door is facing.</td><td class="usage"><code>/tardis direction [north|west|south|east]</code></td><td>tardis.timetravel</td></tr>
    <tr><td id="door"><code>door</code></td><td>Opens or closes the TARDIS door.</td><td class="usage"><code>/tardis door [open|close]</code></td><td>tardis.use</td></tr>
    <tr><td id="egg"><code>egg</code></td><td>Play the Doctor Who Theme (requires TARDIS Resource Pack).</td><td class="usage"><code>/tardis egg [SIXTY_THREE|ZERO_FIVE|TWENTY_TWENTY|RANDOM]</code></td><td>tardis.use</td></tr>
    <tr><td id="eject"><code>eject</code></td><td>Allows a player to eject entities from their TARDIS.</td><td class="usage"><code>/tardis eject</code></td><td>tardis.eject</td></tr>
    <tr><td id="ep1"><code>ep1</code></td><td>Allows a player to make Emergency Programme One appear.</td><td class="usage"><code>/tardis abandon</code></td><td>tardis.use</td></tr>
    <tr><td id="erase"><code>erase</code></td><td>Allows a player to erase the TARDIS Storage Disk they have in their hand.</td><td class="usage"><code>/tardis erase</code></td><td>tardis.storage</td></tr>
    <tr><td id="excite"><code>excite</code></td><td>Allows a player to initiate atmospheric exitation around their TARDIS.</td><td class="usage"><code>/tardis excite</code></td><td>tardis.atmospheric</td></tr>
    <tr><td id="exterminate"><code>exterminate</code></td><td>Delete your TARDIS.</td><td class="usage"><code>/tardis exterminate</code></td><td>tardis.exterminate</td></tr>
    <tr><td id="find"><code>find</code></td><td>Shows you the location of your TARDIS.</td><td class="usage"><code>/tardis find</code></td><td>tardis.find</td></tr>
    <tr><td id="handbrake"><code>handbrake</code></td><td>Allows a player to toggle the TARDIS handbrake on or off.</td><td class="usage"><code>/tardis handbrake [on|off]</code></td><td>tardis.use</td></tr>
    <tr><td id="help"><code>help</code></td><td>View TARDIS help pages.</td><td class="usage"><code>/tardis help [command]</code></td><td>none</td></tr>
    <tr><td id="hide"><code>hide</code></td><td>Hide the TARDIS Police Box.</td><td class="usage"><code>/tardis hide</code></td><td>tardis.rebuild</td></tr>
    <tr><td id="home"><code>home</code></td><td>Save a time travel location as home.</td><td class="usage"><code>/tardis home</code></td><td>tardis.home</td></tr>
    <tr><td id="inside"><code>inside</code></td><td>List players inside your TARDIS.</td><td class="usage"><code>/tardis inside</code></td><td>tardis.use</td></tr>
    <tr><td id="item"><code>item</code></td><td>Allows a player to update TARDIS circuits from previous versions to the newer custom model data item.</td><td class="usage"><code>/tardis item [hand|inventory]</code></td><td>tardis.use</td></tr>
    <tr><td id="jettison"><code>jettison</code></td><td>Remove a TARDIS room.</td><td class="usage"><code>/tardis jettison [room type]</code></td><td>tardis.room</td></tr>
    <tr><td id="lamps"><code>lamps</code></td><td>Scan the main control room for new or moved lamp blocks.</td><td class="usage"><code>/tardis lamps</code></td><td>tardis.use</td></tr>
    <tr><td id="list"><code>list</code></td><td>List saved time travel locations, companions, areas and rechargers.</td><td class="usage"><code>/tardis list [saves|companions|areas|rechargers]</code></td><td>tardis.list</td></tr>
    <tr><td id="make_her_blue"><code>make_her_blue</code></td><td>Makes the TARDIS visible again if it was using the INVISIBLE preset.</td><td class="usage"><code>/tardis make_her_blue</code></td><td>tardis.use</td></tr>
    <tr><td id="monsters"><code>monsters</code></td><td>Reset monster spawning or kill all monsters in the TARDIS.</td><td class="usage"><code>/tardis monsters [reset|kill]</code></td><td>tardis.use</td></tr>
    <tr><td id="namekey"><code>namekey</code></td><td>Rename the TARDIS key Item.</td><td class="usage"><code>Hold the key in your hand, then type /tardis namekey [new name]</code></td><td>tardis.timetravel</td></tr>
    <tr><td id="occupy"><code>occupy</code></td><td>Toggle TARDIS occupation.</td><td class="usage"><code>/tardis occupy</code></td><td>tardis.timetravel</td></tr>
    <tr><td id="rebuild"><code>rebuild</code></td><td>Rebuild the TARDIS.</td><td class="usage"><code>/tardis rebuild</code></td><td>tardis.rebuild</td></tr>
    <tr><td id="remove"><code>remove</code></td><td>Remove a companion from your TARDIS.</td><td class="usage"><code>/tardis remove [player name|all]</code></td><td>tardis.add</td></tr>
    <tr><td id="removesave"><code>removesave</code></td><td>Delete a saved destination.</td><td class="usage"><code>/tardis removesave [name]</code></td><td>tardis.save</td></tr>
    <tr><td id="renamesave"><code>renamesave</code></td><td>Rename a saved destination.</td><td class="usage"><code>/tardis renamesave [old name] [new name]</code></td><td>tardis.save</td></tr>
    <tr><td id="reordersave"><code>reordersave</code></td><td>Reorder a saved destination in the TARDIS Saves GUI.</td><td class="usage"><code>/tardis reordersave [name] [slot number]</code></td><td>tardis.save</td></tr>
    <tr><td id="rescue"><code>rescue</code></td><td>Rescue a player in the TARDIS.</td><td class="usage"><code>/tardis rescue [player]</code></td><td>tardis.timetravel.rescue</td></tr>
    <tr><td id="room"><code>room</code></td><td>Grow a TARDIS room.</td><td class="usage"><code>/tardis room [room type|help]</code></td><td>tardis.room</td></tr>
    <tr><td id="save"><code>save</code></td><td>Save the current location of the TARDIS Police Box.</td><td class="usage"><code>/tardis save [name] &lt;true&gt;</code></td><td>tardis.save</td></tr>
    <tr><td id="saveicon"><code>saveicon</code></td><td>Change a saved destinations icon in the TARDIS Saves GUI.</td><td class="usage"><code>/tardis saveicon [save] [material]</code></td><td>tardis.save</td></tr>
    <tr><td id="save_player"><code>save_player</code></td><td>Allows a player to save a Player to a blank TARDIS Storage Disk.</td><td class="usage"><code>/tardis save_player [player]</code></td><td>tardis.storage</td></tr>
    <tr><td id="secondary"><code>secondary</code></td><td>Set secondary TARDIS controls.</td><td class="usage"><code>/tardis secondary [button|world-repeater|x-repeater|z-repeater|y-repeater|artron|handbrake|door]</code></td><td>tardis.update</td></tr>
    <tr><td id="section"><code>section</code></td><td>Open the TARDIS Update chat GUI.</td><td class="usage"><code>/tardis section</code></td><td>tardis.update</td></tr>
    <tr><td id="setdest"><code>setdest</code></td><td>Save a destination determined by the block you are looking at.</td><td class="usage"><code>/tardis setdest [name]</code></td><td>tardis.save</td></tr>
    <tr><td id="sethome"><code>sethome</code></td><td colspan="3">Alias to the <a href="#home"><code>home</code></a> sub command.</td></tr>
    <tr><td id="tagtheood"><code>tagtheood</code></td><td>Display the 'Tag the Ood' game statistics.</td><td class="usage"><code>/tardis tagtheood</code></td><td>tardis.tag</td></tr>
    <tr><td id="theme"><code>theme</code></td><td colspan="3">Alias to the <a href="#desktop"><code>desktop</code></a> sub command.</td></tr>
    <tr><td id="transmat"><code>transmat</code></td><td>Teleports to or sets an internal TARDIS transmat location.</td><td class="usage"><code>/tardis transmat [tp|add|update|remove|list] [name]</code></td><td>tardis.transmat</td></tr>
    <tr><td id="update"><code>update</code></td><td>Modify the interior of the TARDIS.</td><td class="usage"><code>/tardis update [advanced|ars|artron|back|backdoor|button|chameleon|condenser|creeper|door|eps|farm|handbrake|info|keyboard|light|rail|save-sign|scanner|stable|storage|temporal|terminal|village|world-repeater|x-repeater|y-repeater|z-repeater]</code></td><td>tardis.update</td></tr>
    <tr><td id="upgrade"><code>upgrade</code></td><td colspan="3">Alias to the <a href="#desktop"><code>desktop</code></a> sub command.</td></tr>
    <tr><td id="version"><code>version</code></td><td>Display the TARDIS plugin and CraftBukkit version.</td><td class="usage"><code>/tardis version</code></td><td>none</td></tr>
</table>