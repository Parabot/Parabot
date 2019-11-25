[![Stories in Ready](https://badge.waffle.io/Parabot/Parabot.png?label=ready&title=Ready)](https://waffle.io/Parabot/Parabot)
[![Build Status](https://travis-ci.org/Parabot/Parabot.svg?branch=master)](https://travis-ci.org/Parabot/Parabot)

# Parabot

Parabot V2.8.2

#### Links

[Website](http://www.parabot.org/)

[Download latest version](http://v3.bdn.parabot.org/api/bot/download/client)

#### Compilation
Build the project with the Maven POM file to have all required libraries installed.

#### Automation
Every stable build is automatically generated from the last commits of the master branch. This will take the version from both the `pom.xml` and the version in `.travis.yml`.

The nightly builds are automatically created from the last commits of the development branch. This will take the version from both the `pom.xml` and the version in `.travis.yml`, together with the build ID from travis.

#### Issues
If you've an issues regarding the bot itself, please report them [here](https://github.com/Parabot/Parabot/issues).

#### Maven
To add the client as a library, you'll first have to add our repository to your `pom.xml`:

```
    <repositories>
        <repository>
            <id>git-parabot</id>
            <name>Parabot's Git based repo</name>
            <url>https://maven.parabot.org/</url>
        </repository>
    </repositories>
```

Then you'll need to add the dependency:

```
    <dependencies>
        <dependency>
            <groupId>org.parabot</groupId>
            <artifactId>client</artifactId>
            <version>2.8.1</version>
        </dependency>
    </dependencies>
```

#### Labels
Labels are created with [GHLabel](https://github.com/jimmycuadra/ghlabel), whereas the yml is located in the .github directory