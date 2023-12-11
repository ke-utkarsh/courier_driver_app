package ymsli.com.couriemate.di.component

import ymsli.com.couriemate.di.ActivityScope
import ymsli.com.couriemate.di.module.ActivityModule
import ymsli.com.couriemate.views.login.LoginActivity
import ymsli.com.couriemate.views.main.MainActivity
import ymsli.com.couriemate.views.returntask.detail.ReturnTaskDetailActivity
import ymsli.com.couriemate.views.splash.SplashActivity
import ymsli.com.couriemate.views.taskdetail.TaskDetailActivity
import dagger.Component

/**
 * Project Name : Couriemate
 * @company YMSLI
 * @author  Sushant Somani (VE00YM129)
 * @date   January 10, 2020
 * Copyright (c) 2019, Yamaha Motor Solutions (INDIA) Pvt Ltd.
 *
 * Description
 * -----------------------------------------------------------------------------------
 * ActivityComponent : This is the activity component of dagger2 framework. This is
 *                      responsible for injecting objects in the activity
 * -----------------------------------------------------------------------------------
 *
 * Revision History
 * -----------------------------------------------------------------------------------
 * Modified By          Modified On         Description
 *
 * -----------------------------------------------------------------------------------
 */
@ActivityScope
@Component(dependencies = [ApplicationComponent::class],modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(loginActivity: LoginActivity)

    fun inject(splashActivity: SplashActivity)

    fun inject(taskDetailActivity: TaskDetailActivity)

    fun inject(returnTaskDetailActivity: ReturnTaskDetailActivity)

    fun inject(mainActivity: MainActivity)
}