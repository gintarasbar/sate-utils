package edu.tfai.sate2.synthetic.batch;

public class BatchParameters {

    //FIXME should be moved to something like BATCH_CONSTANTS
    public static final int HI_RES_LIMIT = 130000;

    public static boolean CONSOLE_MODE = true;

    /**
     * General delta for calculations, shift and continuum
     */
    public static int DELTA = 10;

    public static double SHIFT_STEP = 0.01d;

    /**
     * Image delta for wavelength range
     */
    public static int IMAGE_DELTA = 2;

    public static boolean SHOW_CHARTS = false;

    public static boolean SHOW_CHARTS_FOR_PARAMS = false;

    public static boolean RADIAL_CHECK_ENABLED = true;

    public static boolean SMOOTH_ENABLED = false;

    public static boolean CONTINUUM_CORRECTION_ENABLED = true;

    public static boolean MODEL_CHECK_ENABLED = false;

    public static int RATIO_START = 6;

    public static int RATIO_END = 40;

    public static int RATIO_STEP = 2;

    public static String STAR_NAME = "current";

    public static double CONTINUUM_MULTIPLIER_FORCE = 1.0;

    public static double RANGE_START = 0;

    public static double RANGE_END = 0;

    public static boolean TELLURIC_CHECK_ENABLED = true;

    public static float RANDOM = 0;

    public static boolean PREDICT = false;

    public static final String SUN_INI_FILE = "suncfg1_std.ini";
}
