package com.staboss.coding.decoder;

import com.staboss.coding.model.GridPoint;
import com.staboss.coding.model.CoderState;

import java.util.*;

/**
 * Декодер сверточного кода по алгоритму Витерби
 *
 * @author Boris Stasenko
 * @see CoderState
 * @see GridPoint
 */
public final class Decoder {

    private Map<Integer, Set<GridPoint>> layers;        //  слои решетки
    private Map<String, CoderState> states;             //  состояния

    private String firstState;                          //  начальное состояние первой точки решетки
    private String[] vector;                            //  информационная последовательность
    private String format;                              //  формат хранимых состояний

    private GridPoint lastPoint;                        //  последняя точка решетки

    private String[] pointSequence;                     //  полученный путь из точек
    private String[] decodedSequence;                   //  полученная последовательность

    private int errors;                                 //  количество ошибок в последовательности

    public Decoder(Map<String, CoderState> states, String firstState, String[] vector) throws Exception {
        this.layers = new LinkedHashMap<>();
        this.states = states;

        this.firstState = firstState;
        this.vector = vector;

        int len = firstState.length();

        switch (len) {
            case 2:
                format = "%2s";
                break;
            case 3:
                format = "%3s";
                break;
            case 4:
                format = "%4s";
                break;
            case 5:
                format = "%5s";
                break;
            case 6:
                format = "%6s";
                break;
            default:
                format = null;
                break;
        }

        if (format == null) throw new Exception("Invalid Initial State Format");

        //  создание решетки
        createGrid();

        //  поиск пути
        findMinWay();
    }

    public Map<Integer, Set<GridPoint>> getLayers() {
        return layers;
    }

    public String[] getPointSequence() {
        return pointSequence;
    }

    public String[] getDecodedSequence() {
        return decodedSequence;
    }

    public String getDecodedSequenceString() {
        return String.join("", decodedSequence);
    }

    public int getErrors() {
        return errors;
    }

    /**
     * Выводит сравнительный анализ полученной последовательности
     */
    private void findMinWay() {

        //  полученная информационная последовательность
        decodedSequence = new String[vector.length];

        //  зависимость точек решетки
        pointSequence = new String[vector.length];

        //  последняя точка решетки
        GridPoint currentPoint = lastPoint.getPathFromPoint();

        //  в последней точке записано количество ошибок
        errors = lastPoint.getErrors();

        //  собираем путь
        for (int i = 0; i < vector.length; i++) {
            pointSequence[vector.length - i - 1] = currentPoint.getValue();
            currentPoint = currentPoint.getPathFromPoint();
        }

        //  собираем информационную последовательность
        for (int i = 0; i < decodedSequence.length; i++) {
            if (i + 1 < decodedSequence.length) {
                decodedSequence[i] = getBranchValue(pointSequence[i], pointSequence[i + 1]);
            } else {
                decodedSequence[i] = getBranchValue(pointSequence[i], pointSequence[0]);
            }
        }
    }

    /**
     * Строит решетку переходов состояний с количеством ошибок в каждой точке решетки
     */
    private void createGrid() {

        //  количество слоев -> (длина вектора / 2) + 1
        int layersNumber = vector.length + 1;
        int k = firstState.length();

        //  текущий слой решеткки
        Set<GridPoint> currentLayer = new LinkedHashSet<>();

        //  будущий слой решетки
        Set<GridPoint> nextLayer;

        //  множетсво для переходе в точку из двух точек
        Set<GridPoint> points;

        //  выжившие состояния на решетке
        List<String> survivedStates;

        //  начальная точка на решетке
        CoderState initialState = states.get(firstState);
        GridPoint initialPoint = getOutPoint(firstState, 0, 0, initialState, null);

        //  начальный слой в решетке
        currentLayer.add(initialPoint);

        //  множество слоев решетки
        layers.put(0, currentLayer);

        //  количество выживших точек на последних слоях
        int survivedPointsNumber = (int) Math.pow(2, k) / 2;

        //  цикл по каждому слою
        for (int level = 1; level < layersNumber; level++) {

            //  инициализация будущего слоя
            nextLayer = new LinkedHashSet<>();

            //  заполняем слой в зависимости от номера слоя
            if (level >= (layersNumber - k)) {

                survivedStates = new ArrayList<>();

                //  проход по выжившим точкам
                for (int i = 0; i < survivedPointsNumber; i++) {
                    survivedStates.add(
                            String.format(format, Integer.toBinaryString(i))
                                    .replaceAll(" ", "0")
                    );
                }

                //  получаем предыдущий слой и делаем его текущим
                points = new HashSet<>(layers.get(level - 1));

                //  точки из предыдущего слоя совпадают с текущим слоем
                for (GridPoint point : points) {

                    if (!survivedStates.contains(point.getValue())) continue;

                    //  получаем точку, в которую пришли две точки из предыдущего слоя
                    GridPoint newPoint = getNewPoint(points, point, level);

                    if (survivedPointsNumber == 1) lastPoint = newPoint;

                    //  добавляем точку в следующий слой
                    nextLayer.add(newPoint);
                }

                //  уменьшаем количество выживших точек в 2 раза
                survivedPointsNumber /= 2;

            } else if (level < (layersNumber - k) && level > k) {
                //  получаем предыдущий слой и делаем его текущим
                points = new HashSet<>(layers.get(level - 1));

                //  точки из предыдущего слоя совпадают с текущим слоем
                for (GridPoint point : points) {

                    //  получаем точку, в которую пришли две точки из предыдущего слоя
                    GridPoint newPoint = getNewPoint(points, point, level);

                    //  добавляем точку в следующий слой
                    nextLayer.add(newPoint);
                }

                //  переходим к следующему слою
                currentLayer = new LinkedHashSet<>(nextLayer);

            } else {
                //  для каждой точки из текущего слоя
                for (GridPoint point : currentLayer) {

                    //  получаем ветки, по которым точка выходит
                    String branch0 = point.getBranchPath0();
                    String branch1 = point.getBranchPath1();

                    //  получаем количество ошибок в текущей точке
                    int errors = point.getErrors();

                    //  получаем количество ошибок на в новых точках
                    int error0 = getErrors(vector[level - 1], point.getBranchValue0()) + errors;
                    int error1 = getErrors(vector[level - 1], point.getBranchValue1()) + errors;

                    //  добавляем точки в следующий слой
                    GridPoint point0 = getOutPoint(branch0, error0, level, states.get(branch0), point);
                    GridPoint point1 = getOutPoint(branch1, error1, level, states.get(branch1), point);

                    nextLayer.add(point0);
                    nextLayer.add(point1);
                }

                //  переходим к следующему слою
                currentLayer = new LinkedHashSet<>(nextLayer);
            }

            //  добавляем уровень и слой в множество слоев
            layers.put(level, nextLayer);
        }
    }

    /**
     * Возвращает точку, в которую пришли две точки из предыдущего слоя
     *
     * @param points множество точек
     * @param point  точка на решетке
     * @param level  слой
     * @return точка на решетке
     */
    private GridPoint getNewPoint(Set<GridPoint> points, GridPoint point, int level) {
        //  в каждую точку приходят две другие точки (состояния точек)
        CoderState inState1 = states.get(states.get(point.getValue()).getInStateZero());
        CoderState inState2 = states.get(states.get(point.getValue()).getInStateOne());

        //  пришедшие точки
        GridPoint inPoint1 = getInPoint(points, inState1);
        GridPoint inPoint2 = getInPoint(points, inState2);

        //  количество ошибок в точках
        int errorsFromInState1 = inPoint1.getErrors();
        int errorsFromInState2 = inPoint2.getErrors();

        //  количество ошибок в новой точке
        int error1, error2;

        //  определяем по 0 или 1 пришли точки
        if (Integer.parseInt(point.getValue(), 2) >= (states.size() / 2)) {
            error1 = getErrors(vector[level - 1], inState1.getOutOne()) + errorsFromInState1;
            error2 = getErrors(vector[level - 1], inState2.getOutOne()) + errorsFromInState2;
        } else {
            error1 = getErrors(vector[level - 1], inState1.getOutZero()) + errorsFromInState1;
            error2 = getErrors(vector[level - 1], inState2.getOutZero()) + errorsFromInState2;
        }

        //  итоговое количество ошибок, которое будет записано
        int finalError;

        //  полученная точка
        GridPoint newPoint = GridPoint.builder()
                .value(point.getValue())
                .level(level)
                .branchPath0(point.getBranchPath0())
                .branchPath1(point.getBranchPath1())
                .build();

        //  устанавляваем оптимальный пуль и количество ошибок для новой точки
        if (error1 < error2) {
            finalError = error1;
            newPoint.setErrors(finalError);
            newPoint.setPathFromPoint(inPoint1);
        } else {
            finalError = error2;
            newPoint.setErrors(finalError);
            newPoint.setPathFromPoint(inPoint2);
        }

        //  устанавляваем значения С1 и С2 для переходов по 0 и 1
        newPoint.setBranchValue0(point.getBranchValue0());
        newPoint.setBranchValue1(point.getBranchValue1());

        return newPoint;
    }

    /**
     * Возвращает значение на ветке перехода
     *
     * @param fromPoint точка выхода
     * @param toPoint   точка входа
     * @return значение ветки
     */
    private String getBranchValue(String fromPoint, String toPoint) {
        CoderState state = states.get(fromPoint);
        if (state.getOutStateZero().equals(toPoint)) {
            return state.getOutZero();
        } else {
            return state.getOutOne();
        }
    }

    /**
     * Возвращает количество ошибок от 0 до 2
     *
     * @param vector      значение вектора
     * @param branchValue значение ветки
     * @return количество ошибок
     */
    private int getErrors(String vector, String branchValue) {
        int count = 0;
        if (vector.charAt(0) != branchValue.charAt(0)) count++;
        if (vector.charAt(1) != branchValue.charAt(1)) count++;
        return count;
    }

    /**
     * В зависимости от состояния возвращает точку на решетке
     *
     * @param points  множество точек
     * @param inState состояние точки
     * @return новая входящая точка
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")
    private GridPoint getInPoint(Set<GridPoint> points, CoderState inState) {
        return points.stream()
                .filter(p -> Objects.equals(p.getValue(), inState.getState()))
                .findFirst()
                .get();
    }

    /**
     * Создает новую выходящую точку
     *
     * @param branchValue значение точки
     * @param errors      количество ошибок
     * @param level       уровень слоя
     * @param state       состояние
     * @param point       родительская точка
     * @return новая выходящая точка
     */
    private GridPoint getOutPoint(String branchValue, int errors, int level, CoderState state, GridPoint point) {
        GridPoint.GridPointBuilder builder = GridPoint.builder()
                .value(branchValue)
                .errors(errors)
                .level(level)
                .branchPath0(state.getOutStateZero())
                .branchPath1(state.getOutStateOne())
                .branchValue0(state.getOutZero())
                .branchValue1(state.getOutOne());

        if (point != null) {
            builder = builder.pathFromPoint(point);
        }

        return builder.build();
    }

    /**
     * Разбивает последовательность на массив элементов длинной в два символа
     *
     * @param vector информационная последовательность
     * @return массив последовательности
     */
    public static String[] createVectorValues(String vector) {
        String[] vectorValues = new String[(vector.length() >> 1)];

        //  счетчик для индексов массива
        int k = 0;

        //  проход по массиву через каждые 2 символа для нахождения новой подстроки
        for (int i = 0; i < vector.length() - 1; i = i + 2) {
            vectorValues[k++] = vector.substring(i, i + 2);
        }

        return vectorValues;
    }
}
