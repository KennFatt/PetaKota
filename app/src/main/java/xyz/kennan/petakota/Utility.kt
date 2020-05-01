package xyz.kennan.petakota

import android.content.Context
import android.widget.Toast

class Utility {
    companion object {

        /**
         * TODO: Remove this debug method in production
         *
         * Since my Logcat is always empty, Toast is the best and easy way to
         *  debugging.
         */
        public fun d(ctx: Context, m: String) {
            Toast.makeText(ctx, m, Toast.LENGTH_SHORT).show()
        }
    }
}
