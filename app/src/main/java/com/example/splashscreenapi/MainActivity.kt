package com.example.splashscreenapi

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.splashscreenapi.databinding.ActivityMainBinding
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var splashScreen: SplashScreen
    private var mInterstitialAd: InterstitialAd? = null
    private val splashViewModel: SplashViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        MobileAds.initialize(this) {}
        loadAd()

        splashScreen = installSplashScreen()

        splashScreen.apply {
            setKeepOnScreenCondition {
                true
            }
        }

        setContentView(view)
    }

    private fun loadAd() {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest, object :
            InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d("mLog", adError.message)
                mInterstitialAd = null
                splashScreen.apply {
                    setKeepOnScreenCondition {
                        splashViewModel.splashFakeApiResponse.value.not()
                    }
                    setOnExitAnimationListener { splashScreenView ->
                        splashScreenView.remove()
                    }
                }
            }

            override fun onAdLoaded(interstitialAd: InterstitialAd) {
                Log.d("mLog", "Ad was loaded.")
                mInterstitialAd = interstitialAd
                showInterstitial()
            }
        })
    }

    private fun showInterstitial() {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("mLog", "Ad was dismissed.")
                    mInterstitialAd = null
                    splashScreen.apply {
                        setKeepOnScreenCondition {
                            false
                        }
                        setOnExitAnimationListener { splashScreenView ->
                            splashScreenView.remove()
                        }
                    }
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d("mLog", "Ad failed to show.")
                    mInterstitialAd = null

                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("mLog", "Ad showed fullscreen content.")
                }
            }
            mInterstitialAd?.show(this)
        } else {
            splashScreen.apply {
                setKeepOnScreenCondition {
                    false
                }
                setOnExitAnimationListener { splashScreenView ->
                    splashScreenView.remove()
                }
            }
        }
    }
}