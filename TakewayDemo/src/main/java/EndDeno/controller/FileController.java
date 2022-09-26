package EndDeno.controller;

import EndDeno.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class FileController {

    @Value("${regiee.path}")
    private String PicturePath;


    /*文件上传*/
    @PostMapping("/upload")
    public Result<String> update(MultipartFile file){
        //file是一个临时文件 若不转存则程序结束会自动删除
        //log.info(file.toString());
        //原始文件名
        String originalName = file.getOriginalFilename();

        //截取原始文件的后綴名
        String filePostfix = originalName.substring(originalName.lastIndexOf("."));
        //使用UUID重新生成新的文件名
        String fileName = UUID.randomUUID().toString()+filePostfix;
        //判断当前配置文件中的路径是否存在，不存在则创建
        File path = new File(PicturePath);
        if(!path.exists()){
            path.mkdirs();
        }
        try {
            file.transferTo(new File(PicturePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.success(fileName,"文件上传成功");
    }


//    文件下载

    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){

        try {
            //输入流 通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(PicturePath+name));
            //输出流 通过输出流下载输入的文件来返回浏览器
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            int len=0;
            byte bytes[] = new byte[1024];
            // -1即输入流读取到了文件末尾
            while((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes);
               // outputStream.flush();
            }
            outputStream.flush();
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
