/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package matlabinput;

/**
 *
 * @author Justin Lee
 */
public class Spike {

    private int start, first, second, end;
    final static int offset = 500;

    //TODO: check inputs
    //maybe via enum, so steps are only 1-4 and only positive + 0?
    public Spike() {
        this(-1, -1, -1, -1);
    }

    public Spike(int start, int first, int second, int end) {
        this.start = start;
        this.first = first;
        this.second = second;
        this.end = end;
    }

    public int getOffset() {
        return offset;
    }

    public void setStep(int step, int val) {
        switch (step) {
            case 0:
                start = val;
                break;
            case 1:
                first = val;
                break;
            case 2:
                second = val;
                break;
            case 3:
                end = val;
                break;
        }
    }

    public int getStep(int step) {
        switch (step) {
            case 0:
                return start;
            case 1:
                return first;
            case 2:
                return second;
            case 3:
                return end;
        }
        return -1;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getFirst() {
        return first;
    }

    public void setFirst(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

}
