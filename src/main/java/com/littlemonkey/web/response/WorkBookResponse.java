package com.littlemonkey.web.response;

import org.apache.poi.ss.usermodel.Workbook;

public interface WorkBookResponse {

    Workbook getWorkbook();

    String getFileName();

}
