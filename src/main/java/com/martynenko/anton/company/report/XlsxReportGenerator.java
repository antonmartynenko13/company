package com.martynenko.anton.company.report;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class XlsxReportGenerator implements ReportGenerator{
  private final String headerFontName;

  private final short headerFontHeight;

  private final boolean headerFontIsBold;

  private final String contentFontName;

  private final short contentFontHeight;

  private final boolean contentFontIsBold;

  private XlsxReportGenerator(String headerFontName, short headerFontHeight, boolean headerFontIsBold,
      String contentFontName, short contentFontHeight, boolean contentFontIsBold) {
    this.headerFontName = headerFontName;
    this.headerFontHeight = headerFontHeight;
    this.headerFontIsBold = headerFontIsBold;
    this.contentFontName = contentFontName;
    this.contentFontHeight = contentFontHeight;
    this.contentFontIsBold = contentFontIsBold;
  }

  public static class ReportGeneratorBuilder {

    private String headerFontName = "Arial";

    private short headerFontHeight = 14;

    private boolean headerFontIsBold = false;

    private String contentFontName = "Arial";

    private short contentFontHeight = 12;

    private boolean contentFontIsBold = false;

    public ReportGeneratorBuilder headerFontName(String fontName) {
      this.headerFontName = fontName;
      return this;
    }

    public ReportGeneratorBuilder headerFontHeight(short headerFontHeight) {
      this.headerFontHeight = headerFontHeight;
      return this;
    }

    public ReportGeneratorBuilder headerFontIsBold(boolean headerFontIsBold) {
      this.headerFontIsBold = headerFontIsBold;
      return this;
    }

    public ReportGeneratorBuilder contentFont(String fontName) {
      this.contentFontName = fontName;
      return this;
    }

    public ReportGeneratorBuilder contentFontHeight(short contentFontHeight) {
      this.contentFontHeight = contentFontHeight;
      return this;
    }

    public ReportGeneratorBuilder contentFontIsBold(boolean contentFontIsBold) {
      this.contentFontIsBold = contentFontIsBold;
      return this;
    }

    XlsxReportGenerator build() {
      return new XlsxReportGenerator(
          this.headerFontName,
          this.headerFontHeight,
          this.headerFontIsBold,
          this.contentFontName,
          this.contentFontHeight,
          this.contentFontIsBold
          );
    }
  }


  @Override
  public byte[] generate(Map<String, List<String[]>> contentBySheets) {
    if (contentBySheets == null) throw new IllegalStateException("Content is required");

    try(Workbook workbook = new XSSFWorkbook()){
      for (String sheetName: contentBySheets.keySet()) {
        Sheet sheet = workbook.createSheet(sheetName);

        List<String[]> content = contentBySheets.get(sheetName);

        String[] headerArray = content.get(0);

        Row header = sheet.createRow(0);

        CellStyle headerStyle = getCellStyle(workbook, true);

        for (int i = 0; i < headerArray.length; i++) {
          Cell headerCell = header.createCell(i);
          headerCell.setCellValue(headerArray[i]);
          headerCell.setCellStyle(headerStyle);
        }

        CellStyle contentStyle = getCellStyle(workbook, false);

        for (int i = 1; i < content.size(); i++) {
          Row row = sheet.createRow(i);

          for (int j = 0; j < content.get(i).length; j++) {
            Cell cell = row.createCell(j);
            cell.setCellValue(content.get(i)[j]);
            cell.setCellStyle(contentStyle);
          }
        }
        for (int i = 0; i < content.get(0).length; i++) {
          sheet.autoSizeColumn(i);
        }

      }


      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      workbook.write(baos);
      return baos.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private CellStyle getCellStyle(Workbook workbook, boolean isHeader) {
    CellStyle cellStyle = workbook.createCellStyle();
    XSSFFont font = ((XSSFWorkbook) workbook).createFont();
    font.setFontName(isHeader ? this.headerFontName : this.contentFontName);
    font.setFontHeightInPoints(isHeader ? this.headerFontHeight : this.contentFontHeight);
    font.setBold(isHeader ? this.headerFontIsBold : this.contentFontIsBold);
    cellStyle.setFont(font);
    cellStyle.setAlignment(HorizontalAlignment.CENTER);
    return cellStyle;
  }

}
