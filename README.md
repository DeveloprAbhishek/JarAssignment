# Onboarding Animation Implementation 🎬

This project is an **Android assignment** demonstrating a modern onboarding flow with animations, designed according to the provided [Figma prototype](https://www.figma.com/design/ampUCP1qi5pGxZvmiG7jh1/Jar_Dev_Assignment?node-id=0-1&t=RHfHy407LYpnZIt4-1) and powered by data from the given [API endpoint](https://api.npoint.io/796729cca6c55a7d089e).

📸 Demo
Onboarding Screens	Animations


https://github.com/user-attachments/assets/7b5de45b-ffb1-4e09-a663-86bf933011a9


 
The focus is on:
- Clean **MVVM/MVI architecture**
- **Jetpack Compose** for UI
- **Coroutines + Flow** for async work
- **Retrofit + OkHttp** for networking
- Smooth onboarding **animations and transitions**

---

## ✨ Features

- Multi-screen onboarding with data loaded from API
- Page animations & transitions matching the Figma prototype
- Swipe and button navigation (`Next`, `Skip`)
- Optimized image loading with **Coil**
- Error and loading states
- Adaptive light/dark theme
- Fully written in **Kotlin + Compose**

---

## 🛠️ Tech Stack

- **Kotlin**
- **Jetpack Compose** – declarative UI
- **Navigation-Compose** – in-app navigation
- **ViewModel + StateFlow** – state management (MVVM/MVI style)
- **Retrofit + OkHttp** – API integration
- **Moshi / Kotlinx Serialization** – JSON parsing
- **Coil** – image loading
- **Coroutines** – async operations
- **Hilt** – dependency injection
- **JUnit / MockWebServer / Mockito** – testing

---

## 📂 Project Structure
```text
├── data
│   ├── model
│   │   └── OnboardingResponse.kt
│   ├── remote
│   │   └── ApiService.kt
│   └── repository
│       └── OnboardingRepository.kt
├── di
│   ├── NetworkModule.kt
│   └── RepositoryModule.kt
├── JarAssignmentApp.kt
├── LandingActivity.kt
├── OnboardingActivity.kt
├── presentation
│   ├── ui
│   │   ├── components
│   │   │   ├── ErrorComposable.kt
│   │   │   ├── IntroComposable.kt
│   │   │   ├── LoadingComposable.kt
│   │   │   ├── OnboardingCardComposable.kt
│   │   │   ├── OnboardingCardsListComposable.kt
│   │   │   ├── OnboardingScreenComposable.kt
│   │   │   └── TopBarComposable.kt
│   │   └── theme
│   │       ├── Color.kt
│   │       ├── Theme.kt
│   │       └── Typography.kt
│   └── viewmodel
│       ├── OnboardingUiState.kt
│       └── OnboardingViewModel.kt
└── utils
    └── AppConstants.kt
```



