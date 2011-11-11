package ua.pp.fland.labs.identif.lab6.model.storage;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * @author Maxim Bondarenko
 * @version 1.0 10/22/11
 */

public interface TimeTemperatureStorer {

    void store(Map<Double, Map<BigDecimal, Double>> calculatedTemp) throws IOException;
}
