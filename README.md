# Calculator Android App

A simple Android calculator with:

- Addition, subtraction, multiplication and division
- Decimal numbers
- Percentage
- Sign change
- Backspace
- Clear
- Error handling for division by zero

## Automatic APK build

Every push to `main` starts GitHub Actions. The workflow builds a debug APK and uploads the artifact:

`Calculator-debug-apk`

APK path:

`app/build/outputs/apk/debug/app-debug.apk`

## App details

- Package: `com.zabed.calculator`
- Minimum Android: API 23
- Target Android: API 36
- Java/Kotlin JVM target: 17

The APK can be downloaded from the GitHub Actions run and installed on an Android phone.