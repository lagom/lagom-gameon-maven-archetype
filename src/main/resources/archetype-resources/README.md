#set( $symbol_pound = '#' )

$symbol_pound Game On! with Lagom

[Lagom](https://www.lagomframework.com/) is a framework for developing reactive microservices in Java or Scala. Created by [Lightbend](https://www.lightbend.com/), Lagom is built on the proven [Akka](http://akka.io/) toolkit and [Play Framework](https://playframework.com/), and provides a highly productive, guided path for creating responsive, resilient, elastic, message-driven applications.

[Game On!](https://gameontext.org/) is both a sample microservices application, and a throwback text adventure brought to you by the WASdev team at IBM.

This project was created by the Maven archetype from https://github.com/lagom/lagom-gameon-maven-archetype.

See the `README.md` file in that repository for more information.

$symbol_pound$symbol_pound Deploying to Bluemix

1.  Log in to Bluemix with the workshop email address you were provided:
    ```
    bx login -a https://api.ng.bluemix.net -c 1e892c355e0ba37560f028df670c2719
    ```

    You will be prompted for the email address and password. Enter these as provided to you.

2.  Initialize the Bluemix Container Service plugin:
    ```
    bx cs init
    ```

3.  Download the configuration files for the Kubernetes cluster:
    ```
    bx cs cluster-config gameon
    ```

    This should print details similar to the following, though the details might differ slightly:
    ```
    OK
    The configuration for javaone was downloaded successfully. Export environment variables to start using Kubernetes.

    export KUBECONFIG=/home/ubuntu/.bluemix/plugins/container-service/clusters/gameon/kube-config-dal10-gameon.yml
    ```

4.  Copy and run the provided `export` command to configure the Kubernetes CLI. Note that you should copy the command printed to your terminal, which might differ slightly from the example above.

5.  Test that you can run `kubectl` to list the resources in the Kubernetes cluster:
    ```
    kubectl get all
    ```

6.  Log in to the Bluemix Container Registry:
    ```
    bx cr login
    ```

7.  Build the Docker image for your service:
    ```
    mvn clean package docker:build
    ```

8.  Push the Docker image to the Bluemix registry:
    ```
    docker tag javaone/${rootArtifactId}-impl:1.0-SNAPSHOT registry.ng.bluemix.net/javaone/${rootArtifactId}-impl:1.0-SNAPSHOT
    docker push registry.ng.bluemix.net/javaone/${rootArtifactId}-impl:1.0-SNAPSHOT
    ```

9.  Deploy the service to Kubernetes:
    ```
    kubectl create -f deploy/kubernetes/resources/service/
    ```

10. Wait for the service to begin running:
    ```
    kubectl get -w pod ${rootArtifactId}-0
    ```
    Press control-C to exit once this prints a line with "1/1" and "Running".

** Your room is up!**

Do a quick verification: http://169.60.34.197/${rootArtifactId}

$symbol_pound$symbol_pound Register your room

1.  Go to [Game On!](https://gameontext.org) and sign in.

2.  Click on the building icon in the top right of the game screen to go to the Room Management page.

3.  Make sure **Create a new room** is selected from the **Select a room** drop-down.

4.  Provide a descriptive title for your room, e.g. `Paul's Diner`, 'The Red Caboose', ... 

5.  A short nickname will be generated, but please change the value to `${rootArtifactId}`.

6.  Describe your room (optional). The description provided here is used by the interactive map and other lists or indexes of defined rooms. The decription seen in the game will come from your code.

7. The repository field is optional. Come back and fill it in if you decide to push this into a public repository.

8.  Specify the http endpoint as a basic health endpoint: `http://169.60.34.197/${rootArtifactId}`

9.  Use a WebSocket URL for the WebSocket endpoint: `ws://169.60.34.197/${rootArtifactId}`

10. Leave the token blank for now. That is an [Advanced adventure](https://book.gameontext.org/walkthroughs/createMore.html) for another time.

11. Describe the doors to your room (Optional). Describe each door as seen [from the outside](https://book.gameontext.org/walkthroughs/registerRoom.html#doors)

12.  Click **Register** to register the room and add it to the Map!

You can come back to this page to update your room registrations at any time. Choose the room you want to update from the drop-down, make any desired changes, and click either **Update** to save the changes or **Delete** to delete the registration entirely.

$symbol_pound$symbol_pound Hello World!

Use the arrow in the top right to go back to the game screen. Go Play!

* Use `/help` to see available commands (will vary by room).
* Use `/exits` to list the exits in the room.

Remember that shortname you set earlier? To visit your room:

    /teleport <nickname>


It should show something like this: 


> Connecting to Game On Lab. Please hold.
> 
> **Room's descriptive full name**  
> Lots of text about what the room looks like

That isn't very original now, is it? For a first kick of the tires, let's make that a little more friendly.

$symbol_pound$symbol_pound Editing your room

1. Import your project into IDE of choice
  * Using Eclipse
    1. Start Eclipse
    2. **File** -> **Import**, 
    3. Type `maven` to filter and select **Existing Maven project**. 
    4. Click **Next**
    5. Navigate to your project folder `~/${rootArtifactId}`. 
    6. Click **Finish**

  * Using IntelliJ IDEA
    1. From the Welcome screen, click **Import project**
    2. Navigate to your project folder `~/${rootArtifactId}`. 
    3. Click **OK**
    4. On the 'Import Project' screen, select **Import from external model** and choose 
    5. Allow maven projects to _Import projects automatically_. Use default values and the **Next** button until you get to click **Finish**

  * Importing the `~/${rootArtifactId}` folder into an IDE will create two folders of note: 
    - `${rootArtifactId}-api` -- service api
    - `${rootArtifactId}-impl` -- service implementation 

2. In the `${rootArtifactId}-impl` project, look in `src/main/java` to open  `com/lightbend/lagom/gameon/${rootArtifactId}/impl/Room.java`

3. The following constants are defined near line 26: 
  
      static final String FULL_NAME = "Room's descriptive full name";
      static final String DESCRIPTION = "Lots of text about what the room looks like";
  
  Change those to something that suits you better! 

4. Rebuild the docker image
    ```
    mvn clean package docker:build
    ```

5. Re-tag and push the updated Docker image to the Bluemix registry:
    ```
    docker tag javaone/${rootArtifactId}-impl:1.0-SNAPSHOT registry.ng.bluemix.net/javaone/${rootArtifactId}-impl:1.0-SNAPSHOT
    docker push registry.ng.bluemix.net/javaone/${rootArtifactId}-impl:1.0-SNAPSHOT
    ```

6. To make our updated Room service live, we just need to delete the pod and let Kubernetes recreate it. The 'always' image pull policy ensures that Kubernetes will grab the latest Docker image when it recreates the pod.
    ```
    kubectl delete pod ${rootArtifactId}
    ```
    
7. Wait for the service to begin running:
    ```
    kubectl get -w pod ${rootArtifactId}-0
    ```
Press control-C to exit once this prints a line with "1/1" and "Running".