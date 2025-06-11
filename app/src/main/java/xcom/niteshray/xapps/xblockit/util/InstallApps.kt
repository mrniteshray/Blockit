package xcom.niteshray.xapps.xblockit.util

import android.content.Context
import android.content.pm.PackageManager
import xcom.niteshray.xapps.xblockit.model.Appitem

object InstallApps {
        fun getApps(context : Context) : List<Appitem>{
            val packageManager = context.packageManager
            val apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
                .filter { packageManager.getLaunchIntentForPackage(it.packageName) != null }

            return apps.map {
                Appitem(
                    name = it.loadLabel(packageManager).toString(),
                    Icon = it.loadIcon(packageManager),
                    packageName = it.packageName,
                    isBlock = false
                )
            }.sortedBy { it.name.lowercase() }
        }
}