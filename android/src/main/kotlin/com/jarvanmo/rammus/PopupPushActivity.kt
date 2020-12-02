package com.jarvanmo.rammus

import android.os.Bundle
import android.util.Log
import com.alibaba.sdk.android.push.AndroidPopupActivity
import org.json.JSONObject

open class PopupPushActivity: AndroidPopupActivity() {
    /**
     * 阿里云推送回调
     */
    override fun onSysNoticeOpened(title: String, summary: String, extras: MutableMap<String, String>) {
        val notification = buildNotificationMap(title, summary, extras)
        Log.d("AndroidPopupActivity", "onSysNoticeOpened title: $title, content: $summary, extras: ${notification["extras"]}")
        RammusPlugin.launchAppNotification = notification
        if (needStartLauncherActivity()) {
            startActivity(packageManager.getLaunchIntentForPackage(packageName))
        }
        if (needFinish()) {
            finish()
        }
    }

    /**
     * 当此Activity不是点桌面启动的那个Activity时，建议返回true，以启动APP，达到手点桌面启动APP的效果，默认true。
     * 但最好自己试验测试一下该Intent：
     * intent.hasCategory(Intent.CATEGORY_LAUNCHER)
     * Intent.ACTION_MAIN.equals(intent.getAction())
     */
    protected open fun needStartLauncherActivity(): Boolean = true

    /**
     * 处理完毕是否需要finish，默认true。需要手动控制finish()的话请返回false。
     */
    protected open fun needFinish(): Boolean = true

    protected fun buildNotificationMap(title: String, summary: String, extras: MutableMap<String, String>): Map<String, Any?>? {
        return mapOf(
                "title" to title,
                "summary" to summary,
                "extras" to extras
        )
    }

    protected fun extrasMapToJsonString(extras: MutableMap<String, String>): String {
        var jsonExtras = JSONObject()
        for (key in extras.keys){
            jsonExtras.putOpt(key, extras[key])
        }
        return jsonExtras.toString()
    }
}