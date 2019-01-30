# Commander League Tracker

The Commander League Tracker is a web application that helps track your MTG Commander and Brawl games across multiple league sessions.

## Getting Started

1. Clone the project repos

```bash
git clone https://github.com/Tardigrade505/leaguetrackerbackend.git
git clone https://github.com/Tardigrade505/leaguetrackerui.git
```

Currently, the project is split into two separate repos -- a backend repo and a UI repo. To get started, you will need both of these repos.

### Prerequisites

Java 8 or later JDK: https://www.oracle.com/technetwork/java/javase/downloads/index.html

Maven: https://maven.apache.org/install.html

Google Chrome: https://www.google.com/chrome/

### Installing

1. Build the backend code using Maven

```
# Move into the cloned backend repo
cd leaguetrackerbackend

# Build the project using Maven
mvn clean install
```

2. Start the backend server

```bash
java -jar target/restapileaguetracker-0.0.1-SNAPSHOT.jar &
```

3. Open the UI

```bash
# Move into UI project
cd ../leaguetrackerui

# Open index.html
open -a "Google Chrome" index.html
```

## Deploying to Bluemix

TODO

## Built With

* [Java Spring Framework](https://spring.io/) - The backend server 
* [Maven](https://maven.apache.org/) - Dependency Management

## Contributing

TODO

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/your/project/tags). 

## Authors

**Elliot Radsliff**

See also the list of [contributors](https://github.com/Tardigrade505/leaguetrackerbackend/graphs/contributors) who participated in this project.

## License

None

## Acknowledgments

* Hat tip to anyone whose code was used
* Inspiration
* etc

