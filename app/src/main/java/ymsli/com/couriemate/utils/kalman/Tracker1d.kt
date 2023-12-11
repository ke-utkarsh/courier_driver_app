package ymsli.com.couriemate.utils.kalman

import kotlin.math.sqrt

internal class Tracker1D(private val mt: Double,
                         processNoise: Double) {
    private val mt2: Double = mt * mt
    private val mt2d2: Double
    private val mt3d2: Double
    private val mt4d4: Double

    /** Process noise covariance */
    private val mQa: Double
    private val mQb: Double
    private val mQc: Double
    private val mQd: Double

    var position = 0.0
        private set

    var velocity = 0.0
        private set

    private var mPa: Double
    private var mPb: Double
    private var mPc: Double
    private var mPd: Double

    fun setState(position: Double, velocity: Double, noise: Double) {

        // State vector
        this.position = position
        this.velocity = velocity

        // Covariance
        val n2 = noise * noise
        mPa = n2 * mt4d4
        mPb = n2 * mt3d2
        mPc = mPb
        mPd = n2 * mt2
    }

    fun update(position: Double, noise: Double) {
        val r = noise * noise
        val y = position - this.position
        val s = mPa + r
        val si = 1.0 / s
        val Ka = mPa * si
        val Kb = mPc * si
        this.position = this.position + Ka * y
        velocity += Kb * y
        val Pa = mPa - Ka * mPa
        val Pb = mPb - Ka * mPb
        val Pc = mPc - Kb * mPa
        val Pd = mPd - Kb * mPb
        mPa = Pa
        mPb = Pb
        mPc = Pc
        mPd = Pd
    }

    fun predict(acceleration: Double) {
        position += velocity * mt + acceleration * mt2d2
        velocity += acceleration * mt
        val Pdt = mPd * mt
        val FPFtb = mPb + Pdt
        val FPFta = mPa + mt * (mPc + FPFtb)
        val FPFtc = mPc + Pdt
        val FPFtd = mPd
        mPa = FPFta + mQa
        mPb = FPFtb + mQb
        mPc = FPFtc + mQc
        mPd = FPFtd + mQd
    }

    val accuracy: Double
        get() = sqrt(mPd / mt2)

    init {

        // Lookup time step
        mt2d2 = mt2 / 2.0
        mt3d2 = mt2 * mt / 2.0
        mt4d4 = mt2 * mt2 / 4.0

        // Process noise covariance
        val n2 = processNoise * processNoise
        mQa = n2 * mt4d4
        mQb = n2 * mt3d2
        mQc = mQb
        mQd = n2 * mt2

        // Estimated covariance
        mPa = mQa
        mPb = mQb
        mPc = mQc
        mPd = mQd
    }
}