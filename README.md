[![Stories in Ready](https://badge.waffle.io/Parabot/Parabot.png?label=ready&title=Ready)](https://waffle.io/Parabot/Parabot)
[![Build Status](https://travis-ci.org/Parabot/Parabot.svg?branch=master)](https://travis-ci.org/Parabot/Parabot)

# Parabot

Parabot V2.5.

#### Website

[Community, scripts & more](http://www.parabot.org/)

#### Compilation
You'll need the [libraries](https://github.com/Parabot/Parabot/tree/master/libs) in order to compile parabot successfully.
The POM file contains the redirection to the libraries, so simply install them with Maven.

#### Issues
If you've an issues regarding the bot itself, please report them [here](https://github.com/Parabot/Parabot/issues).

#### Maven
Parabot supports Maven as of September 2015. All information is included in the POM.xml.
The API that is supported by Parabot is also published on a Maven repository.
If you'd like to have either or both the client and the API in your project, use the following repository and dependecy tags:
```
    <repositories>
        <repository>
            <id>git-parabot</id>
            <name>Parabot's Git based repo</name>
            <url>https://github.com/parabot/Maven-Repository/raw/master/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>org.parabot</groupId>
            <artifactId>client</artifactId>
            <version>2.</version>
        </dependency>
        <dependency>
            <groupId>org.parabot</groupId>
            <artifactId>317provider</artifactId>
            <version>1.0</version>
        </dependency>
    </dependencies>
```
**For the latest versions of our dependencies, please check our examples on [the Maven Repository](https://github.com/Parabot/Maven-Repository/tree/master/examples)**


#### Labels
Labels are created with [GHLabel](https://github.com/jimmycuadra/ghlabel), whereas the yml is located in the .github directory