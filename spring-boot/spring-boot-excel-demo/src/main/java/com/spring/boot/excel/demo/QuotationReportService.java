package com.spring.boot.excel.demo;

import com.spring.boot.excel.demo.dto.QuoteItem;
import org.apache.commons.io.IOUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.PropertyTemplate;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class QuotationReportService {

    public ByteArrayOutputStream generateQuotation(List<QuoteItem> quoteData, boolean isDomestic) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            XSSFWorkbook workbook = new XSSFWorkbook();
            XSSFSheet sheet = workbook.createSheet("Sheet");

            //Apply column widths for specific columns
            sheet.setColumnWidth(2, 30 * 256);
            sheet.setColumnWidth(4, 30 * 256);
            sheet.setColumnWidth(10, 20 * 256);

            addCompanyDetails(workbook, sheet);
            addQuoteDetails(workbook, sheet, quoteData);

            //Apply borders for specific rows and columns
            PropertyTemplate pt = new PropertyTemplate();
            pt.drawBorders(new CellRangeAddress(12, 12, 7, 10),
                    BorderStyle.MEDIUM,
                    IndexedColors.GREEN.getIndex(),
                    BorderExtent.OUTSIDE);

            pt.drawBorders(new CellRangeAddress(14, 14, 7, 10),
                    BorderStyle.MEDIUM, IndexedColors.BROWN.getIndex(), BorderExtent.OUTSIDE);

            pt.applyBorders(sheet);

            //Added header
            Header header = sheet.getHeader();
            header.setLeft("Furniture Store");
            header.setRight("Quotation");

            //Added footer
            Footer footer = sheet.getFooter();
            footer.setCenter("THANK YOU FOR YOUR BUSINESS");
            footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());

            //Grouping rows
            sheet.groupRow(0, 4);
            sheet.groupRow(9, 14);

            addInfo(sheet, isDomestic);

            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputStream;
    }

    private void addCompanyDetails(XSSFWorkbook workbook, XSSFSheet sheet) {
        XSSFRow titleRow = sheet.createRow(0);
        XSSFCell titleCell = titleRow.createCell(4);

        XSSFFont titleFont = workbook.createFont();
        titleFont.setFontName("Arial");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 24);

        XSSFCellStyle titleCellStyle = workbook.createCellStyle();
        titleCellStyle.setFont(titleFont);
        titleCell.setCellStyle(titleCellStyle);
        titleCell.setCellValue("Product Quote");

        XSSFRow detailsRow1 = sheet.createRow(1);
        XSSFCell contactNo = detailsRow1.createCell(2);
        contactNo.setCellValue("Contact No: xxxxxxxxxx");
        XSSFCell companyName = detailsRow1.createCell(10);
        companyName.setCellValue("The Furniture Store");

        XSSFRow detailsRow2 = sheet.createRow(2);
        XSSFCell email = detailsRow2.createCell(2);
        email.setCellValue("Email: contact@furniture.com");

        XSSFRow detailsRow3 = sheet.createRow(3);
        XSSFCell website = detailsRow3.createCell(2);
        website.setCellValue("Website: www.furniture.com");

        //Merging cells
        sheet.addMergedRegion(new CellRangeAddress(2, 4, 10, 10));

        //Added picture
        byte[] bytes;
        CreationHelper helper = workbook.getCreationHelper();
        try (InputStream is = new FileInputStream("src/main/resources/images/logo.jpg")) {
            bytes = IOUtils.toByteArray(is);
            int pictureIndex = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            Drawing drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();
            anchor.setCol1(10);
            anchor.setRow1(2);
            Picture picture = drawing.createPicture(anchor, pictureIndex);
            picture.resize();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Added Hyperlink Style
        CellStyle hlinkStyle = workbook.createCellStyle();
        Font hlinkFont = workbook.createFont();
        hlinkFont.setUnderline(Font.U_SINGLE);
        hlinkFont.setColor(IndexedColors.BLUE.getIndex());
        hlinkStyle.setFont(hlinkFont);

        //Getting existing cell using sheet and apply style
        XSSFCell cellWeb = sheet.getRow(3).getCell(2);
        Hyperlink linkUrl = helper.createHyperlink(HyperlinkType.URL);
        linkUrl.setAddress("https://poi.apache.org/");
        cellWeb.setHyperlink(linkUrl);
        cellWeb.setCellStyle(hlinkStyle);

        XSSFCell cellEmail = sheet.getRow(2).getCell(2);
        Hyperlink linkEmail = helper.createHyperlink(HyperlinkType.EMAIL);
        linkEmail.setAddress("mailto:contact@furniture.com?subject=Inquiry");
        cellEmail.setHyperlink(linkUrl);
        cellEmail.setCellStyle(hlinkStyle);
    }

    private void addQuoteDetails(XSSFWorkbook workbook, XSSFSheet sheet, List<QuoteItem> quoteData) {
        XSSFRow row1 = sheet.createRow(5);
        XSSFCell cellTo = row1.createCell(2);
        cellTo.setCellValue("To:");
        XSSFCell cellDate = row1.createCell(10);
        cellDate.setCellValue("Date:");

        XSSFRow row2 = sheet.createRow(6);
        XSSFCell cellToVal1 = row2.createCell(2);
        cellToVal1.setCellValue("Mr. John Doe");

        XSSFRow row3 = sheet.createRow(7);
        XSSFCell cellToVal2 = row3.createCell(2);
        cellToVal2.setCellValue("London");

        XSSFRow row4 = sheet.createRow(8);
        XSSFCell cellToVal3 = row4.createCell(2);
        cellToVal3.setCellValue("UK");

        //Apply Date format cell style to a cell
        XSSFCellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setAlignment(HorizontalAlignment.LEFT);
        XSSFCreationHelper helper = workbook.getCreationHelper();
        dateCellStyle.setDataFormat(helper.createDataFormat().getFormat("dd/mm/yyyy"));
        XSSFCell cellDateVal = row2.createCell(10);
        cellDateVal.setCellStyle(dateCellStyle);
        cellDateVal.setCellValue(new Date());

        //Apply currency cell style to a call
        XSSFCellStyle priceCellStyle = workbook.createCellStyle();
        priceCellStyle.setDataFormat(helper.createDataFormat().getFormat("$#,##0.00"));

        //Apply font style and font color to a cell
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();
        XSSFColor col = new XSSFColor(IndexedColors.BLUE, colorMap);
        font.setColor(col);

        //Apply center alignment to a cell
        XSSFCellStyle headerRowCellStyle = workbook.createCellStyle();
        headerRowCellStyle.setFont(font);
        headerRowCellStyle.setAlignment(HorizontalAlignment.CENTER);

        //Apply font style and font weight
        XSSFCellStyle boldItalicCellStyle = workbook.createCellStyle();
        XSSFFont boldItalicFont = workbook.createFont();
        boldItalicFont.setBold(true);
        boldItalicFont.setItalic(true);
        boldItalicCellStyle.setFont(boldItalicFont);

        XSSFRow row5 = sheet.createRow(9);
        XSSFCell cell1 = row5.createCell(2);
        cell1.setCellStyle(headerRowCellStyle);
        cell1.setCellValue("Qty");

        XSSFCell cell2 = row5.createCell(4);
        cell2.setCellStyle(headerRowCellStyle);
        cell2.setCellValue("Description");

        XSSFCell cell3 = row5.createCell(7);
        cell3.setCellStyle(headerRowCellStyle);
        cell3.setCellValue("Unit Price");

        XSSFCell cell4 = row5.createCell(10);
        cell4.setCellStyle(headerRowCellStyle);
        cell4.setCellValue("Line Total");

        XSSFCellStyle lineRowCellStyle = workbook.createCellStyle();
        lineRowCellStyle.setAlignment(HorizontalAlignment.CENTER);

        //Apply foreground color, fill pattern and date format
        XSSFCellStyle taxCellStyle = workbook.createCellStyle();
        taxCellStyle.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        taxCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        taxCellStyle.setDataFormat(helper.createDataFormat().getFormat("$#,##0.00"));

        int rowIndex = 10;
        double subTotal = 0;
        double tax = 25;

        for (QuoteItem qi : quoteData) {
            XSSFRow linerow = sheet.createRow(rowIndex);
            XSSFCell lineCell1 = linerow.createCell(2);
            lineCell1.setCellStyle(lineRowCellStyle);
            lineCell1.setCellValue(qi.getQty());

            XSSFCell lineCell2 = linerow.createCell(4);
            lineCell2.setCellStyle(lineRowCellStyle);
            lineCell2.setCellValue(qi.getProduct().getName());

            XSSFCell lineCell3 = linerow.createCell(7);
            lineCell3.setCellStyle(priceCellStyle);
            lineCell3.setCellValue(qi.getProduct().getPrice());
            double lineTotal = qi.getQty() * qi.getProduct().getPrice();

            XSSFCell lineCell4 = linerow.createCell(10);
            lineCell4.setCellStyle(priceCellStyle);
            lineCell4.setCellValue(lineTotal);

            subTotal = subTotal + lineTotal;
            rowIndex++;
        }

        XSSFRow subTotalrow = sheet.createRow(rowIndex++);
        XSSFCell subTotalCell1 = subTotalrow.createCell(7);
        subTotalCell1.setCellStyle(boldItalicCellStyle);
        subTotalCell1.setCellValue("Sub Total");

        XSSFCell subTotalCell2 = subTotalrow.createCell(10);
        subTotalCell2.setCellStyle(priceCellStyle);
        subTotalCell2.setCellValue(subTotal);

        XSSFRow taxrow = sheet.createRow(rowIndex++);
        XSSFCell taxCell1 = taxrow.createCell(7);
        taxCell1.setCellStyle(boldItalicCellStyle);
        taxCell1.setCellValue("Tax");

        XSSFCell taxCell2 = taxrow.createCell(10);
        taxCell2.setCellStyle(taxCellStyle);
        taxCell2.setCellValue(tax);

        XSSFRow totalrow = sheet.createRow(rowIndex++);
        XSSFCell totalCell1 = totalrow.createCell(7);
        totalCell1.setCellStyle(boldItalicCellStyle);
        totalCell1.setCellValue("Total");

        XSSFCell totalCell2 = totalrow.createCell(10);
        totalCell2.setCellValue(subTotal + tax);
    }

    private void addInfo(XSSFSheet sheet, boolean isDomestic) {
        XSSFRow infoRo1 = sheet.createRow(16);
        XSSFCell infoCell1 = infoRo1.createCell(2);
        infoCell1.setCellValue("Delivery can be arranged within city limits at a cost");

        if (isDomestic) {
            infoRo1.setZeroHeight(false);
        } else {
            infoRo1.setZeroHeight(true);
        }

        XSSFRow infoRo2 = sheet.createRow(18);
        XSSFCell infoCell2 = infoRo2.createCell(4);
        infoCell2.setCellValue("Should you have any inquiries please contact us");
    }


    private void applyPropertiesForACell(XSSFCell cell) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(CellUtil.BORDER_TOP, BorderStyle.MEDIUM);
        properties.put(CellUtil.TOP_BORDER_COLOR, IndexedColors.RED.getIndex());
        CellUtil.setCellStyleProperties(cell, properties);
    }
}
