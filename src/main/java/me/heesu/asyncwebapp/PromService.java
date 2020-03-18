package me.heesu.asyncwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

@Service
public class PromService {

    @Qualifier("myThreadPoolTaskExecutor")
    @Autowired
    Executor executor;

    @Autowired
    AsyncService asyncService;

    static private String comm1 = "common1";

    /* str to dto */
    private ReqDto stringToReqDto(String str) {
        ReqDto reqDto = new ReqDto();
        reqDto.setInputStr(str);

        reqDto.setCommField1(comm1);

        return reqDto;
    }

    /* CompletableFuture 객체를 모아서 allOf + join 으로 모든 결과값 합친 것 return (BLOCKING******) */
    private List<List<String>> processFutureCombine(List<CompletableFuture<List<String>>> futures){
        // 리턴타입은 api응답 반환data
        List<String> resList = new ArrayList<String>();

        Map<String, List<String>> combinedResult = new ConcurrentHashMap<>();

        return (List<List<String>>) futures.stream()
                .map(CompletableFuture::join)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

                /*
                .forEach(res -> {
                    res.stream().forEach(r -> {
                        System.out.println(r);
                    });
                });*/
    }

    public List<List<String>> processAsync(String param) {
        ResDto resDto = new ResDto();

        // input 가공
        List<String> inputList = Arrays.asList(param.split("::"));

        // completableFuture. async + executor 설정
        List<CompletableFuture<List<String>>> futures = new ArrayList<>();
        for (String sglStr : inputList) {
            ReqDto sglReq = this.stringToReqDto(sglStr);
            CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(
                    () -> asyncService.asyncMethodFromAsyncService(sglReq), executor);
            futures.add(future);
        }

        return processFutureCombine(futures);

        /*
        // allof을 통한 결과 aggregation
        CompletableFuture<Void> allFutures = CompletableFuture
                .allOf(futures.toArray(new CompletableFuture[futures.size()]));
eam()

        //.join()을 통한 결과값을 가지고 있는 CompletableFuture 리턴
        CompletableFuture<List<String>> allCompletableFuture =
                allFutures.thenApply(future -> {
                    return futures.stream()
                            .map(completableFuture -> completableFuture.join())
                            .collect(Collectors.toList());
                });

        return new ResDto();
         */
    }
}
