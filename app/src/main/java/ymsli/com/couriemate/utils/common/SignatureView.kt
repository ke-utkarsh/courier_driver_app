package ymsli.com.couriemate.utils.common

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.io.ByteArrayOutputStream
import kotlin.math.abs

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Balraj VE00YM023
 * @date   APR 22, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * CaptureSignatureView : A custom to capture the signature
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */

class SignatureView(context: Context, attr: AttributeSet?) : View(context, attr) {
    private var _Bitmap: Bitmap? = null
    private var _Canvas: Canvas? = null
    private val _Path: Path = Path()
    private val _BitmapPaint: Paint = Paint(Paint.DITHER_FLAG)
    private val _paint: Paint = Paint()
    private var _mX = 0f
    private var _mY = 0f
    private val TouchTolerance = 4f
    private val LineThickness = 4f
    var touchDetected = false
        private set

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        _Bitmap = Bitmap.createBitmap(
            w,
            if (h > 0) h else (this.getParent() as View).getHeight(),
            Bitmap.Config.ARGB_8888
        )
        _Canvas = Canvas(_Bitmap!!)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(_Bitmap!!, 0f, 0f, _BitmapPaint)
        canvas.drawPath(_Path, _paint)
    }

    private fun touchStart(x: Float, y: Float) {
        _Path.reset()
        _Path.moveTo(x, y)
        _mX = x
        _mY = y
    }

    private fun touchMove(x: Float, y: Float) {
        val dx = abs(x - _mX)
        val dy = abs(y - _mY)
        if (dx >= TouchTolerance || dy >= TouchTolerance) {
            _Path.quadTo(_mX, _mY, (x + _mX) / 2, (y + _mY) / 2)
            _mX = x
            _mY = y
        }
    }

    private fun touchUp() {
        if (!_Path.isEmpty) {
            _Path.lineTo(_mX, _mY)
            _Canvas!!.drawPath(_Path, _paint)
        } else {
            _Canvas!!.drawPoint(_mX, _mY, _paint)
        }
        _Path.reset()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        super.onTouchEvent(e)
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStart(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                touchMove(x, y)
                invalidate()
                touchDetected = true
            }
            MotionEvent.ACTION_UP -> {
                touchUp()
                invalidate()
            }
        }
        return true
    }

    fun ClearCanvas() {
        _Canvas!!.drawColor(Color.WHITE)
        invalidate()
    }

    val bytes: ByteArray
        get() {
            val b = bitmap
            val baos = ByteArrayOutputStream()
            b.compress(Bitmap.CompressFormat.PNG, 100, baos)
            return baos.toByteArray()
        }

    val bitmap: Bitmap
        get() {
            val v: View = this.getParent() as View
            val b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888)
            val c = Canvas(b)
            v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom())
            v.draw(c)
            return b
        }

    init {
        _paint.setAntiAlias(true)
        _paint.setDither(true)
        _paint.setColor(Color.argb(255, 0, 0, 0))
        _paint.setStyle(Paint.Style.STROKE)
        _paint.setStrokeJoin(Paint.Join.ROUND)
        _paint.setStrokeCap(Paint.Cap.ROUND)
        _paint.setStrokeWidth(LineThickness)
    }
}