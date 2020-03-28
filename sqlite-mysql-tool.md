---
layout: default
title: SQLite -> MySQL conversion tool
---

[Jump to video](#video)

# SQLite -\> MySQL conversion tool

You can easily change the database storage format of the TARDIS plugin using the SQLite -\> MySQL conversion tool.

![Conversion tool](images/docs/conversion_tool.png)

To open the tool, double click the TARDIS.jar file. Follow the steps below to complete the conversion:

1. Click the first ‘Browse’ button to select your SQLite _TARDIS.db_ file
2. Click the second ‘Browse’ button to set the save location for the generated SQL file. Naviagte to the appropriate directory and give the file a suitable name (such as _TARDIS.sql_)
3. If TARDIS data will be stored in a shared database, enter a prefix (such as _TARDIS\__) in the ‘Prefix’ field
4. Click the ‘Convert’ button
5. Open your MySQL database management tool (e.g. phpMyAdmin)
6. Import the TARDIS data using the generated SQL file
7. Add your MySQL connection credentials to the TARDIS plugin config.yml
8. If you added a table prefix in step 3, remember to add the prefix to the TARDIS plugin config.yml as well

### Video
<iframe src="https://player.vimeo.com/video/139174559" width="600" height="366" frameborder="0" webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>