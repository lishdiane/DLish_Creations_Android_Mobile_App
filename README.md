# D-Lish Creations
 
A mobile e-commerce application for ordering custom cake toppers, built as part of a software development learning journey. This project combines three areas of focus — Java, mobile development, and networking — into a single cohesive application.
 
## Project Structure
 
```
DLish_Creations/
├── android-app/    # Kotlin/Jetpack Compose Android application
└── backend/        # Python Flask server
```
 
## What It Does
 
The D-Lish Creations app allows users to:
- Browse a catalog of custom cake topper products
- Add items to a shopping cart and adjust quantities in real time
- Send messages and requests to a backend server and receive responses
 
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
|-------|-----------|
| Mobile Client | Kotlin, Jetpack Compose, Android Studio |
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
