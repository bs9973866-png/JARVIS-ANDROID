# JARVIS Roadmap

## 0.1 — Voice prototype
- [x] Dark Android screen
- [x] Push-to-talk speech recognition
- [x] Android TextToSpeech replies
- [x] Basic local demo commands

## 0.2 — AI foundation
- [x] Provider abstraction
- [x] Hybrid/cloud/local mode model
- [x] Deterministic offline fallback
- [x] Local conversation history
- [ ] Wire AssistantCore into the UI
- [ ] Add settings for AI mode
- [ ] Add a secure backend for cloud provider credentials

## 0.3 — Voice experience
- [ ] Continuous conversation session
- [ ] User-controlled wake-word foreground service
- [ ] Listening/thinking/speaking UI states
- [ ] Explicit microphone privacy controls

## 0.4 — Android actions
- [ ] Launch selected apps
- [ ] Read device status available through public Android APIs
- [ ] Confirm consequential actions before execution

## Security principles
- Keep provider API keys off-device; use a backend proxy.
- Do not collect or upload audio without clear user consent.
- Make microphone activity visible and user-controllable.
- Keep conversation history local by default and provide a clear-history action.
