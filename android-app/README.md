# D-Lish Creations — Mobile App (In Progress)

## Overview

A Kotlin/Jetpack Compose Android application for browsing and ordering custom cake toppers, featuring real-time cart management and a built-in customer support chat that communicates with a Python Flask backend over HTTP.

## Development Environment

The application was developed using Android Studio with Jetpack Compose for building the user interface. Git and GitHub were used for version control and project management.

The app is written in Kotlin and Java and utilizes Android Jetpack libraries including:

- Jetpack Compose for UI design
- Navigation Compose for multi-screen navigation
- Material 3 components for styling and layout
- OkHttp for HTTP network requests
- Kotlin Coroutines for asynchronous background threading

The backend server is built with Python and Flask. See the `backend` folder for setup instructions.


## Future Work

- Improve UI design with more polished styling and layout consistency
- Add persistent storage so the cart is saved between app sessions
- Implement product images and a more detailed product view
- Add a checkout screen with total pricing and order summary
- Implement live chat so the shop owner can respond to customer messages in real time
- Add user authentication so customers can log in and track their orders
