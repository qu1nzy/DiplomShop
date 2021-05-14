package by.tms.Diplom.Service;

import by.tms.Diplom.Entity.Image;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Map;

@Service
@Slf4j
public class ImageService {

    @Autowired
    private Cloudinary cloudinary;

    public Image upload(MultipartFile file, String entityName, String entityCategoryName, String entityModel) throws IOException {
        Image image = new Image();
        byte[] bytes = file.getBytes();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        Map public_id = cloudinary.uploader().upload("data:"+file.getContentType()+";base64," +base64,
                ObjectUtils.asMap(
                        "folder", entityName,
                        "public_id", entityName+"_"+entityCategoryName+"_"+entityModel+"_"+file.getOriginalFilename()));
        image.setUrl((String)public_id.get("url"));
        log.info("imageService, upload - success");
        return image;
    }
}
