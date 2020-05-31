package com.staboss.coding.coder;

import com.staboss.coding.model.CoderState;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Кодер сверточного кода
 *
 * @author Boris Stasenko
 * @see CoderState
 */
public abstract class Coder {

    private Map<String, CoderState> states;      //  последовательности переходов

    private String format;                       //  формат хранимых состояний

    private char[] polynomial1;                  //  полином 1
    private char[] polynomial2;                  //  полином 2

    private int maxPow;                          //  максимальная степень полинома
    private int stateCount;                      //  количество состояний

    Coder(String polynomial1, String polynomial2) throws Exception {
        this.states = new LinkedHashMap<>();

        this.polynomial1 = polynomial1.toCharArray();
        this.polynomial2 = polynomial2.toCharArray();

        this.maxPow = Math.max(polynomial1.length() - 1, polynomial2.length() - 1);
        this.stateCount = (int) Math.pow(2, maxPow);

        setStates(stateCount);
        setOuts();
    }

    public Map<String, CoderState> getStates() {
        return states;
    }

    public int getMaxPow() {
        return maxPow;
    }

    public int getStateCount() {
        return stateCount;
    }

    char[] getPolynomial1() {
        return polynomial1;
    }

    char[] getPolynomial2() {
        return polynomial2;
    }

    /**
     * Определяет состояния на выходе после сдвига регистра на 0 или 1 для каждого сотояния
     *
     * @param state состояние
     * @param u     входной бит 0 или 1
     */
    protected abstract void code(CoderState state, int u) throws Exception;

    /**
     * Составляет последовательность переходов
     *
     * @param stateCount количество состояний
     * @throws Exception если передано недопустимое количество состояний
     */
    private void setStates(int stateCount) throws Exception {
        //  доступные форматы : '00', '000', '0000', '00000', '000000'
        switch (stateCount) {
            case 4:
                format = "%2s";
                break;
            case 8:
                format = "%3s";
                break;
            case 16:
                format = "%4s";
                break;
            case 32:
                format = "%5s";
                break;
            case 64:
                format = "%6s";
                break;
            default:
                format = null;
                break;
        }

        if (format == null) throw new Exception("Invalid Number of States");

        for (int i = 0; i < stateCount; i++) {
            String value = String.format(format, Integer.toBinaryString(i)).replaceAll(" ", "0");
            states.put(value, new CoderState(value));
        }
    }

    /**
     * Для каждого состояния получает С1 и С2 при передаче 0 и 1
     */
    private void setOuts() {
        states.forEach((value, state) -> {
            try {
                code(state, 0);
                code(state, 1);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Кодирует входную информационную последовательность
     *
     * @param bits информационная последовательность
     * @return статистически зависимая информационная последовательность
     */
    public String encode(String bits) {
        //  результат кодера
        StringBuilder sb = new StringBuilder();

        //  запоминаем значение нулевого состояния
        String initialState = String.format(format, Integer.toBinaryString(0)).replaceAll(" ", "0");

        //  текущее состояние перехода
        CoderState currentState = states.get(initialState);

        //  "101" -> {'1', '0', '1'}
        char[] seq = bits.toCharArray();

        for (char s : seq) {
            if (s == '0') {
                sb.append(currentState.getOutZero());
                currentState = states.get(currentState.getOutStateZero());
            } else if (s == '1') {
                sb.append(currentState.getOutOne());
                currentState = states.get(currentState.getOutStateOne());
            }
        }

        //  переводим регистр сдвига в нулевое состояние
        for (int i = 0; i < initialState.length(); i++) {
            sb.append(currentState.getOutZero());
            currentState = states.get(currentState.getOutStateZero());
        }

        return sb.toString();
    }

    /**
     * Декодирует входную информационную последовательность
     *
     * @param bits информационная последовательность
     * @return декодированная последовательность
     */
    public String decode(String bits) {
        //  результат декодирование
        StringBuilder sb = new StringBuilder();

        //  запоминаем значение нулевого состояния
        String initialState = String.format(format, Integer.toBinaryString(0)).replaceAll(" ", "0");

        //  текущее состояние перехода
        CoderState currentState = states.get(initialState);

        //  "101" -> {'1', '0', '1'}
        char[] seq = bits.toCharArray();

        String value;
        for (int i = 0; i < seq.length - 1; i += 2) {
            value = String.valueOf(seq[i]) + seq[i + 1];
            if (value.equals(currentState.getOutZero())) {
                sb.append('0');
                currentState = states.get(currentState.getOutStateZero());
            } else if (value.equals(currentState.getOutOne())) {
                sb.append('1');
                currentState = states.get(currentState.getOutStateOne());
            }
        }

        String result = sb.toString();
        return result.substring(0, result.length() - initialState.length());
    }
}
