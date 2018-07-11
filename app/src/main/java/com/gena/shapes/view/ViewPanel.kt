package com.gena.shapes.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.gena.domain.model.ShapesModel
import com.gena.domain.model.figures.*
import com.gena.domain.usecases.interfaces.ISelectionInteractor
import com.gena.shapes.R
import com.gena.shapes.model.*
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
    private val mPictureColor = resources.getColor(R.color.defPictureColor)
    private val mSelectorColor = resources.getColor(R.color.defSelectColor)

    private var mSelectionInteractor: ISelectionInteractor? = null

    private var mModelWeakRef: WeakReference<ShapesModel> = WeakReference(ShapesModel())
    private var uiShapes: ArrayList<UIShape> = ArrayList()

    private val mPictures = HashMap<Int, UIPicture>()

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
        val oldImages = getOldImages()
        uiShapes = items.mapTo(ArrayList()) { shape ->
            when (shape) {
                is Rectangle -> UIRectangle(shape, mCenterX, mCenterY, mRectangleColor)
                is Triangle -> UITriangle(shape, mCenterX, mCenterY, mTriangleColor)
                is Oval -> UIOval(shape, mCenterX, mCenterY, mOvalColor)
                is Picture -> getUiPicture(shape, oldImages)
            }
        }
        removeOldImages(oldImages)
    }

    private fun getOldImages(): HashMap<Int, UIPicture> {
        return HashMap(mPictures)
    }

    private fun getUiPicture(picture: Picture, oldImages: HashMap<Int, UIPicture>): UIPicture {
        val uiPicture = UIPicture(picture, mCenterX, mCenterY, mPictureColor)
        oldImages.remove(uiPicture.key)
        mPictures[uiPicture.key] = uiPicture
        return uiPicture
    }

    private fun removeOldImages(oldImages: HashMap<Int, UIPicture>) {
        for (oldImage in oldImages) {
            oldImage.value.onDelete()
            mPictures.remove(oldImage.key)
        }
    }

    fun setInteractor(selectionInteractor: ISelectionInteractor) {
        mSelectionInteractor = selectionInteractor
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

        clearCanvas(canvas)
        drawAllShapes(canvas)
        drawSelector(canvas)
    }

    private fun clearCanvas(canvas: Canvas) {
        canvas.drawPaint(mBackgroundPaint)
    }

    private fun drawAllShapes(canvas: Canvas) {
        for (i in 0 until uiShapes.size) {
            getItem(i)?.apply {
                this.draw(canvas)
            }
        }
    }

    private fun drawSelector(canvas: Canvas) {
        val selectedIdx = mModelWeakRef.get()?.selectedKey?.idx ?: -1
        getItem(selectedIdx)?.apply {
            mDrawSelector.drawSelector(canvas,
                    left, top, right, bottom,
                    mSelectorColor)
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
                mSelectionInteractor?.moveSelectedTo(Point(xToPX(currentX), yToPY(currentY)))

            }
            MotionEvent.ACTION_DOWN -> {
                // Log.i("ViewPanel", "DOWN");
                mMoved = false
                mSelectionInteractor?.findSelection(Point(xToPX(currentX), yToPY(currentY)))
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP, MotionEvent.ACTION_CANCEL ->
                // Log.i("ViewPanel", "UP");
                if (mMoved) {
                    mSelectionInteractor?.movementFinished()
                    mMoved = false
                }
        }
        return true
    }

    private fun xToPX(x: Float): Int {
        return (x - mCenterX).toInt()
    }

    private fun yToPY(y: Float): Int {
        return (y - mCenterY).toInt()
    }

}