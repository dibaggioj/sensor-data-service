# Sensor Data REST API

Spring REST microservice for persisting sensor data in AWS.

## Authors
John DiBaggio

## TODOs:
* Add support for protocol buffers to decrease payload sizes and improve efficiency

## Installation

### MAC

#### Java
1. Install Java 8

#### Gradle
1. Install Gradle per [Installation Instructions](https://gradle.org/install) 

#### AWS

1. Create AWS account https://aws.amazon.com
1. Add `config` file to `~/.aws` directory containing whichever region you want to be using, e.g.,
```
[default]
region = us-west-2
```
1. Create IAM role at https://aws.amazon.com/iam and download the following files to `~/.aws`:
* `credentials`
* `rootkey.csv`  

### Linux

TODO

### Windows

TODO

## Usage
Run Spring application with:<br>
`./gradlew bootRun`
<br>or<br>
`gradle bootRun`

## Documentation

TODO

## Running Tests

TODO

## License

TODO: Write license