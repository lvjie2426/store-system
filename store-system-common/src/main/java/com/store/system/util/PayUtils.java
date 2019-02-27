package com.store.system.util;

import com.google.common.collect.Maps;
import com.quakoo.baseFramework.secure.MD5Utils;
import com.quakoo.baseFramework.xml.XmlUtils;
import com.store.system.bean.OrderExpireUnit;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.dom4j.Document;
import org.dom4j.Element;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.*;

public class PayUtils {

    public static String createLinkString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static String send(String urlStr, String xml) throws Exception {
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            conn.setReadTimeout(1000 * 5);
            conn.connect();
            outputStream = conn.getOutputStream();
            outputStream.write(xml.getBytes());
            outputStream.flush();
            inputStream = conn.getInputStream();
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String response = new String(data);
            return response;
        } finally {
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

    public static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    public static Map<String, String> xmlStrToMap(String xmlStr) {
        try {
            Document doc = XmlUtils.stringToXml(xmlStr);
            Element root = doc.getRootElement();
            Map<String, String> params = Maps.newHashMap();
            for (Object item : root.elements()) {
                Element element = (Element) item;
                String key = element.getName();
                String value = element.getTextTrim();
                params.put(key, value);
            }
            return params;
        } catch (Exception e) {
            throw new IllegalStateException(String.format("支付失败！", "解析xml失败！%s", xmlStr), e);
        }
    }

    public static String createXmlString(Map<String, String> params) {
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);
        Document doc = XmlUtils.createDocument();
        Element root = doc.addElement("xml");
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
            root.addElement(key).setText(value);
        }
        return doc.asXML();
    }


    public static String buildSign(Map<String, String> sParaTemp, String wxpayApiKey) throws Exception {
        Map<String, String> sPara = paraFilter(sParaTemp);
        String prestr = createLinkString(sPara) + "&key=" + wxpayApiKey;
        String mysign = MD5Utils.md5ReStr(prestr.getBytes()).toUpperCase();
        return mysign;
    }

    public static String getOutTradeNo(long orderId, long gmt) {
        String str = "%d%d";
        return String.format(str, gmt, orderId);
    }

    public static long getExpireTime(long create_time, OrderExpireUnit expireUnit, int expireNum) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(create_time);
        if(expireUnit == OrderExpireUnit.inday) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        } else if (expireUnit == OrderExpireUnit.day)  {
            calendar.add(Calendar.DAY_OF_YEAR, expireNum);
        } else if (expireUnit == OrderExpireUnit.hours) {
            calendar.add(Calendar.HOUR_OF_DAY, expireNum);
        } else if (expireUnit == OrderExpireUnit.minutes) {
            calendar.add(Calendar.MINUTE, expireNum);
        } else {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
        }
        return calendar.getTimeInMillis();
    }

    public static String getParam(Map<String, String> params, String param) throws Exception {
        String value = params.get(param);
        if(StringUtils.isBlank(value))
            return null;
        return new String(value.getBytes("ISO-8859-1"), Constant.defaultCharset);
    }

    public static long getOrderId(String outTradeNo) {
        return NumberUtils.toLong(StringUtils.substring(outTradeNo, 14));
    }

    public static String send(String urlStr) {
        String inputLine = "";
        try {
            URL url = new URL(urlStr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            inputLine = in.readLine().toString();
        } catch (Exception e) {
            e.printStackTrace();
            inputLine = "";
        }
        return inputLine;
    }

    public static String sendPKCS(File file, String merchantId, String urlStr, String xml) throws Exception {
        FileInputStream fileInputStream = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            char[] keyPass = merchantId.toCharArray();
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            fileInputStream = new FileInputStream(file);
            keyStore.load(fileInputStream, keyPass);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.
                    getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(keyStore, keyPass);
            SSLContext sslContext = SSLContext.getInstance("TLSv1");
            sslContext.init(keyManagerFactory.getKeyManagers(), null, null);
            URL requestUrl = new URL(urlStr);
            HttpsURLConnection conn = (HttpsURLConnection)requestUrl.openConnection();
            conn.setSSLSocketFactory(sslContext.getSocketFactory());
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(1000 * 5);
            conn.setReadTimeout(1000 * 5);
            conn.connect();
            outputStream = conn.getOutputStream();
            outputStream.write(xml.getBytes());
            outputStream.flush();
            inputStream = conn.getInputStream();
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            String response = new String(data);
            return response;
        } finally {
            IOUtils.closeQuietly(fileInputStream);
            IOUtils.closeQuietly(outputStream);
            IOUtils.closeQuietly(inputStream);
        }
    }

}
