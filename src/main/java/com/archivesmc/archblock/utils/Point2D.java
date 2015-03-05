package com.archivesmc.archblock.utils;

public class Point2D {
    private Integer x;
    private Integer y;

    public Point2D(Integer x, Integer y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point2D point2D = (Point2D) o;

        return x.equals(point2D.x) && y.equals(point2D.y);

    }

    @Override
    public int hashCode() {
        int result = x.hashCode();
        result = 31 * result + y.hashCode();
        return result;
    }
}
