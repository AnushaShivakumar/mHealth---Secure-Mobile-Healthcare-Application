
# mHealth - Secure Mobile Healthcare Application
---
## Project Overview
The **mHealth** project is a secure, patient-centric mobile healthcare application designed to manage and store healthcare data efficiently. Built using modern tools and technologies, this application ensures secure user authentication and scalability for future enhancements.

---

## Features
- **User Authentication**: Implements secure registration, login, and password reset functionality using Firebase Authentication.
- **Data Security**: Utilizes Google KMS for encrypting sensitive healthcare data.
- **Data Organization**: Stores and manages patient data efficiently using Firebase Realtime Database.
- **Responsive Design**: Provides a seamless user experience optimized for mobile devices.
- **Foundation for Scalability**: Designed to support advanced features like machine learning and proximity-based access control.

---

## Technology Stack
- **Languages**: Java, Kotlin
- **Frameworks/Tools**:
  - Android Studio
  - Firebase Authentication
  - Firebase Realtime Database
  - Google KMS
- **Build Tools**: Gradle

---

## Setup Instructions
To set up and run the application locally, follow these steps:

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/AnushaShivakumar/mHealth---Secure-Mobile-Healthcare-Application.git
   cd mHealth---Secure-Mobile-Healthcare-Application


2. **Open in Android Studio**:
   - Open the project folder in Android Studio.
   - Ensure you have the required SDKs and dependencies installed.

3. **Firebase Configuration**:
   - Go to the [Firebase Console](https://console.firebase.google.com/) and create a project.
   - Download the `google-services.json` file and place it in the `app/` directory of the project.
   - Ensure Firebase Authentication and Realtime Database are enabled.

4. **Google KMS Configuration**:
   - Set up Google Cloud Key Management Service (KMS) for encryption.
   - Add the necessary API keys and configurations in your project.

5. **Build and Run**:
   - Sync Gradle files in Android Studio.
   - Run the application on an emulator or physical device.

---

## Folder Structure
```
mHealth/
│
├── app/
│   ├── src/main/
│   │   ├── java/com/example/mhealth/  # Java/Kotlin source code
│   │   ├── res/                       # UI resources (layouts, drawables, etc.)
│   │   └── AndroidManifest.xml        # Android manifest file
│   ├── build.gradle                   # Module-level Gradle file
│   └── google-services.json           # Firebase configuration file
├── gradle/                            # Gradle wrapper files
├── settings.gradle                    # Project-level Gradle settings
└── README.md                          # Project documentation
```

---


## Acknowledgments
- [Firebase Documentation](https://firebase.google.com/docs)
- [Google Cloud KMS](https://cloud.google.com/kms/docs)
- [Android Studio](https://developer.android.com/studio)
```

