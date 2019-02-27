package com.store.system.util;

import javax.crypto.Cipher;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class RSA {

    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";
    public static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";

    /**
     * RSA签名
     *
     * @param content
     *            待签名数据
     * @param privateKey
     *            商户私钥
     * @param input_charset
     *            编码格式
     * @return 签名值
     */
    public static String sign(String content, String privateKey, String input_charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String sign256(String content, String privateKey, String input_charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);

            signature.initSign(priKey);
            signature.update(content.getBytes(input_charset));

            byte[] signed = signature.sign();

            return Base64.encode(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * RSA验签名检查
     *
     * @param content
     *            待签名数据
     * @param sign
     *            签名值
     * @param ali_public_key
     *            支付宝公钥
     * @param input_charset
     *            编码格式
     * @return 布尔值
     */
    public static boolean verify(String content, String sign, String ali_public_key, String input_charset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(ali_public_key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(input_charset));

            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        String checkContent = "app_id=2016091701912250&auth_app_id=2016091701912250&body=test&buyer_id=2088402473368090&buyer_logon_id=lih***@126.com&buyer_pay_amount=0.01&charset=utf-8&fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]&gmt_create=2018-07-19 17:53:40&gmt_payment=2018-07-19 17:53:41&invoice_amount=0.01&notify_id=dc85e632112cabb9c2a17c048e607ebgp5&notify_time=2018-07-19 17:53:41&notify_type=trade_status_sync&out_trade_no=201807191753344&point_amount=0.00&receipt_amount=0.01&seller_email=service@quakoo.com&seller_id=2088121396278670&subject=test&total_amount=0.01&trade_no=2018071921001004090572530779&trade_status=TRADE_SUCCESS&version=1.0";
        String sign = "SdWb0Q0GtJX7xtLV3EN8367IDjUpjTX45y4HrBgc8DLoXpXp2U6xUY2MHHttUeCEQRvenkq0zIW1jfPYratW++Wns4MFeyJlMcr9skTWUc8X6YeuAmc8RGNbAg0R1xAN9ph91+L19EVzYgXTojxVCz65oVRdHFA33Siuv+Y9oXCBK8Pxa/lrz7kJxVun9xPPyEgHGg+NBedtCfdH8bkEEL4wSQGmhQn6ZD00MHpZhEBjFez0/XjTSR/BiK3eRE0jwGSdeEm/MSavrkXBIdGIeFLGp9HE2LkrTcfb1LukXyvx1PglywB6lqTuTPvGsadgOvjHBKy8SMRgLFOAkpxQZA==";
        String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoi3o3DQ8IOnKCY3J4WbOaqDlYDwmkjFBqOOdyqnAkvJ0OeKlh+7MwyRNw2+aacswDRv9HiqYlnmCS4dZ8R0dl0VK4gSWL34wSagHSbjWdTVShMTfqJj+LY/b5xHB6wNDyAek1vARYVJr343iE72vAGPaPZlxI26QC91Lsn1c4zfSr7Ak1I3RjsuwMvwcgVSqMcNpTbmoW7vZlB1U9fdDBkQlVRrNGdl9zFRMo8rklUznfu2dWi9d3rHTVxYVo/tFIaE4FriFzjJkmX+txuTq5yDh81heyF18mDVNAM6BN+NfARCgDkohUxERHh+0Pu9bRQ3s7FLmcRKv/Qg++tgWRwIDAQAB";
        //        String checkContent = "app_id=2018022602273874&auth_app_id=2018022602273874&body=testapp&buyer_id=2088602326772911&buyer_logon_id=186***@163.com&buyer_pay_amount=0.01&charset=utf-8&fund_bill_list=[{\"amount\":\"0.01\",\"fundChannel\":\"ALIPAYACCOUNT\"}]&gmt_create=2018-03-02 18:07:52&gmt_payment=2018-03-02 18:07:52&invoice_amount=0.01&notify_id=484de9ce62f40d0ac5ac05912b2e473n0x&notify_time=2018-03-02 18:07:53&notify_type=trade_status_sync&out_trade_no=2018030218074295&point_amount=0.00&receipt_amount=0.01&seller_email=service@quakoo.com&seller_id=2088121396278670&subject=testapp&total_amount=0.01&trade_no=2018030221001004910565900424&trade_status=TRADE_SUCCESS&version=1.0";
//        String sign = "GtYcLUb87qG7oLKas/lJ4mOjOznEMHiSWo2ZGGvLCy23X2PsO1hGtkzMCPGYV0Sf9gEBHxNdzPkOHLrNMUPxITW2cVotDo7hXZTUpRzlspHtWjHrhxowz7m59GZzN7TsNe2X4LHeXwp4BITpPNSq1C6N2Unw4BFvdOl5i2aBb3BIAdk+H2pTuFzukEYxRExwxcsbioSP1Kkty21LmjUY+wNnB4LS0cv7fb9ZF8fggGrVWi8zxa/hpPfltubTyjPCvRZKwfLPiDTukWXUwA6DTmNAMlKg+CQvFvcoku05W0H/MDyrFgXjc+DxtcOZ7JYWuZqUSYtwrurRN7d/mQuiWg==";
//        String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoi3o3DQ8IOnKCY3J4WbOaqDlYDwmkjFBqOOdyqnAkvJ0OeKlh+7MwyRNw2+aacswDRv9HiqYlnmCS4dZ8R0dl0VK4gSWL34wSagHSbjWdTVShMTfqJj+LY/b5xHB6wNDyAek1vARYVJr343iE72vAGPaPZlxI26QC91Lsn1c4zfSr7Ak1I3RjsuwMvwcgVSqMcNpTbmoW7vZlB1U9fdDBkQlVRrNGdl9zFRMo8rklUznfu2dWi9d3rHTVxYVo/tFIaE4FriFzjJkmX+txuTq5yDh81heyF18mDVNAM6BN+NfARCgDkohUxERHh+0Pu9bRQ3s7FLmcRKv/Qg++tgWRwIDAQAB";
        boolean res = verify256(checkContent, sign, ali_public_key, "utf-8");
        System.out.println(res);
    }

    public static boolean verify256(String content, String sign, String ali_public_key, String input_charset) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(ali_public_key);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_SHA256RSA_ALGORITHMS);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(input_charset));

            boolean bverify = signature.verify(Base64.decode(sign));
            return bverify;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * 解密
     *
     * @param content
     *            密文
     * @param private_key
     *            商户私钥
     * @param input_charset
     *            编码格式
     * @return 解密后的字符串
     */
    public static String decrypt(String content, String private_key, String input_charset) throws Exception {
        PrivateKey prikey = getPrivateKey(private_key);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, prikey);

        InputStream ins = new ByteArrayInputStream(Base64.decode(content));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        // rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), input_charset);
    }

    /**
     * 得到私钥
     *
     * @param key
     *            密钥字符串（经过base64编码）
     * @throws Exception
     */
    public static PrivateKey getPrivateKey(String key) throws Exception {

        byte[] keyBytes;

        keyBytes = Base64.decode(key);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        return privateKey;
    }

}
