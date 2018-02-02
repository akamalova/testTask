import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/*
*Задание:
В архиве в папке in находится набор файлов in1.txt, in2.txt ... in10.txt.
Каждый файл имеет одинаковую структуру:
первая строка - количество строк, которые нужно считать из файла.
вторая строка - количество чисел, которые нужно и обработать из каждой строки.
третья и все последующие строки - целые числа, разделенные пробелом.

Нужно для каждого файла in.txt создать файл out.txt с таким же номером, cодержащий сумму считанных по каждой строке чисел, наибольшее и наименьшее значение,
в конце файла добавить общую сумму всех чисел по всему файлу (в том числе те, что не учитывались при поиски min и max).
Кроме того создать файл с названием result.txt, содержащий названия файлов out, отсортированных по убыванию поля total в них.

Формат выходного файла out должен быть таким:

Для каждой обработанной строки
<номер строки> : sum=<сумма считанных элементов> : max=<наибольшее значение> : min=<наименьшее значение>
В последней строке total=<общая сумма чисел в файле>
Важно:
1) При вычислении общей суммы чисел, первые две строки не учитываются
2) Если в файле in.txt меньше строк, чем указано в первой строке (первая и вторая строка не считаются), то в выходном файле out.txt должна содержаться 2 строки:
 Error, найдено 3 строки вместо 5
 total=0
 где 3 и 5 это общее количество строк и количество обрабатываемых строк
3) Если в какой либо строке файла in.txt чисел меньше чем указано во второй строке, то в файле out.txt для это строки должно быть
 <номер строки> : Error найдено 6 чисел вместо 11
 где 6 и 11 общее количество чисел в строке и количество обрабатываемых чисел для каждой строки.
4) Результатом выполнения задания является код на Java. Код долежен компилироваться, содержать комментарии при необходимости
5) Использовать сторонние библиотеки разрешно, но тогда обязательно использовать систему сборки Maven/Gradle
6) Плюсом будет код, выложенный в публичный репозиторий на Githab, и ссылка на него
 */

public class Solution {

    private static Path zipPath;
    public static final String CORRECT_PATH = "in/in";
    public static final String INPUT = "in";
    public static final String OUTPUT = "out";
    public static final String RESULT_PATH = "result.txt";
    public static final int CARRIAGE_RETURN = 13;
    public static final int LINE_FEED = 10;
    private static String fileName = "";


    public static void main(String[] args) {
        zipPath = getfilePath();
        Map<String, StringBuffer> resultMap = readFromZip();
        writeToZip(zipPath, resultMap);
        System.out.println("Выполнено!");
    }

    public static Path getfilePath() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {            // чтение адреса с консоли
            while (true) {
                System.out.println("Введите путь архива");
                Path path = Paths.get(reader.readLine());
                if (Files.exists(path)) return path;
                else System.out.println("Путь архива введен неверно, попробуйте еще раз");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Map<String, StringBuffer> readFromZip() {
        Map<String, StringBuffer> resultMap = new HashMap<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(zipPath))) {        // Создаем zip поток

            ZipEntry zipEntry;                                                                         // Проходимся по содержимому zip потока
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                fileName = zipEntry.getName();
                String outputFileName = fileName.replace(INPUT, OUTPUT);                                //Создаем имя для файла
                List<String> inputList = new ArrayList<>();

                if (!fileName.contains(CORRECT_PATH)) continue;

                StringBuffer sb = new StringBuffer();
                StringBuffer inputSb = new StringBuffer();
                int lineCountRequiered = 0;
                int numCountRequiered = 0;
                int newChar;
                boolean isRight = true;

                while ((newChar = zipInputStream.read()) != -1) {                                       // чтение файлов из архива и запись в соответствующие поля
                    if (isRight) {
                        if (newChar == CARRIAGE_RETURN) {
                            if (numCountRequiered == 0) {
                                try {
                                    if (lineCountRequiered == 0) lineCountRequiered = Integer.parseInt(sb.toString());
                                    else numCountRequiered = Integer.parseInt(sb.toString());

                                } catch (NumberFormatException n) {
                                    inputList = new ArrayList<>();
                                    isRight = false;
                                }
                            } else inputList.add(sb.toString());

                            sb = new StringBuffer();
                        } else if (newChar != LINE_FEED) sb.append((char) newChar);
                    }
                    inputSb.append((char) newChar);
                }
                inputList.add(sb.toString());

                int lineCountActual = inputList.size();
                StringBuffer outputSB = getOutputSb(inputList, lineCountActual, lineCountRequiered, numCountRequiered);
                resultMap.put(outputFileName, outputSB);                                            // подготовка файлов для записи в архив
                resultMap.put(fileName, inputSb);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public static StringBuffer getOutputSb(List<String> inputList, int lineCountActual, int lineCountRequiered, int numCountRequiered) {  // обработка массива чисел для вывода по условию

        StringBuffer outputSb = new StringBuffer();
        int numCountActual;
        int totalSum = 0;

        if (lineCountRequiered > lineCountActual) {                                                 // если строк в файле меньше заданного
            outputSb.append(String.format("Error, найдено %d строки вместо %d\r\n", lineCountActual, lineCountRequiered));
            outputSb.append("total=0");

        } else {
            try {
                for (int i = 0; i < inputList.size(); i++) {

                    List<String> totalSumList = Arrays.stream(inputList.get(i).split(" ")).collect(Collectors.toList());
                    totalSum += totalSumList.stream().mapToInt(Integer::parseInt).sum();
                    numCountActual = totalSumList.size();

                    if (i < lineCountRequiered) {
                        if (numCountRequiered > numCountActual) {                                   // если чисел в строке меньше заданного
                            outputSb.append(String.format("%d : Error найдено %d чисел вместо %d\r\n", i + 1, numCountActual, numCountRequiered));

                        } else {
                            List<String> collected = Arrays.stream(inputList.get(i).split(" "))
                                    .limit(numCountRequiered)
                                    .sorted()
                                    .collect(Collectors.toList());
                            outputSb.append(String.format("%d : sum=%d : max=%s : min=%s\r\n", i + 1, collected.stream().mapToInt(Integer::parseInt).sum(),
                                    collected.get(collected.size() - 1), collected.get(0)));
                        }
                    }
                }
                outputSb.append("total=").append(totalSum);

            } catch (NumberFormatException n) {
                String str = "Ошибка формата данных в документе " + fileName + ": " + n.getMessage();
                System.out.println(str);
                outputSb = new StringBuffer(str);
            }
        }
        return outputSb;
    }

    public static void writeToZip(Path zipPath, Map<String, StringBuffer> resultMap) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipPath))) {   // поток на запись в архив

            for (Map.Entry<String, StringBuffer> pair : resultMap.entrySet()) {
                zipOutputStream.putNextEntry(new ZipEntry(pair.getKey()));
                zipOutputStream.write(pair.getValue().toString().getBytes());
                zipOutputStream.closeEntry();
            }

            zipOutputStream.putNextEntry(new ZipEntry(RESULT_PATH));
            writeResult(zipOutputStream, resultMap);
            zipOutputStream.closeEntry();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void writeResult(ZipOutputStream zipOutputStream, Map<String, StringBuffer> resultMap) throws IOException {
        Map<String, Integer> map = new HashMap<>();                                                // получение данных для result.txt из файла на запись
        for (Map.Entry<String, StringBuffer> entry : resultMap.entrySet()) {
            int i = 0;
            String st;
            if (entry.getKey().contains(OUTPUT)) {
                if (!entry.getValue().toString().startsWith("Ошибка")) {
                    st = entry.getValue().substring(entry.getValue().lastIndexOf("=") + 1);
                    i = Integer.parseInt(st);
                }
                map.put(entry.getKey(), i);
            }
        }
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> (o1.getValue().compareTo(o2.getValue())));

        List<String> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : list) result.add(entry.getKey().replace(OUTPUT + "/", ""));

        for (String s : result) zipOutputStream.write((s + "\r\n").getBytes());                 //запись файла в архив
    }
}