package com.fypembeddingapplication.embeddingapplication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fypembeddingapplication.embeddingapplication.Service.ConfirmationTokenService;
import com.fypembeddingapplication.embeddingapplication.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.fypembeddingapplication.embeddingapplication.responseModel.requestForSignUp;

import java.util.ArrayList;
import java.util.Map;

@RestController
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private ConfirmationTokenService confirmationTokenService;
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/sign_up")
    @ResponseBody
    public Map<String, Object> userSignUp(@RequestBody String allParams) throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        String jsonString=allParams;
        ArrayList<String> errorMessage = new ArrayList<>();
        ArrayList<String> exceptionMessage = new ArrayList<>();
        JsonCustomized<String,Object> jsonBody =new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        try {
            requestForSignUp requestForSignUp =  mapper.readValue(jsonString, requestForSignUp.class);
            String email=requestForSignUp.getEmail();
            String username= requestForSignUp.getUserName();
            String password =requestForSignUp.getPassword();
            int indicator =userService.signUpUser(email,username,password);
            if (indicator==3){
                jsonBody.put("sign_up","success");
            }else if (indicator ==1){
                errorMessage.add("You have registered the account.");
            }else if(indicator ==2){
                errorMessage.add("You have registered the account. Please go to your email to active the account.");
            }

        }catch (Exception e){
            exceptionMessage.add(e.getMessage());
        }
        jsonOutPut.put("statusCode",200);
        jsonBody.put("exception_message",exceptionMessage);
        jsonBody.put("errorMessage",errorMessage);
        jsonOutPut.put("body",jsonBody.returmMap());
        return jsonOutPut.returmMap();
    }
}
