package com.hms.referenceapp.hishopping.app
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage
import java.util.*

class PushService: HmsMessageService() {

    private val TAG = "PushService"

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        Log.i(TAG, "Receive Token : $token");
    }

    override fun onMessageReceived(message: RemoteMessage?) {
        super.onMessageReceived(message)

        Log.i(
            TAG, "getCollapseKey: " + message?.collapseKey
                    + "\n getData: " + message?.data
                    + "\n getFrom: " + message?.from
                    + "\n getTo: " + message?.to
                    + "\n getMessageId: " + message?.messageId
                    + "\n getSendTime: " + message?.sentTime
                    + "\n getMessageType: " + message?.messageType
                    + "\n getTtl: " + message?.ttl
        )

        val notification: RemoteMessage.Notification = message!!.notification

        Log.i(
            TAG, "\n getImageUrl: " + notification.imageUrl
                    + "\n getTitle: " + notification.title
                    + "\n getTitleLocalizationKey: " + notification.titleLocalizationKey
                    + "\n getTitleLocalizationArgs: " + Arrays.toString(notification.titleLocalizationArgs)
                    + "\n getBody: " + notification.body
                    + "\n getBodyLocalizationKey: " + notification.bodyLocalizationKey
                    + "\n getBodyLocalizationArgs: " + Arrays.toString(notification.bodyLocalizationArgs)
                    + "\n getIcon: " + notification.icon
                    + "\n getSound: " + notification.sound
                    + "\n getTag: " + notification.tag
                    + "\n getColor: " + notification.color
                    + "\n getClickAction: " + notification.clickAction
                    + "\n getChannelId: " + notification.channelId
                    + "\n getLink: " + notification.link
                    + "\n getNotifyId: " + notification.notifyId
        )
    }
}