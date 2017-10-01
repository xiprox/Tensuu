package tr.xip.scd.tensuu

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.support.v7.app.AppCompatDelegate
import io.realm.Realm

class App : Application() {

    /* Fix for vector drawables in pre-23
     *
     * See: http://stackoverflow.com/questions/36867298/using-android-vector-drawables-on-pre-lollipop-crash
     */
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate() {
        context = this
        super.onCreate()

        /* Realm */
        Realm.init(this)
    }

    companion object {
        /**
         * A static [Context] accessible from everywhere.
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
}