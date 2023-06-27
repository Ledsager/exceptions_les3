/*
Напишите приложение, которое будет запрашивать у пользователя следующие данные в произвольном порядке,
разделенные пробелом:
Фамилия Имя Отчество датарождения номертелефона пол
Форматы данных:
фамилия, имя, отчество - строки
дата_рождения - строка формата dd.mm.yyyy
номер_телефона - целое беззнаковое число без форматирования
пол - символ латиницей f или m.
Приложение должно проверить введенные данные по количеству. Если количество не совпадает с требуемым,
вернуть код ошибки, обработать его и показать пользователю сообщение, что он ввел меньше и больше данных,
чем требуется.
Приложение должно попытаться распарсить полученные значения и выделить из них требуемые параметры.
Если форматы данных не совпадают, нужно бросить исключение, соответствующее типу проблемы. Можно использовать
встроенные типы java и создать свои. Исключение должно быть корректно обработано, пользователю выведено сообщение
с информацией, что именно неверно.
Если всё введено и обработано верно, должен создаться файл с названием, равным фамилии, в него в одну строку
должны записаться полученные данные, вида
<Фамилия><Имя><Отчество><датарождения> <номертелефона><пол>
Однофамильцы должны записаться в один и тот же файл, в отдельные строки.
Не забудьте закрыть соединение с файлом.
При возникновении проблемы с чтением-записью в файл, исключение должно быть корректно обработано, пользователь
должен увидеть стектрейс ошибки.
 */

package org.example;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            String str = getNullInUser();
            String[] data = str.split(" ");
            if (data.length != 6) {
                throw new NullInUserException("Введено неверное количество данных.");
            }

            String lastName = data[0];
            String firstName = data[1];
            String patronymic = data[2];
            String dateOfBirth = data[3];
            String phoneNumber = data[4];
            String gender = data[5];

            validateData(lastName, firstName, patronymic, dateOfBirth, phoneNumber, gender);

            String filename = lastName + ".txt";
            String fileContent = lastName + " " + firstName + " " + patronymic +
                    " " + dateOfBirth + " " + phoneNumber + " " + gender;

            FileIOImpl(filename, fileContent);
        } catch (SearchNotFileException | NullInUserException ex) {
            System.out.println(ex.getMessage());
//            ex.printStackTrace();
        }

    }

    public static String getNullInUser() throws NullInUserException{
        Scanner scanner = new Scanner(System.in);
        String str = "";

        System.out.print("Фамилия Имя Отчество датарождения(dd.mm.yyyy) номер телефона пол(f или m): ");
        str = scanner.nextLine();
        if (str.isEmpty()) try {
            throw new Exception();
        } catch (Exception e) {
            throw new NullInUserException("пустые строки вводить нельзя!");
        }
        scanner.close();
        return str;
    }

    public static void FileIOImpl(String path, String str) throws SearchNotFileException {
        try (FileWriter writer = new FileWriter(path, true)) {
            writer.write(str);
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            throw new SearchNotFileException("Файл не найден ошибка");
        }
    }

    private static void validateData(String lastName, String firstName, String middleName, String dateOfBirth,
                                     String phoneNumber, String gender) throws NullInUserException {
        if (!isValidDate(dateOfBirth)) {
            throw new NullInUserException("Неверный формат даты рождения(dd.mm.yyyy): " + dateOfBirth);
        }

        if (!isValidPhoneNumber(phoneNumber)) {
            throw new NullInUserException("Неверный формат, номер телефона без знака(пример:89211525254): " + phoneNumber);
        }

        if (!isValidGender(gender)) {
            throw new NullInUserException("Неверный формат, пол только 'f' или 'm': " + gender);
        }
    }

    private static boolean isValidDate(String date) {
        String regex = "^\\d{2}\\.\\d{2}\\.\\d{4}$";
        return date.matches(regex);
    }

    private static boolean isValidPhoneNumber(String phoneNumber) {
        try {
            Long.parseLong(phoneNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidGender(String gender) {
        return gender.equals("f") || gender.equals("m");
    }

}
class NullInUserException extends Exception {
        public NullInUserException(String message) {
            super(message);
        }
    }
class SearchNotFileException extends IOException {
        public SearchNotFileException(String message) {
            super(message);
        }
    }
