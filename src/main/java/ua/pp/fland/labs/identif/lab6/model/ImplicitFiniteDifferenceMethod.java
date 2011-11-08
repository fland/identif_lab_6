package ua.pp.fland.labs.identif.lab6.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Maxim Bondarenko
 * @version 1.0 11/7/11
 */

public class ImplicitFiniteDifferenceMethod {

    private static final Logger log = LoggerFactory.getLogger(ImplicitFiniteDifferenceMethod.class);

    private final static double DOUBLE_PRECISION = 0.0001;

    private final Map<Double, Double> xStartTemp;

    private final double xStep;

    private final long n;

    private final Double timeStep;

    private final double xi;

    public ImplicitFiniteDifferenceMethod(Map<Double, Double> xStartTemp, double xStep, double timeStep, double xi) {
        this.xStartTemp = xStartTemp;
        this.xStep = xStep;
        this.timeStep = timeStep;
        this.xi = xi;
        n = Math.round(Collections.max(xStartTemp.keySet()) / xStep);
    }

    public void calculate() {
        Map<Double, Map<Double, Double>> calculatedTemp;
        calculatedTemp = new HashMap<Double, Map<Double, Double>>();

        Map<Double, Double> timeTemp = new HashMap<Double, Double>(getTempAtTime(xStartTemp, 1.2f, xi));
    }

    private Map<Double, Double> getTempAtTime(Map<Double, Double> prevTimeXValues, double u0j, double xi) {
        Map<Double, Double> res = new HashMap<Double, Double>();

        double startChi = 0;
        double startNu = u0j;

        Double currX = 0.0d;
        Map<Long, DirectFlowData> directFlowData = new HashMap<Long, DirectFlowData>();
        double lambda = timeStep / (xStep * xStep);
        directFlowData.put(1l, new DirectFlowData(startChi, startNu));

        currX = currX + xStep;
        for (long i = 2; i < n; i++) {
            DirectFlowData prevDirectFlowData = directFlowData.get(i - 1);
            double chi = calculateChi(lambda, prevDirectFlowData.getChi());
            double nu = calculateNu(prevTimeXValues.get(currX), prevDirectFlowData.getNu(), lambda,
                            prevDirectFlowData.getChi());
            directFlowData.put(i, new DirectFlowData(chi, nu));
            currX = currX + xStep;
        }

        Double lastX = currX;
        res.put(lastX, xi);
        currX = currX - xStep;
        for(long i = n; i > 1; i--){
            log.debug("Curr i: " + i);
            DirectFlowData prevDirectFlowData = directFlowData.get(i - 1);
            double temp = prevDirectFlowData.getChi() * res.get(currX + xStep) + prevDirectFlowData.getNu();
            res.put(currX, temp);
            currX = currX - xStep;
        }

        return res;
    }

    private double calculateChi(double lambda, double chiPrev) {
        return lambda / (1 + 2 * lambda - (lambda * chiPrev));
    }

    private double calculateNu(double uPrevTime, double nuPrev, double lambda, double chiPrev) {
        return (uPrevTime + lambda * nuPrev) / (1 + 2 * lambda - (lambda * chiPrev));
    }
}
