package tr.xip.scd.tensuu.student.data.service

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import tr.xip.scd.tensuu.student.ui.feature.local.Credentials

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        super.onTokenRefresh()
        Credentials.notificationsToken = FirebaseInstanceId.getInstance().token
    }
}