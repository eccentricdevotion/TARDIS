---
layout: default
title: How to use BuildTools
---

# How to use BuildTools

TARDIS is always compiled against the latest version of Spigot. Spigot is being constantly updated, and sometimes TARDIS
takes advantage of the new features that have been added to the Spigot API. This means that if you don’t keep your
server JAR file up-to-date, then TARDIS _may not be able to run properly_.

Spigot has released a tool called BuildTools that allows you to get the latest version of Spigot (as well as the Spigot
API — but only plugin developers need to worry about that).

Using BuildTools is a slightly tricky process to setup the first time around, but if you follow the steps below, they
will hopefully help you get it right :)

If you have
problems, [BuildTools is run nightly on the TARDIS Jenkins server](http://tardisjenkins.duckdns.org:8080/job/BuildTools/)
— you can download Spigot and Spigot-API from there.

This guide is for **Windows** , if you use Linux, then you probably already have a good idea how to do this sort of
thing — just follow [Spigot’s instructions](http://www.spigotmc.org/threads/bukkit-craftbukkit-spigot-1-8.36598/)

## Getting the right tools

To compile Spigot you will need three things:

1. The BuildTools JAR — you can download that
   here: [https://hub.spigotmc.org/jenkins/job/BuildTools/](https://hub.spigotmc.org/jenkins/job/BuildTools/)
2. The Java Software Development Kit (JDK) — download from the Oracle
   wbsite: [https://www.oracle.com/java/technologies/downloads/#java17](https://www.oracle.com/java/technologies/downloads/#java17)
   — you probably have Java installed already, but you need this version to actually build anything from code
3. GitHub for Windows — Get it here: [https://windows.github.com](https://windows.github.com)

Once you have downloaded the three required tools:

1. Put BuildTools.jar file in its own folder, for example `Downloads\BuildTools\`
2. Install the Java (JDK) — it will most probably install at `C:\Program Files\Java\jdk17.0.2\`
3. Install GitHub (let it add shortcuts to your desktop), start it up, and create a free account

## Setting up to build the server JARs

You are going to be running BuildTools from the GitHub command line, but before you do that you will need to set up a
Java system variable.

Follow these next steps to do that:

1. Open an Explorer window, right-click **Computer** and choose **Properties** — this should open the **Control Panel**
   to **System information**
2. On the left hand side click **Advanced system settings**
3. In the **System properties** window that comes up click **Environment variables...**
4. In the bottom ( **System variables** ) section click the **New...** button
5. For **Variable name** type `JAVA_HOME`
6. for **Variable value** type the path to your JDK installation, for example: `C:/Program Files/Java/jdk17.0.2/`
7. click OK, OK, Apply etc

## Building the server JARs

Almost there! The last step is to actually compile the server JAR files.

1. Open Git Shell from the desktop shortcut
2. The shell opens in your user directory, so you need to get to the folder where you put the BuildTools.jar file. Use
   the `cd` command to change to the right directory, for example: `cd Downloads/BuildTools`
3. Now you can start the build process, type `java -jar BuildTools.jar`
4. Let the programme do its thing, it will download everything it needs, and takes about 10 minutes to compile the JARs
   when run for the first time — subsequent builds will be much quicker.

Once the process is complete, you will find the Spigot JAR waiting for you in the BuildTools folder.

## Keeping up-to-date

Once you have it all setup, it is easy to keep your server up-to-date — just redo the steps under the _Building the
server JARs_ heading. BuildTools will fetch the latest code changes and produce a new set of server JARs.

