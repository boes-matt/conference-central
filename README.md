# Conference Central

This is a Gradle-based, partially complete (through lesson 4.5) backend and Android app for the Conference Central project in Udacity's "Developing Scalable Apps".
The backend and frontend code was developed entirely in Android Studio. 

The Android frontend is fairly minimal.  It only serves to show how to connect to the backend through Cloud Endpoints,
and how to authenticate the user using his/her Google Account on the phone without requiring any login.

This combined frontend/backend project uses Google's App Engine, Datastore, and Cloud Endpoints.
It also uses Objectify to simplify the Datastore API on the backend.  Finally, there are tests in the backend module to show how testing is done.

Credit goes to the [Udacity course](https://www.udacity.com/course/ud859), its [backend repo](https://github.com/udacity/ud859), and its [Android app repo](https://github.com/udacity/conference-central-android-app).
References are the [App Engine Endpoints Gradle template](https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints) and the [Objectify documentation](https://code.google.com/p/objectify-appengine/wiki/Concepts).

Keys referenced in the PrivateConstants class in both the backend and frontend are not included in the repo.  
You will have to generate your own in [Google's Developer Console](console.developers.google.com).
The application id "alien-program-642" in appengine-web.xml also needs to be generated and replaced.
