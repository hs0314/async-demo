package me.heesu.asyncwebapp;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class AsyncService {

    //@Async("myThreadPoolTaskExecutor")
    public List<String> asyncMethodFromAsyncService(ReqDto reqDto){

        //reqDto를 인자로 넘겨서 api(쿼리) 호출

        List<String> offers = Arrays.asList("offer1","offer2","offer3");
        try {
            Thread.sleep(1000L);
        }catch(Exception e){

        }

        return offers;//CompletableFuture.completedFuture(offers);
    }
}
