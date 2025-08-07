package com.abc.hr.app.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CleanupOldCsvFilesTasklet implements Tasklet {

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile("employees_(\\d{14})\\.csv");

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) {
        File resourceDir = new File("src/main/resources");
        File[] csvFiles = resourceDir.listFiles((dir, name) -> name.endsWith(".csv"));

        if (csvFiles != null && csvFiles.length > 1) {
            // Sort files by timestamp extracted from filename
            Arrays.sort(csvFiles, Comparator.comparingLong(this::extractTimestamp).reversed());

            // Keep the most recent file, delete the rest
            for (int i = 1; i < csvFiles.length; i++) {
                File file = csvFiles[i];
                boolean deleted = file.delete();
                if (deleted) {
                    System.out.println("Deleted old file: " + file.getName());
                } else {
                    System.out.println("Failed to delete file: " + file.getName());
                }
            }
        } else {
            System.out.println("No old CSV files to delete or only one file present.");
        }
        return RepeatStatus.FINISHED;
    }

    private long extractTimestamp(File file) {
        Matcher matcher = TIMESTAMP_PATTERN.matcher(file.getName());
        if (matcher.matches()) {
            try {
                return Long.parseLong(matcher.group(1));
            } catch (NumberFormatException e) {
                System.out.println("Invalid timestamp in file: " + file.getName());
            }
        }
        return 0L; // Treat files without valid timestamp as oldest
    }
}
