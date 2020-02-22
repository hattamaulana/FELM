package com.github.hattamaulana.moviecatalogue.service

import android.content.Intent
import android.widget.RemoteViewsService

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory =
        StackRemoteViewFactory(applicationContext)

}
