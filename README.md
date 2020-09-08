# PhoneScanner
Use your Android smartphone as document scanner

We use the library from https://github.com/jhansireddy/AndroidScannerDemo to scan documents using the smartphone camera.

`AndroidScannerDemo` seems quite abandoned and no longer maintained. I built up this project to provide a working solution.

## Steps to integrate AndroidScannerDemo in your project

* `git clone https://github.com/jhansireddy/AndroidScannerDemo.git` into a standalone dir;
* Create or using your project: `File -> New -> Import module...`;
* As source directory point to: `~/_dirWhereYouClonedAndroidScannerDemo_/AndroidScannerDemo/ScanDemoExample/scanlibrary` and confirm;
* In Android Studio left tree, be sure to visualize project as Android view; right click on main app and on popupmenu choose `Open module settings...` or hit `F4`;
* Choose `Dependencies` and add `Scanlibrary`; hit `Apply` to make a gradle syncronization and `Ok` when finished;

At this point all the lack of updates of AndroidScannerDemo is shown; update as suggested taking a look at my example project, on AndroidManifest.xml and on build.gradle of scanlibrary module.

Now everything should compile and works correctly.

## Issues I've found

**`AndroidManifest.xml` merging errors on `fileprovider`**
I created a derived `FileProvider` class as below:
```kotlin
import androidx.core.content.FileProvider

class MyFileProvider: FileProvider() {
}
```

and on my app manifest: 

```xml
...
        <provider
            android:name=".MyFileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/filepaths" />
        </provider>
...
```

*I know that this solution is not the best but if anyone has a better one, please suggest!*

## How to use the library
On the activity / fragment class:

```kotlin
    private val DOCUMENT_SCAN = 20
        
    ...
        
    scanDocument.setOnClickListener {
        val intent = Intent(applicationContext, ScanActivity::class.java)
        intent.putExtra(ScanConstants.OPEN_INTENT_PREFERENCE, ScanConstants.OPEN_CAMERA)
        startActivityForResult(intent, DOCUMENT_SCAN)
    }
        
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != Activity.RESULT_OK) {
            return
        }

        when(requestCode) {
            DOCUMENT_SCAN -> {
                val uri: Uri = data?.extras?.getParcelable(ScanConstants.SCANNED_RESULT)!!
                var bitmap: Bitmap? = null
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    contentResolver.delete(uri, null, null)
                    imagePost.setImageBitmap(bitmap)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }
```

At this stage on `bitmap` you get an adjusted usable image.
