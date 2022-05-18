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

//    private final StudentActivityPointsService studentActivityPointsService;
//
//
//
//    public PointsExcelImporter(StudentActivityPointsService studentActivityPointsService) {
//        this.studentActivityPointsService = studentActivityPointsService;
//    }

    public List<StudentActivityPoints> excelImport(Long code, String path) {
        List<StudentActivityPoints> listStudent = new ArrayList<>();

        int index = 0;
        double Ppoints = 0;
        System.out.println(path);
        String excelFilePath = "C:\\Users\\User.DESKTOP-9R41TC1\\Desktop\\" + path;
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
                //courseService.addStudentsToCourseManual(courseId,list);
                //listStudent.add(new StudentActivityPoints(index, code, Tpoints));
//                int finalIndex = index;
//                StudentActivityPoints student = listStudent.stream().filter(x -> (x.getIndex().equals(finalIndex))&& !(x.getCode().equals(code))).findFirst().orElseGet(null);
//                if (student == null) {
                    listStudent.add(new StudentActivityPoints(index, code, Ppoints));

//                } else {
//                    student.setPoints(Ppoints);
//                }
                list.clear();
            }
//            for (int i=0;i<listStudent.size();i++) {
//                for (int j=i+1;j<listStudent.size();j++) {
//                    if ((listStudent.get(i).getIndex().equals(listStudent.get(j).getIndex())) && !(listStudent.get(i).getId().equals(listStudent.get(j).getId()))) {
//                        listStudent.remove(listStudent.get(i));
//                        break;
//                    }
//                }
//            }
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