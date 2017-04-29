package com.loganalysis.api;


import com.loganalysis.vo.AuthLog;
import hex.genmodel.GenModel;
import hex.genmodel.easy.exception.PredictException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;

/**
 * Created by pc on 3/27/17.
 */
@Controller
public class MainController {

    @RequestMapping("/filterAuth")
    public ResponseEntity<?> filterAuth(String user, String source, String dest) throws IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, PredictException {

        return new ResponseEntity<Object>(calculateAnomaly(user, source, dest),HttpStatus.valueOf(200));
    }

    @RequestMapping(value = "/filterAuthBatch", consumes = "application/json")
    public ResponseEntity<?> filterAuthBatch(@RequestBody AuthLog[] authLogs) throws IOException, ClassNotFoundException, IllegalAccessException,
            InstantiationException, PredictException {

        double[] results = new double[authLogs.length];

        for(int i = 0; i < authLogs.length; i++){

            results[i] = calculateAnomaly(authLogs[i].user, authLogs[i].source, authLogs[i].destination);

        }

        return new ResponseEntity<Object>(results,HttpStatus.valueOf(200));
    }


    public double calculateAnomaly(String user, String source, String dest){

        //        URL modelURL = (new File(Settings.MODEL_PATH)).toURI().toURL();
//        URLClassLoader modelLoader = new URLClassLoader(new URL[]{modelURL},GenModel.class.getClassLoader());
//        GenModel rawModel = (GenModel) modelLoader.loadClass(Settings.MODEL_CLASS_NAME).newInstance();

        GenModel rawModel = new DeepLearning_model_R_1490600987592_4();

        //        modelLoader.close();

        double data[] = new double[4];

        data[0] = rawModel.mapEnum(rawModel.getColIdx("destination"),dest);
        data[1] = rawModel.mapEnum(rawModel.getColIdx("user"),user);
        data[2] = rawModel.mapEnum(rawModel.getColIdx("source"),source);

        double[] preds = new double [rawModel.nclasses()+1];

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
            if(((int)data[y]) != -1)
                avg += 1 - preds[((int)data[y])];
            y++;

        }

        return avg/ indices.length;
    }

    @RequestMapping("/greeting")
    public ResponseEntity<?> greeting(){
        return new ResponseEntity<Object>("hello",HttpStatus.valueOf(200));
    }

}
