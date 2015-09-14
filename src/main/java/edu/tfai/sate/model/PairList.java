package edu.tfai.sate.model;

import lombok.Getter;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@Getter
public class PairList {

    public List<Double> x = newArrayList();
    public List<Double> y = newArrayList();

    public PairList() {

    }

    public void add(double x, double y) {
        this.x.add(x);
        this.y.add(y);
    }

    public void addAll(PairList list) {
        this.x.addAll(list.getX());
        this.y.addAll(list.getY());
    }

    public int size() {
        return x.size();
    }

    public void clear() {
        x.clear();
        y.clear();
    }
}
