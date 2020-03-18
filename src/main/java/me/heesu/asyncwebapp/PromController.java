package me.heesu.asyncwebapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class PromController {

    @Autowired
    AsyncService asyncService;

    @Autowired
    PromService promService;

    @GetMapping(value="/test/async/{param}")
    public ResDto getAsyncTask(@PathVariable String param){
        ResDto resDto = new ResDto();
        List<List<String>> resList = promService.processAsync(param);

        List<String> offers = new ArrayList<String>();

        //CompletableFuture.allOf(completedFutures.toArray(new CompletableFuture[completedFutures.size()])).join(); // 블로킹

        for(List<String> res : resList) {
            for(String offer : res){
                offers.add(offer);
            }
        }

        resDto.setStrList(offers);

        return resDto;
    }
}
