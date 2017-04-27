package com.loganalysis.api;


import com.loganalysis.config.Settings;
import hex.genmodel.GenModel;
import hex.genmodel.easy.EasyPredictModelWrapper;
import hex.genmodel.easy.RowData;
import hex.genmodel.easy.exception.PredictException;
import hex.genmodel.easy.prediction.AbstractPrediction;
import hex.genmodel.easy.prediction.AutoEncoderModelPrediction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by pc on 3/27/17.
 */
@Controller
public class MainController {

    @RequestMapping("/filterAuth")
    public ResponseEntity<?> filterAuth(String user, String source, String dest) throws IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, PredictException {

//        URL modelURL = (new File(Settings.MODEL_PATH)).toURI().toURL();
//        URLClassLoader modelLoader = new URLClassLoader(new URL[]{modelURL},GenModel.class.getClassLoader());
//        GenModel rawModel = (GenModel) modelLoader.loadClass(Settings.MODEL_CLASS_NAME).newInstance();

        GenModel rawModel = new DeepLearning_model_R_1490600987592_4();

//        modelLoader.close();

        EasyPredictModelWrapper model = new EasyPredictModelWrapper(rawModel);


        RowData row = new RowData();
        row.put("destination", "C1003");
        row.put("user", "U620@DOM1");
        row.put("source", "C17693");

        double data[] = new double[4];

//        data[0] = rawModel.mapEnum(rawModel.getColIdx("destination"),"C368");
//        data[1] = rawModel.mapEnum(rawModel.getColIdx("user"),"U12@DOM1");
//        data[2] = rawModel.mapEnum(rawModel.getColIdx("source"),"C22409");

        data[0] = rawModel.mapEnum(rawModel.getColIdx("destination"),dest);
        data[1] = rawModel.mapEnum(rawModel.getColIdx("user"),user);
        data[2] = rawModel.mapEnum(rawModel.getColIdx("source"),source);

        double[] preds = new double [rawModel.nclasses()+1];

        //AbstractPrediction aemPred = model.predict(row);

        rawModel.score0(data, preds);

        int[] indices = new int[3];
        long[] cutpoints = {301, 406, 412};

        int c = 0;

        for(int i = 0; i < preds.length; i++){

            if(preds[i] > preds[indices[c]]){
                indices[c] = i;
            }

            if(i == cutpoints[c]){
                c++;
            }

        }

        int x =0;
        for (int i :
                indices) {
            if(x!=0)
                System.out.println(((int)data[x]) +" " + (i - cutpoints[x-1] - 1) +" "+ preds[((int)(data[x] + cutpoints[x-1] + 1))] +" "+ preds[i]);
            else
                System.out.println(((int)data[x]) +" " + i +" "+ preds[((int)data[x])] +" "+ preds[i]);
            x++;
        }


        int y = 0;
        double avg = 0;

        for (int i :
                indices) {
//            if(x != 0) {
//                if (((int) data[x]) == (i - cutpoints[x - 1] - 1)) {
//
//                } else {
//
//                }
                if(((int)data[y]) != -1)
                    avg += 1 - preds[((int)data[y])];
                y++;
//            }else{
//                if (((int) data[x]) == i) {
//
//                } else {
//
//                }
//            }

        }

        return new ResponseEntity<Object>(avg/ indices.length,HttpStatus.valueOf(200));
    }

    @RequestMapping("/greeting")
    public ResponseEntity<?> greeting(){
        return new ResponseEntity<Object>("hello",HttpStatus.valueOf(200));
    }

}
