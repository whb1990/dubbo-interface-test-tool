package com.whb.dubbo.service;

import com.whb.dubbo.dto.PomDTO;
import com.whb.dubbo.dto.ResultDTO;
import com.whb.dubbo.model.PomModel;
import org.xml.sax.SAXException;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface PomService {

    /**
     * download jar, push message to redis.
     * <br>
     * it's a backdoor.
     *
     * @return
     */
    ResultDTO<PomDTO> invoke() throws Exception;

    /**
     * parse pom, download jar, push message to redis.
     *
     * @param dto
     * @return
     */
    ResultDTO<PomDTO> invoke(PomDTO dto) throws Exception;

    /**
     * do parse pom.
     *
     * @param xml
     * @return
     * @throws IOException
     * @throws SAXException
     */
    List<PomModel> parsePom(@NotNull String xml) throws IOException, SAXException;

    /**
     * do append content to the end of pom.xml.
     *
     * @param models
     * @param pomXml
     * @throws Exception
     */
    void appendPom(List<PomModel> models, @NotNull String pomXml) throws Exception;

    /**
     * get the real time message from redis.
     *
     * @param requestId
     * @return
     */
    ResultDTO<String> getRealTimeMsg(@NotNull String requestId);

    /**
     * load jars.
     *
     * @param libPath the lib full path.
     * @return
     */
    ResultDTO<String> loadJars(String libPath) throws NoSuchMethodException, MalformedURLException;

    /**
     * list all dependency.
     *
     * @param dto
     * @return
     */
    List<PomModel> listJars(PomDTO dto) throws ParserConfigurationException, IOException, SAXException;

    /**
     * read the content from pom xml.
     *
     * @param pomXmlPath the path of pom xml file
     * @return the pom content
     */
    String loadPomFile(String pomXmlPath);

    /**
     * override the content of pom xml.
     *
     * @param pomXmlPath the path of pom xml file
     * @param content    text
     */
    Boolean overridePomFile(String pomXmlPath, String content);

    /**
     * delete all jars in the specifiy path.
     */
    ResultDTO<String> deleteJars(String path);
}
