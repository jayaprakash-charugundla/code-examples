package com.spring.boot.excel.demo;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
public class ExcelReporter implements Reporter {

    @Override
    public ByteArrayOutputStream generateReport() {

        ByteArrayOutputStream outputStream = null;
        try {
            //Step1: Create an empty Workbook
            XSSFWorkbook workbook = new XSSFWorkbook();

            //Step2: Create a sheet on the Workbook
            XSSFSheet sheet = workbook.createSheet("Sheet");

            //Step3: Create a row on the sheet
            XSSFRow row = sheet.createRow(1);

            //Step4: Create a cell on the row
            XSSFCell cell = row.createCell(1);

            //Step5: Set a value to the cell
            cell.setCellValue("Value");

            //Step6: Create byte output stream to write the content of the Workbook
            outputStream = new ByteArrayOutputStream();

            //Step7: Write the workbook to the OutputStream
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream;
    }

    public void addImageToExcelSheet() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Sheet");
        //Step1: Open InputStream to image file
        InputStream is = new FileInputStream(".../image.jpg");

        //Step2: Read image data into a byte array
        byte[] bytes = IOUtils.toByteArray(is);

        //Step3: Add image to the work book
        int pictureIndex = workbook.addPicture(is, Workbook.PICTURE_TYPE_JPEG);

        //Step4: Create the drawing canvas
        Drawing drawing = sheet.createDrawingPatriarch();

        //Step5: Create anchor point for placing the picture
        CreationHelper helper = workbook.getCreationHelper();
        ClientAnchor anchor = helper.createClientAnchor();

        //Step6: Specify start column (0 based) the anchor
        anchor.setCol1(15);

        //Step7: Specify the starting row (0 based)
        anchor.setRow1(30);

        //Step8: Draw the picture on the drawing canvas
        Picture picture = drawing.createPicture(anchor, pictureIndex);

        //Step9: Auto resize the picture relative to the top left position, to fit the available area of the worksheet
        picture.resize();
    }
}
