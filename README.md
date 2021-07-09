# Eclipse LSP4Jakarta 
[![License](https://img.shields.io/badge/License-EPL%202.0-red.svg?label=license&logo=eclipse)](https://www.eclipse.org/legal/epl-2.0/) [![Build](https://github.com/eclipse/lsp4jakarta/workflows/Java%20CI%20-%20LSP4Jakarta/badge.svg)](https://github.com/eclipse/lsp4jakarta/actions)

The Eclipse LSP4Jakarta (Language Server for Jakarta EE) project provides core language support capabilities for the specifications defined under the Jakarta EE (EE4J) umbrella.

This project contains: 

- [lsp4jakarta](/lsp4jakarta) - Language Server for Jakarta EE
- [jakarta-eclipse](/jakarta-eclipse) - Eclipse JDT LS extension and Eclipse IDE client that consumes the Language Server for Jakarta EE

## Pre-requisites

1. Ensure that you have installed [JavaSE-11](https://www.oracle.com/ca-en/java/technologies/javase-jdk11-downloads.html)

2. Ensure that you have [Maven](https://maven.apache.org/download.cgi) installed

3. Ensure that you have installed [Eclipse](https://www.eclipse.org/downloads/)

## Getting Started

1. Install Eclipse Plugin Development Environment (step 1 here: https://medium.com/@ravi_theja/creating-your-first-eclipse-plugin-9b1b5ba33b58)

2. Clone this repository onto your local machine (`git clone git@github.com:eclipse/lsp4jakarta.git`)

3. Import `org.eclipse.lsp4jakarta.core`, `org.eclipse.lsp4jakarta.tests` and `lsp4jakarta` projects in Eclipse (File --> Import --> General --> Projects from  Folder or Archive --> Select your LSP4Jakarta clone)

4. Ensure that projects are being built with `JavaSE-11` ("Right-click project" --> "Properties" --> "Java Build Path" --> "Libraries")

5. Run the `./buildAll.sh` script to build the `lsp4jakarta-1.0-SNAPSHOT-jar-with-dependencies.jar`. This script also copies the `lsp4jakarta-1.0-SNAPSHOT-jar-with-dependencies.jar` to the `/jakarta-eclipse/org.eclipse.lsp4jakarta.core` directory 

6. Ensure that `lsp4jakarta-1.0-SNAPSHOT-jar-with-dependencies.jar` jar is on the Java Build Path for the `org.eclipse.lsp4jakarta.core` project

7. Run `org.eclipse.lsp4jakarta.core` as an Eclipse Application to launch a new instance of Eclipse with LSP4Jakarta (Right-click on the `org.eclipse.lsp4jakarta.core` project, "Run As" --> "Eclipse Application")

To run the tests:
- Run `mvn verify` from the `jakarta-eclipse` folder

## Common errors 

1. When setting up the Eclipse workspace: 
- *Bundle 'org.apache.commons.lang3' cannot be resolved* in Eclipse worskspace [#46](https://github.com/eclipse/lsp4jakarta/issues/46)

2. If `mvn verify` returns errors or compilation failures, verify that you are using JavaSE-11. You may have to configure $JAVA_HOME variable and make sure it pointing to the correct location.

## Contributing

Our [CONTRIBUTING](CONTRIBUTING.md) document contains details for submitting pull requests.

## Feedback

Please report bugs, issues and feature requests by creating a [GitHub issue](https://github.com/eclipse/lsp4jakarta/issues).
