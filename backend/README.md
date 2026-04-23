# D-Lish Creations — Networking Module

## Overview

As a software developer, one of my goals is to build applications that feel complete and connected — not just local tools, but systems that communicate across a network in real time. This project gave me the opportunity to explore that by integrating a Python Flask server with an Android mobile application I built using Kotlin and Jetpack Compose.

The networking component of D-Lish Creations is a customer support chat feature. Users of the Android app can send messages and select quick-action requests such as checking their order status or retrieving contact information. The Flask server receives these requests, processes them, and returns a response that is displayed in the app as a chat bubble.

To run the software, you will need to start both the server and the Android app:

**Starting the Flask server:**
1. Navigate to the `backend` folder
2. Install dependencies:
   ```
   pip install flask
   ```
3. Run the server:
   ```
   python server.py
   ```
   The server will start on port 5000.

**Starting the Android app:**
1. Open the `android-app` folder in Android Studio
2. Run the app on an emulator or physical device
3. Navigate to the Chat tab in the bottom navigation bar
4. The app connects to the server at `http://10.0.2.2:5000` (the emulator's localhost address)


---

## Network Communication

This project uses a **client/server** architecture. The Android app acts as the client, and the Python Flask server acts as the server.

The application communicates over **TCP** using the HTTP protocol. The Flask server listens on **port 5000**.

Messages are sent and received in **JSON format**. The following endpoints are available:

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/message` | Sends a chat message and receives a reply |
| GET | `/order-status` | Returns a mock order status |
| GET | `/contact-info` | Returns phone and email contact details |
| GET | `/history` | Returns the full message history |

Example POST request body sent from the Android app:
```json
{ "text": "Hello, I have a question." }
```

Example response from the server:
```json
{ "received": "Hello, I have a question.", "reply": "You said: Hello, I have a question." }
```

---

## Development Environment

**Tools:**
- Android Studio — Android app development and emulator
- Visual Studio Code — Flask server development
- Git and GitHub — version control

**Languages and Libraries:**

*Android (Client):*
- Kotlin — primary language
- Jetpack Compose — UI framework
- OkHttp 4.12.0 — HTTP networking library
- Kotlin Coroutines — asynchronous background threading
- Android Navigation Component — screen navigation

*Server:*
- Python 3
- Flask — lightweight web server framework

---

## Future Work

* Implement live chat so the business owner can respond to messages in real time
* Add a database to store message history persistently between sessions
* Improve the mock order status to pull from real order data
* Add push notifications so users are alerted when a response arrives
