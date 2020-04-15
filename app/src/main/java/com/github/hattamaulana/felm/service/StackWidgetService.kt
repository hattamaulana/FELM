package com.github.hattamaulana.felm.service

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewFactory(applicationContext)

}
