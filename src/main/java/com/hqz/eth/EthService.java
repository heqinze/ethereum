package com.hqz.eth.service;

import com.alibaba.fastjson.JSONArray;
import com.hqz.eth.bean.Transation;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.ResourceUtils;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.utils.Numeric;

@Service
@Data
public class EthService {

    @Value("${url}")
    private String url;
    @Value("${keystorePath}")
    private String keystorePath;
    @Value("${password}")
    private String password;
    @Value("${fromAccount}")
    private String fromAccount;
    @Value("${toAccount}")
    private String toAccount;
    //public Web3j web3 = Web3j.build(new HttpService(url));
    public Web3j web3;

    File file = new File("D:\\eth\\src\\main\\resources\\keystore\\test.txt");
    @Value("${filePath}")
    private String filePath;

    private Credentials credentials ;

    static int pre=400;


    /**
     * @param keystorePath 用户keystore文件的路径
     * @param password 创建用户时使用的密码
     * @return 用户私钥
     */
    public static String exportPrivateKey(String keystorePath, String password) {
        try {
            Credentials credentials = WalletUtils.loadCredentials(password, keystorePath);
            BigInteger privateKey = credentials.getEcKeyPair().getPrivateKey();
            return privateKey.toString(16);
        } catch (IOException | CipherException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String transfer(Web3j web3j,Credentials credentials,String fromAccount, String toAccount, BigInteger value, String privateKey, String input) throws Exception {

        Web3j web3 = web3j;

        EthGetBalance ethGetBalance = web3j.ethGetBalance(fromAccount, DefaultBlockParameterName.LATEST).send();

        if (ethGetBalance != null && ethGetBalance.getBalance().compareTo(value) == 1) {
            //方法1私钥
            //Credentials credentials = Credentials.create(privateKey);
            //方法2 Credentials credentials = WalletUtils.loadCredentials(passWord, filenName);
            TxPoolStatus txPoolStatus = web3.txPoolStatus().send();
            BigInteger nonce = web3j.ethGetTransactionCount(fromAccount, DefaultBlockParameterName.LATEST).send().getTransactionCount();
            BigInteger GAS_PRICE = BigInteger.valueOf(pre);//4000000000L
            BigInteger GAS_LIMIT = BigInteger.valueOf(4712388L);//4712388

            RawTransaction rawTransaction = RawTransaction.createTransaction(nonce.add(BigInteger.valueOf(txPoolStatus.getPending())), GAS_PRICE, GAS_LIMIT, toAccount, value, input);
            //签名
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            if (ethSendTransaction.getError() != null) {
                System.out.println(ethSendTransaction.getError().getMessage());
            }
            String transactionHash = ethSendTransaction.getTransactionHash();

            System.out.println(transactionHash);
            return transactionHash;
        }
        return "";
    }

    public String set(Web3j web3,String data) throws Exception{
        //Web3j web3 = Web3j.build(new HttpService(url1));
        if(web3==null){
            web3=Web3j.build(new HttpService(url));
        }

//        if(fileContent==null) {
//            File file=ResourceUtils.getFile(keystorePath);
//            fileContent = FileUtils.readFileToString(file, "utf-8");
//        }

        if(credentials==null) {
            File file=ResourceUtils.getFile(keystorePath);
             credentials = Credentials.create(exportPrivateKey(file.getPath(), password));
        }

        String hash = transfer(web3, credentials, fromAccount, toAccount,
                new BigInteger("100000000000"), "",
                Utils.stringToHexString(Utils.strToUnicode(data))
        );
        //Logger logger = LoggerFactory.getLogger(EthService.class);

        FileUtils.writeStringToFile(new File(filePath),hash+":"+data+"\r","utf-8",true);
        return hash;
    }

    public String get(Web3j web3,String hash) throws Exception{
        //Web3j web3 = Web3j.build(new HttpService("http://59.37.64.21:8545"));
        if(web3==null){
            web3=Web3j.build(new HttpService(url));
        }
        EthTransaction transaction = web3.ethGetTransactionByHash(hash).send();
        String input = transaction.getResult().getInput();
        String unicodeString = Utils.hexStringToString(input.substring(2));
        return Utils.unicodeToStr(unicodeString);
    }
    public List<String> getList(Web3j web3,ArrayList<Transation> transationsArray) throws Exception {
        if(web3==null){
            web3=Web3j.build(new HttpService(url));
        }
        List<String> list=new ArrayList<>();
        for (Transation transation : transationsArray) {
            list.add(get(web3,transation.getHash()));
        }
        return list;
    }
}
