package xyz.kennan.petakota

import android.content.Context
import android.widget.Toast

class Utility {
    companion object {
        public fun d(ctx: Context, m: String) {
            Toast.makeText(ctx, m, Toast.LENGTH_SHORT).show()
        }
    }
}
