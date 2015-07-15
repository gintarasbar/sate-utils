package edu.tfai.sate.objects;

import edu.tfai.sate.swing.MyTableModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;

@Data
@NoArgsConstructor
@Slf4j
public class InputData implements Serializable, Cloneable {

    private static final long serialVersionUID = 6008222985825643958L;

    private float microTurbulence;

    private float spectrumShift;

    /**
     * eqwidth parameter. For sun = 1
     */
    private int iint = 0;

    /**
     * Object name
     */
    private String object;

    /**
     * Element list
     */
    private ArrayList<Element> elements = new ArrayList<Element>();

    /**
     * Element list with repeated elements for automated calculation of the
     * element abundances
     */
    private ArrayList<Element> elementList = new ArrayList<Element>();

    /**
     * Element hash
     */
    private Hashtable<String, Element> elementHash = new Hashtable<String, Element>();

    @Deprecated
    private transient Hashtable<String, MyTableModel> models = new Hashtable<String, MyTableModel>();

    private boolean hiResolutionSpectra = true;


    /**
     * Gets object value
     *
     * @return object value
     */
    public String getObjectName() {
        return object;
    }


    public void clear() {
        elements.clear();
        elementHash.clear();
        if (elementList != null)
            elementList.clear();
    }


    public void setObject(String object) {
        this.object = object;
        if (object != null && object.equalsIgnoreCase("sun"))
            iint = 1;
        else iint = 0;
    }

    public void addElement(Element element) {
        String el = element.getIdentification();
        if (elementHash.get(el.toUpperCase()) == null) {
            elements.add(element);
            rehash();
        } else
            log.warn("Element skipped:" + el);
    }

    public void replaceElement(Element element) {
        String el = element.getIdentification();
        Element elem = elementHash.get(el.toUpperCase());
        if (elem == null || elem.hashCode() != element.hashCode()) {
            if (elem != null)
                elements.remove(elem);
            elements.add(element);
            rehash();
        }
    }

    /**
     * Rehashes the lookup table
     */
    private void rehash() {
        elementHash.clear();
        for (Element el : elements) {
            elementHash.put(el.getIdentification().toUpperCase(), el);
        }

    }

    /**
     * Get selected element. Element symbol + element ionization in Romain
     * number
     *
     * @param identificator element identificator
     * @return element object
     */
    public Element getElement(String identificator) {
        if (identificator != null)
            return elementHash.get(identificator.toUpperCase());
        return null;
    }

    /**
     * Sort list
     *
     * @return sort
     */
    public void sort() {
        Collections.sort(elements, new Comparator<Element>() {
            public int compare(Element a1, Element a2) {
                if (a1.getElementNumber() < a2.getElementNumber())
                    return -1;
                else if (a1.getElementNumber() > a2.getElementNumber())
                    return 1;
                else {
                    if (a1.getIon() < a2.getIon())
                        return -1;
                    else if (a1.getIon() > a2.getIon())
                        return 1;
                    return 0;
                }
            }
        });

        for (Element element : elements) {
            element.sort();
        }
    }

    public void removeElement(Element element) {
        if (element != null) {
            elements.remove(element);
            elementHash.remove(element.getIdentification().toUpperCase());
        }
    }

    public void dispose() {
        for (Element element : elements) {
            element.dispose();
        }
        elements.clear();
    }

    public int getIncludedLineCount() {
        int count = 0;
        for (Element el : getElements()) {
            count += el.getIncludedLineCount();
        }
        return count;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        InputData data = new InputData();
        data.setIint(getIint());
        data.setMicroTurbulence(getMicroTurbulence());
        data.setObject(getObjectName());
        data.setSpectrumShift(getSpectrumShift());
        data.setHiResolutionSpectra(isHiResolutionSpectra());
        for (Element el : getElements()) {
            data.addElement((Element) el.clone());
        }
        ArrayList<Element> list = new ArrayList<Element>();
        if (getElementList() != null)
            for (Element el : getElementList()) {
                list.add((Element) el.clone());
            }
        data.setElementList(list);
        return data;
    }
}
