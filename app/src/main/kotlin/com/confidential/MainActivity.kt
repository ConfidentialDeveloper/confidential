package com.confidential

import android.Manifest.permission.RECEIVE_SMS
import android.Manifest.permission.SEND_SMS
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.confidential.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkAndRequestPermissions()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun checkAndRequestPermissions() {
        val permissionSendSMS = ContextCompat.checkSelfPermission(this, SEND_SMS)
        val permissionRecvSMS = ContextCompat.checkSelfPermission(this, RECEIVE_SMS)
        val listPermissionsNeeded = ArrayList<String>()

        if (permissionSendSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(SEND_SMS)
        }
        if (permissionRecvSMS != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(RECEIVE_SMS)
        }

        if (listPermissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toTypedArray(),
                REQUEST_ID_MULTIPLE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_ID_MULTIPLE_PERMISSIONS -> {
                val perms = HashMap<String, Int>()
                perms[SEND_SMS] = PackageManager.PERMISSION_GRANTED
                perms[RECEIVE_SMS] = PackageManager.PERMISSION_GRANTED

                if (grantResults.isNotEmpty()) {
                    for (i in permissions.indices)
                        perms[permissions[i]] = grantResults[i]

                    if (perms[SEND_SMS] == PackageManager.PERMISSION_GRANTED
                        && perms[RECEIVE_SMS] == PackageManager.PERMISSION_GRANTED
                    ) {
                        Timber.tag("Permission").d("SMS permission granted")
                        // perform your action here
                    } else {
                        requestForPermission()
                    }
                }
            }
        }
    }

    private fun showDialogOK(
        title: String,
        message: String,
        onClickListener: DialogInterface.OnClickListener
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK", onClickListener)
        builder.setNegativeButton("Exit App", onClickListener)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun requestForPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                SEND_SMS
            )
            || ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                RECEIVE_SMS
            )
        ) {
            showDialogOK(
                title = "Permission Required",
                message = "Send and Receive SMS Services Permission required for this app"
            ) { _, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> checkAndRequestPermissions()
                    DialogInterface.BUTTON_NEGATIVE -> finish()
                }
            }

        } else {
            val snackbar = Snackbar.make(
                binding.root,
                "You need to give some mandatory permissions to continue. Do you want to go to app settings?",
                Snackbar.LENGTH_LONG
            )
            snackbar.show()
        }

    }

    companion object {
        const val REQUEST_ID_MULTIPLE_PERMISSIONS = 1234
    }
}