#set( $symbol_pound = '#' )

$symbol_pound Game On! with Lagom

[Lagom](https://www.lagomframework.com/) is a framework for developing reactive microservices in Java or Scala. Created by [Lightbend](https://www.lightbend.com/), Lagom is built on the proven [Akka](http://akka.io/) toolkit and [Play Framework](https://playframework.com/), and provides a highly productive, guided path for creating responsive, resilient, elastic, message-driven applications.

[Game On!](https://gameontext.org/) is both a sample microservices application, and a throwback text adventure brought to you by the WASdev team at IBM.

This project was created by the Maven archetype from https://github.com/lagom/lagom-gameon-maven-archetype.

See the `README.md` file in that repository for more information.

$symbol_pound$symbol_pound Deploying to Bluemix

1.  Log in to Bluemix with the workshop email address you were provided:
    ```
    bx login -a https://api.ng.bluemix.net
    ```

    - You will be prompted for the email address and password. Enter these as provided to you.
    - You will also be prompted to select an account. Enter "2" to choose account ID 1e892c355e0ba37560f028df670c2719.

2.  Initialize the Bluemix Container Service plugin:
    ```
    bx cs init
    ```

3.  Download the configuration files for the Kubernetes cluster:
    ```
    bx cs cluster-config javaone-gameon-test
    ```

    This should print details similar to the following, though the details might differ slightly:
    ```
    OK
    The configuration for javaone was downloaded successfully. Export environment variables to start using Kubernetes.

    export KUBECONFIG=/home/ubuntu/.bluemix/plugins/container-service/clusters/javaone/kube-config-dal10-javaone-gameon-test.yml
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
