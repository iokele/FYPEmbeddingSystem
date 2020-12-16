package com.fypembeddingapplication.embeddingapplication.controller;
import com.fasterxml.jackson.databind.JsonNode;
import com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.MosaicFilter.MosicEmbed;
import com.fypembeddingapplication.embeddingapplication.EmbeddingAlgorithm.PencilPaintFilter.PencilPaintEmbed;
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
//    @PostMapping("/getEmbeddedImage")
//    @ResponseBody
//    public Map<String, Object> getEmbeddedImage(@RequestBody String allParams) {
//        ObjectMapper mapper = new ObjectMapper();
////        System.out.println(allParams.get("userId"));
//        ArrayList<String> errorMessage = new ArrayList<>();
//        String jsonString=allParams;
//        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
//        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
//        try{
//            requestForEmbeddedImageID request = mapper.readValue(jsonString, requestForEmbeddedImageID.class);
//            Long userId = request.getUserId();
//            String imageBase64 =request.getImageBase64();
//            String filter = request.getFilter();
//            Timestamp timestamp=new Timestamp(System.currentTimeMillis());
//            String imageName= request.getName() +"_" +timestamp.toString();
//            ImageCompress compress = new ImageCompress();
//            String imageCompressBase64 = compress.compress(imageBase64);
//            originalImage originalImage =new originalImage(userId,imageName,imageBase64,imageCompressBase64);
//            originalImageRepository.save(originalImage);
//            final Optional<User> retrievedUserByUserID = userRepository.findById(userId);
//            String embeddedInformation= retrievedUserByUserID.get().getDefault_digital_watermark();
//            ASEEncryption encryption = new ASEEncryption();
//            String encryptKey = encryption.getRandomEncryptKey();
//            String encryptedInformation = encryption.encrypt(embeddedInformation,encryptKey);
//            encryptionDetail encryptionDetail =new encryptionDetail(userId,encryptKey,encryptedInformation);
//            encryptionDetailsRepository.save(encryptionDetail);
//            Embedding embedding =new Embedding(encryptedInformation,imageBase64);
//            String imageOutPut = embedding.getEmbededImage();
//            String imageCompressedOut =compress.compress(imageOutPut);
//            String embeddedKey = embedding.getEmbeddedKey();
//            if(embedding.getErrorMessage()!=null){
//                errorMessage.add(embedding.getErrorMessage());
//            }
//            String embeddedImageName = request.getName() + "_" + filter+ "_embedded" + timestamp.toString() ;
//            embeddedImage embeddedImage =new embeddedImage(userId,embeddedImageName,filter,imageOutPut,imageCompressedOut);
//            embeddedImageRepository.save(embeddedImage);
//            final Optional<embeddedImage> retrievedEmbeddedImageByName = embeddedImageRepository.findByName(embeddedImageName);
//            Long embeddedImageId = retrievedEmbeddedImageByName.get().getEmbeddedImageId();
//            final Optional<originalImage> retrievedOriginalImageByName = originalImageRepository.findByName(imageName);
//            Long originalImageId = retrievedOriginalImageByName.get().getOriginalImageId();
//            embeddedDetails embeddedDetails = new embeddedDetails(userId,embeddedKey,originalImageId,embeddedImageId);
//            embeddedDetailsRepository.save(embeddedDetails);
//            jsonPayload.put("embeddedImageId",embeddedImageId.toString());
//        }
//        catch (JsonParseException e) { e.printStackTrace();errorMessage.add(e.getMessage());}
//        catch (JsonMappingException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
//        catch (IOException e) { e.printStackTrace(); errorMessage.add(e.getMessage());}
//        jsonOutPut.put("payload",jsonPayload.returmMap());
//        jsonOutPut.put("error",errorMessage);
//        return jsonOutPut.returmMap();
//
//    }



    @GetMapping ("/test/{userId}")
    public JsonOutput test(@PathVariable("userId") Long id)throws Exception{
        HashMap<String,String>body =new HashMap<>();
        body.put("TestItem","ABC");
        JsonOutput.getJson().setCode("200");
        JsonOutput.getJson().setMessage("Ok");
        JsonOutput.getJson().setBody(body);
        return JsonOutput.getJson();
    }
    @PostMapping("/getTempEmbeddedImage")
    @ResponseBody
    public Map<String, Object> getTempEmbeddedImage(@RequestBody String allParams){
        ObjectMapper mapper = new ObjectMapper();
        String jsonString=allParams;
        ArrayList<String> errorMessage = new ArrayList<>();
        ArrayList<String> exceptionMessage = new ArrayList<>();
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
            if (!tempRepository.findByUserId(userId).isEmpty()){
                try {
                    tempRepository.deleteAllByUserId(userId);
                }catch (Exception e){
                    exceptionMessage.add(e.getMessage());
                }
            }
            final Optional<User> retrievedUserByUserID = userRepository.findById(userId);
            String embeddedInformation=null;
            if (retrievedUserByUserID.isEmpty()){
                errorMessage.add("Error Code 101. Error occur in Database");
            }
            else {
                embeddedInformation =retrievedUserByUserID.get().getDefaultDigitalWatermark();
            }
            ASEEncryption encryption = new ASEEncryption();
            String encryptKey = encryption.getRandomEncryptKey();
            String encryptedInformation = encryption.encrypt(embeddedInformation,encryptKey);
            if(encryption.getErrorMessage().size()>0){
                errorMessage.addAll(encryption.getErrorMessage());
            }
            if (encryption.getExceptionMessage().size()>0){
                exceptionMessage.addAll(encryption.getExceptionMessage());
            }
            if (encryptedInformation==null){
                errorMessage.add("Error Code 102. Fail to encrypt your information. You may consider to change your watermark info");
            }
            String imageCompressedOut=null;
            String imageOutPut=null;

            if (filter.equalsIgnoreCase("fragment")){
                MosicEmbed mosicEmbed = new MosicEmbed(encryptedInformation,imageBase64);
                imageOutPut = mosicEmbed.embedding();
                imageCompressedOut=compress.compress(imageOutPut);
                if (imageCompressedOut==null&&imageOutPut!=null){
                    errorMessage.add("Error Code 105. Fail to generator a review image");
                }
                if(mosicEmbed.getExceptionMessage().size()>0){
                    exceptionMessage.addAll(mosicEmbed.getExceptionMessage());
                }
                if (mosicEmbed.getErrorMessage().size()>0){
                    errorMessage.addAll(mosicEmbed.getErrorMessage());
                }
                String embeddedImageName = imageName + "_" + filter+ "_embedded";
                try {
                    tempTable tempTable = new tempTable(userId,imageName,imageBase64,imageCompressBase64,embeddedImageName,filter,imageOutPut,imageCompressedOut,encryptKey,encryptedInformation);
                    tempRepository.save(tempTable);
                }catch (Exception e){
                    exceptionMessage.add(e.getMessage());
                }
            }
            else if (filter.equalsIgnoreCase("pencil")){
                PencilPaintEmbed pencilPaintEmbed = new PencilPaintEmbed(imageBase64,"PNG");
                imageOutPut = pencilPaintEmbed.embedded(encryptedInformation);
                imageCompressedOut = compress.compress(imageOutPut);
                if (imageCompressedOut==null&&imageOutPut!=null){
                    errorMessage.add("Error Code 105. Fail to generator a review image");
                }
                if(pencilPaintEmbed.getExceptionMessage().size()>0){
                    exceptionMessage.addAll(pencilPaintEmbed.getExceptionMessage());
                }
                if (pencilPaintEmbed.getErrorMessage().size()>0){
                    errorMessage.addAll(pencilPaintEmbed.getErrorMessage());
                }
                String embeddedImageName = imageName + "_" + filter+ "_embedded";
                try {
                    tempTable tempTable = new tempTable(userId,imageName,imageBase64,imageCompressBase64,embeddedImageName,filter,imageOutPut,imageCompressedOut,encryptKey,encryptedInformation);
                    tempRepository.save(tempTable);
                }catch (Exception e){
                    exceptionMessage.add(e.getMessage());
                }

            }
            jsonPayload.put("embeddedImage",imageCompressedOut);
            jsonPayload.put("status","s");
        }
        catch (JsonParseException e) { e.printStackTrace();exceptionMessage.add(e.getMessage());}
        catch (JsonMappingException e) { e.printStackTrace(); exceptionMessage.add(e.getMessage());}
        catch (IOException e) { e.printStackTrace(); exceptionMessage.add(e.getMessage());}
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        jsonOutPut.put("exception",exceptionMessage);
        return jsonOutPut.returmMap();
    }
    @Transactional(rollbackFor = Exception.class)
    @GetMapping ("/confirmEmbeddedImage/{userId}")
    public Map<String, Object> confirmImageEmbedded(@PathVariable("userId") Long id)throws Exception{
        ArrayList<String> errorMessage = new ArrayList<>();
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        ArrayList<String> exceptionMessage = new ArrayList<>();
        final Optional<tempTable> retrieveTempData = tempRepository.findByUserId(id);
        if(retrieveTempData.isEmpty()){
            jsonPayload.put("status","f");
            errorMessage.add("Error Code 106. Fail to confirm embedded image");
        }
        else {
            tempTable tempTable =retrieveTempData.get();
            try {
                originalImage originalImage =new originalImage(id,tempTable.getOriginalImageName(),tempTable.getOriginalImageBase64(),tempTable.getOriginalImageCompressedBase64());
                encryptionDetail encryptionDetail =new encryptionDetail(id,tempTable.getEncryptionKey(),tempTable.getEncryptedString());
                embeddedImage embeddedImage =new embeddedImage(id,tempTable.getEmbeddedImageName(),tempTable.getFilter(),tempTable.getEmbeddedImage1Base64(),tempTable.getEmbeddedImageCompressedBase64());
                originalImageRepository.save(originalImage);
                encryptionDetailsRepository.save(encryptionDetail);
                embeddedImageRepository.save(embeddedImage);
            }catch (Exception e){
                exceptionMessage.add(e.getMessage());
            }
            final Optional<embeddedImage> retrievedEmbeddedImageByName = embeddedImageRepository.findByName(tempTable.getEmbeddedImageName());
            Long embeddedImageId=null;
            if (retrievedEmbeddedImageByName.isEmpty()){
                errorMessage.add("Error Code 101.Error occur in database.");
            }else {
                embeddedImageId = retrievedEmbeddedImageByName.get().getEmbeddedImageId();
            }
            final Optional<originalImage> retrievedOriginalImageByName = originalImageRepository.findByName(tempTable.getOriginalImageName());
            Long originalImageId=null;
            if (retrievedOriginalImageByName.isEmpty()){
                errorMessage.add("Error Code 101.Error occur in database.");
            }
            else{
                originalImageId = retrievedOriginalImageByName.get().getOriginalImageId();
            }
            try {
                embeddedDetails embeddedDetails = new embeddedDetails(id,originalImageId,embeddedImageId,tempTable.getFilter());
                embeddedDetailsRepository.save(embeddedDetails);
                tempRepository.deleteAllByUserId(id);
            }catch (Exception e){
                exceptionMessage.add(e.getMessage());
            }
            jsonPayload.put("status","s");
        }
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        jsonOutPut.put("exception",exceptionMessage);
        return jsonOutPut.returmMap();
    }
    @Transactional(rollbackFor = Exception.class)
    @DeleteMapping("/cancelTempEmbedding/{userId}")
    public Map<String,Object> cancelEmbedding (@PathVariable("userId") Long id)throws Exception  {
        ArrayList<String> errorMessage = new ArrayList<>();
        ArrayList<String> exceptionMessage = new ArrayList<>();
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        final Optional<tempTable> retrieveTempData = tempRepository.findByUserId(id);
        if(retrieveTempData.isEmpty()){
            jsonPayload.put("status","f");
            errorMessage.add("Error Code 101.Error occur in database.");
        }
        else {
            try {
                tempRepository.deleteAllByUserId(id);

            }catch (Exception e){
                exceptionMessage.add(e.getMessage());
            }
            if (!tempRepository.findByUserId(id).isEmpty()){
                errorMessage.add("Error Code 107.Fail to cancel the embedding process.");
            }
            else {
                jsonPayload.put("status","s");
            }
        }
        jsonOutPut.put("payload",jsonPayload.returmMap());
        jsonOutPut.put("error",errorMessage);
        jsonOutPut.put("exception",exceptionMessage);
        return jsonOutPut.returmMap();
    }
    @Transactional(rollbackFor =  Exception.class)
    @PutMapping("/refresh/{userId}")
    public Map<String,Object> refreshEmbedding (@PathVariable("userId") Long id)throws Exception{
        JsonCustomized<String,String> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        final Optional<tempTable> retrieveTempDetail =tempRepository.findByUserId(id);
        tempTable newTempTable =retrieveTempDetail.get();
        String originalImageBase64 = retrieveTempDetail.get().getOriginalImageBase64();
        String encrytedInformation = retrieveTempDetail.get().getEncryptedString();
        MosicEmbed mosicEmbed = new MosicEmbed(encrytedInformation,originalImageBase64);
        String embeddedImageBase64 = mosicEmbed.embedding();
        ImageCompress compress =new ImageCompress();
        String embeddedImageCompressBase64 =  compress.compress(embeddedImageBase64);
        newTempTable.setEmbeddedImage1Base64(embeddedImageBase64);
        newTempTable.setEmbeddedImageCompressedBase64(embeddedImageCompressBase64);
        tempRepository.save(newTempTable);
        jsonPayload.put("embeddedImage",embeddedImageCompressBase64);
        jsonPayload.put("status","s");
        jsonOutPut.put("payload",jsonPayload.returmMap());
        return jsonOutPut.returmMap();
    }
    @GetMapping(path = { "/get/{userId}" })
    public Map<String, String> getUserForTest (@PathVariable("userId") Long id) throws IOException {
        final Optional<User> retrievedUserID = userRepository.findById(id);
        JsonCustomized<String,String> map =new JsonCustomized<String, String>();
        map.put("default_digital_watermark", retrievedUserID.get().getDefaultDigitalWatermark() );
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
            Long userId = request.getUserId();
            String filter = request.getFilter();
            if (filter.equalsIgnoreCase("pencil")){
                    PencilPaintEmbed pencilPaintEmbed = new PencilPaintEmbed();
                    String extractedString = pencilPaintEmbed.extract(embeddedImage);
                    System.out.println(extractedString);
                    final Optional<List<encryptionDetail>> retrieveEmbeddedDetails =encryptionDetailsRepository.findByUserIdAndEncryptedString(userId,extractedString);
                    if (retrieveEmbeddedDetails.isEmpty()){
                        jsonPayload.put("status","f");
                        errorMessage.add("Error code 301. Fail to extract information");
                    }
                    else {
                        ASEEncryption aseEncryption = new ASEEncryption();
                        String encryptionKey =retrieveEmbeddedDetails.get().get(0).getEncryptionKey();
                        String hiddenInformation = aseEncryption.decrypt(extractedString,encryptionKey);
                        jsonPayload.put("status","s");
                        jsonPayload.put("hiddenInformation",hiddenInformation);
                    }
            }
            else if (filter.equalsIgnoreCase("fragment") ){
                MosicEmbed mosicEmbed = new MosicEmbed(embeddedImage);
                String extractedString = mosicEmbed.extraction();
                System.out.println(extractedString);
                final Optional<List<encryptionDetail>> retrieveEmbeddedDetails =encryptionDetailsRepository.findByUserIdAndEncryptedString(userId,extractedString);
                if (retrieveEmbeddedDetails.isEmpty()){
                    jsonPayload.put("status","f");
                    errorMessage.add("Error code 301. Fail to extract information");
                }
                else {
                    ASEEncryption aseEncryption = new ASEEncryption();
                    String encryptionKey =retrieveEmbeddedDetails.get().get(0).getEncryptionKey();
                    String hiddenInformation = aseEncryption.decrypt(extractedString,encryptionKey);
                    jsonPayload.put("status","s");
                    jsonPayload.put("hiddenInformation",hiddenInformation);
                }
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

        List <imageDetail> list =new ArrayList<>();

        JsonCustomized<String,String> jsonStatusAndError =new JsonCustomized<>();
        JsonCustomized<String,Object> jsonPayload= new JsonCustomized<>();
        JsonCustomized<String,Object> jsonOutPut =new JsonCustomized<>();
        final Optional <List<originalImage>> retrieveOriginalImageList = originalImageRepository.findByUserId(userId);
        if(retrieveOriginalImageList.get().size()!=0){
            for (int i =0 ;i<retrieveOriginalImageList.get().size();i++){
                list.add(new imageDetail(retrieveOriginalImageList.get().get(i).getOriginalImageId(),retrieveOriginalImageList.get().get(i).getImageCompressedBase64(),retrieveOriginalImageList.get().get(i).getName()));
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
    class imageDetail{
        String image;
        String imageName;
        Long id;
        public imageDetail(Long id,String image, String imageName) {
            this.image = image;
            this.imageName = imageName;
            this.id = id;
        }

        public imageDetail() {
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }
    }
}
