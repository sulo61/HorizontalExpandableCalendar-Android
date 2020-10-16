# Expandable Calendar - Android

[![JitPack](https://jitpack.io/v/tygalive/HorizontalExpandableCalendar-Android.svg)](https://jitpack.io/#tygalive/HorizontalExpandableCalendar-Android)

[![Screen1](https://i.imgur.com/8U7sU9i.png)](https://i.imgur.com/8U7sU9i.png)
[![Screen2](https://i.imgur.com/m6QVDhy.png)](https://i.imgur.com/m6QVDhy.png)

## Features

- [x] Can alternately expand between week and month mode.
- [x] Highlight events in the calendar.

## Setup

The library uses `java.time` classes via [Java 8+ API desugaring](https://developer.android.com/studio/write/java8-support#library-desugaring) for backward compatibility since these classes were added in Java 8.

#### Step 1

To setup your project for desugaring, you need to first ensure that you are using [Android Gradle plugin](https://developer.android.com/studio/releases/gradle-plugin#updating-plugin) 4.0.0 or higher.

Then include the following in your app's build.gradle file:

```groovy
android {
  defaultConfig {
    // Required ONLY when setting minSdkVersion to 20 or lower
    multiDexEnabled true
  }

  compileOptions {
    // Flag to enable support for the new language APIs
    coreLibraryDesugaringEnabled true
    // Sets Java compatibility to Java 8
    sourceCompatibility JavaVersion.VERSION_1_8
    targetCompatibility JavaVersion.VERSION_1_8
  }
}

dependencies {
  coreLibraryDesugaring 'com.android.tools:desugar_jdk_libs:<latest-version>'
}
```

You can find the latest version of `desugar_jdk_libs` [here](https://mvnrepository.com/artifact/com.android.tools/desugar_jdk_libs).

#### Step 2

Add the JitPack repository to your project level `build.gradle`:

```groovy
allprojects {
 repositories {
    maven { url "https://jitpack.io" }
 }
}
```

Add Expandable Calendar to your app `build.gradle`:

```groovy
dependencies {
	implementation 'com.github.tygalive:HorizontalExpandableCalendar-Android:<latest-version>'
}
```

You can find the latest version of `Expandable Calendar` on the JitPack badge above the preview images.

## Configuration & Customization

Minimum Setup as in sample app

```
    <com.mikesu.horizontalexpcalendar.CalendarView
        android:id="@+id/calendar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_alignParentLeft="true"
        android:layout_alignParentStart="true"
        android:layout_alignParentTop="true"
        exp:bottom_container_height="40dp"
        exp:center_container_expanded_height="250dp"
        exp:top_container_height="50dp"/>
```

Detailed information can be found in the [wiki](/wiki)

## License

```
Copyright 2016 Michał Sułek

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
