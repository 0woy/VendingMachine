package FileIO;

import GUI.StartMachine;

import javax.annotation.processing.Filer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Datas {
    public Datas() {

    }

    // 매출 입력하기
    public static void writeSales(int beverageIdx) {
        
        // 현재 날짜를 구해 읽어올 파일의 이름 저장하기
        LocalDate currentDate = LocalDate.now();
        int monthValue = currentDate.getMonthValue();
        String fileName = "Month_" + monthValue;

        // 선택한 월의 날짜 수 구하기
        YearMonth yearMonth = YearMonth.of(2023, monthValue);
        int daysInMonth = yearMonth.lengthOfMonth();

        // 파일이 존재하지 않는 경우 생성 및 초기화
        File file = new File("C:\\Java_workspace\\VendingMachine\\src\\FileIO\\" + fileName + ".txt");
        if (!file.exists()) {
            try {
                // 파일 생성
                file.createNewFile();
                // 각 행에 대해 초기값 0으로 설정
                FileWriter writer = new FileWriter(file);
                for (int j = 0; j < daysInMonth; j++) {
                    writer.write("0,0,0,0,0\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 파일을 읽고 지정된 행과 열에 있는 셀을 업데이트
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] rows = new String[daysInMonth];    // 일자수에 따라 행 배열 초기화
            String line;
            int row = 0;

            // rows 배열에 파일의 한 줄씩 저장 및 파일 데이터 개수 구함
            while ((line = reader.readLine()) != null) {
                rows[row] = line;
                row++;
            }
            reader.close();

            // 지정된 행과 열에 있는 셀을 업데이트
            int currentDay = LocalDate.now().getDayOfMonth() - 1;   // 오늘 날짜 but 파일의 인덱스는 0부터 시작이므로 -1
            int cellIdx = beverageIdx;                              // 변경할 음료의 cell 위치
            String[] rowValues = rows[currentDay].split(",");
            int cellValue = Integer.parseInt(rowValues[cellIdx]);   // 변경할 음료 cell 위치에 있는 값
            cellValue++;                                            // 해당 셀의 값 1증가
            rowValues[cellIdx] = String.valueOf(cellValue);         // 변경된 값 저장

            // 오늘 날짜에 있는 데이터 업데이트
            StringBuilder updatedRow = new StringBuilder();
            for (int j = 0; j < rowValues.length; j++) {
                updatedRow.append(rowValues[j]);
                if (j != rowValues.length - 1) {
                    updatedRow.append(",");
                }
            }

            // 업데이트된 데이터를 row 배열에 저장
            rows[currentDay] = updatedRow.toString();

            // 변경 사항이 반영된 데이터를 파일에 다시 작성
            FileWriter writer = new FileWriter(file);
            for (String rowValue : rows) {
                writer.write(rowValue + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 선택한 월의 매출 읽어오기
    public String[][] readSales(String fileName) {
        // 데이터 파일의 경로
        String filePath = "src/FileIO/" + fileName + ".txt";
        String[][] daySales = new String[0][];

        int year = 2023;    // 현재 년도
        int month = Integer.parseInt(fileName.split("_")[1]);   // 현재 월

        // 선택한 월의 날짜 수 구하기
        YearMonth yearMonth = YearMonth.of(year, month);
        int daysInMonth = yearMonth.lengthOfMonth();

        File dataFile = new File(filePath);

        // 데이터 파일이 존재하는 경우
        if (dataFile.exists()) {
            int dayIdx = 0;
            int[] Bsum = new int[StartMachine.vm.getBeverageCount()];  // 음료별 월 총 매출액 배열 선언
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                String line;
                daySales = new String[daysInMonth + 1][7];                    // 일별 음료 매출액 및 총액 저장 배열 선언


                // 파일에서 데이터를 읽어와 테이블에 추가
                while ((line = br.readLine()) != null) {
                    String[] data = line.split(",");
                    int sum = 0;

                    for (int i = 0; i < data.length; i++) {
                        int tmp = Integer.parseInt(data[i]) * StartMachine.vm.getBeveragePrice(i);
                        Bsum[i] += tmp;                     // 각 음료별 월별 매출액 합산
                        data[i] = String.valueOf(tmp);        // 각 음료별 일별 매출액 저장
                        sum += Integer.parseInt(data[i]);   // 일별 총 매출액 저장
                    }

                    daySales[dayIdx][0] = String.valueOf(dayIdx + 1) + "일";                     // 일자 표시
                    System.arraycopy(data, 0, daySales[dayIdx], 1, data.length); // daySales 배열에 data 배열 저장
                    daySales[dayIdx++][data.length + 1] = String.valueOf(sum) + " 원";           // 마지막 column에 일별 매출액 저장
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            int total = 0;
            daySales[dayIdx][0] = "총 매출액";  // 마지막 행에 월별 음료 매출액
            // 음료별 총 매출액
            for (int i = 0; i < StartMachine.vm.getBeverageCount(); i++) {
                total += Bsum[i];
                daySales[dayIdx][i + 1] = String.valueOf(Bsum[i]) + "원";

            }
            daySales[dayIdx][6] = String.valueOf(total) + "원";
        } else daySales = null;
        return daySales;
    }

    // 음료 재고 소진 입력하기
    public static void writeSoldout(int beverageIdx) {
        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        LocalDateTime currentDateTime = LocalDateTime.of(currentDate, currentTime);

        // 재고 소진 날짜와 시간을 soldoutTime에 저장
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String soldoutTime = currentDateTime.format(formatter);

        // 파일이 존재하지 않는 경우 파일 생성
        File file = new File("C:\\Java_workspace\\VendingMachine\\src\\FileIO\\Soldout.txt");
        if (!file.exists()) {
            try {
                // 파일 생성
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int row = 0;
            do {
                row++;  // 파일에서 데이터를 작성할 row 위치
            }  while((line = reader.readLine()) != null);
            reader.close();
            reader = new BufferedReader(new FileReader(file));

            String datas[] = new String[row];   // 파일의 정보를 읽어와 저장하는 datas
            row = 0;
            
            // 파일을 한 줄씩 읽어와 datas 배열에 저장
            while ((line = reader.readLine()) != null) {
                datas[row++] = line;
            }

            // 오늘 일자와 재고가 소진된 음료의 이름을 newData에 저장
            String newData = soldoutTime + "," + StartMachine.vm.getBeverageName(beverageIdx);

            // 파일 마지막 줄에 새로운 데이터 저장
            datas[row] = newData;

            // 변경 사항이 반영된 데이터를 파일에 다시 작성
            FileWriter writer = new FileWriter(file);
            for (String rowValue : datas) {
                writer.write(rowValue + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 음료 재고 소진 파일 읽어오기
    public String[][] readSoldout(String fileName) {
        
        // 데이터 파일의 경로
        String filePath = "src/FileIO/" + fileName + ".txt";
        String[][] soldout = new String[0][];

        File dataFile = new File(filePath);

        // 데이터 파일이 존재하는 경우
        if (dataFile.exists()) {
            int idx = 0;
            try {
                BufferedReader br = new BufferedReader(new FileReader(filePath));
                String line;
                int row = 0;
                do {
                    row++;  // 파일의 데이터 개수 읽기
                } while ((line = br.readLine()) != null);

                // 소진날짜, 음료이름을 저장하는 soldout 배열 초기화
                soldout = new String[row][2];

                br = new BufferedReader(new FileReader(filePath));

                // 파일에서 데이터를 한 줄씩 읽어와 soldout 배열에 저장
                while ((line = br.readLine()) != null) {
                    soldout[idx][0] = line.split(",")[0];
                    soldout[idx++][1] =line.split(",")[1];
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return soldout;
    }
}
