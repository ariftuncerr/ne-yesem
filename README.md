# 🍽️ Ne Yesem

**Ne Yesem** is a smart meal planning and recipe management Android app built with **Kotlin (MVVM)**.  
It helps you discover recipes, manage your fridge & pantry, and get personalized suggestions powered by the Spoonacular API.

---

## ✨ Features
- 🔍 **Search by Dish Type** – Quickly find recipes by categories (e.g. breakfast, dessert, main dish)
- 🥘 **Ingredient-Based Suggestions** – Get recipe recommendations with available ingredients
- 📦 **Fridge & Pantry Management** – Track what you have at home
- ⭐ **Favorites** – Save and revisit your favorite recipes
- 🔐 **Firebase Authentication** – Sign in with email or Google
- ⚡ **Modern Material3 UI** – Smooth and responsive user interface

---

## 📲 App Video / Demo
<div align="center">
  <video src="https://github.com/user-attachments/assets/e3cbfc9b-d886-4b1a-a558-e2b4f3ffb982"
         controls
         muted
         loop
         playsinline
         width="360">
  </video>
</div>
---

## 🛠️ Tech Stack

- Kotlin Clean + MVVM Architecture
- XML layouts
- Retrofit, Coroutines, Glide, Room, Firebase, Firetore 
- Firebase Authentication & Firestore
- Spoonacular API integration
- Material3 / Jetpack components

## 📌 Project Structure
 ```bash
- neyesem/
 ┣ app/
 ┃ ┣ src/
 ┃ ┃ ┣ main/
 ┃ ┃ ┃ ┣ java/com/ariftuncer/ne_yesem/
 ┃ ┃ ┃ ┃ ┣ data/             # DTOs, Repository Implementations
 ┃ ┃ ┃ ┃ ┣ domain/           # Models, UseCases, Interfaces
 ┃ ┃ ┃ ┃ ┣ presentation/
 ┃ ┃ ┃ ┃ ┃ ┣ ui/             # Screens (Fragments, Activities)
 ┃ ┃ ┃ ┃ ┃ ┣ adapters/       # RecyclerView Adapters
 ┃ ┃ ┃ ┃ ┃ ┣ viewmodel/      # ViewModels
 ┃ ┃ ┃ ┣ res/                # Layouts, Drawables, Strings, Colors
 ┃ ┃ ┃ ┣ AndroidManifest.xml
 ┣ build.gradle              # App build config
 ┣ gradle.properties         # API keys (ignored in git)
 ┣ settings.gradle           # Project settings
 ┣ CHANGELOG.md              # Release notes
 ┗ README.md                 # Documentation
 ````

## Firebase Firestore Structure
```bash
users (collection)
 └── {userId} (document)
      ├─ email: "user@example.com"
      ├─ name: "Arif Tunçer"
      ├─ preferences
      │    ├─ diet: "vegetarian"
      │    └─ allergies: ["nuts", "gluten"]
      └─ favorites (subcollection)
           └── {recipeId} (document)
                ├─ id: 12345
                ├─ title: "Pasta Carbonara"
                └─ image: "https://..."

recentlyViewed (collection)
 └── {userId} (document)
      └─ viewed: [recipeId1, recipeId2, recipeId3]

pantry (collection)
 └── {userId} (document)
      └─ items (subcollection)
           └── {itemId} (document)
                ├─ name: "Tomato"
                ├─ unit: "kg"
                ├─ qty: 2
                └─ category: "Vegetables"
 ```



## 🚀 Installation

### Option 1 – Download APK
Go to the **[Releases](releases/1.0.0)** section and download the latest `ne-yesem.apk`.

### Option 2 – Build from Source
1. Clone the repository
   ```bash
   git clone https://github.com/<your-username>/neyesem.git
   cd neyesem

2. Add your API KEY
   ```bash
   SPOONACULAR_API_KEY=your_api_key_here
3. Build the project
   ```bash
   ./gradlew assembleDebug

   

   
