import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

// Название класса отражает его функциональное назначение
public class DownloadPictures {
    public static void main(String[] args) {
        // Прямая ссылка на тестовое изображение (JPG)
        String urlString = "https://png.pngtree.com/thumb_back/fh260/background/20230610/pngtree-picture-of-a-blue-bird-on-a-black-background-image_2937385.jpg";

        // Указываем полный путь
        String downloadDir = "F:\\newDownloads";

        // Объединяем директорию и имя файла с правильным расширением для картинок
        String filePath = downloadDir + File.separator + "downloaded_image.jpg";

        Thread downloadThread = new Thread(() -> {
            System.out.println("Подождите, идет скачивание изображения в: " + filePath);
            try {
                // Вызов переименованного метода
                long durationMs = downloadImageFile(filePath, urlString);

                // Используем printf для красивого форматирования вывода
                System.out.printf("Скачивание успешно завершено за %d мс%n", durationMs);

            } catch (IOException ex) {
                System.err.println("Ошибка при скачивании: " + ex.getMessage());
            }
        });

        downloadThread.start();
        try {
            downloadThread.join();
        } catch (InterruptedException ex) {
            System.err.println("Скачивание было прервано");
            Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания потока
        }
    }

    // Название метода четко описывает действие: скачать файл изображения
    public static long downloadImageFile(final String filePath, final String urlString) throws IOException {
        long startTime = System.currentTimeMillis();

        // Проверяем существование папки перед записью (создаем, если ее нет)
        File targetFile = new File(filePath);
        File parentDir = targetFile.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Try-with-resources: потоки закроются автоматически
        try (BufferedInputStream in = new BufferedInputStream(new URL(urlString).openStream());
             FileOutputStream fout = new FileOutputStream(targetFile)) {

            // Буфер 8192 байта (8 КБ) оптимален для скачивания как аудио, так и изображений
            final byte[] data = new byte[8192];
            int count;
            while ((count = in.read(data, 0, 8192)) != -1) {
                fout.write(data, 0, count);
            }
        }

        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}