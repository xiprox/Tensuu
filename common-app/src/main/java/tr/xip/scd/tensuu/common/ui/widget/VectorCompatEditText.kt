package tr.xip.scd.tensuu.common.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v7.content.res.AppCompatResources
import android.support.v7.widget.AppCompatEditText
import android.util.AttributeSet
import tr.xip.scd.tensuu.common.R

/**
 * An EditText that supports vector drawables.
 *
 * See: http://stackoverflow.com/questions/35761636/is-it-possible-to-use-vectordrawable-in-buttons-and-textviews-using-androiddraw
 */
class VectorCompatEditText : AppCompatEditText {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    internal fun init(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val attributeArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.VectorCompatView
        )

        var drawableLeft: Drawable? = null
        var drawableRight: Drawable? = null
        var drawableBottom: Drawable? = null
        var drawableTop: Drawable? = null

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawableLeft = attributeArray.getDrawable(R.styleable.VectorCompatView_drawableLeftCompat)
            drawableRight = attributeArray.getDrawable(R.styleable.VectorCompatView_drawableRightCompat)
            drawableBottom = attributeArray.getDrawable(R.styleable.VectorCompatView_drawableBottomCompat)
            drawableTop = attributeArray.getDrawable(R.styleable.VectorCompatView_drawableTopCompat)
        } else {
            val drawableLeftId = attributeArray.getResourceId(R.styleable.VectorCompatView_drawableLeftCompat, -1)
            val drawableRightId = attributeArray.getResourceId(R.styleable.VectorCompatView_drawableRightCompat, -1)
            val drawableBottomId = attributeArray.getResourceId(R.styleable.VectorCompatView_drawableBottomCompat, -1)
            val drawableTopId = attributeArray.getResourceId(R.styleable.VectorCompatView_drawableTopCompat, -1)

            if (drawableLeftId != -1)
                drawableLeft = AppCompatResources.getDrawable(context, drawableLeftId)
            if (drawableRightId != -1)
                drawableRight = AppCompatResources.getDrawable(context, drawableRightId)
            if (drawableBottomId != -1)
                drawableBottom = AppCompatResources.getDrawable(context, drawableBottomId)
            if (drawableTopId != -1)
                drawableTop = AppCompatResources.getDrawable(context, drawableTopId)
        }

        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, drawableTop, drawableRight, drawableBottom)
        attributeArray.recycle()
    }
}
