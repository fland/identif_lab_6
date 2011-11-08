package ua.pp.fland.labs.identif.lab6.model;

/**
 * @author Maxim Bondarenko
 * @version 1.0 11/8/11
 */

public class DirectFlowData {

    private double chi;

    private double nu;

    public DirectFlowData(double chi, double nu) {
        this.chi = chi;
        this.nu = nu;
    }

    public double getChi() {
        return chi;
    }

    public void setChi(double chi) {
        this.chi = chi;
    }

    public double getNu() {
        return nu;
    }

    public void setNu(double nu) {
        this.nu = nu;
    }
}
