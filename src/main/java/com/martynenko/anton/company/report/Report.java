package com.martynenko.anton.company.report;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

@Getter
@NoArgsConstructor
@Entity
public class Report {
  public enum ReportType {
    WORKLOAD, AVAILABILITY
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "report_type")
  @Enumerated(value = EnumType.STRING)
  private ReportType reportType;

  @Column(name = "binary_data")
  @Lob
  @Type(type="org.hibernate.type.BinaryType")
  private byte[] binaryData;

  @Column(name = "creation_date", insertable = false)
  @CreationTimestamp
  private LocalDateTime creationDate;

  public Report(ReportType reportType, byte[] binaryData) {
    this.reportType = reportType;
    this.binaryData = binaryData;
  }
}
