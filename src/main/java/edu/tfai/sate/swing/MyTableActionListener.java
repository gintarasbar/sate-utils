package edu.tfai.sate.swing;

/**
 * Lentels veiksm stebjimo interfeisas.
 */
public interface MyTableActionListener {
    /**
     * Metodas, kuris kvieiamas, kai atnaujinta lentels lstel.
     *
     * @param col Stulpelis, kur vyko atnaujinimas
     * @param row Eilut, kur vyko atnaujinimas
     */
    public void cellUpdated(int col, int row);

    /**
     * Row deleted
     *
     * @param row row deleted
     */
    public void rowDeleted(int row);
}