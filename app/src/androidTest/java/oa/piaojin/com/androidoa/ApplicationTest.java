package oa.piaojin.com.androidoa;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }
    public void test() {
        String result = null;
        System.out.println("$$$1");
        try {
            HttpPost clientpost = new HttpPost("http://192.168.253.1:8080/cecWeb/employLogin");
            //请求超时
            clientpost.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
            //读取超时
            clientpost.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
            System.out.println("$$$3");
            HttpResponse response = new DefaultHttpClient().execute(clientpost);
            System.out.println("$$$4" + response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {    // 现在已经发现了数据了
                InputStream input = response.getEntity().getContent();
                byte data[] = new byte[512];

                int len = input.read(data); // 输入流读取
                if (len > 0) {    // 已经读取到内容
                    result = new String(data, 0, len).trim();
                }
                input.close();
            } else {

            }
        } catch (Exception e) {
            System.out.println("@@@@@@@@@@@@@@@@" + e);
        }
        System.out.println("@@@@@@@@@@@@@@@@" + result);
    }
}