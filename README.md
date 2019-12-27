# Film Reel
This app helps you to browse for top rated, popular, now playing and upcoming movies which are retrieved from https://www.themoviedb.org/

## Final project for [OTUS Android course](https://otus.ru/lessons/basic-android/)
!<img src="https://github.com/intulion/FilmReel/blob/master/screenshots/now_playing.png" vspace="5" align= "center" width=275 >
!<img src="https://github.com/intulion/FilmReel/blob/master/screenshots/details.png" vspace="5" align= "center" width=275 >

## Features
* MVVM architecture
* Room database
* Navigation component
* Dependency injection with Dagger 2
* Async work with RxJava 2 
* Espresso UI-tests
* Tests with MockWebServer 
* Homescreen Widget that shows #1 now playing movie

## Installing
1. Get TMDb API key from [TMDb website](https://www.themoviedb.org/documentation/api)
2. Get Google Places API key from [Places API website](https://developers.google.com/places/web-service/get-api-key)
3. Get Google Maps API key from [Maps SDK for Android](https://developers.google.com/maps/documentation/android-sdk/get-api-key)
4. Create a local `gradle.properties` file and store the API keys there

```
FilmReel_ApiKey="TMDb API key here"
FilmReel_MapsKey="Places API key here"
FilmReel_PlacesApiKey="Maps API key here"
```

## Libraries
*   [AndroidX](https://developer.android.com/jetpack/androidx/)
*   [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
*   [Navigation component](https://developer.android.com/guide/navigation)
*   [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
*   [Room](https://developer.android.com/topic/libraries/architecture/room)
*   [Retrofit 2](https://github.com/square/retrofit)
*   [Glide](https://github.com/bumptech/glide)
*   [Dagger2](https://google.github.io/dagger/users-guide)
*   [RxJava2](https://github.com/ReactiveX/RxJava)
*   [OkHttp3](https://square.github.io/okhttp)

## Author
Anton Karpenko

## License
This project is licensed under the Apache License 2.0 - See: http://www.apache.org/licenses/LICENSE-2.0.txt
