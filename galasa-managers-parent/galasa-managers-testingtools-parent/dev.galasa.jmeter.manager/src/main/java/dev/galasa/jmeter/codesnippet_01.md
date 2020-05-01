<details><summary>Creating a JMeter session</summary>

The following snippet shows the minimum code that is required to request a JMeter session in a Galasa test:

```
@JMeterSession(jmxPath="test.jmx")
public IJMeterSession session;
```

This code requests the Docker Manager to provision a container with all the JMeter binaries that are required to run a JMX test installed. You can provision your JMX file via the Artifact Manager and point it to the bundle resources, the location of which is specified in the input stream of your JMX file. The container is discarded when the test finishes. 

The following snippet enables you to add a personal properties file to the test by pointing the Artifact Manager at the JMeter properties file.

```
@JMeterSession(jmxPath="test.jmx", propPath="jmeter.properties")
public IJMeterSession session;
```


There is no limit in Galasa on the number of JMeter sessions that can be used within a single test. The only limit is the number of containers that can be started in the Galasa Ecosystem. This limit is set by the Galasa Administrator and is typically set to the maximum number of containers that can be supported by the Docker Server or Swarm.  If there are not enough slots available for an automated run, the run is put back on the queue in *waiting* state to retry. **Local test runs fail if there are not enough container slots available.**
</details>

<details><summary>Setting a JMX file in a JMeter session by using the Artifact Manager</summary>

Use the following code to provision a JMX file by using the Artifact Manager.

```
    IBundleResources bundleResources = artifactManager.getBundleResources(getClass());
    InputStream jmxStream = bundleResources.retrieveFile("/test.jmx");
    session2.setJmxFile(jmxStream);
```
</details>

<details><summary>Setting the properties file in a JMeter session by using the Artifact Manager</summary>

Just as you would provision a JMX file via the Artifact Manager, you can use the following code to provision a personalized properties file that gets used by JMeter at runtime.

```
    IBundleResources bundleResources = artifactManager.getBundleResources(getClass());
    InputStream propStream = bundleResources.retrieveFile("/jmeter.properties");
    session.applyProperties(propStream);
```
</details>

<details><summary>Starting a JMeter session</summary>

You can set a timeout for a JMeter session or use the *default timeout of 60 seconds* for a JMeter session. To use this command, you must configure the JMX file correctly by using the `session.setJmxFile(inputStream)` method. *Timeout is in milli-seconds.*

```
    session.startJmeter();
    ...
     session.startJmeter(60000);
```
</details>

<details><summary>Obtaining the JMX file from the JMeter-execution as a String</summary>

Use the following snippet to access the JMX file that was used in the JMeter session.

```
session.getJmxFile();
```
</details>

<details><summary>Obtaining the log file from the JMeter execution as a String</summary>

Use the following snippet to access the log file that is created when the JMX file that is running inside the container finishes running.

```
session.getLogFile();
```
</details>

<details><summary>Viewing the console output as a String</summary>

Use the following snippet to view any console output that is generated by the JMeter test run. Typically, there is no console output unless the JMX file itself is corrupt or written incorrectly. If a correctly written JMX file generates errors during execution, the errors are held in the log files or in the JTL file.

```
session.getConsoleOutput();
```
</details>

<details><summary>Obtaining a generated file from the JMeter-execution as a String</summary>

Use the following snippet to help you to access any file that is created after execution of a JMX file inside a container completes. In this example, the JTL file *test.jtl* is returned as a String containing the results of the test run which can be exported to a CSV file. The name of the JTL file has the same prefix as the JMX file.

```
session.getListenerFile("test.jtl")
```
</details>


<details><summary>Checking your test ran correctly</summary>

Use the following code to check that the test ran correctly. You can use the logs and JMX files for further investigation. If the JMX file has completed its function successfully, a boolean value of true is returned, otherwise a value of false is returned.

```
session.statusTest();
```
</details>

<details><summary>Stopping the JMeter test</summary>

Use the following code to stop the JMeter test that is running inside the Docker container.

```
session.stopTest();
```
</details>