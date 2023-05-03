---
layout: default
title: Big list of other commands
---

<style>
    table { table-layout: fixed; font-size: 14px; }
    tr.lighter { background-color: #eee; }
    td { vertical-align: top; word-wrap: break-word; }
    td.usage code { font-size: 11px; }
    td a code { color: #3d7dbc; border-bottom: 1px dashed #3d7dbc; }
    strong { color: #069; }
</style>

# Big list of other commands

You can view descriptions, usage and permissions for all commands in-game using the TARDIS help system.
Type `/tardis help` to list all the commands, then use `/tardis? [command]` to view all the relevant subcommands.
Use `/tardis? [command] [subcommand]` when necessary to view the command information.


<table>
    <tr>
        <th>Command</th>
        <th>Aliases</th>
        <th>Description</th>
        <th>Permission</th>
    </tr>
    <tr>
        <td rowspan="28" id="tardistravel"><strong>tardistravel</strong></td>
        <td><code>ttravel</code></td>
        <td>Travel to destinations in various ways.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardistravel [player]</code> or <code>/tardistravel [world x y z]</code> or <code>/tardistravel dest [name]</code> or <code>/tardistravel area [name]</code> or <code>/tardistravel biome [biome]</code>
            <ul>
                <li>Example: <code>/tardistravel eccentric_nz</code> - travel to a player's location </li>
                <li>Example: <code>/tardistravel New_new_earth -117 64 273</code> - travel to co-ordinates in a specific world </li>
                <li>Example: <code>/tardistravel dest mine</code> - travel to saved destination called 'mine' </li>
                <li>Example: <code>/tardistravel area airport</code> - travel to the TARDIS area called 'airport' </li>
                <li>Example: <code>/tardistravel biome DESERT</code> - travel to the nearest DESERT biome </li>
                <li>Example: <code>/tardistravel structure VILLAGE_SAVANNAH</code> - travel to the nearest savannah village </li>
            </ul></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel home</code></td>
        <td>Time travel to the TARDIS 'home' location.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel home</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel player</code></td>
        <td>Time travel to the specified player.</td>
        <td>tardis.timetravel.player</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel [player]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel tpa</code></td>
        <td>Ask to time travel to the specified player.</td>
        <td>tardis.timetravel.player</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel tpa [player]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel coords</code></td>
        <td>Time travel to the specified coordinates.</td>
        <td>tardis.timetravel.location</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel [world x y z]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel dest</code></td>
        <td>Time travel to a saved destination.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel dest [save name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel area</code></td>
        <td>Time travel to a server defined TARDIS area.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel area [area name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel biome</code></td>
        <td>Time travel to the closest specified biome type.</td>
        <td>tardis.timetravel.biome</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel biome [biome type]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel cave</code></td>
        <td>Time travel to an underground cave.</td>
        <td>tardis.timetravel.cave</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel cave</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel village</code></td>
        <td>Time travel to a village or other structure.</td>
        <td>tardis.timetravel.village</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel village [optional structure]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel structure</code></td>
        <td>Time travel to a structure.</td>
        <td>tardis.timetravel.village</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel structure [optional structure]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel costs</code></td>
        <td>Display a list of Artron Energy travel costs.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel costs</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel cancel</code></td>
        <td>Removes the currently set travel destination.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel cancel</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardistravel stop</code></td>
        <td>Stops travelling / materialising and returns to the home location. Use in an emergency only!</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardistravel stop</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="30" id="tardisadmin"><strong>tardisadmin</strong></td>
        <td><code>tadmin</code></td>
        <td>Perform TARDIS administration tasks.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisadmin [sub command] [...]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin create</code></td>
        <td>Create a TARDIS for a player at a targeted location.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin create [player] [schematic] &lt;wall&gt; &lt;floor&gt;</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin list</code></td>
        <td>List TARDISs and their locations (or save them to file), plus list open portals / abandoned TARDISes.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin list [page no.|save|portals|abandoned]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin chunks</code></td>
        <td>List chunks that are being kept loaded by TARDIS.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin chunks</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin recharger</code></td>
        <td>Set the beacon block you are looking at, as a TARDIS recharge station.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin recharger [name]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin decharge</code></td>
        <td>Remove the specified beacon recharger from the config.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin decharge [name]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin enter</code></td>
        <td>Enter a player's TARDIS.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin enter [player]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin delete</code></td>
        <td>Delete a player's TARDIS.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin delete [player]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin purge</code></td>
        <td>Remove all database entries for the named player.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin purge [player]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin arch</code></td>
        <td>View an 'arched' player's real name, or with the 'force' argument, toggle arch status.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin arch [player] (force)</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin prune</code></td>
        <td>Remove TARDISes that haven't been used for an extended period.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin prune [number of days]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin prunelist</code></td>
        <td>List TARDISes that haven't been used for an extended period.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin prunelist [number of days]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin playercount</code></td>
        <td>Check or set a player's TARDIS count.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin playercount [player] [count]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin assemble</code></td>
        <td>Clear HADS dispersal trackers or list dispered TARDISes.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin assemble [clear|list]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisadmin convert_database</code></td>
        <td>Convert an SQLite database to MySQL.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisadmin convert_database</code></td>
    </tr>
    <tr>
        <td rowspan="10" id="tardisconfig"><strong>tardisconfig</strong></td>
        <td><code>tconfig</code></td>
        <td>Set TARDIS configuration options and world inclusion.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisconfig [option] [value]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisconfig options</code></td>
        <td>List the TARDIS config options and values in chat.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisconfig options</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisconfig reload</code></td>
        <td>Reload the TARDIS config from disk.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisconfig reload [config name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisconfig include</code></td>
        <td>Set whether the specified world is included in time travel destinations.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisconfig include [world]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisconfig exclude</code></td>
        <td>Set whether the specified world is excluded from time travel destinations.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisconfig exclude [world]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisdev"><strong>tardisdev</strong></td>
        <td><code>tdev</code></td>
        <td>Commands used by eccentric_nz to help with coding and documenting the TARDIS plugin.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisdev [add_regions|advancements|tree|list|stats] [preset_perms|perms|recipes|blueprints|commands]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisgive"><strong>tardisgive</strong></td>
        <td><code>tgive</code></td>
        <td>Give TARDIS items, Artron energy, Seed blocks, kits, knowledge books, recipe discoveries or mushroom blocks to a player.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisgive [player] [item|artron|kit|seed|knowledge|recipe|mushroom] [amount|recipe|...]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardissudo"><strong>tardissudo</strong></td>
        <td><code>tsudo</code></td>
        <td>Used by TARDIS admins to run commands as another Time Lord or fix players' TARDISes.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardissudo [player] [ars|assemble|back|chameleon|clean|comehere|deadlock|desiege|handbrake|hide|isomorphic|rebuild|repair|travel|update] [...]</code></td>
    </tr>
    <tr>
        <td rowspan="14" id="tardisroom"><strong>tardisroom</strong></td>
        <td><code>troom</code></td>
        <td>Set TARDIS room configuration options, and add new rooms.</td>
        <td>tardis.create</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisroom add [name]</code> or <code>/tardisroom [name] [boolean|integer|string]</code> or <code>/tardisroom blocks [name|save]</code> or <code>/tardisroom required [room]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom required</code></td>
        <td>List the blocks needed to grow a room.</td>
        <td>tardis.create</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom required [room name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom add</code></td>
        <td>Add a new custom room schematic.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom add [schematic name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom seed</code></td>
        <td>Set the seed block of a custom room.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom [schematic name] [material]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom cost</code></td>
        <td>Set the cost of a custom room.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom add [schematic name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom offset</code></td>
        <td>Set the offset of a custom room.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom [schematic name] [negative amount]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisroom enabled</code></td>
        <td>Set the custom room as active/inactive.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisroom [schematic name] [true|false]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="76" id="tardisprefs"><strong>tardisprefs</strong></td>
        <td><code>tprefs</code></td>
        <td>Set player preferences.</td>
        <td>tardis.timetravel</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisprefs [preference] [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs sfx</code></td>
        <td>Toggle the TARDIS sound effects on and off.</td>
        <td>tardis.prefs.sfx</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs sfx [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs quotes</code></td>
        <td>Toggle the TARDIS quotes on and off.</td>
        <td>tardis.prefs.quotes</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs quotes [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs key</code></td>
        <td>Set the TARDIS key item.</td>
        <td>tardis.prefs.key</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs key [material]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs wall</code></td>
        <td>Set the TARDIS room growing wall material.</td>
        <td>tardis.prefs.wall</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs wall [material]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs floor</code></td>
        <td>Set the TARDIS room growing floor material.</td>
        <td>tardis.prefs.floor</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs floor [material]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs auto</code></td>
        <td>Toggle the TARDIS Autonomous Homing feature on and off.</td>
        <td>tardis.prefs.auto</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs auto [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs eps</code></td>
        <td>Toggle Emergency Programme One on and off.</td>
        <td>tardis.prefs.eps</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs eps [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs message</code></td>
        <td>Set the Emergency Programme One message.</td>
        <td>tardis.prefs.message</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs eps_message [message]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs hads</code></td>
        <td>Toggle HADS on and off.</td>
        <td>tardis.prefs.hads</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs hads [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs isomorphic</code></td>
        <td>Toggle isomorphic controls on and off.</td>
        <td>tardis.prefs.isomorphic</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs isomorphic [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs submarine</code></td>
        <td>Toggle submarine landings on and off.</td>
        <td>tardis.prefs.submarine</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs submarine [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs auto_powerup</code></td>
        <td>Toggle automatic powerup on and off (when entering the TARDIS).</td>
        <td>tardis.prefs.auto_powerup</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs auto_powerup [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs auto_siege</code></td>
        <td>Toggle automatic Siege Mode on and off (when the Time Lord dies).</td>
        <td>tardis.prefs.auto_siege</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs submarine [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs build</code></td>
        <td>Toggle companion building inside the TARDIS on and off.</td>
        <td>tardis.prefs.build</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs build [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs beacon</code></td>
        <td>Toggle whether the TARDIS beacon is only on while travelling.</td>
        <td>tardis.prefs.beacon</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs beacon [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs difficulty</code></td>
        <td>Change your personal difficulty level.</td>
        <td>tardis.prefs.difficulty</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs difficulty [easy|medium|hard]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs dnd</code></td>
        <td>Toggle whether other players can request travel/rescue to you.</td>
        <td>tardis.prefs.dnd</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs dnd [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs eps_message</code></td>
        <td>Set the Emergency Programme One message.</td>
        <td>tardis.prefs.eps_message</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs eps_message [message]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs farm</code></td>
        <td>Toggle mob farming on and off.</td>
        <td>tardis.prefs.farm</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs farm [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs flight</code></td>
        <td>Set the TARDIS flight mode.</td>
        <td>tardis.prefs.flight</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs flight [normal|regulator|manual]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs hads_type</code></td>
        <td>Set the TARDIS HADS type.</td>
        <td>tardis.prefs.hads_type</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs hads_type [dispersal|displacement]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs junk</code></td>
        <td>Toggle Junk mode on and off.</td>
        <td>tardis.prefs.junk</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs junk [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs key_menu</code></td>
        <td>Open the TARDIS Key preferences GUI.</td>
        <td>tardis.prefs.key_menu</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs key_menu</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs language</code></td>
        <td>Set the language used in the '/tardis say' command.</td>
        <td>tardis.prefs.language</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs language [language]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs translate</code></td>
        <td>Set the languages and player used when auto translating messages.</td>
        <td>tardis.prefs.translate</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs translate [language to] [language from] [player]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs lanterns</code></td>
        <td>Toggle Sea Lanterns for lights on and off.</td>
        <td>tardis.prefs.lanterns</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs lanterns [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs minecart</code></td>
        <td>Toggle default Minecraft SFX on and off.</td>
        <td>tardis.prefs.minecart</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs minecart [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs renderer</code></td>
        <td>Toggle entering the Renderer room after scanning on and off.</td>
        <td>tardis.prefs.renderer</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs renderer [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs siege_floor</code></td>
        <td>Set the block used for the TARDIS floor when Siege Mode is engaged.</td>
        <td>tardis.prefs.siege_floor</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs siege_floor [block type]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs siege_wall</code></td>
        <td>Set the block used for the TARDIS walls when Siege Mode is engaged.</td>
        <td>tardis.prefs.siege_wall</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs siege_wall [block type]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs sign</code></td>
        <td>Toggle the Police Box 'name' sign on and off.</td>
        <td>tardis.prefs.sign</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs sign [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs sonic</code></td>
        <td>Open the Sonic Screwdriver preferences GUI.</td>
        <td>tardis.prefs.sonic</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs sonic</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs hum</code></td>
        <td>Sets the TARDIS interior hum sound.</td>
        <td>tardis.prefs.hum</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs hum [sound]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs wool_lights</code></td>
        <td>Toggle the wool for lights off setting on and off.</td>
        <td>tardis.prefs.wool_lights</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs wool_lights [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs travelbar</code></td>
        <td>Toggle the travel remaining progress bar on and off.</td>
        <td>tardis.prefs.travelbar</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs travelbar [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs chameleon</code></td>
        <td>Toggle the chameleon circuit on and off.</td>
        <td>tardis.prefs.chameleon</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs chameleon [on|off]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisprefs forcefield</code></td>
        <td>Toggle the TARDIS forcefield on and off.</td>
        <td>tardis.prefs.forcefield</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisprefs forcefield [on|off]</code></td>
    </tr>
    <tr>
        <td rowspan="14" id="tardisarea"><strong>tardisarea</strong></td>
        <td><code>tarea</code></td>
        <td>Set up predefined TARDIS travel areas.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisarea [start|end|parking|show|remove] [name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea start</code></td>
        <td>Start defining a TARDIS area.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea start [name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea end</code></td>
        <td>Finish defining a TARDIS area.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea end</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea show</code></td>
        <td>Show the corners of a TARDIS area.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea show [name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea remove</code></td>
        <td>Remove a TARDIS area.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea remove [name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea parking</code></td>
        <td>Set a TARDIS area's parking space.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea parking [name] [number of blocks between]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisarea yard</code></td>
        <td>Visually mark the area with parking spots, where the first [material] is the block to use as the floor, and the second [material] is the block to use to mark the parking spot.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisarea yard [name] [material] [material]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisartron"><strong>tardisartron</strong></td>
        <td><code>tartron</code></td>
        <td>Transfer Artron Energy to an Artron Storage Cell</td>
        <td>tardis.store</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisartron [tardis|timelord] [amount]</code></td>
    </tr>
    <tr>
        <td rowspan="6" id="tardisbind"><strong>tardisbind</strong></td>
        <td><code>tbind</code></td>
        <td>Bind TARDIS commands to buttons and other controls</td>
        <td>tardis.update</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisbind [add|remove] [save|player|area|biome|chameleon|transmat|hide|rebuild|home|cave|make_her_blue|occupy|remove]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisbind add</code></td>
        <td>Bind something to a button</td>
        <td>tardis.update</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisbind add [save|player|area|biome|chameleon|transmat|hide|rebuild|home|cave|make_her_blue|occupy] [which]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardisbind remove</code></td>
        <td>Remove a 'bound' saved location, command, player, chameleon preset, transmat or area from a button. You will need to click the bound block to remove the record.</td>
        <td>tardis.update</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardisbind remove [bind type]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisgravity"><strong>tardisgravity</strong></td>
        <td><code>tgravity</code></td>
        <td>Add or remove gravity well blocks.</td>
        <td>tardis.gravity</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisgravity [up|down|north|west|south|east|remove] [distance] [velocity]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisbook"><strong>tardisbook</strong></td>
        <td><code>tbook</code></td>
        <td>Get and read TARDIS books and start TARDIS achievements.</td>
        <td>tardis.book</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisbook [list|book name] [get|start]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="10" id="tardistexture"><strong>tardistexture</strong></td>
        <td><code>ttexture</code></td>
        <td>Set texure pack preferences.</td>
        <td>tardis.texture</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardistexture [on|off|in|out] [url]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardistexture on</code></td>
        <td>Turns resource pack switching on.</td>
        <td>tardis.texture</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardistexture on</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardistexture off</code></td>
        <td>Turns resource pack switching off.</td>
        <td>tardis.texture</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardistexture off</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardistexture in</code></td>
        <td>Set resource pack that is switched to inside the TARDIS.</td>
        <td>tardis.texture</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardistexture in [url]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardistexture out</code></td>
        <td>Set resource pack that is switched to in the outside world.</td>
        <td>tardis.texture</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardistexture out [url]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisrecipe"><strong>tardisrecipe</strong></td>
        <td><code>trecipe</code></td>
        <td>View TARDIS item recipes.</td>
        <td>tardis.help</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisrecipe [item] - use /tardisrecipe list - to see all the items</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardissay"><strong>tardissay</strong></td>
        <td><code>tsay</code></td>
        <td>Say something in a different language via the TARDIS Universal Translator.</td>
        <td>tardis.translate</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardissay [language] [message]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisremote"><strong>tardisremote</strong></td>
        <td><code>tremote</code></td>
        <td>Remote control a TARDIS.</td>
        <td>tardis.remote</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisremote [player] [travel|comehere|hide|rebuild|back] [home|area|coords]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="12" id="tardisschematic"><strong>tardisschematic</strong></td>
        <td><code>[tschematic, ts]</code></td>
        <td>Save, load or paste a TARDIS schematic.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisschematic [save|load|paste|clear|replace] [name]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisschematic save</code></td>
        <td>Save a TARDIS schematic.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisschematic save [name]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisschematic load</code></td>
        <td>Load a TARDIS schematic.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisschematic load [folder] [name]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisschematic paste</code></td>
        <td>Paste a loaded TARDIS schematic.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisschematic paste</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisschematic clear</code></td>
        <td>Clears an area selected with the TARDIS schematic wand.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisschematic clear</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisschematic replace</code></td>
        <td>Replaces the specified block type within an area selected with the TARDIS schematic wand with the second block type.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisschematic replace [material] [material]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisnetherportal"><strong>tardisnetherportal</strong></td>
        <td><code>[tnetherportal, tnp]</code></td>
        <td>Get coordinates for linking Nether Portals.</td>
        <td>tardis.help</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisnetherportal &lt;[x] [y] [z] [overworld|nether]&gt;</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="12" id="tardisjunk"><strong>tardisjunk</strong></td>
        <td><code>tjunk</code></td>
        <td>Create, find, return and delete the Junk TARDIS.</td>
        <td>tardis.junk</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisjunk [create|find|return|delete]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisjunk create</code></td>
        <td>Create a Junk TARDIS.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisjunk create</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisjunk find</code></td>
        <td>Find the current location of the Junk TARDIS.</td>
        <td>tardis.junk</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisjunk find</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisjunk time</code></td>
        <td>Find out the time before the Junk TARDIS automatically returns home.</td>
        <td>tardis.junk</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisjunk time</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisjunk return</code></td>
        <td>Return the Junk TARDIS to its home location.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisjunk return</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2"><code>tardisjunk delete</code></td>
        <td>Delete the Junk TARDIS.</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="2" class="usage"><code>/tardisjunk delete</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardishelp"><strong>tardis?</strong></td>
        <td><code>[t?, tardishelp]</code></td>
        <td>Get help with TARDIS commands.</td>
        <td>&mdash;</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardis? [command] [argument]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisteleport"><strong>tardisteleport</strong></td>
        <td><code>[tardistp, ttp]</code></td>
        <td>Teleport to a world's spawn point</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisteleport [world]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisworld"><strong>tardisworld</strong></td>
        <td><code>tworld</code></td>
        <td>Load or unload TARDIS worlds</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisworld [load|unload|gm|enable|disable|update ] [world] &lt;WorldType&gt; &lt;Environment&gt; &lt;generator&gt;</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisgamemode"><strong>tardisgamemode</strong></td>
        <td><code>[tgm, tgms, tgmc, tgma, tgmsp]</code></td>
        <td>Set a player's gamemode</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisgamemode [GameMode] &lt;player&gt;</code></td>
    </tr>
    <tr>
        <td rowspan="8" id="tardischemistry"><strong>tardischemistry</strong></td>
        <td><code>tchemistry</code></td>
        <td>Open a Chemistry creative GUI or show a chemical formula.</td>
        <td>tardis.chemistry</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardischemistry [gui|formula] [which]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardischemistry gui</code></td>
        <td>Show a chemistry GUI.</td>
        <td>tardis.chemistry.command</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardischemistry gui [creative|construct|compound|reduce|product|lab]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardischemistry formula</code></td>
        <td>Show a compound or product formula.</td>
        <td>tardis.formula.show</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardischemistry formula [compound|product]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>tardischemistry recipe</code></td>
        <td>Show a Chemistry GUI block recipe.</td>
        <td>tardis.help</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/tardischemistry recipe [creative|construct|compound|reduce|product|lab]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisinfo"><strong>tardisinfo</strong></td>
        <td>&mdash;</td>
        <td>Internal command used by the TARDIS Information System.</td>
        <td>tardis.help</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisinfo [letter]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardisweather"><strong>tardisweather</strong></td>
        <td><code>tweather</code></td>
        <td>Change the weather in the world the player is in (or where their TARDIS is if they are inside it).</td>
        <td>tardis.use</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardisweather [clear|rain|thunder]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardistime"><strong>tardistime</strong></td>
        <td><code>ttime</code></td>
        <td>Change the time in the world the player is in (or where their TARDIS is if they are inside it).</td>
        <td>tardis.admin</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardistime [day|morning|noon|night|midnight|?AM|?PM|ticks]</code></td>
    </tr>
    <tr>
    <tr class="lighter">
        <td rowspan="2" id="tardisdisplay"><strong>tardisdisplay</strong></td>
        <td><code>tdisplay</code></td>
        <td>Toggle the TARDIS HUD on and off.</td>
        <td>tardis.display</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardisdisplay [ALL|BIOME|COORDS|DIRECTION|TARGET_BLOCK]</code></td>
    </tr>
    <tr>
        <td rowspan="2" id="tardismushroom"><strong>tardismushroom</strong></td>
        <td><code>tmushroom</code></td>
        <td>Fix broken mushroom block textures.</td>
        <td>tardis.mushroom</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/tardismushroom [red|brown|stem] [radius]</code></td>
    </tr>
    <tr class="lighter">
        <td rowspan="2" id="tardiscall"><strong>tardiscall</strong></td>
        <td><code>tcall</code></td>
        <td>Request a player to bring their TARDIS to your targeted location.</td>
        <td>tardis.use</td>
    </tr>
    <tr class="lighter">
        <td colspan="3" class="usage"><code>/tardiscall [player]</code></td>
    </tr>
    <tr>
        <td rowspan="24" id="handles"><strong>handles</strong></td>
        <td><code>[tardishandles, thandles]</code></td>
        <td>These commands are used by the console to enable the Handles companion.</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="3" class="usage"><code>/handles disk [disk name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles takeoff</code></td>
        <td>Make the TARDIS enter the Time Vortex</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles takeoff" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles land</code></td>
        <td>Make the TARDIS leave the Time Vortex</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles land" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles scan</code></td>
        <td>Make Handles scan the environment around the player</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles scan" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles lock</code></td>
        <td>Make Handles lock the TARDIS door</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles lock the door" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles unlock</code></td>
        <td>Make Handles unlock the TARDIS door</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles unlock the door" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles remind</code></td>
        <td>Add a Handles reminder</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles remind me to do [something] in 5" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles say</code></td>
        <td>Make Handles say a phrase</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles say [something]" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles name</code></td>
        <td>Make Handles tell a player their in-game name</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles what is my name" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles time</code></td>
        <td>Make Handles say the time</td>
        <td>tardis.admin</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>Internal use only - use "hey handles what is the time" instead</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles disk</code></td>
        <td>Name a Handles Program Disk</td>
        <td>tardis.handles.program</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/handles disk [new name]</code></td>
    </tr>
    <tr>
        <td rowspan="2"><code>handles remove</code></td>
        <td>Remove the Handles record from the TARDIS database</td>
        <td>tardis.handles.use</td>
    </tr>
    <tr>
        <td colspan="2" class="usage"><code>/handles remove</code></td>
    </tr>
</table>
