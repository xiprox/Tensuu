package tr.xip.scd.tensuu.util.ext

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ViewFlipper

/**
 * Various exts
 */

/**
 * Hack to make it possible to use functions with return types in places where Unit is expected.
 */
fun Any.unitify() {
    /* do nothing */
}

/**
 * Assumes this [Int] is in dp units and converts it into pixels.
 */
fun Int.toPx(context: Context): Int {
    return (this * context.resources.displayMetrics.density).toInt()
}

/**
 * Assumes this [Int] is in pixel units and converts it into dp units.
 */
fun Int.toDp(context: Context): Int {
    return (this / context.resources.displayMetrics.density).toInt()
}

/**
 * Returns a View visibility int from this boolean.
 */
fun Boolean.toVisibility(): Int {
    return if (this) View.VISIBLE else View.GONE
}

/**
 * Convenience function for getting [LayoutInflater]
 */
fun Context.getLayoutInflater(): LayoutInflater {
    return getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
}

/**
 * Safe ViewFlipper child switching for when using animations.
 */
fun ViewFlipper.setDisplayedChildSafe(position: Int) {
    if (displayedChild != position) displayedChild = position
}

/**
 * Executes the lambda param when this View is laid out.
 */
fun View.doOnLayout(body: () -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            body.invoke()
            viewTreeObserver.removeGlobalOnLayoutListener(this)
        }
    })
}

/**
 * Cleans up special characters in Bosnia/Serbian/Croatian names.
 */
fun String.stripSpecialChars(): String {
    return replace("Č", "C")
            .replace("Ć", "C")
            .replace("Ç", "C")
            .replace("ć", "c")
            .replace("č", "c")
            .replace("ç", "c")
            .replace("Ž", "Z")
            .replace("Đ", "D")
            .replace("đ", "d")
            .replace("Š", "S")
            .replace("ž", "z")
            .replace("š", "s")
}