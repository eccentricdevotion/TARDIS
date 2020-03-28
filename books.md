---
layout: default
title: Timelore books
---

# Timelore books

TARDIS books allow you to inform players about TARDIS use and how to gain TARDIS [achievements](achievements.html).

TARDIS books are fully customisable by editing the text files found in the _plugins/TARDIS/books_ folder. Currently there are seven books:

- **tardis** — contains basic information about creating a TARDIS. It is given to the player automatically when they first login to the server if they have the permission `tardis.create`
- **travel** — a book explaining the ‘travel’ achievement goals
- **rooms** — a book explaining the ‘rooms’ achievement goals
- **farm** — a book explaining the ‘farm’ achievement goals
- **energy** — a book explaining the ‘energy’ achievement goals
- **friends** — a book explaining the ‘friends’ achievement goals
- **kill** — a book explaining the ‘kill’ achievement goals

You can view a list of available books ingame with the command:

    /tardisbook list

To get a book, use the command:

    /tardisbook [book name] get

To use the commands a player must have the `tardis.book` permission, or the parent node `tardis.use`.

## Editing the book files

When editing the book files, keep these points in mind:

- Books can have a maximum of 256 characters per page. In the text file separate pages with a double line space (hit enter/return twice)
- You can use the placeholder `@p` to insert the player’s name automatically when they get the book
- You can use standard [Minecraft formatting codes](http://www.minecraftwiki.net/wiki/Formatting_codes). They must be proceeded by the section sign (`§`) — on Windows use _ALT+21_, on a Mac use _Option+6_
