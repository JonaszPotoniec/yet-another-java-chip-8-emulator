package emulator;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Timers {
    private int delayTimer;


    Timers(){
        this.delayTimer = 0;

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {if(delayTimer>0) delayTimer--;}, 16, 16, TimeUnit.MILLISECONDS);
    }

    public int getDelayTimer(){return delayTimer;}
    public void setDelayTimer(int tmr){delayTimer = tmr;}
}
