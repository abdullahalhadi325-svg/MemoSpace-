# MemoSpace — Android Productivity & Personal Knowledge App

MemoSpace is a fast, simple, offline-first personal productivity and knowledge-management application built with Kotlin, Jetpack Compose, Material 3, and Room Database.

## Features Included
- **Notes, Tasks & Checklists**: Create and edit rich notes, tasks with checkboxes, and checklists with strike-through and reordering.
- **Offline-First Room Persistence**: Local SQLite database via Room with reactive Flow updates.
- **Debounced Auto-Save**: Automatically saves notes as you type without requiring manual save buttons.
- **Custom Color Palette**: 10 distinct, calm note colors tailored for light and dark themes.
- **Pin & Archive System**: Pin important notes to the top or archive them safely.
- **Home Workspace**: Filter notes by All, Notes, Tasks, Pinned, and Today.
- **Grid & List Layouts**: Switch seamlessly between modern 2-column staggered card grid and compact list view.
- **Expandable Speed-Dial FAB**: Quick creation of Notes, Tasks, Checklists, and Image Notes.
- **Original Branding & App Icon**: Minimalist memo card combined with a planetary orbit and satellite motif.
- **Material 3 Theming**: Full support for Light, Dark, and System Default themes with Unicode/Bangla typography fallback.

## How to Build APK
1. Open this project directory in **Android Studio** (Hedgehog, Iguana, Jellyfish, Koala or newer).
2. Allow Gradle sync to complete (JDK 17 required).
3. Build the debug APK via menu:
   `Build > Build Bundle(s) / APK(s) > Build APK(s)`
   Or run via terminal:
   ```bash
   ./gradlew assembleDebug
   ```
4. The compiled APK will be generated at:
   `app/build/outputs/apk/debug/app-debug.apk`
