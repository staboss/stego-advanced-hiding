package com.staboss.coding.coder;

import com.staboss.coding.model.CoderState;

/**
 * Несистематический кодер сверточного кода
 *
 * @author Boris Stasenko
 * @see CoderState
 * @see Coder
 */
public final class NonSystematicCoder extends Coder {

    public NonSystematicCoder(String g1, String g2) throws Exception {
        super(g1, g2);
    }

    @Override
    protected void code(CoderState state, int u) throws Exception {
        if (u != 0 && u != 1) throw new Exception("Invalid Bit Value");

        //  "101" -> {'1', '0', '1'}
        char[] bytes = state.getState().toCharArray();

        int out1 = u;
        int out2 = u;

        //  XOR по g1
        for (int i = 1; i < super.getPolynomial1().length; i++)
            if (super.getPolynomial1()[i] == '1')
                out1 ^= Integer.parseInt(String.valueOf(bytes[i - 1]));

        //  XOR по g2
        for (int i = 1; i < super.getPolynomial2().length; i++)
            if (super.getPolynomial2()[i] == '1')
                out2 ^= Integer.parseInt(String.valueOf(bytes[i - 1]));

        //  устанавливаем состояния на выходе и значения C1 и C2 после XOR
        if (u == 0) {
            state.setOutZero(out1 + String.valueOf(out2));
            state.setOutStateZero(u + state.getState().substring(0, state.getState().length() - 1));
        } else {
            state.setOutOne(out1 + String.valueOf(out2));
            state.setOutStateOne(u + state.getState().substring(0, state.getState().length() - 1));
        }

        //  устанавливаем состояния, из которых получается текущее состояние
        state.setInStateZero(state.getState().substring(1).concat("0"));
        state.setInStateOne(state.getState().substring(1).concat("1"));
    }
}
