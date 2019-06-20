package cn.com.thtf.service.impl;

import cn.com.thtf.common.Constants;
import cn.com.thtf.common.exception.CustomException;
import cn.com.thtf.common.response.ResultCode;
import cn.com.thtf.mapper.ImageMapper;
import cn.com.thtf.model.Image;
import cn.com.thtf.service.ImageService;
import cn.com.thtf.utils.SnowflakeId;
import cn.com.thtf.utils.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

/**
 * ========================
 * 主题图片Service实现类
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：14:35
 * Version: v1.0
 * ========================
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class ImageServiceImpl implements ImageService {

    private static final Logger log = LoggerFactory.getLogger(ImageServiceImpl.class);

    @Autowired
    private ImageMapper imageMapper;

    /**
     * 保存主题图片
     * @param picNewName
     * @param picSavePath
     * @param httpPath
     * @return
     */
    @Override
    public Image saveImage(String picNewName, String picSavePath, String httpPath) {
        Image image = new Image();
        image.setId(SnowflakeId.getId() + "");
        image.setPath(picSavePath + "/" + picNewName);//相对路径
        image.setHttpPath(httpPath);//图片访问路径
        image.setUserId(UserUtil.getUserId());
        image.setCreateTime(new Timestamp(new Date().getTime()));
        image.setType(Constants.IMAGE_TYPE_THEME);
        int count = imageMapper.insert(image);
        if (count != 1) {
            log.error("### 保存主题图片：添加数据库错误 ###");
            throw new CustomException(ResultCode.FAIL);
        }
        return image;
    }
}
