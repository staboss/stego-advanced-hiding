package com.staboss.coding.model;

import java.util.Objects;

public final class CoderState {

    private final String state;     //  текущее состояние

    private String outZero;         //  С1 и С2 (результат кодирования) при 0
    private String outOne;          //  С1 и С2 (результат кодирования) при 1

    private String outStateZero;    //  новое состояние после сдвига регистра на 0
    private String outStateOne;     //  новое состояние после сдвига регистра на 1

    private String inStateZero;     //  состояние до сдвига регистра на 0
    private String inStateOne;      //  состояние до сдвига регистра на 1

    public CoderState(String state) {
        this.state = state;
    }

    private CoderState(StateBuilder builder) {
        this.state = builder.state;
        this.outZero = builder.outZero;
        this.outOne = builder.outOne;
        this.outStateZero = builder.outStateZero;
        this.outStateOne = builder.outStateOne;
        this.inStateZero = builder.inStateZero;
        this.inStateOne = builder.inStateOne;
    }

    public static StateBuilder builder() {
        return new StateBuilder();
    }

    public static class StateBuilder {

        private String state;

        private String outZero;
        private String outOne;

        private String outStateZero;
        private String outStateOne;

        private String inStateZero;
        private String inStateOne;

        public StateBuilder state(String state) {
            this.state = state;
            return this;
        }

        public StateBuilder outZero(String outZero) {
            this.outZero = outZero;
            return this;
        }

        public StateBuilder outOne(String outOne) {
            this.outOne = outOne;
            return this;
        }

        public StateBuilder outStateZero(String outStateZero) {
            this.outStateZero = outStateZero;
            return this;
        }

        public StateBuilder outStateOne(String outStateOne) {
            this.outStateOne = outStateOne;
            return this;
        }

        public StateBuilder inStateZero(String inStateZero) {
            this.inStateZero = inStateZero;
            return this;
        }

        public StateBuilder inStateOne(String inStateOne) {
            this.inStateOne = inStateOne;
            return this;
        }

        public CoderState build() {
            return new CoderState(this);
        }
    }

    public String getState() {
        return state;
    }

    public String getOutStateZero() {
        return outStateZero;
    }

    public void setOutStateZero(String outStateZero) {
        this.outStateZero = outStateZero;
    }

    public String getOutStateOne() {
        return outStateOne;
    }

    public void setOutStateOne(String outStateOne) {
        this.outStateOne = outStateOne;
    }

    public String getOutZero() {
        return outZero;
    }

    public void setOutZero(String outZero) {
        this.outZero = outZero;
    }

    public String getOutOne() {
        return outOne;
    }

    public void setOutOne(String outOne) {
        this.outOne = outOne;
    }

    public String getInStateZero() {
        return inStateZero;
    }

    public void setInStateZero(String inState1) {
        this.inStateZero = inState1;
    }

    public String getInStateOne() {
        return inStateOne;
    }

    public void setInStateOne(String inState2) {
        this.inStateOne = inState2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CoderState state1 = (CoderState) o;
        return state.equals(state1.state) &&
                Objects.equals(outStateZero, state1.outStateZero) &&
                Objects.equals(outStateOne, state1.outStateOne) &&
                Objects.equals(inStateZero, state1.inStateZero) &&
                Objects.equals(inStateOne, state1.inStateOne);
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, outStateZero, outStateOne, inStateZero, inStateOne);
    }

    @Override
    public String toString() {
        return String.format("%s:\t0|%s|%s\t1|%s|%s\t{%s, %s}",
                state, outZero, outStateZero, outOne, outStateOne, inStateZero, inStateOne
        );
    }
}
