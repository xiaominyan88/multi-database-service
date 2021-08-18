package data.tree.service.plat;


import data.tree.bean.ParameterBean;
import data.tree.threadlocal.ParameterThreadLocal;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Component
public class StationInfoService extends AbstractPlatService {

    private static Map<String,String> map = new HashMap<>();

    private static Map<String,String> resMap = new HashMap<>();

    private static List<String> list = new ArrayList<>();

    @Override
    public Map<String,String> platService(){
        ParameterBean parameterBean = ParameterThreadLocal.get();
        String stationId = parameterBean.getStationId();
        String type = parameterBean.getType();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        EnergyThread energyThread = new EnergyThread(stationId,type,countDownLatch);
        MapThread mapThread = new MapThread("JZLY_NYLX",countDownLatch);
        FutureTask energyTask = new FutureTask(energyThread);
        FutureTask mapTask = new FutureTask(mapThread);
        new Thread(energyTask).start();
        new Thread(mapTask).start();

        try {
            list = (List<String>) energyTask.get();
            map = (Map<String, String>) mapTask.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < list.size(); i++){
            if(map.get(list.get(i))!= null){
                resMap.put(map.get(list.get(i)),list.get(i));
            }
        }
        return resMap;
    }

    class EnergyThread implements Callable<List<String>>{

        private String stationId;

        private String type;

        private CountDownLatch countDownLatch;

        public EnergyThread(String stationId,String type,CountDownLatch countDownLatch){
            this.stationId = stationId;
            this.type = type;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public List<String> call() throws Exception {
            countDownLatch.await();
            String[] energyTypeArray = findEnergyType(stationId,type).split(",");
            return Arrays.asList(energyTypeArray);
        }
    }

    class MapThread implements Callable<Map<String,String>>{

        private String codeType;

        private CountDownLatch countDownLatch;

        public MapThread(String codeType,CountDownLatch countDownLatch){
            this.codeType = codeType;
            this.countDownLatch = countDownLatch;
        }

        @Override
        public Map<String, String> call() throws Exception {
            countDownLatch.countDown();
            return findCodeMap(codeType);
        }
    }
}
