package com.hqz.eth.controller;

import com.hqz.eth.bean.Transation;
import com.hqz.eth.service.EthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class EthController {
    @Autowired
    EthService ethService;
    @PostMapping("/set")
    public String set(@RequestBody Transation transation) throws Exception {
        String hash=ethService.set(ethService.web3,transation.getData());
        return hash;
    }
    @GetMapping("/get")
    public String get(String hash) throws Exception {
        String data=ethService.get(ethService.web3,hash);
        return data;
    }
    @PostMapping("/getList")
    public List<String> getList(@RequestBody ArrayList<Transation> transationsArray) throws Exception {
        List<String> list = ethService.getList(ethService.web3,transationsArray);
        return list;
    }
}
