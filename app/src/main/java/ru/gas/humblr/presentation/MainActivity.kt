package ru.gas.humblr.presentation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.*
import ru.gas.humblr.R
import ru.gas.humblr.data.remote.models.TokenStorage
import ru.gas.humblr.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private var isAuthorized = false


    @OptIn(NavigationUiSaveStateControl::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        TokenStorage.accessToken = sharedPref.getString("accessToken", null)
        TokenStorage.refreshToken = sharedPref.getString("refreshToken", null)
        Log.d("Auth1", "refresh token on create ${TokenStorage.refreshToken}")
        val authViewModel: AuthViewModel by viewModels()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Auth1", "Token on Create ${TokenStorage.accessToken}")

//        val authIntent = authViewModel.startAuthIntent()
        val getAuthResponse =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                Log.d("Auth1", "result code ${it.resultCode}")
                val dataIntent = it.data ?: return@registerForActivityResult
                val error = Uri.parse(dataIntent.toString()).getQueryParameter("error")
                val codeee = Uri.parse(dataIntent.toString()).getQueryParameter("code")
                Log.d("Auth1", "result codeee $codeee")
                Log.e("Auth1", "An error has occurred : $error")
                Log.e("Auth1", "dataIntent : $dataIntent")
                if (Uri.parse(dataIntent.toString()).getQueryParameter("error") != null) {

                    if (it.resultCode == RESULT_OK) {
                        Toast.makeText(applicationContext, "SUCCESS", Toast.LENGTH_SHORT).show()
                        Log.d("Auth1", "On SUCCESS")
                        val code = Uri.parse(dataIntent.toString()).getQueryParameter("code")
                        Log.d("Auth1", "code is $code")
                    }
                    if (it.resultCode == RESULT_CANCELED) {
                        Toast.makeText(applicationContext, "FAILURE", Toast.LENGTH_SHORT).show()
                        Log.d("Auth1", "On FAILURE")
                    }
                }
            }

        if (TokenStorage.accessToken == null) {
            if (intent.data == null) {
                Log.d("Auth1", "intent.data == null")
                //ALERT put startauthintent here from above
                val authIntent = authViewModel.startAuthIntent()
                getAuthResponse.launch(authIntent)


            } else {
                val code = Uri.parse(intent.data.toString()).getQueryParameter("code")
                Log.d("Auth1", "intent data not null")
                Log.d("Auth1", "code is $code")
                code?.let { authViewModel.requestToken(it) }
                lifecycleScope.launchWhenStarted {
                    authViewModel.isAuthSuccess.collect {
                        if (true) {
                            Toast.makeText(
                                applicationContext,
                                "Authorization successfull",
                                Toast.LENGTH_SHORT
                            ).show()
                            isAuthorized = true
                        } else Toast.makeText(
                            applicationContext,
                            "Authorization failed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }


            }
        }

//when (isAuthorized) {
//    true -> {
        val navView
                : BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_favorite, R.id.navigation_profile
            )
        )
//    } else -> {}
//}


//        NavigationUI.setupWithNavController(navView, navController, false)
//        navView.setOnItemReselectedListener {
//            navController.popBackStack(destinationId = it.itemId, inclusive = false)
//        }
//        navView.setOnItemSelectedListener { item ->
//            NavigationUI.onNavDestinationSelected(item, navController)
//            true
//        }
//        setupActionBarWithNavController(navController, appBarConfiguration)
//
//        navView.setOnItemSelectedListener { item ->
//            when (item.itemId) {
//                R.id.navigation_home -> {
//                    navController.popBackStack(R.id.navigation_home, false)
//                }
//                R.id.navigation_favorite -> {
//                    navController.popBackStack(R.id.navigation_favorite, false)
//                }
//                else -> {
//                    navController.popBackStack(R.id.navigation_profile, false)
//                }
//            }
//        }
    }

    override fun onStop() {
        super.onStop()
        val sharedPref = getPreferences(Context.MODE_PRIVATE) ?: return
        with(sharedPref.edit()) {
            putString("accessToken", TokenStorage.accessToken)
            putString("refreshToken", TokenStorage.refreshToken)
            apply()
        }
        Log.d("Auth1", "token in storage on stop ${TokenStorage.accessToken}")
    }
}
