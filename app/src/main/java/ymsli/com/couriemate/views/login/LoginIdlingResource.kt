package ymsli.com.couriemate.views.login

import androidx.annotation.Nullable
import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicBoolean

class LoginIdlingResource : IdlingResource {

    @Nullable
    private lateinit var resourceCallback: IdlingResource.ResourceCallback

    private var isIdleNow : AtomicBoolean = AtomicBoolean(true)

    override fun getName(): String = this.javaClass.name

    override fun isIdleNow(): Boolean = this.isIdleNow.get()

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.resourceCallback = callback
    }

    fun setIdleState(isIdleNow : Boolean){
        this.isIdleNow.getAndSet(isIdleNow)
        if(isIdleNow and (resourceCallback!=null)) resourceCallback.onTransitionToIdle()
    }
}