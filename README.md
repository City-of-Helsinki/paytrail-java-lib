Unofficial Paytrail Java Library
=

See documentation at https://docs.paytrail.com/#/

# Install

#### Example for maven:
```
  <dependencies>
    <!-- Paytrail custom java lib -->
    <dependency>
        <groupId>org.helsinki</groupId>
        <artifactId>paytrail-java-lib-core</artifactId>
        <version>1.0.1-SNAPSHOT</version>
        <exclusions>
            <exclusion>
                <groupId>ch.qos.logback</groupId>
                <artifactId>logback-classic</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
  </dependencies>
```

## Debug request, response and request headers to debug/ folder as json files

### debug/ folder is ignored from git

#### Example for setting environment variable to debug client:
```
  # Linux
  export DEBUG_CLIENT=true
  
  # Windows
  set DEBUG_CLIENT=true
```
