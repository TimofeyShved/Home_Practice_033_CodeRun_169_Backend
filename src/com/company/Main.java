package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

public class Main {

    /*
	Для чтения входных данных необходимо получить их
	из стандартного потока ввода (System.in).
	Данные во входном потоке соответствуют описанному
	в условии формату. Обычно входные данные состоят
	из нескольких строк. Можно использовать более производительные
	и удобные классы BufferedReader, BufferedWriter, Scanner, PrintWriter.

	С помощью BufferedReader можно прочитать из стандартного потока:
	* строку -- reader.readLine()
	* число -- int n = Integer.parseInt(reader.readLine());
	* массив чисел известной длины (во входном потоке каждое число на новой строке) --
	int[] nums = new int[len];
    for (int i = 0; i < len; i++) {
        nums[i] = Integer.parseInt(reader.readLine());
    }
	* последовательность слов в строке --
	String[] parts = reader.readLine().split(" ");

	Чтобы вывести результат в стандартный поток вывода (System.out),
	Через BufferedWriter можно использовать методы
	writer.write("Строка"), writer.write('A') и writer.newLine().

	Возможное решение задачи "Вычислите сумму чисел в строке":
	int sum = 0;
    String[] parts = reader.readLine().split(" ");
    for (int i = 0; i < parts.length; i++) {
        int num = Integer.parseInt(parts[i]);
        sum += num;
    }
    writer.write(String.valueOf(sum));
	*/

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(System.out));

        // базовые настройки
        double square = 1.0;
        double maxRadius = square/Math.sqrt(2);
        double minRadius = square/2;

        // получаем сколько будет кругов и их радиус
        String[] parts = reader.readLine().split(" ");
        double N = Double.parseDouble(parts[0]);
        double R = Double.parseDouble(parts[1]);

        // получаем их координаты
        List<Double[]> koordinateXY = new ArrayList<Double[]>();

        for (int i = 0; i < N; i++) {
            String[] newXY = reader.readLine().split(" ");
            Double[] pasingXY = new Double[2];

            for (int j = 0; j < pasingXY.length; j++){
                pasingXY[j] = Double.parseDouble(newXY[j]);
            }

            koordinateXY.add(pasingXY);
        }

        // получаем площадь
        double place = 0;
        for (Double[] doubles: koordinateXY){
            if (R <= minRadius){
                place += CircleSmal(doubles, R, square);
            }else if(R < maxRadius){
                place += CircleMedium();
            }else {
                place += CircleBig();
            }
        }

        System.out.println(place);

        /*
        float balls = 0;

        for (Double[] doubles: koordinateXY){
            double newBalls = 1;
            for (double d: doubles){
                if (d < R) {
                    newBalls *= d;
                }else {
                    newBalls *= R;
                }
            }
            newBalls *= Math.PI;
            balls += newBalls;
        }
        */

        Formatter formatter = new Formatter();
        formatter.format("%.10f\n", place);
        System.out.println(formatter);



        //writer.write(s);


        reader.close();
        writer.close();
    }

    // расчёты мальних кругов
    public static double CircleSmal(Double[] koordinateXY, double R, double square){
        double x = koordinateXY[0];
        double y = koordinateXY[1];
        x = Polarity(x, square);
        y = Polarity(y, square);

        double place = 0.0;

        if (x<R){
            if(y<R){
                double placeX = PlaceTriangle(x, R);
                double placeY = PlaceTriangle(y, R);
                double angelA = AngelTriangle(x, R);
                double angelB = AngelTriangle(y, R);

                // доходит до угла или нет?
                if(x<(R/2)){
                    //2
                    double placeSquare = PlaceSquare(x, y);
                    double angelSector = 360 - angelA - angelB - 90;
                    double placeSector = PlaceSector(angelSector, R);
                    place += placeSector + placeX + placeY + placeSquare;
                }else {
                    //4
                    double angelSector = 360 - (angelA * 2) - (angelB * 2);
                    double placeSector = PlaceSector(angelSector, R);
                    place += placeSector + (placeX * 2) + (placeY * 2);
                }
            }else {
                //1 боковая грань
                double placeX = PlaceTriangle(x, R);
                double angelA = AngelTriangle(x, R);
                double angelSector = 360 - (angelA * 2);
                double placeSector = PlaceSector(angelSector, R);
                place += placeSector + (placeX * 2);
            }
        }else {
            if (y >= R){
                //5 полность в квадрате
                return R * R * Math.PI;
            }else {
                //3 боковая грань
                double placeY = PlaceTriangle(y, R);
                double angelA = AngelTriangle(y, R);
                double angelSector = 360 - (angelA * 2);
                double placeSector = PlaceSector(angelSector, R);
                place += placeSector + (placeY * 2);
            }
        }

        return place;
    }

    // расчёты средних кругов
    public static double CircleMedium(){
        return 1.0;
    }

    // расчёты больших кругов
    public static double CircleBig(){
        return 1.0;
    }

    // --------------------------- нахождение площади --------------------------

    // изменение полярности, что бы все точки оказались в +- одном квадрте (для простоты подсчёта)
    public static double Polarity(double z, double square){
        if(z > square/2){
            return square - z;
        }
        return z;
    }

    // площадь прямоугольников
    public static double PlaceSquare(double x, double y){
        double place = x * y;
        return place;
    }

    // площадь сектора круга
    public static double PlaceSector(double angelSector, double R){
        double place = ((Math.PI * Math.pow(R, 2)) / 360) * angelSector;
        return place;
    }

    // площадь треугольника
    public static double PlaceTriangle(double catetOne, double R){
        double catetTwo =  Math.sqrt(Math.pow(R, 2) - Math.pow(catetOne, 2));
        double place = (catetOne * catetTwo) / 2;
        return place;
    }

    // нахождения угла треугольника
    public static double AngelTriangle(double catetOne, double R){
        double angelTriangle = Math.cos(catetOne / R) * 180.0d / Math.PI;
        return angelTriangle;
    }
}
