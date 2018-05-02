package com.gena.shapes

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.Oval
import com.gena.domain.model.figures.Rectangle
import com.gena.domain.model.figures.Triangle
import com.gena.domain.usecases.interfaces.IInteractor
import com.gena.shapes.model.UIOval
import com.gena.shapes.model.UIRectangle
import com.gena.shapes.model.UIShape
import com.gena.shapes.model.UITriangle
import com.gena.shapes.view.DrawSelector
import com.gena.shapes.view.draw
import java.lang.ref.WeakReference

@Suppress("DEPRECATION")
class ViewPanel : View {

    private var mMoved: Boolean = false
    private val mDrawSelector = DrawSelector()

    private var mCenterX = 0f
    private var mCenterY = 0f

    private val mBackgroundPaint = Paint().apply {
        color = resources.getColor(R.color.defBackColor)
    }
    private val mRectangleColor = resources.getColor(R.color.defRectangleColor)
    private val mTriangleColor = resources.getColor(R.color.defTriangleColor)
    private val mOvalColor = resources.getColor(R.color.defOvalColor)
    private val mSelectorColor = resources.getColor(R.color.defSelectColor)

    private var mInteractor: IInteractor? = null

    private var mModelWeakRef: WeakReference<ShapesModel> = WeakReference(ShapesModel())
    private var uiShapes: ArrayList<UIShape> = ArrayList()

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
    }

    // Init ViewPanel
    private fun init() {
        isFocusable = true // make sure we get key events
    }

    fun setModel(model: ShapesModel) {
        mModelWeakRef = WeakReference(model)
        prepareUiItems()
    }

    private fun prepareUiItems() {
        val items = mModelWeakRef.get()?.items ?: ArrayList()
        uiShapes = items.mapTo(ArrayList()) { shape ->
            when (shape) {
                is Rectangle -> UIRectangle(shape, mCenterX, mCenterY, mRectangleColor)
                is Triangle -> UITriangle(shape, mCenterX, mCenterY, mTriangleColor)
                is Oval -> UIOval(shape, mCenterX, mCenterY, mOvalColor)
                else -> TODO()
            }
        }
    }

    fun setInteractor(interactor: IInteractor) {
        mInteractor = interactor
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        mCenterY = this.height.toFloat() / 2
        mCenterX = this.width.toFloat() / 2
        prepareUiItems()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas ?: return

        // clear background
        canvas.drawPaint(mBackgroundPaint)

        val selectedIdx = mModelWeakRef.get()?.selectedIdx ?: -1

        // show all shapes
        for (i in 0 until uiShapes.size) {
            getItem(i)?.apply {
                this.draw(canvas)
                if (i == selectedIdx) {
                    mDrawSelector.drawSelector(canvas,
                            left, top, right, bottom,
                            mSelectorColor)
                }
            }
        }
    }

    private fun getItem(idx: Int): UIShape? = if (idx in 0..uiShapes.size)
        uiShapes[idx]
    else
        null

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val currentX = event.x
        val currentY = event.y
        val action = event.action

        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_MOVE -> {
                // Log.i("ViewPanel", "MOVE");
                mMoved = true
                mInteractor?.moveSelected(xToPX(currentX), yToPY(currentY))

            }
            MotionEvent.ACTION_DOWN -> {
                // Log.i("ViewPanel", "DOWN");
                mMoved = false
                mInteractor?.findSelection(xToPX(currentX), yToPY(currentY))
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL ->
                // Log.i("ViewPanel", "UP");
                if (mMoved) {
                    mInteractor?.movementFinished()
                    mMoved = false
                }
        }
        return true
    }

    private fun xToPX(x: Float): Int {
        val px = (x - mCenterX).toInt()
        return px
    }

    private fun yToPY(y: Float): Int {
        val py = (y - mCenterY).toInt()
        return py
    }

}