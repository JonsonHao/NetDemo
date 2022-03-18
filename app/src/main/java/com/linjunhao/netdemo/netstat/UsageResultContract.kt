package com.linjunhao.netdemo.netstat

import android.app.AppOpsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContract

/**
 * @author: linjunhao
 * @e-mail: linjunhao@xmiles.cn
 * @date: 2021/12/20
 * @desc: 使用权限申请
 */
class UsageResultContract(context: Context) : ActivityResultContract<String, Boolean>() {

    private val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
    private val packageName = context.packageName

    override fun createIntent(context: Context, input: String?): Intent = Intent(
        Settings.ACTION_USAGE_ACCESS_SETTINGS)

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
        val mode = appOps.checkOpNoThrow(
            AppOpsManager.OPSTR_GET_USAGE_STATS,
            Process.myUid(), packageName
        )
        return mode == AppOpsManager.MODE_ALLOWED
    }
}