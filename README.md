# NewsGlance - Android News Application

## Project Description
NewsGlance is an Android application that allows users to browse and view details of various news articles. The news is fetched from a REST API and presented to the user with a modern interface. The application also offers additional features such as offline support, search, and article filtering.

## Technical Overview

The NewsGlance application is built using modern Android development tools and libraries, adhering to the principles of Clean Architecture and MVVM (Model-View-ViewModel) for scalability and maintainability. This ensures a clear separation between the business logic, data management, and UI components.

- **Jetpack Compose**: The UI is built using Jetpack Compose, Android's declarative UI toolkit, allowing for flexible and dynamic interface development with less code.
- **Hilt**: Dependency Injection is handled by Hilt, ensuring loose coupling between components and improving testability.
- **Flow**: Kotlin Flow is used for managing asynchronous data streams, enabling reactive data handling across the app.
- **Room**: Jetpack Room is used for database operations, providing local data storage and enabling offline support by caching news articles.
- **Retrofit**: Retrofit is used for fetching news data from REST APIs, offering a modern and convenient HTTP client.
- **Coil**: Coil is utilized for efficient image loading, providing fast and lightweight handling of news article images.
- **Navigation Component**: Navigation between screens is handled by the Navigation Component, ensuring safe and structured navigation throughout the app.
- **Firebase Crashlytics**: Firebase Crashlytics is integrated for tracking crashes and bugs, allowing easy identification and resolution of issues in the app.
- **Firebase Analytics**: Firebase Analytics is used to track user behavior and analyze the app’s performance metrics.
- **Firebase Cloud Messaging (FCM)**: Firebase Cloud Messaging is used to send real-time push notifications to users.
- **WorkManager**: WorkManager is employed for reliably executing background tasks, such as periodic updates of news content.
- **Kotlin Coroutines**: Kotlin Coroutines are used to manage asynchronous operations efficiently and with minimal boilerplate code.

# Screens

## Onboarding Screen

The onboarding screen is designed to welcome users when they open the app for the first time. It introduces the app’s main features and guides users through its functionality in a simple and informative way. After completing these steps, users are directed to the main screen.

The onboarding screens are only shown the first time the app is opened. Once the user completes the onboarding process, these screens will not appear again. This control is managed using SharedPreferences, where the completion status is stored on the device, determining whether the onboarding screens should be shown again.

![onboardgif](https://github.com/user-attachments/assets/0b530254-f28c-4e97-9aca-fb5759d6f9c9)


## Home Screen

The home screen is designed to allow users to browse, search, and sort news articles efficiently. It provides a dynamic interface that helps users quickly scan through current news items.

### Search Functionality
At the top of the home screen, there is a search box where users can filter news articles by their title. The search query is sent to the REST API, which uses the endpoint `https://newsapi.org/v2/everything` to fetch the results. The search operation is handled asynchronously using Coroutines and Flow to ensure smooth and responsive UI updates.

### Initial Search and Saving the Last Searched Title
When the app is first opened, it automatically searches for news with the default title "android," displaying the results on the home screen. If the user performs a search with a different title, that title is saved on the device using **SharedPreferences**. The next time the app is opened, the last searched title is used to refresh the page automatically, ensuring that the user sees the most recent search results. This creates a seamless experience, allowing the user to quickly access the news they last searched for.

### Coroutine and Flow
Asynchronous data operations are managed using Kotlin Coroutines and Flow. This provides efficient data fetching and reactive updates to the UI as news data is fetched and displayed. Flow observes the news data and reacts to user interactions by updating the screen accordingly.

### Sorting Feature
Users can sort news articles based on publication date, relevance, or popularity. This sorting is done via a modal bottom sheet that appears at the bottom of the screen. Once the user selects a sorting option, it is saved using SharedPreferences, ensuring that the chosen option is remembered for future sessions.

### Top News Carousel
At the top of the home screen, there is a news carousel that displays the top 5 news articles, showing their images and titles. The carousel can be manually swiped or will automatically scroll through the articles at intervals. Tapping on a news card will navigate the user to a detail screen where they can view the full article information.

### Shimmer Effect
While the news data is loading, animated **shimmer** placeholders are shown in place of the actual news items. These shimmer components provide a smooth visual experience, giving the user a sense of progress while waiting for the data to load.

### Pull-to-Refresh
Users can manually refresh the news feed by pulling the screen down from the top (pull-to-refresh). This allows them to fetch the latest news articles easily. During the refresh operation, a loading indicator is shown, providing visual feedback that the operation is in progress.

### News List and Coil Usage
Each news item displayed on the home screen shows an image, title, author, and publication date. Images are loaded using the Coil library, which ensures fast and efficient image loading. Coil is optimized for loading images quickly, providing a smooth user experience. The news item also displays the author's name, the news title, and the publication date. The title is limited to two lines, and longer titles are truncated with an **ellipsis** for a clean display.


![4 (1)](https://github.com/user-attachments/assets/68206d29-c8fd-4822-beb9-e1ed99149830)

## Detail Screen (DetailArticleScreen)

The detail screen is designed to display detailed information about a news article that the user clicked on from the home screen. It shows information such as the article title, image, author, publication date, and description. There is also a button to open the article in the original web source.

### Technologies and Usages

- **Hilt**: Dependency injection is managed using Hilt, making the `DetailViewModel` independent. The ViewModel is injected via Hilt and handles the news data for the screen.
- **State Management (StateFlow)**: The news data is managed reactively using `StateFlow`. When the news data is updated, the screen automatically reflects the new state.
- **Coil**: News images are loaded using the Coil library. Coil efficiently loads both normal and SVG format images, ensuring quick and smooth image rendering.
- **Composable Functions**: The detail screen is built using Jetpack Compose, with each component defined as a separate `Composable` function, allowing for a flexible and maintainable UI structure.
- **Navigation Component**: The Navigation Component is used to allow the user to either go back to the previous screen or open the original article in a web browser. The web URL is encoded and passed securely to the WebView screen for safe navigation.



![5](https://github.com/user-attachments/assets/4e71e3f8-5ef1-4888-b965-1eb0a1cb4bc1)


## WebView Screen (WebViewScreen)

The WebView screen allows users to view the original source of the news article directly within the app. It uses a built-in WebView component to load and display the web page. The URL of the news article is passed to the WebView, enabling the user to access the source without leaving the app.

### Technologies and Usages

- **Hilt**: Dependency injection is handled using Hilt, with the `WebViewViewModel` injected via Hilt. This ensures that the ViewModel, which manages the URL and loading state, is independent and reusable.
- **State Management (StateFlow)**: The loading state and URL are managed reactively using `StateFlow`. When the page is fully loaded, the state is updated via `StateFlow` to reflect the completion of the loading process.
- **AndroidView**: The WebView component is integrated into Compose using `AndroidView`, allowing traditional Android components to be used within a Compose screen.
- **WebView**: The WebView component allows users to view the news article's source directly within the app. JavaScript is enabled within the WebView, and the `onPageFinished` method is used to track when the page has finished loading.
- **CircularProgressIndicator**: A loading indicator (CircularProgressIndicator) is displayed while the page is loading. Once the page is fully loaded, the indicator disappears, providing feedback to the user during the loading process.


![6](https://github.com/user-attachments/assets/dd8b8c1a-a3b3-4c8f-8107-28dd08c3075e)


## Settings Screen (SettingScreen)

The settings screen provides users with options to learn more about the app, view frequently asked questions (FAQ), and send feedback. It includes three main options: About, FAQ, and Send Feedback.

### Technologies and Usages

- **Navigation Component**: Integration with the `WebViewScreen` allows users to navigate to web pages when they click on the "About" or "FAQ" options. The URLs are securely encoded and passed during navigation.
- **Intent Usage**: When the user selects the feedback option, the email application is opened with a pre-configured feedback template. This is achieved using Android’s `Intent.ACTION_SENDTO` mechanism.
- **Material Design**: The user interface is designed following Material Design principles. Each settings option (About, FAQ, Send Feedback) is implemented as a **ListItem**, enriched with **Icons** for visual clarity.


![7](https://github.com/user-attachments/assets/96ba4194-a882-4bf8-bf10-580a75c22aec)

## Offline Feature

The NewsGlance application includes an offline feature that allows users to view previously loaded news articles even when they don't have an internet connection. This feature ensures that data is cached locally, enabling access to it when the device is offline.

### Technologies Used

- **Room Database**: The Room library is used to store news data locally on the device. Room provides an SQLite-based database solution that allows data to be accessed when offline. The app saves news data using Room, ensuring users can view previously loaded articles even without an internet connection.
- **Retrofit & Caching Mechanism**: When news articles are fetched from the API, Retrofit is used to retrieve the data, which is then cached on the device. In offline mode, the app serves the cached data, allowing users to access previously loaded articles.



![8](https://github.com/user-attachments/assets/01f1a300-b745-4fcf-b28e-1156e47dfffb)

## Firebase Notification Features

In the NewsGlance application, Firebase is used to provide both online and local notifications. Users can receive real-time notifications from the server and also local notifications after a specified period of time.

### Technologies Used

- **Firebase Cloud Messaging (FCM)**: The app uses Firebase Cloud Messaging (FCM) to send real-time notifications to users. With FCM, users can receive news updates, important announcements, and other app-related content in real-time.
- **Firebase Analytics**: Firebase Analytics is used to track user interactions with notifications and provide insights into user behavior within the app.
- **Local Notifications with WorkManager**: The app uses WorkManager to send local notifications. One hour after the user opens the app, WorkManager triggers a notification related to the app. This notification helps encourage users to re-engage with the app. The local notification works even when the device is offline, as it is triggered locally.


![9](https://github.com/user-attachments/assets/5a29ec33-c6cb-40e8-9424-19bfccec2218)


## Libraries and Technologies Used

In the NewsGlance application, modern Android development tools and libraries were selected to enhance performance, scalability, and user experience. Each library has been carefully chosen for its benefits in specific use cases.

### 1. **Retrofit**
- **Why Chosen?**: Retrofit is one of the most popular libraries for managing HTTP requests. It provides a simple API and offers automatic JSON-to-object conversion.
- **Advantages**:
  - Easy to use and quick to integrate.
  - Advanced error handling and request retry options.
  - Seamless integration with JSON converters like Gson and Moshi.

### 2. **Coil**
- **Why Chosen?**: Coil is a fast and lightweight image loading library designed for modern Android applications. It excels at quickly loading images while optimizing memory usage.
- **Advantages**:
  - Natural integration with Kotlin Coroutines.
  - Lightweight with efficient memory management.
  - Support for SVG and other image formats.

### 3. **Room**
- **Why Chosen?**: Room is Google's SQLite abstraction library for managing database operations. It was selected for reliable data storage and offline access capabilities.
- **Advantages**:
  - Makes local database operations safer and easier.
  - Ensures developers can write SQLite queries simply and without errors.
  - Seamless integration with reactive data sources like LiveData and Flow.

### 4. **Firebase Cloud Messaging (FCM)**
- **Why Chosen?**: FCM is a robust and reliable solution for sending real-time notifications to users. It is used for both local and server-based notifications.
- **Advantages**:
  - Fast and reliable solution for real-time notifications.
  - Cross-platform support for both iOS and Android.
  - Integrates with Firebase Analytics for advanced tracking and user segmentation.

### 5. **Hilt**
- **Why Chosen?**: Hilt is a dependency injection library designed to simplify dependency management in Android applications. It was selected for securely and cleanly managing dependencies.
- **Advantages**:
  - Simple and intuitive API.
  - High testability and low coupling.
  - Seamless integration with Android components like ViewModel and WorkManager.

### 6. **Kotlin Coroutines**
- **Why Chosen?**: Coroutines were chosen for managing asynchronous operations in a simple and efficient way. They allow background tasks to be handled without blocking the UI.
- **Advantages**:
  - Simplified structure for managing asynchronous tasks.
  - Less memory usage and improved performance.
  - Automatic thread management, reducing the need for manual handling.





