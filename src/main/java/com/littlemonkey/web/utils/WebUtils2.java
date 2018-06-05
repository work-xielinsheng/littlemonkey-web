package com.littlemonkey.web.utils;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.littlemonkey.utils.gzip.GZipUtils;
import com.littlemonkey.web.request.StandardMultipartFileForm;
import com.littlemonkey.web.response.FileResponse;
import com.littlemonkey.web.response.WorkBookResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.SerializationUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.WebUtils;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 23:56 2018/3/27
 * @Version: 1.0
 */
public final class WebUtils2 extends WebUtils {
    private final static Logger logger = LoggerFactory.getLogger(WebUtils2.class);

    /**
     * 获取客户端IP
     */
    public static final String getHost(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (StringUtils.hasText(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.indexOf(",") > 0) {
            logger.info(ip);
            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            String[] ips = ip.split(",");
            for (int index = 0; index < ips.length; index++) {
                String strIp = (String) ips[index];
                if (!("unknown".equalsIgnoreCase(strIp))) {
                    ip = strIp;
                    break;
                }
            }
        }
        if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
            InetAddress inetAddress = null;
            try { // 根据网卡取本机配置的IP
                inetAddress = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                logger.error("getCurrentIP", e);
            }
            if (inetAddress != null) {
                ip = inetAddress.getHostAddress();
            }
        }
        logger.info("getRemoteAddress ip: " + ip);
        return ip;
    }

    /**
     * <p>json and jsonArray</p>
     *
     * @param request
     * @return
     */
    public static String getRequestBodyJson(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        String jsonString = null;
        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                char[] charBuffer = new char[128];
                int bytesRead;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
            jsonString = stringBuilder.toString();
        } catch (IOException ex) {
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                }
            }
        }
        return jsonString;
    }

    public static String getQueryString(HttpServletRequest request) {
        return request.getQueryString();
    }

    public static String encodeFileUploadContent(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = getNativeRequest(request, MultipartHttpServletRequest.class);
        Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
        Map<String, List<MultipartFile>> listMap = Maps.newHashMap();
        String name;
        while (iterator.hasNext()) {
            name = iterator.next();
            List<MultipartFile> multipartFileList = multipartHttpServletRequest.getFiles(name);
            listMap.put(name, Lists.newArrayList(Lists.transform(multipartFileList, new Function<MultipartFile, MultipartFile>() {
                @Override
                public MultipartFile apply(MultipartFile multipartFile) {
                    try {
                        return new StandardMultipartFileForm(multipartFile.getName(),
                                multipartFile.getOriginalFilename(), multipartFile.getContentType(),
                                multipartFile.getBytes(), multipartFile.getSize());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            })));
        }
        return Base64.getEncoder().encodeToString(SerializationUtils.serialize(listMap));
    }

    public static Map<String, List<MultipartFile>> decodeFileUploadContent(String content) {
        return (Map<String, List<MultipartFile>>) SerializationUtils.deserialize(Base64.getDecoder().decode(content));
    }

    /**
     * <p> 输出XLS </p>
     *
     * @param response
     * @param workbookResult
     */
    public static void outHSSFWorkbook(HttpServletResponse response, WorkBookResponse workbookResult) {
        OutputStream outputStream = null;
        try {
            Workbook wb = workbookResult.getWorkbook();
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(workbookResult.getFileName(), "utf-8"));
            outputStream = response.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();
            outputStream = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {
                    logger.error("Fail to out HSSFWorkbook! errMsg=" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * <p> 响应字节流 </p>
     *
     * @param response
     * @param bytes
     */
    public static void outByte(HttpServletResponse response, byte[] bytes) {
        OutputStream os = null;
        try {
            response.setHeader("Content-Type", "application/octet-stream;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Encoding", "gzip");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            bytes = GZipUtils.compress(bytes);
            response.setContentLength(bytes.length);
            os = response.getOutputStream();
            os.write(bytes);
            os.flush();
        } catch (Exception var13) {
            var13.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("Fail to outByte! errMsg=" + e.getMessage(), e);
                }
            }

        }
    }

    /**
     * <p> 输出文件 </p>
     *
     * @param fileResult
     */
    public static void outFile(HttpServletResponse response, FileResponse fileResult) {
        OutputStream os = null;
        try {
            response.setContentType("application/octet-stream");
            response.setHeader("Connection", "close");
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(fileResult.getFileName(), "utf-8"));
            InputStream input = new ByteArrayInputStream(fileResult.getContent().getBytes("UTF8"));
            int read;
            byte[] bytes = new byte[1024];
            os = response.getOutputStream();
            while ((read = input.read(bytes)) != -1) {
                os.write(bytes, 0, read);
            }
            os.flush();
            os.close();
            os = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>响应json</p>
     *
     * @param response
     * @param content
     */
    public static void outJson(HttpServletResponse response, String content) {
        OutputStream os = null;
        try {
            logger.info("content:" + content);
            response.setHeader("Content-Type", "application/json;charset=utf-8");
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Encoding", "gzip");
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
            response.setHeader("Access-Control-Allow-Headers", "*");
            byte[] bs = content.getBytes(StandardCharsets.UTF_8);
            bs = GZipUtils.compress(bs);
            response.setContentLength(bs.length);
            os = response.getOutputStream();
            os.write(bs);
            os.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("Fail to close io", e);
                }
            }
        }
    }

    /**
     * <p> 响应图片 </p>
     *
     * @param response
     * @param bufferedImage
     */
    public static void outBufferedImage(HttpServletResponse response, BufferedImage bufferedImage) {
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            ImageIO.write(bufferedImage, "jpeg", out);
            out.flush();
            out.close();
            out = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    logger.error("Fail to out bufferedImage! errMsg=" + e.getMessage(), e);
                }
            }
        }
    }

    /**
     * <p> 响应字符串 </p>
     *
     * @param response
     * @param content
     */
    public static void outText(HttpServletResponse response, String content) {
        PrintWriter pw = null;
        try {
            response.setContentType("text/html;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-length", "" + content.getBytes("UTF-8").length);
            pw = response.getWriter();
            pw.write(content);
            pw.flush();
        } catch (Exception exp) {
            exp.printStackTrace();
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }
}
