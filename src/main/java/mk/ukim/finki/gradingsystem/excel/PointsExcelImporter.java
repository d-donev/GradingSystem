package mk.ukim.finki.gradingsystem.excel;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mk.ukim.finki.gradingsystem.model.StudentActivityPoints;
import mk.ukim.finki.gradingsystem.service.CourseService;
import mk.ukim.finki.gradingsystem.service.StudentActivityPointsService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class PointsExcelImporter {


    public List<StudentActivityPoints> excelImport(Long code, String path) {
        List<StudentActivityPoints> listStudent = new ArrayList<>();

        int index = 0;
        double Ppoints = 0;
        System.out.println(path);
        String excelFilePath = "C:\\Users\\dimit\\Desktop\\" + path;
        //static only

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
                            Ppoints = nextCell.getNumericCellValue();
                            System.out.println(Ppoints);
                            break;
                    }
                }
                List<Integer> list = new ArrayList<>();
                list.add(index);

                    listStudent.add(new StudentActivityPoints(index, code, Ppoints));

                list.clear();
            }

            workbook.close();
            long end = System.currentTimeMillis();
            System.out.printf("Import done in %d ms\n", (end - start));

        } catch (Exception e) {
            // TODO Auto-generated catch block

            e.printStackTrace();
        }
        System.out.println(listStudent);



        return listStudent;
    }

}