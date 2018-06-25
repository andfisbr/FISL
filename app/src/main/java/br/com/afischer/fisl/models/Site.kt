package br.com.afischer.fisl.models


import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.apache.commons.io.IOUtils
import java.net.CookieManager
import java.net.CookiePolicy
import java.util.concurrent.TimeUnit


class Site {
        
        val url: Url = Url()
        var client: OkHttpClient
        
        
        init {
                val cookieManager = CookieManager()
                cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)

                
                client = OkHttpClient.Builder()
                        .readTimeout(45, TimeUnit.SECONDS)
                        .connectTimeout(45, TimeUnit.SECONDS)
                        .cookieJar(JavaNetCookieJar(cookieManager))
                        .build()
        }
        
        
        fun get(url: String, encoding: String = "UTF-8"): String {
                var result = ""
                
                
                val request = Request.Builder().url(url).get().build()
                
                
                val response = client.newCall(request).execute()
                
                
                response?.let { r ->
                        if (r.code() != 200) return@let
                        
                        r.body()?.let { rb ->
                                result = IOUtils.toString(rb.byteStream(), encoding)
                                rb.close()
                        }
                        r.close()
                }
                
                
                return result
        }
        
        
        fun post(url: String, body: RequestBody, encoding: String = "UTF-8"): String {
                var result = ""
                
                
                val request = Request.Builder().url(url).post(body).build()
                
                
                val response = client.newCall(request).execute()
                
                
                response?.let { r ->
                        if (r.code() != 200) return@let
                        
                        r.body()?.let { rb ->
                                result = IOUtils.toString(rb.byteStream(), encoding)
                                rb.close()
                        }
                        r.close()
                }
                
                
                return result
        }
}
