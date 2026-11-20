# J.A.R.V.I.S. — Android Assistant

**Prototype 0.1.0** · Kotlin · Android

Personal Android voice assistant. This first milestone provides a dark interface, Russian push-to-talk speech recognition, a small local command router, and spoken replies through Android TextToSpeech.

## Prototype features
- Dark JARVIS home screen
- Microphone permission request
- Speech input through the Android speech-recognition service
- Local demo commands: greeting, current time, date, identity
- Spoken responses through Android TextToSpeech

## Planned milestones
1. Core prototype — UI, speech input/output, local command routing
2. AI engine — cloud/local model adapters
3. Memory — Room database and user-controlled memory
4. Android actions — permission-aware device/app actions
5. Wake word — opt-in foreground listening service with persistent notification
6. Polished UI — animated core and listening/thinking/speaking states

## Build requirements
- Android Gradle Plugin 8.7.3
- Kotlin 2.0.21
- JDK 17
- compileSdk 35
- minSdk 26

Open this project in an Android IDE with Kotlin and Gradle support, sync, then build the debug APK.

## Notes
- Always-on wake-word detection is not implemented yet.
- Speech recognition availability/offline support depends on the speech service installed on the device.
- Background microphone use requires a user-visible foreground service and applicable Android restrictions.
- Cloud AI is not connected. Never embed private API keys in the Android app.
- Device actions will require appropriate permissions and confirmation for consequential operations.

Package: `com.jarvis.ai`
