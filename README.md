# Game On! with Lagom

[Lagom](https://www.lagomframework.com/) is a framework for developing reactive microservices in Java or Scala. Created by [Lightbend](https://www.lightbend.com/), Lagom is built on the proven [Akka](http://akka.io/) toolkit and [Play Framework](https://playframework.com/), and provides a highly productive, guided path for creating responsive, resilient, elastic, message-driven applications.

[Game On!](https://gameontext.org/) is both a sample microservices application, and a throwback text adventure brought to you by the WASdev team at IBM.

## Before you start

Get your participant ID from the workshop facilitators. It will be a two-digit number between 01 and 50. You will need this number to create your project.

## Creating your project

1.  Clone this repository to your home directory:
    ```
    cd ~
    git clone https://github.com/lagom/lagom-gameon-maven-archetype.git
    ```
2.  Install the archetype:
    ```
    cd lagom-gameon-maven-archetype
    mvn install
    ```
3.  Create a project from the archetype in your home directory:
    ```
    cd ~
    mvn archetype:generate -DarchetypeGroupId=com.lightbend.lagom.gameon -DarchetypeArtifactId=lagom-gameon-maven-archetype -DarchetypeVersion=1.0-SNAPSHOT
    ```
    This will prompt you for several values. Enter them as shown below:
    - groupId: com.lightbend.lagom.gameon
    - artifactId: gameon17sNN (replace "NN" with your assigned participant ID, for example: "gameon17s50")
    - version: (press enter to accept the default)
    - package: (press enter to accept the default)

This creates a directory named gameon17sNN, with "NN" replaced with your assigned participant ID.

```
cd gameon17sNN
```

See the `README.md` file in the created project for further instructions.
