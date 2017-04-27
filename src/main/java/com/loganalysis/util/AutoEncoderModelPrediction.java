package com.loganalysis.util;

import hex.genmodel.easy.prediction.AbstractPrediction;

/**
 * Created by pc on 3/28/17.
 */
public class AutoEncoderModelPrediction extends AbstractPrediction {
    public double[] predictions;
    public double[] feature;
    public double[] reconstrunctionError;
    public double averageReconstructionError;
}
