package com.store.system.util;


import com.quakoo.baseFramework.http.HttpPoolParam;
import com.quakoo.baseFramework.http.HttpResult;
import com.quakoo.baseFramework.http.MultiHttpPool;
import com.quakoo.baseFramework.secure.Base64Util;
import com.store.system.exception.StoreSystemException;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    //重置响应对象
    public void exportInfoTemplate(HttpServletRequest request, HttpServletResponse response, Workbook workbook,String filename)throws Exception{
        response.reset();
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String nowTime = df.format(new Date());
        String fileName = filename + nowTime + ".xls";
        final String userAgent = request.getHeader("USER-AGENT");
        String finalFileName = null;
        if (StringUtils.contains(userAgent, "MSIE")) {//IE浏览器
            finalFileName = URLEncoder.encode(fileName, "UTF8");
        } else if (StringUtils.contains(userAgent, "Mozilla")) {//google,火狐浏览器
            finalFileName = new String(fileName.getBytes(), "ISO8859-1");
        } else {
            finalFileName = URLEncoder.encode(fileName, "UTF8");//其他浏览器
        }
        response.setHeader("Content-Disposition", "attachment; filename=\"" + finalFileName + "\"");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        //文件放入输出流
        OutputStream output = null;
        BufferedOutputStream bufferedOutPut = null;
        try{
             output = response.getOutputStream();
             bufferedOutPut = new BufferedOutputStream(output);
            workbook.write(bufferedOutPut);

        }catch (StoreSystemException s){
            throw new StoreSystemException(s.getMessage());
        }finally{
            bufferedOutPut.flush();
            bufferedOutPut.close();
            output.close();
        }
    }
}
