## HBV2 - Team 7
# Physiotherapist Registration App
An app for a system that manages requests for physiotherapist clinics.

Supports creation of Users, new requests and the ability to answer health questionnaires to determine the urgency of the request.

This is a port of last year's Java Spring Application, which can be found [here](https://hbv1.onrender.com/).

### API
The API used for this app is a conversion of last year's Java Spring Application to a REST-ful API.
It is currently hosted on Render and can be accessed at [https://hbv1-api.onrender.com/api/v1/](https://hbv1-api.onrender.com/api/v1/).
* The API source code can be found [on GitHub](https://github.com/AndriFannar/HBV1/tree/api).
* The API documentation can be found [here](https://andrifannar.github.io/HBV1/target/site/index.html).

### App Requirements
This app is developed for Android, and requires an SDK level of at least 26. Gradle is used to manage dependencies and build the project.
The app is created in Java and the version used is Java 17.

### Build
To build the project, run the following command in the root directory of the project:
```./gradlew build```

### Install
To run the project, run the following command in the root directory of the project (assuming an AVD has been created and the android emulator is running):
```./gradlew installDebug```

### Run
After installing the app on an Android emulator, the app can be run by clicking on the app icon.

Note: Since the API is hosted on a free Render server, it can take some time to spin up if it has not been contacted for some time.

### Documentation
No documentation is currently available for the app. 

The API documentation can be found [here](https://andrifannar.github.io/HBV1/target/site/index.html).

### Design Documents
The design documents for the app can be viewed [here](app/site/markdown/UML.md).

### License
This project is licensed under the MIT License - see the [LICENSE](LICENSE), and [SPDX](https://spdx.org/licenses/MIT.html)

