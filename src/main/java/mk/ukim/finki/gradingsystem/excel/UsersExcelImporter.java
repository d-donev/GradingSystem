package mk.ukim.finki.gradingsystem.excel;

import mk.ukim.finki.gradingsystem.enumerations.Role;
import mk.ukim.finki.gradingsystem.model.Student;
import mk.ukim.finki.gradingsystem.model.User;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UsersExcelImporter {

    public List<Student> excelStudentsImport(String path) {

        List<Student> listStudents = new ArrayList<>();

        int index = 0;
        String name = "";
        String surname = "";
        String email;

        String excelFilePath = "C:\\Users\\USER\\Desktop\\" + path;

        long start = System.currentTimeMillis();

        FileInputStream inputStream;
        try {
            inputStream = new FileInputStream(excelFilePath);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> rowIterator = firstSheet.iterator();
            rowIterator.next();

            while (rowIterator.hasNext()) {
                Row nextRow = rowIterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                while (cellIterator.hasNext()) {
                    Cell nextCell = cellIterator.next();
                    int columnIndex = nextCell.getColumnIndex();
                    switch (columnIndex) {
                        case 0:
                            index = (int) nextCell.getNumericCellValue();
                            break;
                        case 1:
                            name = nextCell.getStringCellValue();
                            break;
                        case 2:
                            surname = nextCell.getStringCellValue();
                            break;
                    }

                }
                listStudents.add(new Student(index, new User(name, surname, Role.STUDENT, new ArrayList<>()), new ArrayList<>()));
            }
            workbook.close();
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }


        return listStudents;
    }
}