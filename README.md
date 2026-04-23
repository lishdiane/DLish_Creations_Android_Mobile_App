# D-Lish Creations
 
A full-stack mobile application built with a Kotlin/Jetpack Compose Android client and a Python Flask backend server. The two components communicate over HTTP, with the Android app sending requests and the Flask server handling responses.

> 🚧 Active development — additional features in progress
 
## What It Does
- Browse and order custom cake toppers from a mobile interface
- Manage a shopping cart with real-time quantity updates
- Chat with the shop through a support screen backed by a live server


## Project Structure

```
DLish_Creations/
├── android-app/    # Kotlin/Jetpack Compose Android application
└── backend/        # Python Flask server
```

See each folder's README for setup and implementation details.

## How To Run
 
**1. Start the Flask server** (see `backend/README.md` for full details):
```
cd backend
pip install flask
python server.py
```
 
**2. Open the Android app** in Android Studio:
- Open the `android-app` folder in Android Studio
- Run on an emulator or physical device
- Navigate to the Chat tab to interact with the server
 
The Android emulator connects to the server at `http://10.0.2.2:5000`.

## Tech Stack
| Layer | Technology |
|---|---|
| Mobile Client | Kotlin, Jetpack Compose |
| Networking | OkHttp, Kotlin Coroutines |
| Backend Server | Python, Flask |
| Version Control | Git, GitHub |
 
## Documentation
 
- [Android App README](android-app/README.md)
- [Backend Server README](backend/README.md)
 
## Author
 
**Diane Lish**
- LinkedIn: [diane-lish](https://linkedin.com/in/diane-lish-8b4b5529b)
- GitHub: [lishdiane](https://github.com/lishdiane)
