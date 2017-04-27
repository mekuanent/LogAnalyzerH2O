package com.loganalysis;

import com.loganalysis.api.MainController;
import com.loganalysis.util.CustomEasyPredictModelWrapper;
import hex.genmodel.easy.exception.PredictException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

/**
 * Created by pc on 3/27/17.
 */
@SpringBootApplication
public class Application {


    public static void main(String... args) throws ClassNotFoundException, PredictException, InstantiationException, IllegalAccessException, IOException {

        SpringApplication.run(Application.class, args);

    }

}
