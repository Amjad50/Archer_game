package utils;

public class Measurement {

    // Factor of measurement PIXEL / METER
    private static double factor = 1;

    /**
     * setter for factor
     *
     * @param factor PIXEL / METER
     */
    public static void setFactor(double factor) {
        Measurement.factor = factor;
    }

    /**
     * getter for factor
     *
     * @return factor PIXEL / METER
     */
    public static double getFactor() {
        return factor;
    }

    /**
     * convert PIXELS to METERS
     *
     * @param a number of pixels
     * @return number of meters
     */
    public static double pixel_to_meter(double a) {
        return a / factor;
    }

    /**
     * convert METERS to PIXELS
     *
     * @param a number of meters
     * @return number of pixels
     */
    public static double meter_to_pixel(double a) {
        return a * factor;
    }

    /**
     * convert a vector from meters to pixels
     *
     * @param v meter measured vector
     * @return pixel measured vector
     */
    public static Vector meter_to_pixel(Vector v) {
        return v.scale(factor);
    }

    /**
     * convert a vector from pixels to meters
     *
     * @param v pixel measured vector
     * @return meter measured vector
     */
    public static Vector pixel_to_meter(Vector v) {
        return v.scale(1 / factor);
    }
}
