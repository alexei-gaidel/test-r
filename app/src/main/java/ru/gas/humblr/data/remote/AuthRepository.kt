package ru.gas.humblr.data.remote

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import net.openid.appauth.*
import ru.gas.humblr.authorization.network.AuthConfig
import ru.gas.humblr.data.remote.models.TokenStorage
import ru.gas.humblr.data.remote.models.TokensModel
import javax.inject.Singleton
import kotlin.coroutines.suspendCoroutine

@Singleton
class AuthRepository {

    val serviceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfig.AUTH_URI),
        Uri.parse(AuthConfig.TOKEN_URI),
        null, // registration endpoint
        Uri.parse(AuthConfig.END_SESSION_URI)
    )

    fun getAuthRequest(): AuthorizationRequest {
        val redirectUri = Uri.parse(AuthConfig.CALLBACK_URL)

        return AuthorizationRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID,
            AuthConfig.RESPONSE_TYPE,
            redirectUri
        )
            .setState(AuthConfig.STATE)
            .setAdditionalParameters(mapOf(Pair("duration", AuthConfig.DURATION)))
            .setScope(AuthConfig.SCOPE)
            .build()

    }


    fun getRefreshTokenRequest(refreshToken: String): TokenRequest {
        return TokenRequest.Builder(
            serviceConfiguration,
            AuthConfig.CLIENT_ID
        )
            .setGrantType(GrantTypeValues.REFRESH_TOKEN)
            .setRefreshToken(refreshToken)
            .build()
    }


    fun sendAuthRequest(authService: AuthorizationService): Intent {
        val authRequest = getAuthRequest()
        Log.d("Auth1", "sending auth request")
        val customTabsIntent = CustomTabsIntent.Builder().build()
        return authService.getAuthorizationRequestIntent(authRequest, customTabsIntent)
    }


    fun performRequestToken(code: String, authService: AuthorizationService): Boolean {

        Log.d("Auth1", "requesting token")
        var isAuthSucessfull = false
        val clientAuth: ClientAuthentication = ClientSecretBasic(AuthConfig.CLIENT_SECRET)
        authService.performTokenRequest(
            TokenRequest.Builder(serviceConfiguration, AuthConfig.CLIENT_ID)
                .setAuthorizationCode(code)
                .setRedirectUri(AuthConfig.CALLBACK_URL.toUri())
                .setGrantType(GrantTypeValues.AUTHORIZATION_CODE)
                .build(),
            clientAuth
        ) { response, exception -> // если авторизация успешна, вернется token в response, если ошибка, вернется exception
            if (response != null) {
                TokenStorage.accessToken = response.accessToken
                TokenStorage.refreshToken = response.refreshToken
                isAuthSucessfull = true
                Log.d("Auth1", "Auth response token ${response.accessToken.toString()}")
                Log.d("Auth1", "Auth refresh token ${response.refreshToken.toString()}")
            }
            if (exception != null) {
                Log.d("Auth1", "Auth exception ${response?.accessToken.toString()}")
                isAuthSucessfull = false

            }
        }
        return isAuthSucessfull
    }


    suspend fun performRefreshTokenSuspend(
        authService: AuthorizationService,
    ): TokensModel {
//        val clientAuth: ClientAuthentication = ClientSecretBasic(AuthConfig.CLIENT_SECRET)
        val clientAuth: ClientAuthentication = getClientAuthentication()
        return suspendCoroutine { continuation ->
            Log.d("Auth1", "performTokenRequestSuspend -- continuation")
            authService.performTokenRequest(
                TokenRequest.Builder(serviceConfiguration, AuthConfig.CLIENT_ID)
                    .setGrantType(GrantTypeValues.REFRESH_TOKEN)
                    ///ALERT added .orEmpty()
                    .setRefreshToken(TokenStorage.refreshToken)
                    .build(),
//                getRefreshTokenRequest(TokenStorage.refreshToken.orEmpty()),
                clientAuth
            ) { response, ex ->
                when {
                    response != null -> {
                        //получение токена произошло успешно
                        val tokens = TokensModel(
                            accessToken = response.accessToken.orEmpty(),
                            refreshToken = response.refreshToken.orEmpty(),
                            idToken = response.idToken.orEmpty()
                        )
                        Log.d(
                            "Auth1",
                            "performTokenRequestSuspend repo response!=null, refresh Token ${response.refreshToken}"
                        )
                        continuation.resumeWith(Result.success(tokens))
                    }
                    ex != null -> {
                        continuation.resumeWith(Result.failure(ex))
                        Log.d("Auth1", "performTokenRequestSuspend repo failure $response")
                    }
                    else -> error("unreachable")
                }
            }
        }
    }


    fun getEndSessionRequest(): EndSessionRequest {
        return EndSessionRequest.Builder(serviceConfiguration)
            .setPostLogoutRedirectUri(AuthConfig.LOGOUT_CALLBACK_URL.toUri())
            .build()
    }


    private fun getClientAuthentication(): ClientAuthentication {
        return ClientSecretPost(AuthConfig.CLIENT_SECRET)
    }


    fun logout() {
        TokenStorage.accessToken = null
    }

}
