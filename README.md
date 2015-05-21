# espresso.contribでRecyclerViewをテスト

RecyclerViewのテストにespresso-contribライブラリを導入する.

[espresso-contrib](https://code.google.com/p/android-test-kit/wiki/ReleaseNotes#Version_2.0_(Released_on:_2014.12.19))

 - RecyclerViewActions: handles interactions with RecyclerViews
 - PickerActions: handles interactions with Date and Time pickers

今回の環境は下記.

 - Android Studio 1.2.1.1
 - Espresso 2.1
 - appcompat-v7:22.1.1
 - recyclerview-v7:21.0.3
 - espresso-contrib 2.1


## build.gradle

JUnit4サポートのためTestRunnerを定義しておく.

```gradle
  android {
    defaultConfig {
      ...
      testInstrumentationRunner "android.support.test.runner.AndroidJUnitRunner"
    }
  }
```

今回はAppCompatとRecyclerViewを導入.

```gradle
  dependencies {
    compile 'com.android.support:appcompat-v7:22.1.1'
    compile 'com.android.support:recyclerview-v7:21.0.3'
```

espresso-contribはsub-dependenciesとして次のライブラリへの依存関係を含んでいる.

 - com.android.support:support-annotations
 - com.android.support:support-v4
 - com.android.support:recyclerview-v7
 - com.android.support.test.espresso:espresso-core

espresso-contribの導入にはGradleの依存関係ツリーをコントロールする必要がある.

support-anotationsはAppCompatにも含まれおり, コンフリクト回避のため除外しておく.
compileコンフィギュレーションでsupport-anotationsを取込み済みなのでandroidTestCompileコンフィギュレーションでは不要.

```gradle
  android {
     configurations {
         // Resolved 'com.android.support:support-annotations' versions for app (xx.x.x) and test app (xx.x.x) differ.
         // Add this statement if 'com.android.support:support-annotations:x.x.x' dependencies was already defined in compile configuration.
         androidTestCompile.exclude group: 'com.android.support', module: 'support-annotations'
     }
   }
```

espresso-contribは`com.android.support.test.espresso:espresso-core`を含んでいるためこれを定義する必要はない.

```gradle
  dependencies {
    // You can compile without 'espresso-core'. Because 'espresso-contrib' has.
    // androidTestCompile 'com.android.support.test.espresso:espresso-core:2.1'
```

また, espresso-contribは`com.android.support:support-v4`と`com.android.support:recyclerview-v7`も含んでいるため,
コンフリクト回避のためこれを除外しておく.

```gradle
  dependencies {
    androidTestCompile ('com.android.support.test.espresso:espresso-contrib:2.1') {
        exclude group: 'com.android.support', module: 'support-v4'
        exclude group: 'com.android.support', module: 'recyclerview-v7'
    }
    androidTestCompile 'com.android.support.test:rules:0.2'
  }
```

お決まりの`LICENSE.txt`のコンフリクトも解消しておく.

```gradle
  android {
    packagingOptions {
        exclude 'LICENSE.txt'
    }
  }
```

Gradleの全容は下記を参照.
[build.gradle](https://github.com/YukiMatsumura/EspressoContribSample/blob/master/app/build.gradle)


## テストケースを書く

既にEspressoを実行できる準備はできているのでテストケースを書けば完了.

```java
@RunWith(AndroidJUnit4.class)
public class ApplicationTest  {
    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void RecyclerViewItemのクリック() {
        onView(withId(R.id.list)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}
```

[ApplicationTest.java](https://github.com/YukiMatsumura/EspressoContribSample/blob/master/app/src/androidTest/java/yuki/m/android/espressocontribsample/ApplicationTest.java)

## Point

```gradle
// build.gradle
android {
    packagingOptions {
        exclude 'LICENSE.txt'
    }
    configurations {
        // Resolved 'com.android.support:support-annotations' versions for app (22.1.1) and test app (22.0.0) differ.
        // Add this statement if 'com.android.support:support-annotations:x.x.x' dependencies was already defined in compile configuration.
        androidTestCompile.exclude group: 'com.android.support', module: 'support-annotations'
    }
}

dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')

    // 'appcompat-v7' has 'support-annotations'.
    compile 'com.android.support:appcompat-v7:22.1.1'
    compile 'com.android.support:recyclerview-v7:21.0.3'

    testCompile 'junit:junit:4.12'
    testCompile "org.mockito:mockito-core:1.9.5"

    // If you are on a Mac, you will probably need to configure the default JUnit test runner configuration
    // in order to work around a bug where Android Studio does not set the working directory to the module being tested.
    // This can be accomplished by editing the run configurations, Defaults -> JUnit and changing the working directory value to $MODULE_DIR$.
    //  - Resolved java.io.FileNotFoundException: build/intermediates/bundles/debug/AndroidManifest.xml (No such file or directory)
    //  - Resolved java.lang.UnsupportedOperationException: Robolectric does not support API level 1.
    //  - see. https://github.com/robolectric/robolectric/issues/1648
    //  - see. http://robolectric.org/getting-started/#Note for Mac Users
    testCompile 'org.robolectric:robolectric:3.0-rc2'

    // You can compile without 'espresso-core'. Because 'espresso-contrib' has.
    // androidTestCompile 'com.android.support.test.espresso:espresso-core:2.1'

    // When you use "espresso-contrib:2.0" with "rules:0.2".
    // might "com.android.dex.DexException: Multiple dex files define Landroid/support/test/BuildConfig;" occur.
    androidTestCompile ('com.android.support.test.espresso:espresso-contrib:2.1') {
        // Resolved java.lang.NoClassDefFoundError: your.package.name.EspressoTargetActivity
        // 'espresso-contrib' has already 'support-v4' package.
        exclude group: 'com.android.support', module: 'support-v4'

        // Resolved java.lang.IllegalAccessError: Class ref in pre-verified class resolved to unexpected implementation
        // 'espresso-contrib' has already 'recyclerview-v7' package.
        exclude group: 'com.android.support', module: 'recyclerview-v7'
    }

    // You can compile without 'runner'. Because 'rules' has.
    // androidTestCompile 'com.android.support.test:runner:0.2'
    androidTestCompile 'com.android.support.test:rules:0.2'
}
```

## Copyright 

Copyright 2015 yuki312 All Right Reserved. 

Licensed under the Apache License, Version 2.0 (the "License"); 
you may not use this file except in compliance with the License.   
You may obtain a copy of the License at 
 
  http://www.apache.org/licenses/LICENSE-2.0 
 
Unless required by applicable law or agreed to in writing, software 
distributed under the License is distributed on an "AS IS" BASIS, 
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
See the License for the specific language governing permissions and 
limitations under the License. 

以上.