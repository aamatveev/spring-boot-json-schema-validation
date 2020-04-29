package net.matve.controllers;

import lombok.extern.slf4j.Slf4j;
import net.matve.json.schema.annotation.JsonRequestBody;
import net.matve.model.Config;
import net.matve.model.Rectangle;
import net.matve.model.RectangleWrapper;
import net.matve.repo.ConfigRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/config")
//@Validated // позволяет добавлять аннотации валидации прямо к параметрам методов класса
public class ConfigController {

    @Autowired
    private ConfigRepository repository;

    @GetMapping
    public List<Config> getConfig(){
        return repository.findAll();
    }

    /**
     * validate by annotations
     * @param config
     */
    @PostMapping("/validateByAnnotations")
    public void saveJson(@RequestBody @Valid Config config){
        repository.save(config);
    }

    /**
     * validate by json schemas
     * @param config
     */
    @PostMapping("/validateBySchema")
    public void validateJsonAndSave(@JsonRequestBody Config config){
        repository.save(config);
    }

    @PostMapping("/validateRectangle")
    public void validateRectangle(@JsonRequestBody RectangleWrapper rectangle){
        log.trace("rectangle", rectangle);
    }
}
