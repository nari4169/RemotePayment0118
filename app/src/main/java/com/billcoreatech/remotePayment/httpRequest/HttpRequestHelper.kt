package com.billcoreatech.remotePayment.httpRequest

import android.util.Log
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URLEncoder

class HttpRequestHelper {

    companion object {
        val TAG: String = HttpRequestHelper::class.java.name
        var feedbackUrl = "https://us-central1-boss0426-f0490.cloudfunctions.net/boss0426_request"
        val appUrl = "boss0426://card_pay" // 계좌이체 : acnt_pay
    }

    private val client: HttpClient = HttpClient(CIO)

    /**
     * https://api.payapp.kr/oapi/apiLoad.html
     *
     * cmd=payrequest&userid=payapptest&goodname=testGood&price=1000&recvphone=01055559999&smsuse=n
     *
     */
    suspend fun doPayRequest(
        productTitle: String,
        totalSummary: Int,
        payAppUserId: String,
        receivePhoneNo: String,
        uid: String
    ): String =
        withContext(Dispatchers.IO) {
            // set HttpRequestBuilder
            val response: HttpResponse = client.request("https://api.payapp.kr/oapi/apiLoad.html") {
                method = HttpMethod.Post
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
                parameter("cmd","payrequest")
                parameter("userid", payAppUserId.replace("[^A-Za-z0-9]".toRegex(),"").trim()) // payapptest
                parameter("goodname", URLEncoder.encode(productTitle, "UTF-8")) // 한글이 있기 때문에 변환
                parameter("price",totalSummary.toString().replace("[^0-9]".toRegex(),""))
                parameter("recvphone",receivePhoneNo)
                parameter("var1", uid)
                parameter("smsuse","n")
                parameter("appurl",appUrl)
                parameter("skip_cstpage","y") // 매출전표 페이지 스킵
                parameter("feedbackurl", URLEncoder.encode(feedbackUrl, "UTF-8")) // 특수문자들 때문에
                parameter("checkretry", "y")
            }
            val responseStatus = response.status
            Log.e(TAG, "requestKtorIo: $responseStatus")

            if (responseStatus == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "error: $responseStatus"
            }
        }

    /**
     * linkkey을 받아야 되는 데 이것은 feedback URL 을 받는 경우에 온다.
     *
     */
    suspend fun doPayCancel(mulNo: String, payAppUserId: String, linkKey: String): String =
        withContext(Dispatchers.IO) {
            // set HttpRequestBuilder
            val response: HttpResponse = client.request("https://api.payapp.kr/oapi/apiLoad.html") {
                method = HttpMethod.Post
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
                parameter("cmd","paycancel")
                parameter("userid", payAppUserId) // payapptest
                parameter("linkkey", linkKey)
                parameter("mul_no", mulNo)
                parameter("cancelmode", "0") // 전체 취소 0, 부분 취소 1
                
            }
            val responseStatus = response.status
            Log.d(TAG, "requestKtorIo: $responseStatus")

            if (responseStatus == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "error: $responseStatus"
            }
        }

    /**
     * payapp 사용자 체크
     */
    suspend fun doPayAppIdCheck(payAppUserId: String, resellerid: String): String =
        withContext(Dispatchers.IO) {
            // set HttpRequestBuilder
            val response: HttpResponse = client.request("https://api.payapp.kr/oapi/apiLoad.html") {
                method = HttpMethod.Post
                headers {
                    append("Content-Type", "application/x-www-form-urlencoded")
                }
                parameter("cmd","useridCheck")
                parameter("userid", payAppUserId) // payapptest
                parameter("resellerid", resellerid)

            }
            val responseStatus = response.status
            Log.d(TAG, "requestKtorIo: $responseStatus")

            if (responseStatus == HttpStatusCode.OK) {
                response.bodyAsText()
            } else {
                "error: $responseStatus"
            }
        }
}