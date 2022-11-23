package com.martynenko.anton.company.report;

import static com.martynenko.anton.company.utils.Constants.XLSX_CONTENT_TYPE;

import com.martynenko.anton.company.report.Report.ReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @GetMapping("/last")
  public ResponseEntity<?> getLast(@RequestParam ReportType reportType){
    Report report = reportService.getLast(reportType);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
                  String.format("attachment; filename=\"%s %s.xlsx\"",reportType, report.getCreationDate().getMonth()))
        .header(HttpHeaders.CONTENT_TYPE, XLSX_CONTENT_TYPE)
        .body(report.getBinaryData());
  }

  @GetMapping("generate")
  public ResponseEntity<?> getLast(){
    reportService.generateReports();
    return ResponseEntity.ok().build();
  }

}
