package com.fypembeddingapplication.embeddingapplication.controller;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.fypembeddingapplication.embeddingapplication.database.EmbeddedDetailsRepository;
import com.fypembeddingapplication.embeddingapplication.database.EmbeddedImageRepository;
import com.fypembeddingapplication.embeddingapplication.database.OriginalImageRepository;
import com.fypembeddingapplication.embeddingapplication.database.UserRepository;
import com.fypembeddingapplication.embeddingapplication.database.EncryptionDetailsRepository;
import com.fypembeddingapplication.embeddingapplication.database.TempRepository;
import com.fypembeddingapplication.embeddingapplication.model.embeddedDetails;
import com.fypembeddingapplication.embeddingapplication.model.embeddedImage;
import com.fypembeddingapplication.embeddingapplication.model.User;
import com.fypembeddingapplication.embeddingapplication.model.originalImage;
import com.fypembeddingapplication.embeddingapplication.model.encryptionDetail;
import com.fypembeddingapplication.embeddingapplication.model.tempTable;
import com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.MosaicFilter.Embedding;
import com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.MosaicFilter.ImageCompress;
import com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.MosaicFilter.Extraction;
import com.fypembeddingapplication.embeddingapplication.Encryption.ASEEncryption;
import com.fypembeddingapplication.embeddingapplication.controller.JsonCustomized;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fypembeddingapplication.embeddingapplication.responseModel.requestForEmbeddedImageID;
import com.fypembeddingapplication.embeddingapplication.responseModel.requestForExtraction;

@RestController
public class uploadImageForEmbedded {
    @Autowired
    EmbeddedDetailsRepository embeddedDetailsRepository;
    @Autowired
    EmbeddedImageRepository embeddedImageRepository;
    @Autowired
    OriginalImageRepository originalImageRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EncryptionDetailsRepository encryptionDetailsRepository;
    @Autowired
    TempRepository tempRepository;
    @PostMapping("/getEmbeddedImage")
    @ResponseBody
    public Map<String, Object> getEmbeddedImage(@RequestBody String allParams) {
        ObjectMapper mapper = new ObjectMapper();
//        System.out.println(allParams.get("userId"));
        ArrayList<String> errorMessage = new ArrayList<>();
        String jsonString=allParams;
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        try{
            requestForEmbeddedImageID request = mapper.readValue(jsonString, requestForEmbeddedImageID.class);
            Long userId = request.getUserId();
            String imageBase64 =request.getImageBase64();
            String filter = request.getFilter();
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            String imageName= request.getName() +"_" +timestamp.toString();
            ImageCompress compress = new ImageCompress();
            String imageCompressBase64 = compress.compress(imageBase64);
            originalImage originalImage =new originalImage(userId,imageName,imageBase64,imageCompressBase64);
            originalImageRepository.save(originalImage);
            final Optional<User> retrievedUserByUserID = userRepository.findById(userId);
            String embeddedInformation= retrievedUserByUserID.get().getDefault_digital_watermark();
            ASEEncryption encryption = new ASEEncryption();
            String encryptKey = encryption.getRandomEncryptKey();
            String encryptedInformation = encryption.encrypt(embeddedInformation,encryptKey);
            encryptionDetail encryptionDetail =new encryptionDetail(userId,encryptKey,encryptedInformation);
            encryptionDetailsRepository.save(encryptionDetail);
            Embedding embedding =new Embedding(encryptedInformation,imageBase64);
            String imageOutPut = embedding.getEmbededImage();
            String imageCompressedOut =compress.compress(imageOutPut);
            String embeddedKey = embedding.getEmbeddedKey();
            if(embedding.getErrorMessage()!=null){
                errorMessage.add(embedding.getErrorMessage());
            }
            String embeddedImageName = request.getName() + "_" + filter+ "_embedded" + timestamp.toString() ;
            embeddedImage embeddedImage =new embeddedImage(userId,embeddedImageName,filter,imageOutPut,imageCompressedOut);
            embeddedImageRepository.save(embeddedImage);
            final Optional<embeddedImage> retrievedEmbeddedImageByName = embeddedImageRepository.findByName(embeddedImageName);
            Long embeddedImageId = retrievedEmbeddedImageByName.get().getEmbeddedImageId();
            final Optional<originalImage> retrievedOriginalImageByName = originalImageRepository.findByName(imageName);
            Long originalImageId = retrievedOriginalImageByName.get().getOriginalImageId();
            embeddedDetails embeddedDetails = new embeddedDetails(userId,embeddedKey,originalImageId,embeddedImageId);
            embeddedDetailsRepository.save(embeddedDetails);
            jsonPayload.put("embeddedImageId",embeddedImageId.toString());
        }
        catch (JsonParseException e) { e.printStackTrace();errorMessage.add(e.getMessage());}
        catch (JsonMappingException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
        catch (IOException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        return jsonOutPut.returmMap();

    }
    @PostMapping("/getTempEmbeddedImage")
    @ResponseBody
    public Map<String, Object> getTempEmbeddedImage(@RequestBody String allParams){
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<String> errorMessage = new ArrayList<>();
        String jsonString=allParams;
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        try{
            requestForEmbeddedImageID request = mapper.readValue(jsonString, requestForEmbeddedImageID.class);
            Long userId = request.getUserId();
            String imageBase64 =request.getImageBase64();
            String filter = request.getFilter();
            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
            String imageName= request.getName() +"_" +timestamp.toString();
            ImageCompress compress = new ImageCompress();
            String imageCompressBase64 = compress.compress(imageBase64);
            final Optional<User> retrievedUserByUserID = userRepository.findById(userId);
            String embeddedInformation= retrievedUserByUserID.get().getDefault_digital_watermark();
            ASEEncryption encryption = new ASEEncryption();
            String encryptKey = encryption.getRandomEncryptKey();
            String encryptedInformation = encryption.encrypt(embeddedInformation,encryptKey);
            Embedding embedding =new Embedding(encryptedInformation,imageBase64);
            String imageOutPut = embedding.getEmbededImage();
            String imageCompressedOut =compress.compress(imageOutPut);
            String embeddedKey = embedding.getEmbeddedKey();
            if(embedding.getErrorMessage()!=null){
                errorMessage.add(embedding.getErrorMessage());
            }
            String embeddedImageName = imageName + "_" + filter+ "_embedded";
            tempTable tempTable = new tempTable(userId,imageName,imageBase64,imageCompressBase64,embeddedImageName,filter,imageOutPut,imageCompressedOut,embeddedKey,encryptKey,encryptedInformation);
            tempRepository.save(tempTable);
            jsonPayload.put("embeddedImage",imageCompressedOut);
            jsonPayload.put("status","s");
        }
        catch (JsonParseException e) { e.printStackTrace();errorMessage.add(e.getMessage());}
        catch (JsonMappingException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
        catch (IOException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        return jsonOutPut.returmMap();
    }
    @Transactional(rollbackFor = Exception.class)
    @GetMapping ("/confirmEmbeddedImage/{userId}")
    public Map<String, String> confirmImageEmbedded(@PathVariable("userId") Long id)throws Exception{
        JsonCustomized<String,String> jsonOutPut= new JsonCustomized<>();
        final Optional<tempTable> retrieveTempData = tempRepository.findByUserId(id);
        tempTable tempTable =retrieveTempData.get();
        if(tempTable==null){
            jsonOutPut.put("status","f");
            jsonOutPut.put("error","Fail to access database");
        }
        else {
            originalImage originalImage =new originalImage(id,tempTable.getOriginalImageName(),tempTable.getOriginalImageBase64(),tempTable.getOriginalImageCompressedBase64());
            originalImageRepository.save(originalImage);
            encryptionDetail encryptionDetail =new encryptionDetail(id,tempTable.getEncryptionKey(),tempTable.getEncryptedString());
            encryptionDetailsRepository.save(encryptionDetail);
            embeddedImage embeddedImage =new embeddedImage(id,tempTable.getEmbeddedImageName(),tempTable.getFilter(),tempTable.getEmbeddedImage1Base64(),tempTable.getEmbeddedImageCompressedBase64());
            embeddedImageRepository.save(embeddedImage);
            final Optional<embeddedImage> retrievedEmbeddedImageByName = embeddedImageRepository.findByName(tempTable.getEmbeddedImageName());
            Long embeddedImageId = retrievedEmbeddedImageByName.get().getEmbeddedImageId();
            final Optional<originalImage> retrievedOriginalImageByName = originalImageRepository.findByName(tempTable.getOriginalImageName());
            Long originalImageId = retrievedOriginalImageByName.get().getOriginalImageId();
            embeddedDetails embeddedDetails = new embeddedDetails(id,tempTable.getEmbeddedKey(),originalImageId,embeddedImageId);
            embeddedDetailsRepository.save(embeddedDetails);
            jsonOutPut.put("status","s");
        }
        tempRepository.deleteAllByUserId(id);
        return jsonOutPut.returmMap();
    }
    @Transactional(rollbackFor = Exception.class)
    @GetMapping("/cancelTempEmbedding/{userId}")
    public Map<String, String> cancelEmbedding (@PathVariable("userId") Long id)throws Exception  {
        JsonCustomized<String,String> jsonOutPut= new JsonCustomized<>();
        final Optional<tempTable> retrieveTempData = tempRepository.findByUserId(id);
        tempTable tempTable =retrieveTempData.get();
        if(tempTable==null){
            jsonOutPut.put("status","f");
            jsonOutPut.put("error","fail to access the database");
        }
        else {
            tempRepository.deleteAllByUserId(id);
            jsonOutPut.put("status","s");
        }
        return jsonOutPut.returmMap();
    }
    @GetMapping(path = { "/get/{userId}" })
    public Map<String, String> getUserForTest (@PathVariable("userId") Long id) throws IOException {
        final Optional<User> retrievedUserID = userRepository.findById(id);
        JsonCustomized<String,String> map =new JsonCustomized<String, String>();
        map.put("default_digital_watermark", retrievedUserID.get().getDefault_digital_watermark() );
        return map.returmMap();
    }
    @GetMapping(path = { "/getEmbeddedImageFromDatabase/{embeddedImageId}" })
    public Map<String, Object> getEmbeddedImageFromDatabase (@PathVariable("embeddedImageId") String id) throws IOException{
        Long embeddedImageId = Long.parseLong(id);
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        final Optional<embeddedImage> retrievedEmbeddedImage = embeddedImageRepository.findByEmbeddedImageId(embeddedImageId);
        String embeddedImageCompressedBase64 = retrievedEmbeddedImage.get().imageCompressedBase64();
        if(embeddedImageCompressedBase64==null){
            String errorMessage = "Cannot find the embeddedImage";
            jsonOutPut.put("error",errorMessage);
        }
        jsonPayload.put("embeddedImageCompressedBase64",embeddedImageCompressedBase64);
        jsonOutPut.put("payload",jsonPayload.returmMap());
        return jsonOutPut.returmMap();
    }
    @PostMapping (path = {"/extractFromImage"})
    @ResponseBody
    public Map<String,Object> getHiddenInformation (@RequestBody String allParams){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString = allParams;
        ArrayList<String> errorMessage = new ArrayList<>();
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        try{
            requestForExtraction request = mapper.readValue(jsonString, requestForExtraction.class);
            String embeddedImage = request.getEmbeddedImage();
            Long originalImageId = request.getOriginalImageId();
            Long userId = request.getUserId();
            List<embeddedDetails> embeddedKeyList =null;
            if(originalImageId==null || originalImageId ==0){
                final Optional<List<embeddedDetails>>  retrieveEmbeddedDetailsWithoutOriginalID =  embeddedDetailsRepository.getAllByUserId(userId);
                embeddedKeyList = retrieveEmbeddedDetailsWithoutOriginalID.get();
            }
            else {
                final Optional<List<embeddedDetails>> retrieveEmbeddedDetails = embeddedDetailsRepository.getAllByUserIdAndOriginalImagesID(userId,originalImageId);

                embeddedKeyList = retrieveEmbeddedDetails.get();

            }
            if (embeddedKeyList!=null){
                Extraction extraction = new Extraction(getEmbeddKeyList(embeddedKeyList),embeddedImage);
                ArrayList<String> extractList =extraction.extract();
                    for (int i =0 ; i<extractList.size();i++){
                        final Optional<List<encryptionDetail>> retrieveEmbeddedDetails =encryptionDetailsRepository.findByUserIdAndEncryptedString(userId,extractList.get(i));
                        if(retrieveEmbeddedDetails.get().size()==1){
                            String encryptionKey =retrieveEmbeddedDetails.get().get(0).getEncryptionKey();
                            ASEEncryption aseEncryption = new ASEEncryption();
                            String hiddenInformation = aseEncryption.decrypt(extractList.get(i),encryptionKey);
                            System.out.println(hiddenInformation);
                            jsonPayload.put("hiddenInformation",hiddenInformation);
                            break;
                        }

                    }
            }
            else{
                errorMessage.add("Fail to extract information");
            }
            if(jsonPayload.returmMap().size()!=1){
                errorMessage.add("Error Occur. Fail to extract information");
            }
        }

        catch (IOException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        return jsonOutPut.returmMap();
    }
    @GetMapping(path = { "/getOriginalImageList/{userId}" })
    public Map<String, Object> getOriginalImageList (@PathVariable("userId") String id) throws IOException{
        Long userId = Long .parseLong(id);
        List <String> list =new ArrayList<>();
        JsonCustomized<String,String> jsonStatusAndError =new JsonCustomized<>();
        JsonCustomized<String,Object> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        final Optional <List<originalImage>> retrieveOriginalImageList = originalImageRepository.findByUserId(userId);
        if(retrieveOriginalImageList.get().size()!=0){
            for (int i =0 ;i<retrieveOriginalImageList.get().size();i++){
                list.add(retrieveOriginalImageList.get().get(i).getImageCompressedBase64());
            }
            jsonPayload.put("originalImageList" , list);
            jsonStatusAndError.put("status","s");
        }
        else {
            jsonStatusAndError.put("status","f");
            jsonStatusAndError.put("error","Cannot retrieve data from database");
        }
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("statusAndError",jsonStatusAndError.returmMap());

        return jsonOutPut.returmMap();
    }
    public List<List<Integer>> getEmbeddKeyList (List<embeddedDetails> embeddedKeyListInput){
        List <String> embeddedKey = new ArrayList<>();
        List <List <Integer>> embeddedKeyList = new ArrayList<>();
        for(int i =0 ; i <embeddedKeyListInput.size(); i++){
           embeddedKey.add(embeddedKeyListInput.get(i).getEmbededKey()) ;
        }
        for (int j =0 ; j<embeddedKey.size();j++){
            List<Integer> embeddedKeyInt = new ArrayList<>();
            String [] keys= embeddedKey.get(j).split(",");
            for(int k = 0; k<keys.length;k++){
                embeddedKeyInt.add(Integer.parseInt(keys[k]));
            }
            embeddedKeyList.add(embeddedKeyInt);
        }
        return embeddedKeyList;
    }
}
