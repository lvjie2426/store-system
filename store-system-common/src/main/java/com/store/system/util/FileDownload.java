package com.store.system.util;


import com.quakoo.baseFramework.http.HttpPoolParam;
import com.quakoo.baseFramework.http.HttpResult;
import com.quakoo.baseFramework.http.MultiHttpPool;
import com.quakoo.baseFramework.secure.Base64Util;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileDownload {

    public void downloadFile(HttpServletRequest request,
                             HttpServletResponse response, String fileName) throws Exception {
        response.setContentType("text/html;charset=UTF-8");
        BufferedInputStream in = null;
        BufferedOutputStream out = null;
        request.setCharacterEncoding("UTF-8");
        String rootpath = request.getSession().getServletContext().getRealPath("/");
        try {
            File f = new File(rootpath + "/upload/" + fileName);
            response.setContentType("application/x-excel");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename="
                    + new String(fileName.getBytes("gbk"), "iso-8859-1"));
            response.setHeader("Content-Length", String.valueOf(f.length()));
            in = new BufferedInputStream(new FileInputStream(f));
            out = new BufferedOutputStream(response.getOutputStream());
            byte[] data = new byte[1024];
            int len = 0;
            while (-1 != (len = in.read(data, 0, data.length))) {
                out.write(data, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }

    }

    /**
     * file对象转byte数组
     * @param filePath
     * @return
     * @throws IOException
     */
    public byte[] inputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        in.close();

        return out.toByteArray();
    }

    /**
     * 上传图片
     * @param data
     * @param suffix
     * @return
     * @throws Exception
     */
    public String uploadImg(byte[] data,String suffix) throws Exception {
        HttpPoolParam httpParam = new HttpPoolParam(10000, 10000, 3);
        MultiHttpPool pool = MultiHttpPool.getMultiHttpPool(httpParam);
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("file", Base64Util.encode(data));
        postMap.put("suffix", suffix);
        System.err.println(suffix);
        HttpResult httpResult = pool.httpQuery("http://39.107.247.82:11116/storage/handle64", null, "post", null, postMap, null, false, false, true);
        String result = httpResult.getResult();
        String str = "\"ok\":";
        String result2 = result.replace(str,"");
        String str2 = "{\"";
        String result3 = result2.replace(str2,"");
        String str3 = "\"}";
        return result3.replace(str3,"");
    }
}
