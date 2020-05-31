package com.staboss.coding.model;

import java.util.Objects;

public final class GridPoint {

    private final String value;           //  значение точки
    private int errors;                   //  количество ошибок
    private int level;                    //  уровень слоя

    private String branchPath0;           //  путь по branch0
    private String branchPath1;           //  путь по branch1

    private String branchValue0;          //  значение на branch0
    private String branchValue1;          //  значение на branch1

    private GridPoint pathFromPoint;      //  из какой точки получилась эта точка

    private GridPoint(GridPointBuilder builder) {
        this.value = builder.value;
        this.errors = builder.errors;
        this.level = builder.level;
        this.branchPath0 = builder.branchPath0;
        this.branchPath1 = builder.branchPath1;
        this.branchValue0 = builder.branchValue0;
        this.branchValue1 = builder.branchValue1;
        this.pathFromPoint = builder.pathFromPoint;
    }

    public static GridPointBuilder builder() {
        return new GridPointBuilder();
    }

    public static class GridPointBuilder {

        private String value;
        private int errors;
        private int level;

        private String branchPath0;
        private String branchPath1;

        private String branchValue0;
        private String branchValue1;

        private GridPoint pathFromPoint;

        public GridPointBuilder value(String value) {
            this.value = value;
            return this;
        }

        public GridPointBuilder errors(int errors) {
            this.errors = errors;
            return this;
        }

        public GridPointBuilder level(int level) {
            this.level = level;
            return this;
        }

        public GridPointBuilder branchPath0(String branchPath0) {
            this.branchPath0 = branchPath0;
            return this;
        }

        public GridPointBuilder branchPath1(String branchPath1) {
            this.branchPath1 = branchPath1;
            return this;
        }

        public GridPointBuilder branchValue0(String branchValue0) {
            this.branchValue0 = branchValue0;
            return this;
        }

        public GridPointBuilder branchValue1(String branchValue1) {
            this.branchValue1 = branchValue1;
            return this;
        }

        public GridPointBuilder pathFromPoint(GridPoint pathFromPoint) {
            this.pathFromPoint = pathFromPoint;
            return this;
        }

        public GridPoint build() {
            return new GridPoint(this);
        }
    }

    public String getValue() {
        return value;
    }

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getBranchValue0() {
        return branchValue0;
    }

    public void setBranchValue0(String branchValue0) {
        this.branchValue0 = branchValue0;
    }

    public String getBranchValue1() {
        return branchValue1;
    }

    public void setBranchValue1(String branchValue1) {
        this.branchValue1 = branchValue1;
    }

    public String getBranchPath0() {
        return branchPath0;
    }

    public void setBranchPath0(String branchPath0) {
        this.branchPath0 = branchPath0;
    }

    public String getBranchPath1() {
        return branchPath1;
    }

    public void setBranchPath1(String branchPath1) {
        this.branchPath1 = branchPath1;
    }

    public GridPoint getPathFromPoint() {
        return pathFromPoint;
    }

    public void setPathFromPoint(GridPoint pathFromPoint) {
        this.pathFromPoint = pathFromPoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GridPoint point = (GridPoint) o;
        return errors == point.errors &&
                level == point.level &&
                value.equals(point.value) &&
                Objects.equals(branchPath0, point.branchPath0) &&
                Objects.equals(branchPath1, point.branchPath1) &&
                Objects.equals(branchValue0, point.branchValue0) &&
                Objects.equals(branchValue1, point.branchValue1) &&
                Objects.equals(pathFromPoint, point.pathFromPoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, errors, level, branchPath0, branchPath1, branchValue0, branchValue1, pathFromPoint);
    }

    @Override
    public String toString() {
        return String.format("%s : %d", value, errors);
    }
}
