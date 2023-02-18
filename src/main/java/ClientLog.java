import com.opencsv.CSVWriter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private List<String[]> logs;

    public ClientLog() {
        logs = new ArrayList<>();
    }

    public void log(int productNum, int amount) {
        logs.add(new String[]{String.valueOf(productNum), String.valueOf(amount)});
    }

    public void exportAsCSV(File txtFile) throws IOException {
        if (txtFile.exists()) {
            try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile, true))) {
                for (String[] log : logs) {
                    writer.writeNext(log, false);
                }
            }
        } else {
            txtFile.createNewFile();
            try (CSVWriter writer = new CSVWriter(new FileWriter(txtFile))) {
                writer.writeNext(new String[]{"productNum", "amount"}, false);
                for (String[] log : logs) {
                    writer.writeNext(log, false);
                }
            }
        }
    }
}
