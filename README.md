# Waiter the File Sharing Application

This is a simple JavaFX application that enables file sharing over the same network using socket connections. I created this project out of boredom, and it's designed to run on Linux systems.

## Features
- Share files within the same network.
- Lightweight and easy to use.
- Built with JavaFX for a simple graphical interface.
- Includes a jlink-generated runtime for direct execution on Linux.

## Prerequisites

- Linux operating system.
- Java 17 or later (if not using the prebuilt jlink runtime).

## How to Run Using run.sh

To start the application, simply execute the run.sh script:

    ./run.sh

## Using Maven

If you want to build the project and resolve dependencies, use the included Maven wrapper:

    ./mvnw clean install

# Jlink Runtime

A prebuilt jlink runtime is included for Linux users. You can run the application directly without needing a system-wide JDK installation.
Notes

- This application is designed specifically for Linux. Compatibility with other operating systems has not been tested.
- Ensure that all devices are connected to the same network for file sharing to work properly.
- Make sure to unzip src/main/resources/img.zip File

<div style="display: flex;">
<img src="https://github.com/ArithaBandara/Green-House-application/blob/main/application_/welcome.jpg" alt="Greenhouse" width="250" />
<img src="https://github.com/ArithaBandara/Green-House-application/blob/main/application_/home.jpg" alt="Greenhouse" width="250" />
<img src="https://github.com/ArithaBandara/Green-House-application/blob/main/application_/dashbord.jpg" alt="Greenhouse" width="250" />
<img src="https://github.com/ArithaBandara/Green-House-application/blob/main/application_/weather.jpg" alt="Greenhouse" width="250" />
</div>
