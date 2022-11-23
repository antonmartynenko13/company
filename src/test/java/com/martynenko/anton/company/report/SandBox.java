package com.martynenko.anton.company.report;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.martynenko.anton.company.csv.CsvHelper;
import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.department.DepartmentDTO;
import com.martynenko.anton.company.department.DepartmentRepository;
import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.project.ProjectDTO;
import com.martynenko.anton.company.project.ProjectRepository;
import com.martynenko.anton.company.projectposition.ProjectPosition;
import com.martynenko.anton.company.projectposition.ProjectPositionDTO;
import com.martynenko.anton.company.projectposition.ProjectPositionRepository;
import com.martynenko.anton.company.user.User;
import com.martynenko.anton.company.user.UserDTO;
import com.martynenko.anton.company.user.UserRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Collection;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.util.ResourceUtils;

public class SandBox {

  @Autowired
  ProjectPositionRepository projectPositionRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  DepartmentRepository departmentRepository;

  @Autowired
  ProjectRepository projectRepository;


  void run() throws IOException {
    //autoSizeColumn(int column)

    String reportName = "Employees workload report - " + LocalDate.now().getMonth().name();
    Workbook workbook = new XSSFWorkbook();

    Sheet sheet = workbook.createSheet(reportName);
    sheet.setColumnWidth(0, 6000);
    sheet.setColumnWidth(1, 4000);

    Row header = sheet.createRow(0);

    CellStyle headerStyle = workbook.createCellStyle();
    headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

    XSSFFont font = ((XSSFWorkbook) workbook).createFont();
    font.setFontName("Arial");
    font.setFontHeightInPoints((short) 16);
    font.setBold(true);
    headerStyle.setFont(font);

    Cell headerCell = header.createCell(0);
    headerCell.setCellValue("Name");
    headerCell.setCellStyle(headerStyle);

    headerCell = header.createCell(1);
    headerCell.setCellValue("Age");
    headerCell.setCellStyle(headerStyle);

    CellStyle style = workbook.createCellStyle();
    style.setWrapText(true);

    Row row = sheet.createRow(1);
    Cell cell = row.createCell(0);
    cell.setCellValue("John Smith");
    cell.setCellStyle(style);

    cell = row.createCell(1);
    cell.setCellValue(20);
    cell.setCellStyle(style);

    File currDir = new File(".");
    String path = currDir.getAbsolutePath();
    String fileLocation = path.substring(0, path.length() - 1) + reportName + ".xlsx";

    FileOutputStream outputStream = new FileOutputStream(fileLocation);
    workbook.write(outputStream);
    workbook.close();
  }

  void importTestDataFromCsv() throws FileNotFoundException {
    CsvHelper<DepartmentDTO> departmentDTOCsvHelper = new CsvHelper<>();
    CsvHelper<ProjectDTO> projectDTOCsvHelper = new CsvHelper<>();
    CsvHelper<UserDTO> userDTOCsvHelper = new CsvHelper<>();
    CsvHelper<ProjectPositionDTO> projectPositionDTOCsvHelper = new CsvHelper<>();

    Collection<DepartmentDTO> departmentDTOS = departmentDTOCsvHelper.readAll(new FileInputStream(ResourceUtils.getFile("classpath:csv/departments.csv")), DepartmentDTO.class);
    Collection<ProjectDTO> projectDTOS = projectDTOCsvHelper.readAll(new FileInputStream(ResourceUtils.getFile("classpath:csv/projects.csv")), ProjectDTO.class);
    Collection<UserDTO> userDTOS = userDTOCsvHelper.readAll(new FileInputStream(ResourceUtils.getFile("classpath:csv/users.csv")), UserDTO.class);
    Collection<ProjectPositionDTO> projectPositionDTOS = projectPositionDTOCsvHelper.readAll(new FileInputStream(ResourceUtils.getFile("classpath:csv/project_positions.csv")), ProjectPositionDTO.class);

    Collection<Department> departments = departmentDTOS.stream().map(DepartmentDTO::createInstance).toList();
    departmentRepository.saveAll(departments);

    Collection<Project> projects = projectDTOS.stream().map(ProjectDTO::createInstance).toList();
    projectRepository.saveAll(projects);

    Collection<User> users = userDTOS.stream().map(userDTO -> userDTO.createInstance(departmentRepository.getReferenceById(userDTO.departmentId()))).toList();
    userRepository.saveAll(users);

    Collection<ProjectPosition> projectPositions
        = projectPositionDTOS.stream()
                              .map(projectPositionDTO
                                  -> projectPositionDTO.createInstance(userRepository.getReferenceById(projectPositionDTO.userId()),
                                                                        projectRepository.getReferenceById(projectPositionDTO.projectId()))).toList();
    projectPositionRepository.saveAll(projectPositions);
  }
}
