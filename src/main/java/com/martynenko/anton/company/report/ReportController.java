package com.martynenko.anton.company.report;

import static com.martynenko.anton.company.utils.Constants.XLSX_CONTENT_TYPE;

import com.martynenko.anton.company.openapi.GetLastReport;
import com.martynenko.anton.company.report.Report.ReportType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.HttpURLConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "reports")
@RestController
@RequestMapping("/api/reports")
public class ReportController {

  @Autowired
  private ReportService reportService;

  @GetLastReport
  @GetMapping(value = "/last", produces = XLSX_CONTENT_TYPE)
  public ResponseEntity<?> getLast(@RequestParam ReportType reportType){
    Report report = reportService.getLast(reportType);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
                  String.format("attachment; filename=\"%s %s.xlsx\"",reportType, report.getCreationDate().getMonth()))
        .header(HttpHeaders.CONTENT_TYPE, XLSX_CONTENT_TYPE)
        .body(report.getBinaryData());
  }
}
